package vedam.subkuch.ui.vehicle;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;

import java.util.ArrayList;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseFragment;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.ui.vehicle.models.VehicleTiming;
import vedam.subkuch.ui.vehicle.models.VehicleTimingResponse;
import vedam.subkuch.utils.UiUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class VehicleTimingFragment extends BaseFragment {

    private String vehicleType;
    private String cityName;
    private RecyclerView recyclerView;

    public VehicleTimingFragment() {
        // Required empty public constructor
    }


    public static VehicleTimingFragment newInstance(Bundle bundle) {
        VehicleTimingFragment fragment = new VehicleTimingFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getActivity() != null && getArguments() != null) {
            vehicleType = getArguments().getString(Constants.EXTRA_CATEGORY_ID);
            cityName = getArguments().getString(Constants.EXTRA_CITY_NAME);
            setTitle(cityName);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vehicle_timing, container, false);
    }

    public void onViewCreated(@NonNull View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        recyclerView = v.findViewById(R.id.rv_vehicles);
        getTimings();
    }

    private void getTimings() {
        UiUtil.showProgressDialog(context, R.string.please_wait);
        DataFetcher.getVehicleTimings(context, onVehicleSuccessListener, VehicleTimingResponse.class,
                onErrorListener, vehicleType, cityName);

    }

    private Response.Listener<VehicleTimingResponse> onVehicleSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (response != null && response.getReturnMessage().equalsIgnoreCase(Constants.SUCCESS)) {
            loadValues(response.getReturnData());
        } else
            UiUtil.showToast(context, getString(R.string.no_data));
    };

    private void loadValues(ArrayList<VehicleTiming> returnData) {

        VehicleTimingAdapter adapter = new VehicleTimingAdapter(context, returnData);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
    }
}
