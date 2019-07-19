package vedam.subkuch.ui.transport;


import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseFragment;
import vedam.subkuch.databinding.FragmentTransportBinding;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.network.models.ProfileResponse;
import vedam.subkuch.utils.UiUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransportFragment extends BaseFragment {

    private FragmentTransportBinding fragmentTransportBinding;
    private int userTypeId;

    public TransportFragment() {
        // Required empty public constructor
    }

    public static TransportFragment newInstance() {

        return new TransportFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentTransportBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_transport, container, false);
        return fragmentTransportBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getUserTypeId();
        bindCallbacks();
    }

    private void getUserTypeId() {
        UiUtil.showProgressDialog(context, getString(R.string.loading));
        DataFetcher.getProfile(context, onProfileSuccessListener, ProfileResponse.class, onErrorListener);

    }

    private Response.Listener<ProfileResponse> onProfileSuccessListener = response -> {
        UiUtil.cancelProgressDialog();
        if (response != null && response.getReturnMessage().equals(Constants.SUCCESS) &&
                response.getReturnData() != null && response.getReturnData().size() > 0) {
            userTypeId = response.getReturnData().get(0).getUserTypeId();
        } else {
            UiUtil.showToast(context, getString(R.string.err_occurred));
        }
    };

    private void bindCallbacks() {

        fragmentTransportBinding.ibBookFlight.setOnClickListener(v -> UiUtil.showToast(context, getString(R.string.coming_soon)));

        fragmentTransportBinding.ibBookHotels.setOnClickListener(v -> UiUtil.showToast(context, getString(R.string.coming_soon)));

        fragmentTransportBinding.ibBookTransport.setOnClickListener(v -> showBookings());
    }

    private void showBookings() {

        if (userTypeId == 0)
            UiUtil.showToast(context, getString(R.string.no_user_type));
        else if (userTypeId == Constants.USER_TYPE_TRANSPORT)
            addFragmentWithAnimation(R.id.content_frame, AllBookingsFragment.newInstance(), null, true);
        else
            addFragmentWithAnimation(R.id.content_frame, MyBookingsFragment.newInstance(), null, true);
    }
}
