package vedam.subkuch.ui.directory;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Response;

import java.util.ArrayList;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseListFragment;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.ui.directory.models.Category;
import vedam.subkuch.ui.directory.models.CategoryResponse;
import vedam.subkuch.ui.stafftrack.StaffTrackActivity;
import vedam.subkuch.utils.UiUtil;

public class DirectoryFragment extends BaseListFragment {

    private ArrayList<Category> categories;

    public DirectoryFragment() {
        // Required empty public constructor
    }

    public static DirectoryFragment newInstance() {
        return new DirectoryFragment();
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
        DataFetcher.getCategories(context, onCategorySuccessListener, CategoryResponse.class, onErrorListener);

    }

    private Response.Listener<CategoryResponse> onCategorySuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (response != null && response.getStatus().equals(Constants.TRUE)) {
            categories = response.getCategoryResult().getCategories();
            loadValues();
        } else
            UiUtil.showToast(context, getString(R.string.no_data));
    };

    private void loadValues() {

        ArrayAdapter<Category> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1,
                android.R.id.text1, categories);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);


        Intent intent = new Intent(getActivity(),
                SubDirectoryActivity.class);
        Category category = categories.get(position);
        intent.putExtra(Constants.EXTRA_CATEGORY_ID,
                category.getCategoryId());
        intent.putExtra(Constants.EXTRA_CATEGORY_NAME, category.getName());
        startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.directory, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_add:
                startActivity(new Intent(getActivity(), AddDirectoryActivity.class));
                return true;
            case R.id.action_staff:
                startActivity(new Intent(getActivity(), StaffTrackActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
