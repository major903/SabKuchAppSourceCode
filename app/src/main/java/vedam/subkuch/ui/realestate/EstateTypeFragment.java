package vedam.subkuch.ui.realestate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseListFragment;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.RegistrationApiClient;
import vedam.subkuch.network.models.EstateTypeListResponse;
import vedam.subkuch.network.models.EstateTypeOption;
import vedam.subkuch.utils.UiUtil;

public class EstateTypeFragment extends BaseListFragment {

    private ArrayList<EstateTypeOption> estateTypes;

    public EstateTypeFragment() {
        // Required empty public constructor
    }

    public static EstateTypeFragment newInstance() {
        return new EstateTypeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_directory, container, false);
    }

    public void onViewCreated(@NonNull View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        getEstateTypes();
    }

    private void getEstateTypes() {
        if (!RegistrationApiClient.isConfigured()) {
            UiUtil.showToast(context, getString(R.string.registration_api_not_configured));
            return;
        }
        UiUtil.showProgressDialog(context, R.string.please_wait);
        RegistrationApiClient.getApi(context).getEstateTypes().enqueue(
                new Callback<EstateTypeListResponse>() {
                    @Override
                    public void onResponse(Call<EstateTypeListResponse> call,
                                           Response<EstateTypeListResponse> response) {
                        if (!isAdded() || getActivity() == null) return;
                        UiUtil.cancelProgressDialog();
                        if (response.isSuccessful() && response.body() != null
                                && response.body().getReturnData() != null
                                && !response.body().getReturnData().isEmpty()) {
                            estateTypes = response.body().getReturnData();
                            loadValues();
                        } else {
                            UiUtil.showToast(context, getString(R.string.no_data));
                        }
                    }

                    @Override
                    public void onFailure(Call<EstateTypeListResponse> call, Throwable throwable) {
                        if (!isAdded() || getActivity() == null) return;
                        UiUtil.cancelProgressDialog();
                        UiUtil.showToast(context, getString(R.string.connectionError));
                    }
                });
    }

    private void loadValues() {

        ArrayAdapter<EstateTypeOption> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1,
                android.R.id.text1, estateTypes);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);


        EstateTypeOption estateType = estateTypes.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.EXTRA_ESTATE_TYPE_ID, estateType.getId());
        bundle.putString(Constants.EXTRA_ESTATE_TYPE_NAME, estateType.getName());
        // Instant transition, matching the Jobs category -> jobs list navigation.
        addFragment(R.id.content_frame, EstateListingsFragment.newInstance(bundle), null, true, 0, 0, 0, 0);
    }
}
