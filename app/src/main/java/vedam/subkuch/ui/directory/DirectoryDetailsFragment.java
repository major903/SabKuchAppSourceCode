package vedam.subkuch.ui.directory;


import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.android.volley.Response;

import java.util.ArrayList;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseFragment;
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
    private ExpandableListView expandableListView;

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
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_directory_details, container, false);
    }

    public void onViewCreated(@NonNull View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        expandableListView = v.findViewById(R.id.expandableListView);
        getDirectoryDetails();
    }


    private void getDirectoryDetails() {

        UiUtil.showProgressDialog(context, getString(R.string.please_wait));
        DataFetcher.getDirectoryDetails(context, onDirectoryDetailSuccessListener, DirectoryDetailResponse.class, onErrorListener, categoryId, subCategoryId, null);
    }

    private Response.Listener<DirectoryDetailResponse> onDirectoryDetailSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (getActivity() != null && response != null && response.getStatus().equals(Constants.TRUE)) {
            loadValues(response.getBusinessesResult().getBusinesses());
        } else
            UiUtil.showToast(context, getString(R.string.no_data));
    };

    private void loadValues(ArrayList<Business> response) {

        DirectoryDetailAdapter directoryDetailAdapter = new DirectoryDetailAdapter(context, response, this);
        expandableListView.setAdapter(directoryDetailAdapter);
//        expandableListView.setOnGroupClickListener(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.add, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_add:
                startActivity(new Intent(getActivity(), AddDirectoryActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
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
}
