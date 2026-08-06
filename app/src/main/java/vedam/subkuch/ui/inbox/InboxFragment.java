package vedam.subkuch.ui.inbox;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vedam.subkuch.network.Response;

import java.util.ArrayList;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseFragment;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.ui.inbox.models.Inbox;
import vedam.subkuch.ui.inbox.models.InboxResponse;
import vedam.subkuch.utils.UiUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class InboxFragment extends BaseFragment {

    private RecyclerView rvInbox;

    public InboxFragment() {
        // Required empty public constructor
    }

    public static InboxFragment newInstance() {

        return new InboxFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inbox, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvInbox = view.findViewById(R.id.rv_inbox);
        rvInbox.setLayoutManager(new LinearLayoutManager(mContext));
        getInbox();
    }

    private void getInbox() {
        UiUtil.showProgressDialog(mContext, getString(R.string.please_wait));
        DataFetcher.getInbox(mContext, onInboxSuccessListener, InboxResponse.class, onErrorListener);
    }

    private Response.Listener<InboxResponse> onInboxSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (getActivity() != null)
            if (response != null && response.getReturnMessage().equals(Constants.SUCCESS)) {
                if (response.getReturnData().size() > 0) {
                    loadValues(response.getReturnData());
                } else
                    UiUtil.showToast(mContext, getString(R.string.no_data));
            } else
                UiUtil.showToast(mContext, getString(R.string.err_occurred));
    };

    private void loadValues(ArrayList<Inbox> returnData) {

        rvInbox.setAdapter(new InboxAdapter(mContext, returnData));
    }
}
