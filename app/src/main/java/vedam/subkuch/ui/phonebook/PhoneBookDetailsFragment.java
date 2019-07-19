package vedam.subkuch.ui.phonebook;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;

import java.util.ArrayList;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseListFragment;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.network.models.PhoneBookCategory;
import vedam.subkuch.network.models.PhoneBookDetail;
import vedam.subkuch.network.models.PhoneBookDetailsResponse;
import vedam.subkuch.utils.UiUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhoneBookDetailsFragment extends BaseListFragment {


    private String categoryId;
    PhoneBookDetailsAdapter phoneBookDetailsAdapter;

    public PhoneBookDetailsFragment() {
        // Required empty public constructor
    }

    public static PhoneBookDetailsFragment newInstance(PhoneBookCategory phoneBookCategory) {
        PhoneBookDetailsFragment fragment = new PhoneBookDetailsFragment();

        Bundle args = new Bundle();
        args.putString(Constants.EXTRA_CATEGORY_NAME, phoneBookCategory.getName());
        args.putString(Constants.EXTRA_CATEGORY_ID, phoneBookCategory.getPhonebookcategoryid());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryId = getArguments().getString(Constants.EXTRA_CATEGORY_ID);
//            String categoryName = getArguments().getString(Constants.EXTRA_CATEGORY_NAME);
//            setTitle(categoryName);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movies, container, false);
    }

    public void onViewCreated(View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        getPhoneBookDetails();

    }

    private void getPhoneBookDetails() {

        UiUtil.showProgressDialog(context, getString(R.string.please_wait));
        DataFetcher.getPhoneBookDetails(context, onPhoneBookDetailsSuccessListener, PhoneBookDetailsResponse.class, onErrorListener, categoryId);
    }

    private Response.Listener<PhoneBookDetailsResponse> onPhoneBookDetailsSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (getActivity() != null)
            if (response != null && response.getReturnMessage().equals(Constants.SUCCESS) && response.getReturnData().size() > 0) {
                loadValues(response.getReturnData());
            } else
                UiUtil.showToast(context, getString(R.string.err_occurred));
    };

    private void loadValues(ArrayList<PhoneBookDetail> returnData) {

        phoneBookDetailsAdapter = new PhoneBookDetailsAdapter(getActivity(), returnData);
        getListView().setAdapter(phoneBookDetailsAdapter);
    }

}
