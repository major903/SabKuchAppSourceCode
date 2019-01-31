package vedam.subkuch.ui.jobs.jobmela;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.android.volley.Response;

import java.util.ArrayList;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseFragment;
import vedam.subkuch.databinding.FragmentExperienceBinding;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.ui.jobs.models.JobExperience;
import vedam.subkuch.ui.jobs.models.JobExperienceResponse;
import vedam.subkuch.ui.jobs.models.JobMelaRequest;
import vedam.subkuch.ui.jobs.models.JobQualification;
import vedam.subkuch.ui.jobs.models.JobQualificationResponse;
import vedam.subkuch.utils.UiUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExperienceFragment extends BaseFragment implements AdapterView.OnItemSelectedListener {

    private FragmentExperienceBinding fragmentExperienceBinding;
    private JobMelaRequest jobMelaRequest;
    private String jobQualificationId;
    private String jobExperienceId;

    public ExperienceFragment() {
        // Required empty public constructor
    }

    public static ExperienceFragment newInstance(Bundle args) {

        ExperienceFragment fragment = new ExperienceFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            jobMelaRequest = getArguments().getParcelable(Constants.EXTRA_DATA);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentExperienceBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_experience, container, false);
        return fragmentExperienceBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bindCallbacks();
        getJobQualifications();
        getJobExperiences();
    }

    private void getJobExperiences() {

        UiUtil.showProgressDialog(context, R.string.please_wait);
        DataFetcher.getJobExperiences(context, onExperienceSuccessListener, JobExperienceResponse.class, onErrorListener);
    }

    private Response.Listener<JobExperienceResponse> onExperienceSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (response != null && response.getReturnMessage().equals(Constants.SUCCESS)) {
            setJobExperiences(response.getReturnData());
        } else
            UiUtil.showToast(context, getString(R.string.no_data));
    };

    private void setJobExperiences(ArrayList<JobExperience> jobExperiences) {

        JobExperience jobExperience = new JobExperience();
        jobExperience.setJobExpName(getString(R.string.select_a_job_experience));
        jobExperiences.add(0, jobExperience);

        ArrayAdapter<JobExperience> adapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_dropdown_item, jobExperiences);
        fragmentExperienceBinding.spExperience.setAdapter(adapter);
        fragmentExperienceBinding.spExperience.setOnItemSelectedListener(this);
        fragmentExperienceBinding.spExperience.setSelection(0);
    }

    private void getJobQualifications() {
        UiUtil.showProgressDialog(context, R.string.please_wait);
        DataFetcher.getJobQualifications(context, onQualifySuccessListener, JobQualificationResponse.class, onErrorListener);

    }

    private Response.Listener<JobQualificationResponse> onQualifySuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (response != null && response.getReturnMessage().equals(Constants.SUCCESS)) {
            setJobQualifications(response.getReturnData());
        } else
            UiUtil.showToast(context, getString(R.string.no_data));
    };

    private void setJobQualifications(ArrayList<JobQualification> jobQualifications) {

        JobQualification jobQualification = new JobQualification();
        jobQualification.setQualificationName(getString(R.string.select_a_job_qualification));
        jobQualifications.add(0, jobQualification);

        ArrayAdapter<JobQualification> adapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_dropdown_item, jobQualifications);
        fragmentExperienceBinding.spQualification.setAdapter(adapter);
        fragmentExperienceBinding.spQualification.setOnItemSelectedListener(this);
        fragmentExperienceBinding.spQualification.setSelection(0);
    }


    private void bindCallbacks() {

        fragmentExperienceBinding.btPrevious.setOnClickListener(v -> {
            if (getActivity() != null && getActivity().getSupportFragmentManager() != null)
                getActivity().getSupportFragmentManager().popBackStack();
        });

        fragmentExperienceBinding.btNext.setOnClickListener(v -> {
            int errorMessage = validateErrorMessage();
            if (errorMessage == 0) {
                next();
            } else
                UiUtil.showDialog(context, getString(errorMessage), true);
        });
    }

    private void next() {
        jobMelaRequest.setJobQualificationId(jobQualificationId);
        jobMelaRequest.setJobExperienceId(jobExperienceId);
        jobMelaRequest.setJobExperienceDetails(fragmentExperienceBinding.etMoreExperience.getText().toString());
        Bundle args = new Bundle();
        args.putParcelable(Constants.EXTRA_DATA, jobMelaRequest);
        addFragmentWithAnimation(R.id.content_frame, SubmitFragment.newInstance(args), null, true);

    }

    private int validateErrorMessage() {

        int errorMessage = 0;
        if (TextUtils.isEmpty(jobQualificationId))
            errorMessage = R.string.select_a_job_qualification;
        else if (TextUtils.isEmpty(jobExperienceId))
            errorMessage = R.string.select_a_job_experience;
        else if (!jobExperienceId.equals(Constants.FRESHER) && TextUtils.isEmpty(fragmentExperienceBinding.etMoreExperience.getText()))
            errorMessage = R.string.enter_experience_details;

        return errorMessage;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.sp_qualification:
                jobQualificationId = ((JobQualification) parent.getItemAtPosition(position)).getQulaificationId();
                break;
            case R.id.sp_experience:
                jobExperienceId = ((JobExperience) parent.getItemAtPosition(position)).getJobExpId();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
