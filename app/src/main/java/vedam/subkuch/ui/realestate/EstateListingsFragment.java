package vedam.subkuch.ui.realestate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseFragment;
import vedam.subkuch.databinding.FragmentClassifiedDetailsBinding;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.RegistrationApiClient;
import vedam.subkuch.network.models.DataEntryListResponse;
import vedam.subkuch.network.models.EstateListItem;
import vedam.subkuch.network.models.EstateTypeListResponse;
import vedam.subkuch.network.models.EstateTypeOption;
import vedam.subkuch.ui.contribute.ContributionHistoryHelper;
import vedam.subkuch.utils.UiUtil;

/** Paged list of real estate listings, mirroring ClassifiedDetailsFragment. */
public class EstateListingsFragment extends BaseFragment {

    private FragmentClassifiedDetailsBinding binding;
    private RealEstateListingsAdapter adapter;
    private final ArrayList<EstateListItem> listings = new ArrayList<>();
    private boolean loading = true;
    private LinearLayoutManager linearLayoutManager;
    private int pageNo = 1;
    private final int pageSize = 20;
    private boolean hasMoreListings = true;
    private int estateTypeId;
    private String estateTypeName;

    public EstateListingsFragment() {
        // Required empty public constructor
    }

    public static EstateListingsFragment newInstance(Bundle args) {
        EstateListingsFragment fragment = new EstateListingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            estateTypeId = getArguments().getInt(Constants.EXTRA_ESTATE_TYPE_ID);
            estateTypeName = getArguments().getString(Constants.EXTRA_ESTATE_TYPE_NAME);
            if (estateTypeName != null && !estateTypeName.isEmpty()) setTitle(estateTypeName);
        }
    }

    // Reuses the classifieds list layout: both are a plain RecyclerView list.
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_classified_details, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        binding.tvEmptyMyAds.setText(R.string.no_real_estate_listings);
        initUI();
        loadEstateTypes();
        getListings();
    }

    private void initUI() {
        linearLayoutManager = new LinearLayoutManager(mContext);
        binding.rvClassifieds.setLayoutManager(linearLayoutManager);
        adapter = new RealEstateListingsAdapter(mContext);
        binding.rvClassifieds.setAdapter(adapter);
        binding.rvClassifieds.addOnScrollListener(new OnScrollListener());
    }

    /** Type labels for the cards; only strictly needed when browsing all types. */
    private void loadEstateTypes() {
        if (!RegistrationApiClient.isConfigured()) return;
        RegistrationApiClient.getApi(mContext).getEstateTypes().enqueue(
                new Callback<EstateTypeListResponse>() {
                    @Override
                    public void onResponse(Call<EstateTypeListResponse> call,
                                           Response<EstateTypeListResponse> response) {
                        if (!isAdded() || getActivity() == null) return;
                        if (response.isSuccessful() && response.body() != null
                                && response.body().getReturnData() != null) {
                            Map<Integer, String> types = new HashMap<>();
                            for (EstateTypeOption option : response.body().getReturnData()) {
                                if (option.getId() != 0 && option.getName() != null) {
                                    types.put(option.getId(), option.getName());
                                }
                            }
                            adapter.setEstateTypes(types);
                        }
                    }

                    @Override
                    public void onFailure(Call<EstateTypeListResponse> call, Throwable throwable) {
                        // Type labels are optional; the list still shows without them.
                    }
                });
    }

    private void getListings() {
        if (!RegistrationApiClient.isConfigured()) {
            showEmpty();
            return;
        }
        UiUtil.showProgressDialog(mContext, getString(R.string.please_wait));
        RegistrationApiClient.getApi(mContext).getDataEstates(null,
                estateTypeId == 0 ? null : estateTypeId, null, pageNo, pageSize).enqueue(
                new Callback<DataEntryListResponse>() {
                    @Override
                    public void onResponse(Call<DataEntryListResponse> call,
                                           Response<DataEntryListResponse> response) {
                        if (!isAdded() || getActivity() == null) return;
                        UiUtil.cancelProgressDialog();
                        if (response.isSuccessful() && response.body() != null) {
                            ArrayList<EstateListItem> entries = ContributionHistoryHelper
                                    .extractEntries(response.body(), EstateListItem.class);
                            if (!entries.isEmpty()) {
                                hasMoreListings = entries.size() >= pageSize;
                                loading = true;
                                loadValues(entries);
                            } else if (listings.isEmpty()) {
                                showEmpty();
                            }
                        } else if (listings.isEmpty()) {
                            showEmpty();
                        }
                    }

                    @Override
                    public void onFailure(Call<DataEntryListResponse> call, Throwable throwable) {
                        if (!isAdded() || getActivity() == null) return;
                        UiUtil.cancelProgressDialog();
                        if (listings.isEmpty()) showEmpty();
                    }
                });
    }

    private void loadValues(ArrayList<EstateListItem> entries) {
        pageNo++;
        listings.addAll(entries);
        adapter.setListings(listings);
    }

    private void showEmpty() {
        binding.tvEmptyMyAds.setVisibility(View.VISIBLE);
    }

    public class OnScrollListener extends RecyclerView.OnScrollListener {

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
                        if (hasMoreListings) getListings();
                    }
                }
            }
        }
    }
}
