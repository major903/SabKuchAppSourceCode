package vedam.subkuch.ui.vehicle;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import vedam.subkuch.ui.vehicle.models.Vehicle;
import vedam.subkuch.ui.vehicle.models.VehicleResponse;
import vedam.subkuch.utils.UiUtil;

public class VehicleFragment extends BaseListFragment {

    private ArrayList<Vehicle> vehicles;

    public VehicleFragment() {
        // Required empty public constructor
    }

    public static VehicleFragment newInstance() {
        return new VehicleFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_directory, container, false);
    }

    public void onViewCreated(@NonNull View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        getVehicles();
        setTitle(R.string.vehicles);
    }

    private void getVehicles() {
        UiUtil.showProgressDialog(context, R.string.please_wait);
        DataFetcher.getVehicles(context, onVehicleSuccessListener, VehicleResponse.class, onErrorListener);

    }

    private Response.Listener<VehicleResponse> onVehicleSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (response != null && response.getReturnMessage().equalsIgnoreCase(Constants.SUCCESS)) {
            vehicles = response.getReturnData();
            loadValues();
        } else
            UiUtil.showToast(context, getString(R.string.no_data));
    };

    private void loadValues() {

        ArrayAdapter<Vehicle> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1,
                android.R.id.text1, vehicles);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);


        Intent intent = new Intent(getActivity(),
                DestinationCityActivity.class);
        Vehicle vehicle = vehicles.get(position);

        intent.putExtra(Constants.EXTRA_CATEGORY_ID,
                vehicle.getVehicletype());
        intent.putExtra(Constants.EXTRA_CATEGORY_NAME, vehicle.getVehicle());
        startActivity(intent);
    }
}
