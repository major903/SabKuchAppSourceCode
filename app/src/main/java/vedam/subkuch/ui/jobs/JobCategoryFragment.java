package vedam.subkuch.ui.jobs;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Response;

import java.util.ArrayList;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseListFragment;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.utils.UiUtil;

public class JobCategoryFragment extends BaseListFragment {

    public JobCategoryFragment() {
        // Required empty public constructor
    }


    public static JobCategoryFragment newInstance() {

        Bundle args = new Bundle();

        JobCategoryFragment fragment = new JobCategoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_phone_book, container, false);
    }

    public void onViewCreated(@NonNull View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        getJobCategory();
    }


    private void getJobCategory() {

        UiUtil.showProgressDialog(context, getString(R.string.please_wait));
        DataFetcher.getJobsCategory(context, onJobCategorySuccessListener, JobCategoryResponse.class, onErrorListener);
    }

    private Response.Listener<JobCategoryResponse> onJobCategorySuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (response != null && response.getStatus().equals(Constants.TRUE)) {
            loadValues(response.getJobCategoriesResult().getJobCategories());
        } else
            UiUtil.showToast(context, getString(R.string.no_data));
    };

    private void loadValues(ArrayList<JobCategory> response) {

        JobCategoryAdapter jobCategoryAdapter= new JobCategoryAdapter(context, response);
        getListView().setAdapter(jobCategoryAdapter);
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        JobCategory jobCategory = ((JobCategory) l.getItemAtPosition(position));
        addFragment(R.id.content_frame, JobsFragment.newInstance(jobCategory),
                null, true, 0, 0, 0, 0);
    }
}
