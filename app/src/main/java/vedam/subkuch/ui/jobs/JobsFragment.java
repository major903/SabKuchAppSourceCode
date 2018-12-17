package vedam.subkuch.ui.jobs;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import com.android.volley.Response;

import java.util.ArrayList;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseListFragment;
import vedam.subkuch.databinding.FragmentJobsBinding;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.utils.UiUtil;

public class JobsFragment extends BaseListFragment {

    private String categoryId;
    private FragmentJobsBinding fragmentJobsBinding;

    public JobsFragment() {
        // Required empty public constructor
    }

    public static JobsFragment newInstance(JobCategory jobCategory) {
        JobsFragment fragment = new JobsFragment();

        Bundle args = new Bundle();
        args.putString(Constants.EXTRA_CATEGORY_NAME, jobCategory.getJobCategoryName());
        args.putString(Constants.EXTRA_CATEGORY_ID, jobCategory.getJobCategoryId());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryId = getArguments().getString(Constants.EXTRA_CATEGORY_ID);
//            String categoryName = getArguments().getString(Constants.EXTRA_CATEGORY_NAME);
//            setTitle(categoryName);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentJobsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_jobs, container, false);
        return fragmentJobsBinding.getRoot();
    }

    public void onViewCreated(@NonNull View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        getJobs(null);

        fragmentJobsBinding.etSearch.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_GO) {
                getJobs(textView.getText().toString());
                return true;
            }
            return false;
        });
        fragmentJobsBinding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (TextUtils.isEmpty(editable))
                    fragmentJobsBinding.ibClose.setVisibility(View.GONE);
                else
                    fragmentJobsBinding.ibClose.setVisibility(View.VISIBLE);
            }
        });

        fragmentJobsBinding.ibClose.setOnClickListener(view -> {
            fragmentJobsBinding.etSearch.setText("");
            getJobs(null);
        });

    }

    private void getJobs(String searchText) {

        UiUtil.showProgressDialog(context, getString(R.string.please_wait));
        DataFetcher.getJobs(context, onJobsSuccessListener, JobResponse.class, onErrorListener, categoryId, searchText);
    }

    private Response.Listener<JobResponse> onJobsSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (response != null && response.getStatus().equals(Constants.TRUE)) {
            loadValues(response.getJobsResult().getJobs());
        } else
            UiUtil.showToast(context, getString(R.string.err_occurred));
    };

    private void loadValues(ArrayList<Job> returnData) {

        JobsAdapter jobsAdapter = new JobsAdapter(getActivity(), returnData);
        getListView().setAdapter(jobsAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.add, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_add:
                startActivity(new Intent(getActivity(), AddJobsActivity.class)
                        .putExtra(Constants.EXTRA_CATEGORY_ID, categoryId));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
