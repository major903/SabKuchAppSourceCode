package vedam.subkuch.ui.transport;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import vedam.subkuch.network.Response;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseFragment;
import vedam.subkuch.databinding.FragmentTransportBinding;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.network.models.Profile;
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
        UiUtil.showProgressDialog(mContext, getString(R.string.loading));
        DataFetcher.getUserProfile(mContext, onProfileSuccessListener, Profile.class, onErrorListener);

    }

    private Response.Listener<Profile> onProfileSuccessListener = response -> {
        UiUtil.cancelProgressDialog();
        if (getActivity() != null)
            if (response != null && !TextUtils.isEmpty(response.getProfileId())) {
                userTypeId = response.getUserTypeId();
            } else {
                UiUtil.showToast(mContext, getString(R.string.err_occurred));
            }
    };

    private void bindCallbacks() {

        fragmentTransportBinding.ibBookFlight.setOnClickListener(v -> UiUtil.showToast(mContext, getString(R.string.coming_soon)));

        fragmentTransportBinding.ibBookHotels.setOnClickListener(v -> UiUtil.showToast(mContext, getString(R.string.coming_soon)));

        fragmentTransportBinding.ibBookTransport.setOnClickListener(v -> showBookings());
    }

    private void showBookings() {

        if (userTypeId == 0)
            UiUtil.showToast(mContext, getString(R.string.no_user_type));
        else if (userTypeId == Constants.USER_TYPE_TRANSPORT)
            addFragmentWithAnimation(R.id.content_frame, AllBookingsFragment.newInstance(), null, true);
        else
            addFragmentWithAnimation(R.id.content_frame, MyBookingsFragment.newInstance(), null, true);
    }
}
