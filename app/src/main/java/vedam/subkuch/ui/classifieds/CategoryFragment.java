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

import com.android.volley.Response;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseListFragment;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.network.models.classifieds.ClassifiedCategory;
import vedam.subkuch.network.models.classifieds.ClassifiedResponse;
import vedam.subkuch.utils.UiUtil;

public class CategoryFragment extends BaseListFragment {

    private ArrayList<ClassifiedCategory> categories;

    public CategoryFragment() {
        // Required empty public constructor
    }

    public static CategoryFragment newInstance() {
        return new CategoryFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_directory, container, false);
    }

    public void onViewCreated(@NonNull View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        getCategories();
    }

    private void getCategories() {
        UiUtil.showProgressDialog(context, R.string.please_wait);
        Type type = new TypeToken<ClassifiedResponse<ClassifiedCategory>>() {
        }.getType();
        DataFetcher.getClassifiedsCategories(context, onCategorySuccessListener, type, onErrorListener);
    }

    private Response.Listener<ClassifiedResponse<ClassifiedCategory>> onCategorySuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (getActivity() != null)
            if (response != null && response.getReturnMessage().equals(Constants.SUCCESS)) {
                categories = response.getReturnData();
                loadValues();
            } else
                UiUtil.showToast(context, getString(R.string.no_data));
    };

    private void loadValues() {

        ArrayAdapter<ClassifiedCategory> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1,
                android.R.id.text1, categories);
        setListAdapter(adapter);
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
            startActivity(new Intent(getActivity(), AddClassifiedsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);


        ClassifiedCategory category = categories.get(position);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.EXTRA_CATEGORY_ID,
                category.getCategoryId());
        bundle.putString(Constants.EXTRA_CATEGORY_NAME, category.getCategory());
        addFragmentWithAnimation(R.id.content_frame, SubCategoryFragment.newInstance(bundle), null, true);
    }
}
