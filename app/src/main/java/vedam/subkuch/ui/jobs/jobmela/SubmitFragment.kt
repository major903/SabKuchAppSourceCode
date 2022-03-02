package vedam.subkuch.ui.jobs.jobmela

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import androidx.databinding.DataBindingUtil
import com.android.volley.Response
import com.google.gson.Gson
import vedam.subkuch.R
import vedam.subkuch.base.BaseAddImagesFragment
import vedam.subkuch.databinding.FragmentSubmitBinding
import vedam.subkuch.helpers.Constants
import vedam.subkuch.network.DataFetcher.addJobProfile
import vedam.subkuch.network.DataFetcher.getJobSalaries
import vedam.subkuch.network.DataFetcher.uploadJobProfileImage
import vedam.subkuch.network.NetworkConstants
import vedam.subkuch.network.models.DataPart
import vedam.subkuch.network.models.GeneralResponse
import vedam.subkuch.ui.jobs.models.JobMelaRequest
import vedam.subkuch.ui.jobs.models.JobSalary
import vedam.subkuch.ui.jobs.models.JobSalaryResponse
import vedam.subkuch.utils.AppPrefs
import vedam.subkuch.utils.AppUtil
import vedam.subkuch.utils.UiUtil
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class SubmitFragment : BaseAddImagesFragment(), OnItemSelectedListener {
    private var fragmentSubmitBinding: FragmentSubmitBinding? = null
    private var isTwoWheelerOwner = false
    private var jobMelaRequest: JobMelaRequest? = null
    private var salaryId: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        jobMelaRequest = arguments?.getParcelable(Constants.EXTRA_DATA)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentSubmitBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_submit, container, false)
        return fragmentSubmitBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setImagesLayout(view, 1)
        bindCallbacks()
        getSalaries()
    }

    private fun getSalaries() {
        UiUtil.showProgressDialog(context, R.string.please_wait)
        getJobSalaries(
            context,
            onSalarySuccessListener,
            JobSalaryResponse::class.java,
            onErrorListener
        )
    }
    private val onSalarySuccessListener = Response.Listener { response: JobSalaryResponse? ->
        UiUtil.cancelProgressDialog()
        if (response != null && response.returnMessage == Constants.SUCCESS) {
            setJobQualifications(response.returnData)
        } else UiUtil.showToast(context, getString(R.string.no_data))
    }

    private fun setJobQualifications(jobSalaries: ArrayList<JobSalary>) {
        val jobSalary = JobSalary()
        jobSalary.salary = getString(R.string.select_a_salary)
        jobSalaries.add(0, jobSalary)
        val adapter = ArrayAdapter(
            context,
            android.R.layout.simple_spinner_dropdown_item, jobSalaries
        )
        fragmentSubmitBinding!!.spSalaryExpected.adapter = adapter
        fragmentSubmitBinding!!.spSalaryExpected.onItemSelectedListener = this
        fragmentSubmitBinding!!.spSalaryExpected.setSelection(0)
    }

    private fun bindCallbacks() {
//        fragmentSubmitBinding!!.btPrevious.setOnClickListener { v: View? -> if (activity != null && activity?.supportFragmentManager != null) activity?.supportFragmentManager?.popBackStack() }
        fragmentSubmitBinding!!.btSubmit.setOnClickListener { v: View? ->
            val errorMessage = validateErrorMessage()
            if (errorMessage == 0) {
                submit()
            } else UiUtil.showDialog(context, getString(errorMessage), true)
        }
        fragmentSubmitBinding!!.rbYes.setOnCheckedChangeListener { buttonView: CompoundButton?, isChecked: Boolean ->
            isTwoWheelerOwner = isChecked
        }
        fragmentSubmitBinding!!.rbNo.setOnCheckedChangeListener { buttonView: CompoundButton?, isChecked: Boolean ->
            isTwoWheelerOwner = !isChecked
        }
    }

    private fun uploadImage() {
        UiUtil.showProgressDialog(context, getString(R.string.please_wait))
        val params: MutableMap<String?, DataPart?> = HashMap()
        params[NetworkConstants.ProfileImage] = DataPart(
            AppUtil.getUniqueFileName(),
            AppUtil.getBytesFromBitmap(AppUtil.getSingleBitmap(context, imageItemMap)),
            NetworkConstants.JPEG_MIME_TYPE
        )
        uploadJobProfileImage(
            context,
            params,
            onImageUploadSuccessListener,
            GeneralResponse::class.java,
            onErrorListener
        )
    }

    private val onImageUploadSuccessListener = Response.Listener { response: GeneralResponse? ->
        UiUtil.cancelProgressDialog()
        if (activity != null) if (response != null && response.returnMessage == Constants.SUCCESS) {
            UiUtil.showToast(context, getString(R.string.job_profile_added))
            activity?.finish()
        } else UiUtil.showToast(context, getString(R.string.err_occurred))
    }

    private fun submit() {
        UiUtil.showProgressDialog(context, getString(R.string.please_wait))
        val userId = AppPrefs.getPrefsUserId(context)
        jobMelaRequest!!.userId = userId
        jobMelaRequest!!.isOwnTwoWheeler = isTwoWheelerOwner
        jobMelaRequest!!.jobSalaryId = salaryId
        addJobProfile(
            context,
            Gson().toJson(jobMelaRequest),
            onAddJobProfileSuccessListener,
            GeneralResponse::class.java,
            onErrorListener
        )
    }

    private val onAddJobProfileSuccessListener = Response.Listener { response: GeneralResponse? ->
        UiUtil.cancelProgressDialog()
        if (activity != null) if (response != null && response.returnMessage == Constants.SUCCESS) {
            if (imageItemMap.size > 0) uploadImage() else {
                UiUtil.showToast(context, getString(R.string.job_profile_added))
                activity?.finish()
            }
        } else UiUtil.showToast(context, getString(R.string.err_occurred))
    }

    private fun validateErrorMessage(): Int {
        var errorMessage = 0
        if (!fragmentSubmitBinding!!.rbYes.isChecked && !fragmentSubmitBinding!!.rbNo.isChecked) errorMessage =
            R.string.select_if_own_two_wheeler else if (TextUtils.isEmpty(salaryId)) errorMessage =
            R.string.enter_expected_salary
        return errorMessage
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        salaryId = (parent.getItemAtPosition(position) as JobSalary).jobSalaryId
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    companion object {
        fun newInstance(args: Bundle?): SubmitFragment {
            val fragment = SubmitFragment()
            fragment.arguments = args
            return fragment
        }
    }
}