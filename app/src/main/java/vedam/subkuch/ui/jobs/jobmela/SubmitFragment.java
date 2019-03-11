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
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseAddImagesFragment;
import vedam.subkuch.databinding.FragmentSubmitBinding;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.network.DataPart;
import vedam.subkuch.network.NetworkConstants;
import vedam.subkuch.network.models.GeneralResponse;
import vedam.subkuch.ui.jobs.models.JobMelaRequest;
import vedam.subkuch.ui.jobs.models.JobSalary;
import vedam.subkuch.ui.jobs.models.JobSalaryResponse;
import vedam.subkuch.utils.AppPrefs;
import vedam.subkuch.utils.AppUtil;
import vedam.subkuch.utils.UiUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubmitFragment extends BaseAddImagesFragment implements AdapterView.OnItemSelectedListener {

    private FragmentSubmitBinding fragmentSubmitBinding;
    private boolean isTwoWheelerOwner;
    private JobMelaRequest jobMelaRequest;
    private String salaryId;

    public SubmitFragment() {
        // Required empty public constructor
    }

    public static SubmitFragment newInstance(Bundle args) {

        SubmitFragment fragment = new SubmitFragment();
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
        fragmentSubmitBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_submit, container, false);
        return fragmentSubmitBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setImagesLayout(view, 1);
        bindCallbacks();
        getSalaries();
    }

    private void getSalaries() {
        UiUtil.showProgressDialog(context, R.string.please_wait);
        DataFetcher.getJobSalaries(context, onSalarySuccessListener, JobSalaryResponse.class, onErrorListener);

    }

    private Response.Listener<JobSalaryResponse> onSalarySuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (response != null && response.getReturnMessage().equals(Constants.SUCCESS)) {
            setJobQualifications(response.getReturnData());
        } else
            UiUtil.showToast(context, getString(R.string.no_data));
    };

    private void setJobQualifications(ArrayList<JobSalary> jobSalaries) {

        JobSalary jobSalary = new JobSalary();
        jobSalary.setSalary(getString(R.string.select_a_salary));
        jobSalaries.add(0, jobSalary);

        ArrayAdapter<JobSalary> adapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_dropdown_item, jobSalaries);
        fragmentSubmitBinding.spSalaryExpected.setAdapter(adapter);
        fragmentSubmitBinding.spSalaryExpected.setOnItemSelectedListener(this);
        fragmentSubmitBinding.spSalaryExpected.setSelection(0);
    }


    private void bindCallbacks() {

        fragmentSubmitBinding.btPrevious.setOnClickListener(v -> {
            if (getActivity() != null && getActivity().getSupportFragmentManager() != null)
                getActivity().getSupportFragmentManager().popBackStack();
        });

        fragmentSubmitBinding.btSubmit.setOnClickListener(v -> {
            int errorMessage = validateErrorMessage();
            if (errorMessage == 0) {
                submit();
            } else
                UiUtil.showDialog(context, getString(errorMessage), true);
        });

        fragmentSubmitBinding.rbYes.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isTwoWheelerOwner = isChecked;
        });

        fragmentSubmitBinding.rbNo.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isTwoWheelerOwner = !isChecked;
        });
    }

    private void uploadImage() {
        UiUtil.showProgressDialog(context, getString(R.string.please_wait));
        Map<String, DataPart> params = new HashMap<>();
        params.put(NetworkConstants.ProfileImage, new DataPart(AppUtil.getUniqueFileName(),
                AppUtil.getBytesFromBitmap(AppUtil.getSingleBitmap(context, getImageItemMap()))
                , NetworkConstants.JPEG_MIME_TYPE));

        DataFetcher.uploadJobProfileImage(context, params, onImageUploadSuccessListener, GeneralResponse.class, onErrorListener);
    }

    private Response.Listener<GeneralResponse> onImageUploadSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (getActivity() != null)
            if (response != null && response.getReturnMessage().equals(Constants.SUCCESS)) {
                UiUtil.showToast(context, getString(R.string.job_profile_added));
                getActivity().finish();
            } else
                UiUtil.showToast(context, getString(R.string.err_occurred));
    };

    private void submit() {

        UiUtil.showProgressDialog(context, getString(R.string.please_wait));
        String userId = AppPrefs.getPrefsUserId(context);
        jobMelaRequest.setUserId(userId);
        jobMelaRequest.setIsOwnTwoWheeler(isTwoWheelerOwner);
        jobMelaRequest.setJobSalaryId(salaryId);
        DataFetcher.addJobProfile(context, new Gson().toJson(jobMelaRequest), onAddJobProfileSuccessListener, GeneralResponse.class, onErrorListener);

    }


    private Response.Listener<GeneralResponse> onAddJobProfileSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (getActivity() != null)
            if (response != null && response.getReturnMessage().equals(Constants.SUCCESS)) {
                if (getImageItemMap().size() > 0)
                    uploadImage();
                else {
                    UiUtil.showToast(context, getString(R.string.job_profile_added));
                    getActivity().finish();
                }
            } else
                UiUtil.showToast(context, getString(R.string.err_occurred));
    };

    private int validateErrorMessage() {

        int errorMessage = 0;
        if (!fragmentSubmitBinding.rbYes.isChecked() && !fragmentSubmitBinding.rbNo.isChecked())
            errorMessage = R.string.select_if_own_two_wheeler;
        else if (TextUtils.isEmpty(salaryId))
            errorMessage = R.string.enter_expected_salary;

        return errorMessage;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        salaryId = ((JobSalary) parent.getItemAtPosition(position)).getJobSalaryId();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
