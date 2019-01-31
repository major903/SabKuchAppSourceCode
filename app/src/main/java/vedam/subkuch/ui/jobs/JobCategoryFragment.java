package vedam.subkuch.ui.jobs;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Response;

import java.util.ArrayList;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseListFragment;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.ui.jobs.jobmela.JobMelaActivity;
import vedam.subkuch.ui.jobs.models.JobCategory;
import vedam.subkuch.ui.jobs.models.JobCategoryResponse;
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
        return inflater.inflate(R.layout.fragment_directory, container, false);
    }

    public void onViewCreated(@NonNull View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        setHasOptionsMenu(true);
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

        ArrayAdapter<JobCategory> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1,
                android.R.id.text1, response);
        setListAdapter(adapter);
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        JobCategory jobCategory = ((JobCategory) l.getItemAtPosition(position));
        addFragment(R.id.content_frame, JobsFragment.newInstance(jobCategory),
                null, true, 0, 0, 0, 0);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.jobs, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_job_mela:
                startActivity(new Intent(getActivity(), JobMelaActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
