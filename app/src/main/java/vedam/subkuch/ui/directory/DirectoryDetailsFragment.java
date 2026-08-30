package vedam.subkuch.ui.directory;


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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import vedam.subkuch.network.Response;

import java.util.ArrayList;
import java.util.Locale;
import android.text.TextUtils;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseFragment;
import vedam.subkuch.databinding.FragmentDirectoryDetailsBinding;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.interfaces.OnListViewItemClickListener;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.ui.directory.models.Business;
import vedam.subkuch.ui.directory.models.DirectoryDetailResponse;
import vedam.subkuch.utils.ListItemClickAction;
import vedam.subkuch.utils.UiUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class DirectoryDetailsFragment extends BaseFragment implements OnListViewItemClickListener {

    private String categoryId;
    private String subCategoryId;
    private String categoryName;
    private DirectoryDetailsAdapter adapter;
    private FragmentDirectoryDetailsBinding binding;
    private LinearLayoutManager linearLayoutManager;
    private int pageNo = 1;
    private final int pageSize = 15;
    private boolean hasMoreProjects = true;
    private boolean loading = true;
    private final ArrayList<Business> businessList = new ArrayList<>();

    public DirectoryDetailsFragment() {
        // Required empty public constructor
    }

    public static DirectoryDetailsFragment newInstance(Bundle extras) {

        DirectoryDetailsFragment fragment = new DirectoryDetailsFragment();
        fragment.setArguments(extras);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryId = getArguments().getString(Constants.EXTRA_CATEGORY_ID);
            subCategoryId = getArguments().getString(Constants.EXTRA_SUB_CATEGORY_ID);
            categoryName = getArguments().getString(Constants.EXTRA_CATEGORY_NAME);
            if (TextUtils.isEmpty(categoryName)) {
                categoryName = getArguments().getString(Constants.EXTRA_SUB_CATEGORY_NAME);
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_directory_details, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        installMenu(R.menu.add, item -> {
            if (item.getItemId() == R.id.action_add) {
                startActivity(new Intent(getActivity(), AddDirectoryActivity.class));
                return true;
            }
            return false;
        });
        linearLayoutManager = new LinearLayoutManager(mContext);
        binding.rvDirectory.setLayoutManager(linearLayoutManager);
        adapter = new DirectoryDetailsAdapter(this);
        binding.rvDirectory.setAdapter(adapter);
        binding.rvDirectory.addOnScrollListener(new DirectoryOnScrollListener());
        getDirectoryDetails();
    }


    private void getDirectoryDetails() {
        if (!TextUtils.isEmpty(subCategoryId)) {
            UiUtil.showProgressDialog(mContext, getString(R.string.please_wait));
            DataFetcher.getDirectoryDetails(mContext, onDirectoryDetailSuccessListener,
                    DirectoryDetailResponse.class, onErrorListener, categoryId, subCategoryId,
                    null, pageNo, pageSize);
        } else {
            searchBusinessesByCategory();
        }
    }

    private void searchBusinessesByCategory() {
        String keyword = getSearchKeywordForCategory(categoryName);
        if (TextUtils.isEmpty(keyword)) {
            keyword = "a";
        }
        UiUtil.showProgressDialog(mContext, getString(R.string.please_wait));
        DataFetcher.searchBusiness(mContext, onSearchFallbackSuccessListener,
                DirectoryDetailResponse.class, onErrorListener, keyword);
    }

    private final Response.Listener<DirectoryDetailResponse> onDirectoryDetailSuccessListener = response -> {
        if (getActivity() != null) {
            if (response != null && response.getStatus() != null && response.getStatus().equals(Constants.TRUE)
                    && response.getBusinessesResult() != null
                    && response.getBusinessesResult().getBusinesses() != null
                    && !response.getBusinessesResult().getBusinesses().isEmpty()) {
                UiUtil.cancelProgressDialog();
                ArrayList<Business> businesses = response.getBusinessesResult().getBusinesses();
                hasMoreProjects = businesses.size() >= pageSize;
                loading = true;
                loadValues(businesses);
            } else {
                searchBusinessesByCategory();
            }
        } else {
            UiUtil.cancelProgressDialog();
        }
    };

    private final Response.Listener<DirectoryDetailResponse> onSearchFallbackSuccessListener = response -> {
        UiUtil.cancelProgressDialog();
        if (getActivity() != null) {
            if (response != null && response.getStatus() != null && response.getStatus().equals(Constants.TRUE)
                    && response.getBusinessesResult() != null
                    && response.getBusinessesResult().getBusinesses() != null
                    && !response.getBusinessesResult().getBusinesses().isEmpty()) {
                ArrayList<Business> businesses = response.getBusinessesResult().getBusinesses();
                hasMoreProjects = false;
                loading = false;
                loadValues(businesses);
            } else {
                UiUtil.showToast(mContext, getString(R.string.no_data));
            }
        }
    };

    private String getSearchKeywordForCategory(String name) {
        if (TextUtils.isEmpty(name)) return "";
        String lower = name.toLowerCase(Locale.ENGLISH);
        if (lower.contains("account")) return "Account";
        if (lower.contains("advocate") || lower.contains("notary")) return "Advocate";
        if (lower.contains("bank")) return "Bank";
        if (lower.contains("hotel") || lower.contains("resort")) return "Hotel";
        if (lower.contains("medical") || lower.contains("health") || lower.contains("hospital")) return "Hospital";
        if (lower.contains("college") || lower.contains("school") || lower.contains("education")) return "College";
        if (lower.contains("food") || lower.contains("beverage") || lower.contains("restaurant")) return "Hotel";
        if (lower.contains("electric")) return "Electric";
        if (lower.contains("comput") || lower.contains("software")) return "Computer";
        if (lower.contains("build") || lower.contains("construct")) return "Construction";
        if (lower.contains("travel") || lower.contains("tour")) return "Travel";
        if (lower.contains("beauty")) return "Beauty";
        if (lower.contains("auto")) return "Automobile";
        if (lower.contains("stationery") || lower.contains("book")) return "Book";
        if (lower.contains("jewel")) return "Jewel";
        if (lower.contains("garment") || lower.contains("textile")) return "Garment";
        if (lower.contains("advertis")) return "Advertis";
        if (lower.contains("agri")) return "Agri";

        String[] tokens = name.split("[/&\\-–,\\s]+");
        for (String token : tokens) {
            if (token.length() >= 3) {
                return token;
            }
        }
        return name;
    }

    private void loadValues(ArrayList<Business> response) {

        if (response != null && !response.isEmpty()) {
            pageNo++;
            businessList.addAll(response);
            adapter.submitList(new ArrayList<>(businessList));
        }
    }

    /*@Override
    public boolean onGroupClick(ExpandableListView expandableListView, View view, int position, long l) {
        Business directoryDetail = ((Business) expandableListView.getItemAtPosition(position));
        addFragment(R.id.content_frame, DetailFragment.newInstance(directoryDetail),
                null, true, 0, 0, 0, 0);
        return false;
    }*/

    @Override
    public <E> void onItemClick(E item, int position, View view, ListItemClickAction action) {
        if (item != null) {
            Business directoryDetail = (Business) item;
            addFragmentWithAnimation(R.id.content_frame, DetailFragment.newInstance(directoryDetail),
                    null, true);
        }
    }

    public class DirectoryOnScrollListener extends RecyclerView.OnScrollListener {

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
                        if (hasMoreProjects) getDirectoryDetails();

                    }
                }
            }
        }
    }
}
