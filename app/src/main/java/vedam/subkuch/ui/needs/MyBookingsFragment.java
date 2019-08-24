package vedam.subkuch.ui.needs;

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
import androidx.annotation.Nullable;
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
import vedam.subkuch.network.models.needs.Need;
import vedam.subkuch.network.models.needs.NeedResponse;
import vedam.subkuch.network.models.needs.Provider;
import vedam.subkuch.ui.jobs.models.AddResponse;
import vedam.subkuch.utils.UiUtil;

public class MyBookingsFragment extends BaseFragment implements MyBookingsAdapter.BookingCompleteListener {

    private FragmentMyBookingsBinding fragmentMyBookingsBinding;
    private MyBookingsAdapter adapter;
    private ArrayList<Need> needBookings = new ArrayList<>();
    private boolean loading = true;
    private LinearLayoutManager linearLayoutManager;
    private int pageNo = 1;
    private int pageSize = 20;
    private boolean hasMoreProjects = true;
    private Provider provider;

    public MyBookingsFragment() {
        // Required empty public constructor
    }

    public static MyBookingsFragment newInstance(Provider provider) {
        MyBookingsFragment fragment = new MyBookingsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.EXTRA_PROVIDER, provider);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            provider = getArguments().getParcelable(Constants.EXTRA_PROVIDER);
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
        setTitle(getString(R.string.my_needs));
    }

    private void initUI() {

        linearLayoutManager = new LinearLayoutManager(context);
        fragmentMyBookingsBinding.rvEvents.setLayoutManager(linearLayoutManager);
        fragmentMyBookingsBinding.rvEvents.setHasFixedSize(true);
        adapter = new MyBookingsAdapter(context, needBookings, this);
        fragmentMyBookingsBinding.rvEvents.setAdapter(adapter);
        fragmentMyBookingsBinding.rvEvents.addOnScrollListener(new OnScrollListener());
    }

    private void getMyBookings() {
        UiUtil.showProgressDialog(context, getString(R.string.please_wait));
        DataFetcher.getMyNeeds(context, onNeedBookingsSuccessListener, NeedResponse.class, onErrorListener, provider.getProviderId(), pageNo, pageSize);

    }

    private Response.Listener<NeedResponse> onNeedBookingsSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (getActivity() != null)
            if (response != null && response.getReturnMessage().equals(Constants.SUCCESS)) {
                if (response.getReturnData().size() > 0) {
                    hasMoreProjects = response.getReturnData().size() >= pageSize;
                    loading = true;
                    loadValues(response.getReturnData());
                } else
                    UiUtil.showCenterToast(context, getString(R.string.no_needs_found_details));
            } else
                UiUtil.showToast(context, getString(R.string.err_occurred));
    };

    private void loadValues(ArrayList<Need> response) {

        if (response != null && !response.isEmpty()) {
            pageNo++;
            needBookings.addAll(response);
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
            startActivityForResult(new Intent(context, AddNeedActivity.class)
                    .putExtra(Constants.EXTRA_ID, provider.getProviderId()), Constants.REQUEST_ADD_TRANSPORT);
        }
        return super.onOptionsItemSelected(item);
    }

    private void setDefaults() {
        pageNo = 1;
        hasMoreProjects = true;
        needBookings.clear();
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
    public void onBookingCompleteRequest(String needId) {
        markComplete(needId);
    }

    private void markComplete(String needId) {
        UiUtil.showProgressDialog(context, getString(R.string.please_wait));
        DataFetcher.setNeedBookingComplete(context, onNeedCompleteSuccessListener, AddResponse.class, onErrorListener, needId);

    }

    private Response.Listener<AddResponse> onNeedCompleteSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (getActivity() != null)
            if (response != null && response.getReturnMessage().equals(Constants.SUCCESS)) {
                UiUtil.showToast(context, getString(R.string.booking_completed_successfully));
                setDefaults();
                getMyBookings();
            } else
                UiUtil.showToast(context, getString(R.string.err_occurred));
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
