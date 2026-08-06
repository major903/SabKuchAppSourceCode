package vedam.subkuch.ui.ask;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
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
import vedam.subkuch.ui.ask.models.AskCategory;
import vedam.subkuch.ui.ask.models.AskCategoryResponse;
import vedam.subkuch.utils.UiUtil;

public class AskCategoryFragment extends BaseListFragment {

    private ArrayList<AskCategory> categories;

    public AskCategoryFragment() {
        // Required empty public constructor
    }

    public static AskCategoryFragment newInstance() {
        return new AskCategoryFragment();
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
        getCategories();
    }

    private void getCategories() {
        UiUtil.showProgressDialog(context, R.string.please_wait);
        DataFetcher.getAskCategories(context, onAskCategorySuccessListener, AskCategoryResponse.class, onErrorListener);

    }

    private Response.Listener<AskCategoryResponse> onAskCategorySuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (response != null && response.getReturnMessage().equalsIgnoreCase(Constants.SUCCESS)) {
            categories = response.getReturnData();
            loadValues();
        } else
            UiUtil.showToast(context, getString(R.string.no_data));
    };

    private void loadValues() {

        ArrayAdapter<AskCategory> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1,
                android.R.id.text1, categories);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);


        Intent intent = new Intent(getActivity(),
                ConversationActivity.class);
        AskCategory category = categories.get(position);

        intent.putExtra(Constants.EXTRA_CATEGORY_ID,
                category.getId());
        intent.putExtra(Constants.EXTRA_CATEGORY_NAME, category.getCategoryname());
        startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.add, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                startActivity(new Intent(context, AddQuestionActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
