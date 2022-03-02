package vedam.subkuch.ui.jobs.jobmela

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import com.android.volley.Response
import vedam.subkuch.R
import vedam.subkuch.base.BaseFragment
import vedam.subkuch.databinding.FragmentExperienceBinding
import vedam.subkuch.helpers.Constants
import vedam.subkuch.network.DataFetcher.getJobExperiences
import vedam.subkuch.network.DataFetcher.getJobQualifications
import vedam.subkuch.ui.jobs.models.*
import vedam.subkuch.utils.UiUtil
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class ExperienceFragment : BaseFragment(), OnItemSelectedListener {
    private var fragmentExperienceBinding: FragmentExperienceBinding? = null
    private var jobMelaRequest: JobMelaRequest? = null
    private var jobQualificationId: String? = null
    private var jobExperienceId: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        jobMelaRequest = arguments?.getParcelable(Constants.EXTRA_DATA)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentExperienceBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_experience, container, false)
        return fragmentExperienceBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindCallbacks()
        getJobQualifications()
        getJobExperiences()
    }

    private fun getJobExperiences() {
        UiUtil.showProgressDialog(context, R.string.please_wait)
        getJobExperiences(
            context,
            onExperienceSuccessListener,
            JobExperienceResponse::class.java,
            onErrorListener
        )
    }
    private val onExperienceSuccessListener =
        Response.Listener { response: JobExperienceResponse? ->
            UiUtil.cancelProgressDialog()
            if (activity != null) if (response != null && response.returnMessage == Constants.SUCCESS) {
                setJobExperiences(response.returnData)
            } else UiUtil.showToast(context, getString(R.string.no_data))
        }

    private fun setJobExperiences(jobExperiences: ArrayList<JobExperience>) {
        val jobExperience = JobExperience()
        jobExperience.jobExpName = getString(R.string.select_a_job_experience)
        jobExperiences.add(0, jobExperience)
        val adapter = ArrayAdapter(
            context,
            android.R.layout.simple_spinner_dropdown_item, jobExperiences
        )
        fragmentExperienceBinding!!.spExperience.adapter = adapter
        fragmentExperienceBinding!!.spExperience.onItemSelectedListener = this
        fragmentExperienceBinding!!.spExperience.setSelection(0)
    }

    private fun getJobQualifications() {
        UiUtil.showProgressDialog(context, R.string.please_wait)
        getJobQualifications(
            context,
            onQualifySuccessListener,
            JobQualificationResponse::class.java,
            onErrorListener
        )
    }
    private val onQualifySuccessListener =
        Response.Listener { response: JobQualificationResponse? ->
            UiUtil.cancelProgressDialog()
            if (activity != null) if (response != null && response.returnMessage == Constants.SUCCESS) {
                setJobQualifications(response.returnData)
            } else UiUtil.showToast(context, getString(R.string.no_data))
        }

    private fun setJobQualifications(jobQualifications: ArrayList<JobQualification>) {
        val jobQualification = JobQualification()
        jobQualification.qualificationName = getString(R.string.select_a_job_qualification)
        jobQualifications.add(0, jobQualification)
        val adapter = ArrayAdapter(
            context,
            android.R.layout.simple_spinner_dropdown_item, jobQualifications
        )
        fragmentExperienceBinding!!.spQualification.adapter = adapter
        fragmentExperienceBinding!!.spQualification.onItemSelectedListener = this
        fragmentExperienceBinding!!.spQualification.setSelection(0)
    }

    private fun bindCallbacks() {
        fragmentExperienceBinding!!.btPrevious.setOnClickListener { v: View? -> if (activity != null && activity?.supportFragmentManager != null) activity?.supportFragmentManager?.popBackStack() }
        fragmentExperienceBinding!!.btNext.setOnClickListener { v: View? ->
            val errorMessage = validateErrorMessage()
            if (errorMessage == 0) {
                next()
            } else UiUtil.showDialog(context, getString(errorMessage), true)
        }
    }

    private operator fun next() {
        jobMelaRequest!!.jobQualificationId = jobQualificationId
        jobMelaRequest!!.jobExperienceId = jobExperienceId
        jobMelaRequest!!.jobExperienceDetails =
            fragmentExperienceBinding!!.etMoreExperience.text.toString()
        val args = Bundle()
        args.putParcelable(Constants.EXTRA_DATA, jobMelaRequest)
        addFragmentWithAnimation(R.id.content_frame, SubmitFragment.newInstance(args), null, true)
    }

    private fun validateErrorMessage(): Int {
        var errorMessage = 0
        if (TextUtils.isEmpty(jobQualificationId)) errorMessage =
            R.string.select_a_job_qualification else if (TextUtils.isEmpty(jobExperienceId)) errorMessage =
            R.string.select_a_job_experience else if (jobExperienceId != Constants.FRESHER && TextUtils.isEmpty(
                fragmentExperienceBinding!!.etMoreExperience.text
            )
        ) errorMessage = R.string.enter_experience_details
        return errorMessage
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        when (parent.id) {
            R.id.sp_qualification -> jobQualificationId =
                (parent.getItemAtPosition(position) as JobQualification).qulaificationId
            R.id.sp_experience -> jobExperienceId =
                (parent.getItemAtPosition(position) as JobExperience).jobExpId
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    companion object {
        @JvmStatic
        fun newInstance(args: Bundle?): ExperienceFragment {
            val fragment = ExperienceFragment()
            fragment.arguments = args
            return fragment
        }
    }
}