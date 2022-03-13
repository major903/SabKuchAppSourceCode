package vedam.subkuch.ui.jobs

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
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
import com.google.gson.reflect.TypeToken
import vedam.subkuch.R
import vedam.subkuch.base.BaseAddImageFragment
import vedam.subkuch.databinding.FragmentSubmitBinding
import vedam.subkuch.helpers.Constants
import vedam.subkuch.network.DataFetcher
import vedam.subkuch.network.DataFetcher.getJobSalaries
import vedam.subkuch.network.DataFetcher.uploadJobProfileImage
import vedam.subkuch.network.DataFetcher.withdraw
import vedam.subkuch.network.NetworkConstants
import vedam.subkuch.network.models.*
import vedam.subkuch.network.models.wallet.WalletResponse
import vedam.subkuch.ui.jobs.models.*
import vedam.subkuch.ui.shopping.show
import vedam.subkuch.utils.AppPrefs
import vedam.subkuch.utils.AppUtil
import vedam.subkuch.utils.UiUtil
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class ApplyJobFragment : BaseAddImageFragment(), OnItemSelectedListener {
    private var binding: FragmentSubmitBinding? = null
    private var isTwoWheelerOwner = false
    private var jobMelaRequest: JobMelaRequest? = null
    private var post: Post? = null
    private var salaryId: String? = null
    private var jobExperienceId: String? = null
    private var walletResponse: WalletResponse? = null
    private var jobExperiences: ArrayList<JobExperience>? = null
    private var jobSalaries: ArrayList<JobSalary>? = null
    private var stack = Stack<Any>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        post = arguments?.getParcelable(Constants.EXTRA_DATA)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_submit, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setImagesLayout(view)
        bindCallbacks()
        getWalletDetails()
    }

    private val onWalletSuccessListener = Response.Listener { response: WalletResponse? ->
        walletResponse = response
        loadUI()
    }

    private val onViewJobSuccessListener =
        Response.Listener { response: BaseGetMasterModel<JobMelaRequest>? ->
            fillJobDetails(response?.returnData?.get(0))
        }

    private fun fillJobDetails(response: JobMelaRequest?) {
        binding?.etMoreExperience?.setText(response?.jobExperienceDetails ?: "")
        binding?.etQualification?.setText(response?.jobQualification ?: "")
        if (response?.isOwnTwoWheeler == true)
            binding?.rbYes?.isChecked = true
        else
            binding?.rbNo?.isChecked = true
        response?.jobExpName?.let { expName ->
            val expPos = jobExperiences?.indexOfFirst { it.jobExpName == expName } ?: 0
            if (expPos >= 0)
                binding?.spExperience?.setSelection(expPos)
        }

        response?.jobSalaryId?.let { salId ->
            val pos = jobSalaries?.indexOfFirst { it.jobSalaryId == salId } ?: 0
            if (pos >= 0)
                binding?.spSalaryExpected?.setSelection(pos)
        }
    }

    private fun loadUI() {
        UiUtil.cancelProgressDialog()
        val balance =
            (walletResponse?.returnData?.wallet?.availableBalance?.split(".")?.get(1))?.trim()
                ?.toIntOrNull() ?: 0
        if (balance >= 10) {
            binding?.tvMessage?.show()
            binding?.tvMessage?.text = getString(R.string.yes_money, balance)
            binding?.scrollView?.show()
            binding?.btSubmit?.show()
            stack.add(Any())
            stack.add(Any())
            getSalaries()
//            getJobQualifications()
            getJobExperiences()
        } else {
            binding?.tvMessage?.show()
            val ss = SpannableString(getString(R.string.no_money, balance))
            val clickableSpan: ClickableSpan = object : ClickableSpan() {
                override fun onClick(textView: View) {
                    AppUtil.openUrl(context, "https://vedam-it.com/sabkuch.html")
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = true
                }
            }
            ss.setSpan(clickableSpan, 169, 179, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            binding?.tvMessage?.movementMethod = LinkMovementMethod.getInstance()
            binding?.tvMessage?.text = ss
            binding?.tvMessage?.highlightColor = Color.TRANSPARENT
        }
    }

    private fun getViewProfile() {
        val type = object : TypeToken<BaseGetMasterModel<JobMelaRequest>>() {}.type
        DataFetcher.getJobProfile(
            context,
            onViewJobSuccessListener,
            type,
            onErrorListener
        )
    }

    private fun getWalletDetails() {
        UiUtil.showProgressDialog(context, R.string.please_wait)
        DataFetcher.getWalletDetails(
            context,
            onWalletSuccessListener,
            WalletResponse::class.java,
            onErrorListener
        )
    }

    private fun getJobExperiences() {
        UiUtil.showProgressDialog(context, R.string.please_wait)
        DataFetcher.getJobExperiences(
            context,
            onExperienceSuccessListener,
            JobExperienceResponse::class.java,
            onErrorListener
        )
    }

    private val onExperienceSuccessListener =
        Response.Listener { response: JobExperienceResponse? ->
            UiUtil.cancelProgressDialog()
            stack.pop()
            if (activity != null) if (response != null && response.returnMessage == Constants.SUCCESS) {
                setJobExperiences(response.returnData)
                checkAndViewProfile()
            } else UiUtil.showToast(context, getString(R.string.no_data))
        }

    private fun setJobExperiences(jobExperiences: ArrayList<JobExperience>) {
        this.jobExperiences = jobExperiences
        val jobExperience = JobExperience()
        jobExperience.jobExpName = getString(R.string.select_a_job_experience)
        jobExperiences.add(0, jobExperience)
        val adapter = ArrayAdapter(
            context,
            android.R.layout.simple_spinner_dropdown_item, jobExperiences
        )
        binding?.spExperience?.adapter = adapter
        binding?.spExperience?.onItemSelectedListener = this
        binding?.spExperience?.setSelection(0)
    }

    private fun getJobQualifications() {
//        UiUtil.showProgressDialog(context, R.string.please_wait)
//        DataFetcher.getJobQualifications(
//            context,
//            onQualifySuccessListener,
//            JobQualificationResponse::class.java,
//            onErrorListener
//        )
    }

//    private val onQualifySuccessListener =
//        Response.Listener { response: JobQualificationResponse? ->
//            UiUtil.cancelProgressDialog()
//            if (activity != null) if (response != null && response.returnMessage == Constants.SUCCESS) {
//                setJobQualifications(response.returnData)
//            } else UiUtil.showToast(context, getString(R.string.no_data))
//        }

//    private fun setJobQualifications(jobQualifications: ArrayList<JobQualification>) {
//        val jobQualification = JobQualification()
//        jobQualification.qualificationName = getString(R.string.select_a_job_qualification)
//        jobQualifications.add(0, jobQualification)
//        val adapter = ArrayAdapter(
//            context,
//            android.R.layout.simple_spinner_dropdown_item, jobQualifications
//        )
//        binding?.spQualification?.adapter = adapter
//        binding?.spQualification?.onItemSelectedListener = this
//        binding?.spQualification?.setSelection(0)
//    }

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
        stack.pop()
        if (response != null && response.returnMessage == Constants.SUCCESS) {
            setJobSalaries(response.returnData)
            checkAndViewProfile()
        } else UiUtil.showToast(context, getString(R.string.no_data))
    }

    private fun setJobSalaries(jobSalaries: ArrayList<JobSalary>) {
        this.jobSalaries = jobSalaries
        val jobSalary = JobSalary()
        jobSalary.salary = getString(R.string.select_a_salary)
        jobSalaries.add(0, jobSalary)
        val adapter = ArrayAdapter(
            context,
            android.R.layout.simple_spinner_dropdown_item, jobSalaries
        )
        binding!!.spSalaryExpected.adapter = adapter
        binding!!.spSalaryExpected.onItemSelectedListener = this
        binding!!.spSalaryExpected.setSelection(0)
    }

    private fun checkAndViewProfile() {
        if (stack.isEmpty()) {
            getViewProfile()
        }
    }

    private fun bindCallbacks() {
        binding!!.btSubmit.setOnClickListener { v: View? ->
            val errorMessage = validateErrorMessage()
            if (errorMessage == 0) {
                submit()
            } else UiUtil.showDialog(context, getString(errorMessage), true)
        }
        binding!!.rbYes.setOnCheckedChangeListener { buttonView: CompoundButton?, isChecked: Boolean ->
            isTwoWheelerOwner = isChecked
        }
        binding!!.rbNo.setOnCheckedChangeListener { buttonView: CompoundButton?, isChecked: Boolean ->
            isTwoWheelerOwner = !isChecked
        }
    }

    private fun uploadImage() {
        UiUtil.showProgressDialog(context, getString(R.string.please_wait))
        val params: MutableMap<String?, DataPart?> = HashMap()
        params[NetworkConstants.ProfileImage] = DataPart(
            AppUtil.getUniqueFileName(),
            AppUtil.getBytesFromBitmap(AppUtil.getBitmap(context, imageUri)),
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
        withdraw()
    }

    private fun withdraw() {
        UiUtil.showProgressDialog(context, getString(R.string.please_wait))
        val withdrawalRequest = WithdrawalRequest()
        withdrawalRequest.setAmount("10")
        withdrawalRequest.setVendorCode("180204")
        withdraw(
            context, Gson().toJson(withdrawalRequest), onWithdrawalSuccessListener,
            AddResponse::class.java, onErrorListener
        )
    }

    private val onWithdrawalSuccessListener =
        Response.Listener { response: AddResponse? ->
            UiUtil.cancelProgressDialog()
            var errorMessage: String? = getString(R.string.err_occurred)
            if (response != null && !TextUtils.isEmpty(response.returnMessage)) errorMessage =
                response.returnMessage
            if (response != null && response.returnMessage == Constants.SUCCESS) {
                submitJobApplication()
            } else UiUtil.showToast(context, errorMessage!!)
        }

    private fun submitJobApplication() {
        val userId = AppPrefs.getPrefsUserId(context)
        jobMelaRequest = JobMelaRequest()
        jobMelaRequest!!.jobpostId = post?.jobpostId
        jobMelaRequest!!.userId = userId
        jobMelaRequest!!.isOwnTwoWheeler = isTwoWheelerOwner
        jobMelaRequest!!.jobSalaryId = salaryId
        jobMelaRequest!!.jobTypes = intArrayOf(0)
        jobMelaRequest!!.jobQualification = binding!!.etQualification.text.toString()
        jobMelaRequest!!.jobExperienceId = jobExperienceId
        jobMelaRequest!!.jobExperienceDetails =
            binding!!.etMoreExperience.text.toString()

        DataFetcher.addJobProfile(
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
            if (imageUri != null) uploadImage() else {
                UiUtil.showToast(context, getString(R.string.job_profile_added))
                activity?.finish()
            }
        } else UiUtil.showToast(context, getString(R.string.err_occurred))
    }

    private fun validateErrorMessage(): Int {
        var errorMessage = 0
        if (binding?.etQualification?.text?.isBlank() == true) errorMessage =
            R.string.select_a_job_qualification else if (TextUtils.isEmpty(jobExperienceId)) errorMessage =
            R.string.select_a_job_experience else if (jobExperienceId != Constants.FRESHER && TextUtils.isEmpty(
                binding?.etMoreExperience?.text
            )
        ) errorMessage = R.string.enter_experience_details
        else if (!binding!!.rbYes.isChecked && !binding!!.rbNo.isChecked) errorMessage =
            R.string.select_if_own_two_wheeler else if (TextUtils.isEmpty(salaryId)) errorMessage =
            R.string.enter_expected_salary
        return errorMessage
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        when (parent.id) {
//            R.id.sp_qualification -> jobQualificationId =
//                (parent.getItemAtPosition(position) as JobQualification).qulaificationId
            R.id.sp_experience -> jobExperienceId =
                (parent.getItemAtPosition(position) as JobExperience).jobExpId
            R.id.sp_salary_expected -> salaryId =
                (parent.getItemAtPosition(position) as JobSalary).jobSalaryId
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    companion object {

        fun newInstance(extras: Bundle?): ApplyJobFragment {
            val fragment = ApplyJobFragment()
            fragment.arguments = extras
            return fragment

        }
    }
}