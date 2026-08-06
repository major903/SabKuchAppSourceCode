package vedam.subkuch.ui.public_utility;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import vedam.subkuch.network.Response;

import java.util.ArrayList;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseListFragment;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.network.models.SubCategory;
import vedam.subkuch.ui.directory.models.SubCategoryResponse;
import vedam.subkuch.utils.UiUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubDirectoryFragment extends BaseListFragment {

    private ArrayList<SubCategory> subCategories;

    public SubDirectoryFragment() {
        // Required empty public constructor
    }

    public static SubDirectoryFragment newInstance() {

        return new SubDirectoryFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_directory, container, false);
    }

    public void onViewCreated(@NonNull View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        getSubCategories();
    }

    private void getSubCategories() {
        UiUtil.showProgressDialog(context, R.string.please_wait);
        DataFetcher.getUtilitySubCategories(context, onCategorySuccessListener, SubCategoryResponse.class, onErrorListener);

    }

    private Response.Listener<SubCategoryResponse> onCategorySuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (getActivity() != null)
            if (response != null && response.getStatus().equals(Constants.TRUE)) {
                subCategories = response.getSubCategoryResult().getSubCategories();
                loadValues();
            } else
                UiUtil.showToast(context, getString(R.string.no_data));
    };

    private void loadValues() {

        setListAdapter(new ArrayAdapter<>(context, android.R.layout.simple_list_item_1,
                android.R.id.text1, subCategories));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);


        SubCategory subCategory = subCategories.get(position);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.EXTRA_SUB_CATEGORY_NAME, subCategory.getSubCategoryName());
        bundle.putString(Constants.EXTRA_SUB_CATEGORY_ID, subCategory.getSubCategoryId());
        addFragmentWithAnimation(R.id.content_frame, PublicUtilityFragment.newInstance(bundle), null, true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.add, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_add) {
            startActivity(new Intent(getActivity(), AddPublicUtilityActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
