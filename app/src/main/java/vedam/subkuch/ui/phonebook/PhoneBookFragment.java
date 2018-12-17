package vedam.subkuch.ui.phonebook;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Response;

import java.util.ArrayList;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseListFragment;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.network.models.PhoneBookCategory;
import vedam.subkuch.network.models.PhoneBookResponse;
import vedam.subkuch.utils.UiUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhoneBookFragment extends BaseListFragment {


    public PhoneBookFragment() {
        // Required empty public constructor
    }


    public static PhoneBookFragment newInstance() {

        Bundle args = new Bundle();

        PhoneBookFragment fragment = new PhoneBookFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_phone_book, container, false);
    }

    public void onViewCreated(@NonNull View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        getPhoneBook();
    }


    private void getPhoneBook() {

        UiUtil.showProgressDialog(context, getString(R.string.please_wait));
        DataFetcher.getPhoneBook(context, onPhoneBookSuccessListener, PhoneBookResponse.class, onErrorListener);
    }

    private Response.Listener<PhoneBookResponse> onPhoneBookSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (response != null && response.getReturnMessage().equals(Constants.SUCCESS) && response.getReturnData().size() > 0) {
            loadValues(response.getReturnData());
        } else
            UiUtil.showToast(context, getString(R.string.err_occurred));
    };

    private void loadValues(ArrayList<PhoneBookCategory> response) {

        PhoneBookAdapter phoneBookAdapter = new PhoneBookAdapter(context, response);
        getListView().setAdapter(phoneBookAdapter);
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        PhoneBookCategory phoneBookCategory = ((PhoneBookCategory) l.getItemAtPosition(position));
        addFragment(R.id.content_frame, PhoneBookDetailsFragment.newInstance(phoneBookCategory),
                null, true, 0, 0, 0, 0);
    }
}
