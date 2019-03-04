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

import java.util.HashMap;
import java.util.Map;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseAddImagesFragment;
import vedam.subkuch.databinding.FragmentAddEventBinding;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.network.models.AddEventResponse;
import vedam.subkuch.utils.AppPrefs;
import vedam.subkuch.utils.AppUtil;
import vedam.subkuch.utils.UiUtil;

import static android.app.Activity.RESULT_OK;


public class AddEventFragment extends BaseAddImagesFragment {

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
                startActivityForResult(builder.build(getActivity()), Constants.REQUEST_PLACE_PICKER);
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.done, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
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
        request.put(Constants.About, fragmentAddEventBinding.etDetails.getText().toString());
        request.put(Constants.EntryFee, fragmentAddEventBinding.etEntryFee.getText().toString());
        request.put("Lat", String.valueOf(latLng.latitude));
        request.put("Long", String.valueOf(latLng.longitude));
        if (!getImageItemMap().isEmpty())
            request.put(Constants.image, AppUtil.getBase64FromBitmap(AppUtil.getSingleBitmap(context, getImageItemMap())));


        DataFetcher.addEvent(context, new Gson().toJson(request), onAddEventSuccessListener, AddEventResponse.class, onErrorListener);
    }

    private Response.Listener<AddEventResponse> onAddEventSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (response != null && response.getReturnMessage().equals(Constants.SUCCESS)) {
            UiUtil.showToast(context, context.getString(R.string.event_added));
            if (getGlobalFragmentInteractionListener() != null) {
                getGlobalFragmentInteractionListener().finishActivity();
            }
        } else
            UiUtil.showToast(context, context.getString(R.string.err_occurred));
    };

    private int validateErrorMessage() {
        int errorMessage = 0;
        if (TextUtils.isEmpty(fragmentAddEventBinding.etDate.getText()))
            errorMessage = R.string.enter_date;
        else if (!AppUtil.validateDob(fragmentAddEventBinding.etDate.getText().toString()))
            errorMessage = R.string.enter_valid_date;
        else if (TextUtils.isEmpty(fragmentAddEventBinding.etDetails.getText().toString()))
            errorMessage = R.string.enter_details;
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
}
