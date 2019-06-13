package vedam.subkuch.ui.events;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.tsongkha.spinnerdatepicker.DatePicker;
import com.tsongkha.spinnerdatepicker.DatePickerDialog;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseAddImagesFragment;
import vedam.subkuch.databinding.FragmentAddEventBinding;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.network.NetworkConstants;
import vedam.subkuch.network.models.AddEventResponse;
import vedam.subkuch.network.models.DataPart;
import vedam.subkuch.network.models.GeneralResponse;
import vedam.subkuch.utils.AppPrefs;
import vedam.subkuch.utils.AppUtil;
import vedam.subkuch.utils.UiUtil;

import static android.app.Activity.RESULT_OK;


public class AddEventFragment extends BaseAddImagesFragment implements DatePickerDialog.OnDateSetListener {

    private FragmentAddEventBinding fragmentAddEventBinding;
    private LatLng latLng;

    public AddEventFragment() {
        // Required empty public constructor
    }

    public static AddEventFragment newInstance() {

        Bundle args = new Bundle();

        AddEventFragment fragment = new AddEventFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentAddEventBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_event, container, false);
        return fragmentAddEventBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setImagesLayout(view, 1);
        bind();
    }

    private void bind() {

        fragmentAddEventBinding.btAddLocation.setOnClickListener(view -> {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

            try {
                if (getActivity() != null)
                    startActivityForResult(builder.build(getActivity()), Constants.REQUEST_PLACE_PICKER);
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            }
        });

        fragmentAddEventBinding.etDate.setOnClickListener(v -> showDatePickerDialog());
    }

    public void showDatePickerDialog() {

        long millis = System.currentTimeMillis();
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millis);
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        new SpinnerDatePickerDialogBuilder()
                .context(context)
                .callback(this)
                .spinnerTheme(R.style.DatePickerTheme)
                .minDate(mYear, mMonth, mDay)
                .defaultDate(mYear, mMonth, mDay)
                .build()
                .show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.done, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_done) {
            int errorMessage = validateErrorMessage();
            if (errorMessage == 0) {
                createEvent();
            } else
                UiUtil.showDialog(context, getString(errorMessage), true);
        }
        return super.onOptionsItemSelected(item);
    }

    private void createEvent() {
        UiUtil.showProgressDialog(context, context.getString(R.string.please_wait));
        Map<String, String> request = new HashMap<>();
//        request.put(Constants.Title, "Title");
        String userId = AppPrefs.getPrefsUserId(context);
        request.put(Constants.userid, userId);
        request.put(Constants.Date, fragmentAddEventBinding.etDate.getText().toString());
        request.put(Constants.Time, fragmentAddEventBinding.etTime.getText().toString());
        request.put(Constants.Venue, fragmentAddEventBinding.etVenue.getText().toString());
        request.put(Constants.Title, fragmentAddEventBinding.etTitle.getText().toString());
        request.put(Constants.EntryFee, fragmentAddEventBinding.etEntryFee.getText().toString());
        request.put(Constants.Latitude, String.valueOf(latLng.latitude));
        request.put(Constants.Longitude, String.valueOf(latLng.longitude));
        /*if (!getImageItemMap().isEmpty())
            request.put(Constants.image, AppUtil.getBase64FromBitmap(AppUtil.getSingleBitmap(context, getImageItemMap())));*/


        DataFetcher.addEvent(context, new Gson().toJson(request), onAddEventSuccessListener, AddEventResponse.class, onErrorListener);
    }

    private Response.Listener<AddEventResponse> onAddEventSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (getActivity() != null)
            if (response != null && response.getReturnMessage().equals(Constants.SUCCESS))
                isImageAvailable(response.getReturnData().getID());
            else
                UiUtil.showToast(context, context.getString(R.string.err_occurred));
    };

    private void isImageAvailable(String eventId) {

        if (getImageItemMap().size() > 0)
            uploadEventImage(eventId);
        else {
            UiUtil.showToast(context, context.getString(R.string.event_added));
            if (getGlobalFragmentInteractionListener() != null) {
                getGlobalFragmentInteractionListener().finishActivity();
            }
        }
    }

    private void uploadEventImage(String eventId) {

        UiUtil.showProgressDialog(context, getString(R.string.please_wait));
        Map<String, DataPart> params = new HashMap<>();
        params.put(NetworkConstants.ProfileImage, new DataPart(AppUtil.getUniqueFileName(),
                AppUtil.getBytesFromBitmap(AppUtil.getSingleBitmap(context, getImageItemMap()))
                , NetworkConstants.JPEG_MIME_TYPE));

        DataFetcher.uploadEventImage(context, params, onImageUploadSuccessListener, GeneralResponse.class, onErrorListener, eventId);
    }

    private Response.Listener<GeneralResponse> onImageUploadSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (getActivity() != null)
            if (response != null && response.getReturnMessage().equals(Constants.SUCCESS)) {
                UiUtil.showToast(context, getString(R.string.event_added));
                getActivity().finish();
            } else
                UiUtil.showToast(context, getString(R.string.err_occurred));
    };

    private int validateErrorMessage() {
        int errorMessage = 0;
        if (TextUtils.isEmpty(fragmentAddEventBinding.etTitle.getText().toString()))
            errorMessage = R.string.enter_title;
        else if (TextUtils.isEmpty(fragmentAddEventBinding.etDate.getText()))
            errorMessage = R.string.enter_date;
        else if (!AppUtil.validateDob(fragmentAddEventBinding.etDate.getText().toString()))
            errorMessage = R.string.enter_valid_date;
        else if (TextUtils.isEmpty(fragmentAddEventBinding.etTime.getText()))
            errorMessage = R.string.enter_event_time;
        else if (TextUtils.isEmpty(fragmentAddEventBinding.etVenue.getText()))
            errorMessage = R.string.enter_event_venue;
        else if (latLng == null)
            errorMessage = R.string.add_a_location;

        return errorMessage;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_PLACE_PICKER) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(context, data);
                fragmentAddEventBinding.tvLocation.setText(place.getName());
                latLng = place.getLatLng();
            }
        } else
            super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(AppUtil.getZeroedString(dayOfMonth)).append("/").append(AppUtil.getZeroedString(monthOfYear + 1))
                .append("/").append(year);
        fragmentAddEventBinding.etDate.setText(stringBuilder);
    }
}
