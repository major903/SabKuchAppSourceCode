package vedam.subkuch.ui.jobs;

import android.content.Intent;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;

import java.util.ArrayList;
import java.util.Locale;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseFragment;
import vedam.subkuch.databinding.FragmentJobsBinding;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.interfaces.OnListViewItemClickListener;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.network.models.ShareResponse;
import vedam.subkuch.ui.jobs.jobmela.JobMelaActivity;
import vedam.subkuch.ui.jobs.models.Job;
import vedam.subkuch.ui.jobs.models.JobCategory;
import vedam.subkuch.ui.jobs.models.JobResponse;
import vedam.subkuch.ui.jobs.models.Post;
import vedam.subkuch.utils.ListItemClickAction;
import vedam.subkuch.utils.ShareUtils;
import vedam.subkuch.utils.UiUtil;

public class JobsFragment extends BaseFragment implements OnListViewItemClickListener {

    private String categoryId;
    private FragmentJobsBinding fragmentJobsBinding;
    private JobsAdapter adapter;
    private ArrayList<Job> jobsList = new ArrayList<>();
    private boolean loading = true;
    private LinearLayoutManager linearLayoutManager;
    private int pageNo = 1;
    private int pageSize = 20;
    private boolean hasMoreProjects = true;
    private String searchText;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentJobsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_jobs, container, false);
        return fragmentJobsBinding.getRoot();
    }

    public void onViewCreated(@NonNull View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        initUI();
        getJobs();

        fragmentJobsBinding.etSearch.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_GO) {
                searchText = textView.getText().toString();
                getJobs();
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
            setDefaults();
            getJobs();
        });

    }

    private void setDefaults() {
        searchText = null;
        pageNo = 1;
        hasMoreProjects = true;
        jobsList.clear();
        adapter.notifyDataSetChanged();

    }

    private void initUI() {

        linearLayoutManager = new LinearLayoutManager(context);
        fragmentJobsBinding.rvJobs.setLayoutManager(linearLayoutManager);
        adapter = new JobsAdapter(context, jobsList, this);
        fragmentJobsBinding.rvJobs.setHasFixedSize(true);
        fragmentJobsBinding.rvJobs.setAdapter(adapter);
        fragmentJobsBinding.rvJobs.addOnScrollListener(new JobsOnScrollListener());
    }

    private void getJobs() {

        UiUtil.showProgressDialog(context, getString(R.string.please_wait));
        DataFetcher.getJobs(context, onJobsSuccessListener, JobResponse.class, onErrorListener, categoryId, searchText, pageNo, pageSize);
    }

    private Response.Listener<JobResponse> onJobsSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (getActivity() != null)
            if (response != null && response.getStatus().equals(Constants.TRUE)) {
                if (!response.getJobsResult().getJobs().isEmpty()) {
                    hasMoreProjects = response.getJobsResult().getJobs().size() >= pageSize;
                    loading = true;
                    loadValues(response.getJobsResult().getJobs());
                } else
                    UiUtil.showToast(context, getString(R.string.no_jobs_found));
            } else
                UiUtil.showToast(context, getString(R.string.err_occurred));
    };

    private void loadValues(ArrayList<Job> returnData) {

        if (returnData != null && !returnData.isEmpty()) {
            pageNo++;
            jobsList.addAll(returnData);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.add_search, menu);

        /*MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) context.getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null && searchManager != null && getActivity() != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        }*/
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_add:
                startActivity(new Intent(getActivity(), AddJobsActivity.class));
                return true;
            case R.id.action_search:
                if (fragmentJobsBinding.cvSearch.getVisibility() == View.VISIBLE)
                    fragmentJobsBinding.cvSearch.setVisibility(View.GONE);
                else {
                    fragmentJobsBinding.etSearch.requestFocus();
                    fragmentJobsBinding.cvSearch.setVisibility(View.VISIBLE);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public <E> void onItemClick(E item, int position, View view, ListItemClickAction action) {

        if (item instanceof Job)
            getShareMessage((Job) item);
        else if (item instanceof Post) {
            startActivity(new Intent(context, JobMelaActivity.class).putExtra(Constants.EXTRA_DATA, (Post) item));
        }
    }

    private void getShareMessage(Job job) {
        UiUtil.showProgressDialog(context, R.string.loading);
        DataFetcher.getShareContent(context, onShareSuccessListener(job), ShareResponse.class, onErrorListener);

    }

    private Response.Listener<ShareResponse> onShareSuccessListener(Job job) {

        return response -> {
            if (response != null && response.getReturnMessage().equals(Constants.SUCCESS) && response.getReturnData() != null) {

                ShareUtils.shareMessage(context, String.format("Sharing this job ad with you. If you want to find jobs near your home install Sabkuch App from the link given below. \n\n" +
                                "https://play.google.com/store/apps/details?id=vedam.subkuch&referrer=%s\n\n%s", getCode(response.getReturnData()),
                        JobsFragment.this.getShareJobPost(job)), null);
                UiUtil.cancelProgressDialog();

            } else {
                UiUtil.cancelProgressDialog();
                UiUtil.showToast(context, JobsFragment.this.getString(R.string.err_occurred));
            }
        };
    }

    private String getCode(String returnData) {
        return returnData.split("referrer=")[1];
    }


    private CharSequence getShareJobPost(Job job) {

        if (job == null || job.getPosts() == null || job.getPosts().isEmpty())
            return null;

        StringBuilder sbPost = new StringBuilder();

        if (job.getPosts().size() == 1)
            sbPost.append(job.getPosts().get(0).getJobTitle());
        else
            for (int i = 0; i < job.getPosts().size(); i++) {
                Post post = job.getPosts().get(i);
                sbPost.append(String.format(Locale.US, "%d) %s ", i + 1, post.getJobTitle()));
            }

        return String.format(Locale.US, "%s dealing in %s is looking for %s\n%s", job.getOrganisationName(),
                job.getDealingIn(), sbPost.toString().trim(), job.getHowToContact());
    }

    public class JobsOnScrollListener extends RecyclerView.OnScrollListener {

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
                        if (hasMoreProjects) getJobs();

                    }
                }
            }
        }
    }
}
