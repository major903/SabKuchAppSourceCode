package vedam.subkuch.ui.transport;


import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;

import java.util.ArrayList;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseFragment;
import vedam.subkuch.databinding.FragmentAllBookingsBinding;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.network.models.transport.TransportBooking;
import vedam.subkuch.network.models.transport.TransportBookingResponse;
import vedam.subkuch.utils.UiUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllBookingsFragment extends BaseFragment {

    private FragmentAllBookingsBinding fragmentAllBookingsBinding;
    private AllBookingsAdapter adapter;
    private ArrayList<TransportBooking> transportBookings = new ArrayList<>();
    private boolean loading = true;
    private LinearLayoutManager linearLayoutManager;
    private int pageNo = 1;
    private int pageSize = 20;
    private boolean hasMoreProjects = true;

    public AllBookingsFragment() {
        // Required empty public constructor
    }

    public static AllBookingsFragment newInstance() {
        return new AllBookingsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentAllBookingsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_all_bookings, container, false);
        return fragmentAllBookingsBinding.getRoot();
    }

    public void onViewCreated(@NonNull View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        initUI();
        getAllBookings();
    }

    private void initUI() {

        linearLayoutManager = new LinearLayoutManager(context);
        fragmentAllBookingsBinding.rvEvents.setLayoutManager(linearLayoutManager);
        fragmentAllBookingsBinding.rvEvents.setHasFixedSize(true);
        adapter = new AllBookingsAdapter(context, transportBookings);
        fragmentAllBookingsBinding.rvEvents.setAdapter(adapter);
        fragmentAllBookingsBinding.rvEvents.addOnScrollListener(new OnScrollListener());
    }

    public void getAllBookings() {
        UiUtil.showProgressDialog(context, getString(R.string.please_wait));
        DataFetcher.getAllTransportBookings(context, onTransportBookingsSuccessListener, TransportBookingResponse.class, onErrorListener, pageNo, pageSize);

    }

    private Response.Listener<TransportBookingResponse> onTransportBookingsSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (getActivity() != null)
            if (response != null && response.getReturnMessage().equals(Constants.SUCCESS)) {
                if (response.getReturnData().size() > 0) {
                    hasMoreProjects = response.getReturnData().size() >= pageSize;
                    loading = true;
                    loadValues(response.getReturnData());
                } else
                    UiUtil.showToast(context, getString(R.string.no_transport_booking_found));
            } else
                UiUtil.showToast(context, getString(R.string.err_occurred));
    };

    private void loadValues(ArrayList<TransportBooking> response) {

        if (response != null && !response.isEmpty()) {
            pageNo++;
            transportBookings.addAll(response);
            adapter.notifyDataSetChanged();
        }
    }

    public class OnScrollListener extends RecyclerView.OnScrollListener {

        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            /*final Picasso picasso = Picasso.get();

            if (newState == RecyclerView.SCROLL_STATE_IDLE || newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                picasso.resumeTag(context);
            } else {
                picasso.pauseTag(context);
            }*/
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            if (dy > 0) //check for scroll down
            {
                int visibleItemCount = linearLayoutManager.getChildCount();
                int totalItemCount = linearLayoutManager.getItemCount();
                int pastVisibleItems = linearLayoutManager.findFirstVisibleItemPosition();

                if (loading) {
                    if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                        loading = false;
                        if (hasMoreProjects) getAllBookings();

                    }
                }
            }
        }
    }
}
