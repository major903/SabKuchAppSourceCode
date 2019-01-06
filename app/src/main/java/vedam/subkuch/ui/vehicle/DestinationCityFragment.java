package vedam.subkuch.ui.vehicle;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Response;

import java.util.ArrayList;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseListFragment;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.utils.UiUtil;

public class DestinationCityFragment extends BaseListFragment {

    private ArrayList<DestinationCity> destinationCities;
    private String vehicleType;

    public DestinationCityFragment() {
        // Required empty public constructor
    }

    public static DestinationCityFragment newInstance(Bundle bundle) {
        DestinationCityFragment fragment = new DestinationCityFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getActivity() != null && getArguments() != null) {
            vehicleType = getArguments().getString(Constants.EXTRA_CATEGORY_ID);
            String vehicleName = getArguments().getString(Constants.EXTRA_CATEGORY_NAME);
            setTitle(vehicleName);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_directory, container, false);
    }

    public void onViewCreated(@NonNull View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        getDestinationCities();
    }

    private void getDestinationCities() {
        UiUtil.showProgressDialog(context, R.string.please_wait);
        DataFetcher.getDestinationCities(context, onVehicleSuccessListener, DestinationCityResponse.class, onErrorListener, vehicleType);

    }

    private Response.Listener<DestinationCityResponse> onVehicleSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (response != null && response.getReturnMessage().equalsIgnoreCase(Constants.SUCCESS)) {
            destinationCities = response.getReturnData();
            loadValues();
        } else
            UiUtil.showToast(context, getString(R.string.no_data));
    };

    private void loadValues() {

        ArrayAdapter<DestinationCity> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1,
                android.R.id.text1, destinationCities);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);


        Intent intent = new Intent(getActivity(),
                VehicleTimingActivity.class);
        DestinationCity vehicle = destinationCities.get(position);
        intent.putExtra(Constants.EXTRA_CATEGORY_ID, vehicleType);
        intent.putExtra(Constants.EXTRA_CITY_NAME, vehicle.getCityname());
        startActivity(intent);
    }
}
