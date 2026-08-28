package vedam.subkuch.ui.events

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import com.adevinta.leku.LATITUDE
import com.adevinta.leku.LOCATION_ADDRESS
import com.adevinta.leku.LONGITUDE
import com.adevinta.leku.LocationPickerActivity
import vedam.subkuch.network.Response
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.tsongkha.spinnerdatepicker.DatePicker
import com.tsongkha.spinnerdatepicker.DatePickerDialog
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder
import vedam.subkuch.R
import vedam.subkuch.base.BaseAddImagesFragment
import vedam.subkuch.databinding.FragmentAddEventBinding
import vedam.subkuch.helpers.Constants
import vedam.subkuch.network.DataFetcher.addEvent
import vedam.subkuch.network.DataFetcher.uploadEventImage
import vedam.subkuch.network.NetworkConstants
import vedam.subkuch.network.models.AddEventResponse
import vedam.subkuch.network.models.DataPart
import vedam.subkuch.network.models.GeneralResponse
import vedam.subkuch.utils.AppPrefs
import vedam.subkuch.utils.AppUtil
import vedam.subkuch.utils.UiUtil
import java.util.*

class AddEventFragment : BaseAddImagesFragment(), DatePickerDialog.OnDateSetListener {
    private var fragmentAddEventBinding: FragmentAddEventBinding? = null
    private var latLng: LatLng? = null
    private var successMessage: String? = null
    private val locationPickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            fragmentAddEventBinding!!.tvLocation.text = result.data?.getStringExtra(LOCATION_ADDRESS)
            latLng = LatLng(
                result.data?.getDoubleExtra(LATITUDE, 0.0) ?: 0.0,
                result.data?.getDoubleExtra(LONGITUDE, 0.0) ?: 0.0
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentAddEventBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_add_event, container, false)
        return fragmentAddEventBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menu.clear()
                menuInflater.inflate(R.menu.done, menu)
            }

            override fun onMenuItemSelected(item: MenuItem): Boolean {
                if (item.itemId != R.id.action_done) return false
                val errorMessage = validateErrorMessage()
                if (errorMessage == 0) createEvent()
                else UiUtil.showDialog(mContext, getString(errorMessage), true)
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
        setImagesLayout(view, 1)
        bind()
    }

    private fun bind() {
        fragmentAddEventBinding!!.btAddLocation.setOnClickListener {
            val locationPickerIntent = LocationPickerActivity.Builder()
                .withGeolocApiKey(Constants.MAPS_API_KEY)
                .withGooglePlacesApiKey(Constants.MAPS_API_KEY)
                .withDefaultLocaleSearchZone()
                .shouldReturnOkOnBackPressed()
                .withStreetHidden()
                .withCityHidden()
                .withZipCodeHidden()
                .withGoogleTimeZoneEnabled()
                .withVoiceSearchHidden()
                .build(requireContext())

            locationPickerLauncher.launch(locationPickerIntent)
        }
        fragmentAddEventBinding!!.etDate.setOnClickListener { v: View? -> showDatePickerDialog() }
    }

    private fun showDatePickerDialog() {
        val millis = System.currentTimeMillis()
        val c = Calendar.getInstance()
        c.timeInMillis = millis
        val mYear = c[Calendar.YEAR]
        val mMonth = c[Calendar.MONTH]
        val mDay = c[Calendar.DAY_OF_MONTH]
        SpinnerDatePickerDialogBuilder()
            .context(mContext)
            .callback(this)
            .spinnerTheme(R.style.DatePickerTheme)
            .minDate(mYear, mMonth, mDay)
            .defaultDate(mYear, mMonth, mDay)
            .build()
            .show()
    }

    private fun createEvent() {
        UiUtil.showProgressDialog(mContext, getString(R.string.please_wait))
        val request: MutableMap<String, String> = HashMap()
        //        request.put(Constants.Title, "Title");
        val userId = AppPrefs.getPrefsUserId(mContext)
        request[Constants.userid] = userId
        request[Constants.Date] = fragmentAddEventBinding!!.etDate.text.toString()
        request[Constants.Time] = fragmentAddEventBinding!!.etTime.text.toString()
        request[Constants.Venue] =
            fragmentAddEventBinding!!.etVenue.text.toString()
        request[Constants.Title] =
            fragmentAddEventBinding!!.etTitle.text.toString()
        request[Constants.EntryFee] =
            fragmentAddEventBinding!!.etEntryFee.text.toString()
        request[Constants.Latitude] = latLng!!.latitude.toString()
        request[Constants.Longitude] = latLng!!.longitude.toString()
        /*if (!getImageItemMap().isEmpty())
            request.put(Constants.image, AppUtil.getBase64FromBitmap(AppUtil.getSingleBitmap(context, getImageItemMap())));*/
        addEvent(
            mContext,
            Gson().toJson(request),
            onAddEventSuccessListener,
            AddEventResponse::class.java,
            onErrorListener
        )
    }

    private val onAddEventSuccessListener = Response.Listener { response: AddEventResponse? ->
        UiUtil.cancelProgressDialog()
        if (activity != null) if (response != null && !TextUtils.isEmpty(response.returnMessage)) {
            successMessage = response.returnMessage
            isImageAvailable(response.returnData.id)
        } else UiUtil.showToast(mContext, getString(R.string.err_occurred))
    }

    private fun isImageAvailable(eventId: String) {
        if (imageItemMap.size > 0) uploadEventImage(eventId) else {
            UiUtil.showToast(mContext, successMessage!!)
            if (globalFragmentInteractionListener != null) {
                globalFragmentInteractionListener?.finishActivity()
            }
        }
    }

    private fun uploadEventImage(eventId: String) {
        UiUtil.showProgressDialog(mContext, getString(R.string.please_wait))
        val params: MutableMap<String?, DataPart?> = HashMap()
        params[NetworkConstants.ProfileImage] = DataPart(
            AppUtil.getUniqueFileName(),
            AppUtil.getBytesFromBitmap(AppUtil.getSingleBitmap(mContext, imageItemMap)),
            NetworkConstants.JPEG_MIME_TYPE
        )
        uploadEventImage(
            mContext,
            params,
            onImageUploadSuccessListener,
            GeneralResponse::class.java,
            onErrorListener,
            eventId
        )
    }

    private val onImageUploadSuccessListener = Response.Listener { response: GeneralResponse? ->
        UiUtil.cancelProgressDialog()
        if (activity != null) if (response != null && Constants.SUCCESS == response.returnMessage) {
            UiUtil.showToast(mContext, successMessage!!)
            requireActivity().finish()
        } else UiUtil.showToast(mContext, getString(R.string.err_occurred))
    }

    private fun validateErrorMessage(): Int {
        var errorMessage = 0
        if (TextUtils.isEmpty(fragmentAddEventBinding!!.etTitle.text.toString())) errorMessage =
            R.string.enter_title else if (TextUtils.isEmpty(
                fragmentAddEventBinding!!.etDate.text
            )
        ) errorMessage = R.string.enter_date else if (!AppUtil.validateDob(
                fragmentAddEventBinding!!.etDate.text.toString()
            )
        ) errorMessage = R.string.enter_valid_date else if (TextUtils.isEmpty(
                fragmentAddEventBinding!!.etTime.text
            )
        ) errorMessage = R.string.enter_event_time else if (TextUtils.isEmpty(
                fragmentAddEventBinding!!.etVenue.text
            )
        ) errorMessage = R.string.enter_event_venue else if (latLng == null) errorMessage =
            R.string.add_a_location
        return errorMessage
    }

    override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val stringBuilder = StringBuilder()
        stringBuilder.append(AppUtil.getZeroedString(dayOfMonth)).append("/")
            .append(AppUtil.getZeroedString(monthOfYear + 1))
            .append("/").append(year)
        fragmentAddEventBinding!!.etDate.setText(stringBuilder)
    }

    companion object {
        @JvmStatic
        fun newInstance(): AddEventFragment {
            val args = Bundle()
            val fragment = AddEventFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
