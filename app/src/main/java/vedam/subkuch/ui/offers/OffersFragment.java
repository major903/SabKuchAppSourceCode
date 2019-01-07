package vedam.subkuch.ui.offers;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;

import java.util.ArrayList;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseListFragment;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.ui.offers.models.Offer;
import vedam.subkuch.ui.offers.models.OfferResponse;
import vedam.subkuch.utils.UiUtil;

public class OffersFragment extends BaseListFragment {

    public OffersFragment() {
        // Required empty public constructor
    }

    public static OffersFragment newInstance() {
        return new OffersFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_directory, container, false);
    }

    public void onViewCreated(@NonNull View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        getOffers();
    }

    private void getOffers() {
        UiUtil.showProgressDialog(context, R.string.please_wait);
        DataFetcher.getOffers(context, onOffersSuccessListener, OfferResponse.class, onErrorListener);

    }

    private Response.Listener<OfferResponse> onOffersSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (getActivity() != null)
            if (response != null && response.getReturnMessage().equals(Constants.SUCCESS) && response.getReturnData().size() > 0) {
                loadValues(response.getReturnData());
            } else
                UiUtil.showToast(context, getString(R.string.no_data));
    };

    private void loadValues(ArrayList<Offer> response) {

        OffersAdapter offersAdapter = new OffersAdapter(getActivity(), response);
        setListAdapter(offersAdapter);
    }
}
