package vedam.subkuch.ui.classifieds;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseFragment;
import vedam.subkuch.databinding.FragmentClassifiedDetailsBinding;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.interfaces.OnListViewItemClickListener;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.network.models.BaseGetMasterModel;
import vedam.subkuch.network.models.classifieds.Classified;
import vedam.subkuch.ui.jobs.models.AddResponse;
import vedam.subkuch.utils.AppUtil;
import vedam.subkuch.utils.ListItemClickAction;
import vedam.subkuch.utils.UiUtil;

public class MyClassifiedFragment extends BaseFragment implements OnListViewItemClickListener {

    private FragmentClassifiedDetailsBinding binding;
    private MyClassifiedsAdapter adapter;
    private ArrayList<Classified> classifieds = new ArrayList<>();
    private boolean loading = true;
    private LinearLayoutManager linearLayoutManager;
    private int pageNo = 1;
    private int pageSize = 20;
    private boolean hasMoreProjects = true;

    public MyClassifiedFragment() {
        // Required empty public constructor
    }

    public static MyClassifiedFragment newInstance() {

        return new MyClassifiedFragment();
    }

    //Layout of BulleinFragment is used because both needed to same i.e. ListView
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_classified_details, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        setTitle(getString(R.string.my_classifieds));
        initUI();
        getClassifieds();
    }

    private void initUI() {

        linearLayoutManager = new LinearLayoutManager(mContext);
        binding.rvClassifieds.setLayoutManager(linearLayoutManager);
        binding.rvClassifieds.setHasFixedSize(true);
        adapter = new MyClassifiedsAdapter(mContext, classifieds, this);
        binding.rvClassifieds.setAdapter(adapter);
        binding.rvClassifieds.addOnScrollListener(new MyClassifiedFragment.OnScrollListener());
    }

    private void getClassifieds() {
        UiUtil.showProgressDialog(mContext, getString(R.string.please_wait));
        Type type = new TypeToken<BaseGetMasterModel<Classified>>() {
        }.getType();
        DataFetcher.getMyClassifieds(mContext, onGetClassifiedsSuccessListener, type, onErrorListener, pageNo, pageSize);

    }

    private Response.Listener<BaseGetMasterModel<Classified>> onGetClassifiedsSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (getActivity() != null)
            if (response != null && response.getReturnMessage().equals(Constants.SUCCESS)) {
                if (response.getReturnData().size() > 0) {
                    hasMoreProjects = response.getReturnData().size() >= pageSize;
                    loading = true;
                    loadValues(response.getReturnData());
                } else
                    UiUtil.showToast(mContext, getString(R.string.no_ads_found));
            } else
                UiUtil.showToast(mContext, getString(R.string.err_occurred));
    };


    private void loadValues(ArrayList<Classified> response) {

        if (response != null && !response.isEmpty()) {
            pageNo++;
            classifieds.addAll(response);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.add, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            startActivity(new Intent(mContext, AddClassifiedsActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public <E> void onItemClick(E item, int position, View view, ListItemClickAction action) {
        switch (action) {
            case EDIT:
                Intent intent = new Intent(mContext, EditClassifiedActivity.class);
                intent.putExtra(Constants.EXTRA_DATA, (Classified) item);
                startActivityForResult(intent, Constants.REQUEST_EDIT_AD);
                break;
            case DELETE:
                deleteAd((Classified) item);
                break;
        }
    }

    private void deleteAd(Classified item) {

        UiUtil.showProgressDialog(mContext, getString(R.string.please_wait));
        DataFetcher.deleteClassified(mContext, onDeleteSuccessListener, AddResponse.class, onErrorListener, item.getClassifiedAdId());
    }

    private Response.Listener<AddResponse> onDeleteSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (getActivity() != null)
            if (response != null && response.getReturnCode() == Constants.SUCCESS_RETURN_CODE) {
                UiUtil.showToast(mContext, AppUtil.deNull(response.getReturnMessage()));
                refreshData();
            } else
                UiUtil.showToast(mContext, getString(R.string.err_occurred));
    };

    private void refreshData() {
        setDefaults();
        getClassifieds();
    }

    private void setDefaults() {
        pageNo = 1;
        hasMoreProjects = true;
        classifieds.clear();
        adapter.notifyDataSetChanged();

    }

    public class OnScrollListener extends RecyclerView.OnScrollListener {

        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            /*final Picasso picasso = Picasso.get();

            if (newState == RecyclerView.SCROLL_STATE_IDLE || newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                picasso.resumeTag(context);
            } else {
                picasso.pauseTag(context);
            }*/
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            if (dy > 0) //check for scroll down
            {
                int visibleItemCount = linearLayoutManager.getChildCount();
                int totalItemCount = linearLayoutManager.getItemCount();
                int pastVisibleItems = linearLayoutManager.findFirstVisibleItemPosition();

                if (loading) {
                    if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                        loading = false;
                        if (hasMoreProjects) getClassifieds();

                    }
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Constants.REQUEST_EDIT_AD && resultCode == Activity.RESULT_OK)
            refreshData();
        else
            super.onActivityResult(requestCode, resultCode, data);
    }
}
