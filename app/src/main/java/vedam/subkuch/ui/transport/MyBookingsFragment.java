package vedam.subkuch.ui.transport;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;

import java.util.ArrayList;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseFragment;
import vedam.subkuch.databinding.FragmentMyBookingsBinding;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.network.models.transport.TransportBooking;
import vedam.subkuch.network.models.transport.TransportBookingResponse;
import vedam.subkuch.ui.jobs.models.AddResponse;
import vedam.subkuch.utils.UiUtil;

public class MyBookingsFragment extends BaseFragment implements MyBookingsAdapter.BookingCompleteListener {

    private FragmentMyBookingsBinding fragmentMyBookingsBinding;
    private MyBookingsAdapter adapter;
    private ArrayList<TransportBooking> transportBookings = new ArrayList<>();
    private boolean loading = true;
    private LinearLayoutManager linearLayoutManager;
    private int pageNo = 1;
    private int pageSize = 20;
    private boolean hasMoreProjects = true;

    public MyBookingsFragment() {
        // Required empty public constructor
    }

    public static MyBookingsFragment newInstance() {
        return new MyBookingsFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        fragmentMyBookingsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_bookings, container, false);
        return fragmentMyBookingsBinding.getRoot();
    }

    public void onViewCreated(@NonNull View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        initUI();
        getMyBookings();
        setTitle(getString(R.string.my_bookings));
    }

    private void initUI() {

        linearLayoutManager = new LinearLayoutManager(mContext);
        fragmentMyBookingsBinding.rvEvents.setLayoutManager(linearLayoutManager);
        fragmentMyBookingsBinding.rvEvents.setHasFixedSize(true);
        adapter = new MyBookingsAdapter(mContext, transportBookings, this);
        fragmentMyBookingsBinding.rvEvents.setAdapter(adapter);
        fragmentMyBookingsBinding.rvEvents.addOnScrollListener(new OnScrollListener());
    }

    private void getMyBookings() {
        UiUtil.showProgressDialog(mContext, getString(R.string.please_wait));
        DataFetcher.getMyTransportBookings(mContext, onTransportBookingsSuccessListener, TransportBookingResponse.class, onErrorListener, pageNo, pageSize);

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
                    UiUtil.showToast(mContext, getString(R.string.no_transport_booking_found));
            } else
                UiUtil.showToast(mContext, getString(R.string.err_occurred));
    };

    private void loadValues(ArrayList<TransportBooking> response) {

        if (response != null && !response.isEmpty()) {
            pageNo++;
            transportBookings.addAll(response);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.add, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            startActivityForResult(new Intent(mContext, AddTransportActivity.class), Constants.REQUEST_ADD_TRANSPORT);
        }
        return super.onOptionsItemSelected(item);
    }

    private void setDefaults() {
        pageNo = 1;
        hasMoreProjects = true;
        transportBookings.clear();
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Constants.REQUEST_ADD_TRANSPORT) {
            if (resultCode == Activity.RESULT_OK) {
                setDefaults();
                getMyBookings();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBookingCompleteRequest(String transportId) {
        markComplete(transportId);
    }

    private void markComplete(String transportId) {
        UiUtil.showProgressDialog(mContext, getString(R.string.please_wait));
        DataFetcher.setTransportBookingComplete(mContext, onTransportCompleteSuccessListener, AddResponse.class, onErrorListener, transportId);

    }

    private Response.Listener<AddResponse> onTransportCompleteSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (getActivity() != null)
            if (response != null && response.getReturnMessage().equals(Constants.SUCCESS)) {
                UiUtil.showToast(mContext, getString(R.string.booking_completed_successfully));
                setDefaults();
                getMyBookings();
            } else
                UiUtil.showToast(mContext, getString(R.string.err_occurred));
    };
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
                        if (hasMoreProjects) getMyBookings();

                    }
                }
            }
        }
    }
}
