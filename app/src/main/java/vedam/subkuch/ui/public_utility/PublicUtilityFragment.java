package vedam.subkuch.ui.public_utility;


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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;

import java.util.ArrayList;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseFragment;
import vedam.subkuch.databinding.FragmentPublicUtilitiesBinding;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.network.models.public_utility.PublicUtility;
import vedam.subkuch.network.models.public_utility.PublicUtilityResponse;
import vedam.subkuch.utils.UiUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class PublicUtilityFragment extends BaseFragment {

    private FragmentPublicUtilitiesBinding binding;
    private PublicUtilityAdapter adapter;
    private boolean loading = true;
    private LinearLayoutManager linearLayoutManager;
    private int pageNo = 1;
    private int pageSize = 20;
    private boolean hasMoreProjects = true;
    private ArrayList<PublicUtility> publicUtilities = new ArrayList<>();
    private String subCategoryId;

    public PublicUtilityFragment() {
        // Required empty public constructor
    }

    public static PublicUtilityFragment newInstance(Bundle args) {

        PublicUtilityFragment fragment = new PublicUtilityFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            subCategoryId = getArguments().getString(Constants.EXTRA_SUB_CATEGORY_ID);
            setTitle(getArguments().getString(Constants.EXTRA_SUB_CATEGORY_NAME));
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_public_utilities, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        initUI();
        getPublicUtilities();
    }

    private void initUI() {

        linearLayoutManager = new LinearLayoutManager(context);
        binding.rvUtilities.setLayoutManager(linearLayoutManager);
        binding.rvUtilities.setHasFixedSize(true);
        adapter = new PublicUtilityAdapter(context, publicUtilities);
        binding.rvUtilities.setAdapter(adapter);
        binding.rvUtilities.addOnScrollListener(new OnScrollListener());
    }

    private void getPublicUtilities() {

        UiUtil.showProgressDialog(context, getString(R.string.please_wait));
        DataFetcher.getPublicUtilities(context, onDirectoryDetailSuccessListener, PublicUtilityResponse.class, onErrorListener, subCategoryId, pageNo, pageSize);
    }

    private Response.Listener<PublicUtilityResponse> onDirectoryDetailSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (getActivity() != null)
            if (response != null && response.getReturnMessage().equals(Constants.SUCCESS)) {
                if (response.getReturnData().size() > 0) {
                    hasMoreProjects = response.getReturnData().size() >= pageSize;
                    loading = true;
                    loadValues(response.getReturnData());
                } else
                    UiUtil.showToast(context, getString(R.string.no_data));
            } else
                UiUtil.showToast(context, getString(R.string.err_occurred));
    };

    private void loadValues(ArrayList<PublicUtility> response) {

        if (response != null && !response.isEmpty()) {
            pageNo++;
            publicUtilities.addAll(response);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.add, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_add) {
            startActivity(new Intent(getActivity(), AddPublicUtilityActivity.class));
            return true;
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
                        if (hasMoreProjects) getPublicUtilities();

                    }
                }
            }
        }
    }
}
