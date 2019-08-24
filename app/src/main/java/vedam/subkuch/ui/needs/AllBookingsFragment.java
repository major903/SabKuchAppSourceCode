package vedam.subkuch.ui.needs;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;

import java.util.ArrayList;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseFragment;
import vedam.subkuch.databinding.FragmentAllBookingsBinding;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.network.models.needs.Need;
import vedam.subkuch.network.models.needs.NeedResponse;
import vedam.subkuch.network.models.needs.Provider;
import vedam.subkuch.utils.UiUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllBookingsFragment extends BaseFragment {

    private FragmentAllBookingsBinding fragmentAllBookingsBinding;
    private AllBookingsAdapter adapter;
    private ArrayList<Need> needBookings = new ArrayList<>();
    private boolean loading = true;
    private LinearLayoutManager linearLayoutManager;
    private int pageNo = 1;
    private int pageSize = 20;
    private boolean hasMoreProjects = true;
    private Provider provider;

    public AllBookingsFragment() {
        // Required empty public constructor
    }

    public static AllBookingsFragment newInstance(Provider provider) {
        AllBookingsFragment fragment = new AllBookingsFragment();
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
        // Inflate the layout for this fragment
        fragmentAllBookingsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_all_bookings, container, false);
        return fragmentAllBookingsBinding.getRoot();
    }

    public void onViewCreated(@NonNull View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        setTitle(getString(R.string.my_needs));
        initUI();
        getAllBookings();
    }

    private void initUI() {

        linearLayoutManager = new LinearLayoutManager(context);
        fragmentAllBookingsBinding.rvEvents.setLayoutManager(linearLayoutManager);
        fragmentAllBookingsBinding.rvEvents.setHasFixedSize(true);
        adapter = new AllBookingsAdapter(context, needBookings);
        fragmentAllBookingsBinding.rvEvents.setAdapter(adapter);
        fragmentAllBookingsBinding.rvEvents.addOnScrollListener(new OnScrollListener());
    }

    private void getAllBookings() {
        UiUtil.showProgressDialog(context, getString(R.string.please_wait));
        DataFetcher.getAllNeeds(context, onNeedsSuccessListener, NeedResponse.class, onErrorListener, provider.getProviderId(), pageNo, pageSize);

    }

    private Response.Listener<NeedResponse> onNeedsSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (getActivity() != null)
            if (response != null && response.getReturnMessage().equals(Constants.SUCCESS)) {
                if (response.getReturnData().size() > 0) {
                    hasMoreProjects = response.getReturnData().size() >= pageSize;
                    loading = true;
                    loadValues(response.getReturnData());
                } else
                    UiUtil.showToast(context, getString(R.string.no_needs_found));
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
