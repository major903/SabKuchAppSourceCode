package vedam.subkuch.ui.needs;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import vedam.subkuch.network.Response;

import java.util.ArrayList;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseListFragment;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.network.models.Profile;
import vedam.subkuch.network.models.needs.Provider;
import vedam.subkuch.network.models.needs.ProviderResponse;
import vedam.subkuch.utils.UiUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class NeedsFragment extends BaseListFragment {

    private int userTypeId;

    public NeedsFragment() {
        // Required empty public constructor
    }

    public static NeedsFragment newInstance() {

        return new NeedsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_directory, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        installMenu(R.menu.add, item -> {
            if (item.getItemId() == R.id.action_add) {
                startActivity(new Intent(context, AddNeedActivity.class));
                return true;
            }
            return false;
        });
        getProviders();
        getUserTypeId();
    }

    private void getProviders() {

        UiUtil.showProgressDialog(context, getString(R.string.loading));
        DataFetcher.getProviders(context, onProviderSuccessListener, ProviderResponse.class, onErrorListener);
    }

    private Response.Listener<ProviderResponse> onProviderSuccessListener = response -> {
        UiUtil.cancelProgressDialog();
        if (getActivity() != null)
            if (response != null && response.getReturnMessage().equals(Constants.SUCCESS)) {
                if (response.getReturnData().size() > 0) {
                    loadValues(response.getReturnData());
                } else
                    UiUtil.showToast(context, getString(R.string.no_providers_found));
            } else
                UiUtil.showToast(context, getString(R.string.err_occurred));
    };

    private void loadValues(ArrayList<Provider> response) {

        ArrayAdapter<Provider> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1,
                android.R.id.text1, response);
        setListAdapter(adapter);
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Provider provider = ((Provider) l.getItemAtPosition(position));
        showBookings(provider);
    }

    private void getUserTypeId() {
        UiUtil.showProgressDialog(context, getString(R.string.loading));
        DataFetcher.getUserProfile(context, onProfileSuccessListener, Profile.class, onErrorListener);

    }

    private Response.Listener<Profile> onProfileSuccessListener = response -> {
        UiUtil.cancelProgressDialog();
        if (getActivity() != null)
            if (response != null && !TextUtils.isEmpty(response.getProfileId())) {
                userTypeId = response.getUserTypeId();
            } else {
                UiUtil.showToast(context, getString(R.string.err_occurred));
            }
    };

    private void showBookings(Provider provider) {

        if (userTypeId == 0)
            UiUtil.showToast(context, getString(R.string.no_user_type));
        else if (userTypeId == provider.getUserType())
            addFragmentWithAnimation(R.id.content_frame, AllBookingsFragment.newInstance(provider), null, true);
        else
            addFragmentWithAnimation(R.id.content_frame, MyBookingsFragment.newInstance(provider), null, true);
    }

}
