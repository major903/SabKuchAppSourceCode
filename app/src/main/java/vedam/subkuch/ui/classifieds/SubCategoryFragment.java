package vedam.subkuch.ui.classifieds;

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
import androidx.annotation.Nullable;

import vedam.subkuch.network.Response;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseListFragment;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.network.models.classifieds.ClassifiedResponse;
import vedam.subkuch.network.models.classifieds.ClassifiedSubCategory;
import vedam.subkuch.utils.UiUtil;

public class SubCategoryFragment extends BaseListFragment {

    private String categoryId;
    private ArrayList<ClassifiedSubCategory> subCategories;

    public static SubCategoryFragment newInstance(Bundle args) {

        SubCategoryFragment fragment = new SubCategoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryId = getArguments().getString(Constants.EXTRA_CATEGORY_ID);
            setTitle(getArguments().getString(Constants.EXTRA_CATEGORY_NAME));
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
                startActivity(new Intent(getActivity(), AddClassifiedsActivity.class));
                return true;
            }
            return false;
        });
        getSubCategories();
    }

    private void getSubCategories() {
        UiUtil.showProgressDialog(context, R.string.please_wait);
        Type type = new TypeToken<ClassifiedResponse<ClassifiedSubCategory>>() {
        }.getType();
        DataFetcher.getClassifiedSubCategories(context, onCategorySuccessListener, type, onErrorListener, categoryId);

    }

    private Response.Listener<ClassifiedResponse<ClassifiedSubCategory>> onCategorySuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (getActivity() != null)
            if (response != null && response.getReturnMessage().equals(Constants.SUCCESS)) {
                subCategories = response.getReturnData();
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


        Intent intent = new Intent(getActivity(),
                ClassifiedDetailsActivity.class);
        ClassifiedSubCategory subCategory = subCategories.get(position);
        intent.putExtra(Constants.EXTRA_SUB_CATEGORY_NAME, subCategory.getSubCategory());
        intent.putExtra(Constants.EXTRA_SUB_CATEGORY_ID, subCategory.getSubCategoryId());
        startActivity(intent);
    }

}
