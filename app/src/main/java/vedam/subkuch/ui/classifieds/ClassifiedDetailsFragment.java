package vedam.subkuch.ui.classifieds;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.network.models.BaseGetMasterModel;
import vedam.subkuch.network.models.classifieds.Classified;
import vedam.subkuch.utils.UiUtil;

public class ClassifiedDetailsFragment extends BaseFragment {

    private FragmentClassifiedDetailsBinding binding;
    private ClassifiedDetailsAdapter adapter;
    private ArrayList<Classified> classifieds = new ArrayList<>();
    private boolean loading = true;
    private LinearLayoutManager linearLayoutManager;
    private int pageNo = 1;
    private int pageSize = 20;
    private boolean hasMoreProjects = true;
    String subCategoryId;

    public ClassifiedDetailsFragment() {
        // Required empty public constructor
    }

    public static ClassifiedDetailsFragment newInstance(Bundle args) {

        ClassifiedDetailsFragment fragment = new ClassifiedDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            subCategoryId = getArguments().getString(Constants.EXTRA_SUB_CATEGORY_ID);
            setTitle(getArguments().getString(Constants.EXTRA_SUB_CATEGORY_NAME));
        }
    }

    //Layout of BulleinFragment is used because both needed to same i.e. ListView
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_classified_details, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        initUI();
        getClassifieds();
    }

    private void initUI() {

        linearLayoutManager = new LinearLayoutManager(mContext);
        binding.rvClassifieds.setLayoutManager(linearLayoutManager);
        binding.rvClassifieds.setHasFixedSize(true);
        adapter = new ClassifiedDetailsAdapter(mContext, classifieds);
        binding.rvClassifieds.setAdapter(adapter);
        binding.rvClassifieds.addOnScrollListener(new ClassifiedDetailsFragment.OnScrollListener());
    }

    private void getClassifieds() {
        UiUtil.showProgressDialog(mContext, getString(R.string.please_wait));
        Type type = new TypeToken<BaseGetMasterModel<Classified>>() {
        }.getType();
        DataFetcher.getClassifieds(mContext, onGetClassifiedsSuccessListener, type, onErrorListener, pageNo, pageSize, subCategoryId);

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
        inflater.inflate(R.menu.classifed_details, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                startActivity(new Intent(mContext, AddClassifiedsActivity.class));
                break;
            case R.id.action_edit:
                addFragmentWithAnimation(R.id.content_frame, MyClassifiedFragment.newInstance(), null, true);
                break;

        }
        return super.onOptionsItemSelected(item);
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
}
