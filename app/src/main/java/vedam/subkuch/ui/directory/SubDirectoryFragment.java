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
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

    private String categoryId;
    private String categoryName;
    private ArrayList<SubCategory> subCategories;

    public SubDirectoryFragment() {
        // Required empty public constructor
    }

    public static SubDirectoryFragment newInstance(String categoryId, String categoryName) {
        SubDirectoryFragment fragment = new SubDirectoryFragment();
        Bundle args = new Bundle();
        args.putString(Constants.EXTRA_CATEGORY_ID, categoryId);
        args.putString(Constants.EXTRA_CATEGORY_NAME, categoryName);
        fragment.setArguments(args);
        return fragment;
    }

    public static SubDirectoryFragment newInstance(String categoryId) {
        return newInstance(categoryId, "");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryId = getArguments().getString(Constants.EXTRA_CATEGORY_ID);
            categoryName = getArguments().getString(Constants.EXTRA_CATEGORY_NAME);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_directory, container, false);
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
        getSubCategories();
    }

    private void getSubCategories() {
        UiUtil.showProgressDialog(context, R.string.please_wait);
        DataFetcher.getSubCategories(context, onCategorySuccessListener, SubCategoryResponse.class, onErrorListener, categoryId);

    }

    private final Response.Listener<SubCategoryResponse> onCategorySuccessListener = response -> {
        UiUtil.cancelProgressDialog();
        if (getActivity() != null) {
            if (response != null && response.getStatus().equals(Constants.TRUE)) {
                subCategories = response.getSubCategoryResult().getSubCategories();
                if (subCategories != null && !subCategories.isEmpty()) {
                    loadValues();
                    return;
                }
            }
            openDirectoryDetailsDirectly();
        }
    };

    private void openDirectoryDetailsDirectly() {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.EXTRA_CATEGORY_ID, categoryId);
        bundle.putString(Constants.EXTRA_CATEGORY_NAME, categoryName);
        bundle.putString(Constants.EXTRA_SUB_CATEGORY_NAME, categoryName);
        bundle.putString(Constants.EXTRA_SUB_CATEGORY_ID, "");
        if (getActivity() != null && !getActivity().isFinishing()) {
            replaceFragment(R.id.content_frame, DirectoryDetailsFragment.newInstance(bundle), null, false, 0, 0, 0, 0);
        }
    }

    private void loadValues() {

        setListAdapter(new ArrayAdapter<>(context, android.R.layout.simple_list_item_1,
                android.R.id.text1, subCategories));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);


        Intent intent = new Intent(getActivity(),
                DirectoryDetailsActivity.class);
        SubCategory subCategory = subCategories.get(position);
        intent.putExtra(Constants.EXTRA_CATEGORY_ID, categoryId);
        intent.putExtra(Constants.EXTRA_SUB_CATEGORY_NAME, subCategory.getSubCategoryName());
        intent.putExtra(Constants.EXTRA_SUB_CATEGORY_ID, subCategory.getSubCategoryId());
        startActivity(intent);
    }

}
