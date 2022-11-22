package vedam.subkuch.ui.jobs.jobmela;


import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.Response;

import java.util.ArrayList;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseFragment;
import vedam.subkuch.databinding.FragmentJobMelaBinding;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.interfaces.OnListViewItemClickListener;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.ui.jobs.models.JobMelaRequest;
import vedam.subkuch.ui.jobs.models.JobType;
import vedam.subkuch.ui.jobs.models.JobTypeResponse;
import vedam.subkuch.utils.ListItemClickAction;
import vedam.subkuch.utils.UiUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class JobMelaFragment extends BaseFragment implements OnListViewItemClickListener {

    private FragmentJobMelaBinding fragmentJobMelaBinding;
    private SparseArray<JobType> saJobType = new SparseArray<>();
    private boolean isAttending;

    public JobMelaFragment() {
        // Required empty public constructor
    }

    public static JobMelaFragment newInstance() {

        Bundle args = new Bundle();

        JobMelaFragment fragment = new JobMelaFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentJobMelaBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_job_mela, container, false);
        // Inflate the layout for this fragment
        return fragmentJobMelaBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getJobTypes();
        initUI();
    }

    private void initUI() {

        fragmentJobMelaBinding.rvJobType.setLayoutManager(new LinearLayoutManager(mContext));
        fragmentJobMelaBinding.btNext.setOnClickListener(v -> {
            int errorMessage = validateErrorMessage();
            if (errorMessage == 0) {
                next();
            } else
                UiUtil.showDialog(mContext, getString(errorMessage), true);
        });
        fragmentJobMelaBinding.rbYes.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isAttending = isChecked;
        });

        fragmentJobMelaBinding.rbNo.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isAttending = !isChecked;
        });
    }

    private int validateErrorMessage() {

        int errorMessage = 0;
        if (!fragmentJobMelaBinding.rbYes.isChecked() && !fragmentJobMelaBinding.rbNo.isChecked())
            errorMessage = R.string.select_attending_job_mela;
        else if (saJobType.size() == 0)
            errorMessage = R.string.select_interesting_jobs;

        return errorMessage;
    }

    private void next() {
        JobMelaRequest jobMelaRequest = new JobMelaRequest();
//        jobMelaRequest.setJobTypes(getJobTypeIds());
        jobMelaRequest.setIsInterestedInJob(isAttending);
        Bundle args = new Bundle();
        args.putParcelable(Constants.EXTRA_DATA, jobMelaRequest);
        addFragmentWithAnimation(R.id.content_frame, ExperienceFragment.newInstance(args), null, true);

    }

    private String[] getJobTypeIds() {
        String[] jobTypeIds = new String[saJobType.size()];
        for (int i = 0; i < saJobType.size(); i++) {
            jobTypeIds[i] = saJobType.valueAt(i).getJobTypeId();
        }
        return jobTypeIds;
    }


    private void getJobTypes() {

        UiUtil.showProgressDialog(mContext, getString(R.string.please_wait));
        DataFetcher.getJobTypes(mContext, onJobTypesSuccessListener, JobTypeResponse.class, onErrorListener);
    }

    private Response.Listener<JobTypeResponse> onJobTypesSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (getActivity() != null)
            if (response != null && response.getReturnMessage().equals(Constants.SUCCESS)) {
                loadValues(response.getReturnData());
            } else
                UiUtil.showToast(mContext, getString(R.string.no_data));
    };

    private void loadValues(ArrayList<JobType> returnData) {

        fragmentJobMelaBinding.rvJobType.setAdapter(new JobTypeAdapter(returnData, this));
    }

    @Override
    public <E> void onItemClick(E item, int position, View view, ListItemClickAction action) {

        JobType jobType = (JobType) item;
        if (jobType.isChecked())
            saJobType.put(position, jobType);
        else
            saJobType.remove(position);
    }
}
