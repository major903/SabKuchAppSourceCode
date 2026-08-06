package vedam.subkuch.ui.ask;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import vedam.subkuch.network.Response;

import java.util.ArrayList;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseFragment;
import vedam.subkuch.databinding.FragmentConversationBinding;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.ui.ask.models.Conversation;
import vedam.subkuch.ui.ask.models.ConversationResponse;
import vedam.subkuch.utils.UiUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConversationFragment extends BaseFragment implements AskReplyListener, AskReplyDialog.AskReplyPostedListener {

    private FragmentConversationBinding fragmentConversationBinding;
    private ConversationAdapter adapter;
    private ArrayList<Conversation> conversations = new ArrayList<>();
    private boolean loading = true;
    private LinearLayoutManager linearLayoutManager;
    private int pageNo = 1;
    private int pageSize = 20;
    private boolean hasMoreProjects = true;
    private String categoryId;

    public ConversationFragment() {
        // Required empty public constructor
    }

    public static ConversationFragment newInstance(Bundle extras) {

        ConversationFragment fragment = new ConversationFragment();
        fragment.setArguments(extras);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryId = getArguments().getString(Constants.EXTRA_CATEGORY_ID);
        }
    }

    //Layout of BulleinFragment is used because both needed to same i.e. ListView
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentConversationBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_conversation, container, false);
        return fragmentConversationBinding.getRoot();
    }

    public void onViewCreated(@NonNull View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        initUI();
        getConversation();
        setHasOptionsMenu(true);
    }

    private void initUI() {

        linearLayoutManager = new LinearLayoutManager(mContext);
        fragmentConversationBinding.rvQuestions.setLayoutManager(linearLayoutManager);
        fragmentConversationBinding.rvQuestions.setHasFixedSize(true);
        adapter = new ConversationAdapter(mContext, conversations, this);
        fragmentConversationBinding.rvQuestions.setAdapter(adapter);
        fragmentConversationBinding.rvQuestions.addOnScrollListener(new ConversationOnScrollListener());
    }

    private void getConversation() {
        UiUtil.showProgressDialog(mContext, getString(R.string.please_wait));
        DataFetcher.getAskConversation(mContext, onConversationSuccessListener, ConversationResponse.class,
                onErrorListener, categoryId, pageNo, pageSize);

    }

    private Response.Listener<ConversationResponse> onConversationSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (getActivity() != null)
            if (response != null && response.getReturnMessage().equals(Constants.SUCCESS)) {
                if (response.getReturnData().size() > 0) {
                    hasMoreProjects = response.getReturnData().size() >= pageSize;
                    loading = true;
                    loadValues(response.getReturnData());
                } else
                    UiUtil.showToast(mContext, getString(R.string.no_data));
            } else
                UiUtil.showToast(mContext, getString(R.string.err_occurred));
    };

    /*private void startEventCalendarService(JSONArray jsonArray) {

        if (jsonArray != null) {
            Intent intent = new Intent(getActivity(), EventCalendarIntentService.class);
            intent.putExtra(EXTRA_DATA, jsonArray.toString());
            getActivity().startService(intent);
        }
    }*/


    private void loadValues(ArrayList<Conversation> response) {

        if (response != null && !response.isEmpty()) {
            pageNo++;
            conversations.addAll(response);
            adapter.notifyDataSetChanged();
        }
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
                startActivityForResult(new Intent(mContext, AddQuestionActivity.class), Constants.REQUEST_ADD_QUESTION);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setDefaults() {
        pageNo = 1;
        hasMoreProjects = true;
        conversations.clear();
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case Constants.REQUEST_ADD_QUESTION:
                if (resultCode == Activity.RESULT_OK) {
                    setDefaults();
                    getConversation();
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onReplyClick(String questionId) {

        startAskReplyDialog(questionId);
    }

    private void startAskReplyDialog(String questionId) {

        AskReplyDialog askReplyDialog = new AskReplyDialog(mContext, this, questionId);
        askReplyDialog.setCancelable(true);
        Window window = askReplyDialog.getWindow();
        if (window != null) {
            window.setGravity(Gravity.CENTER);
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.getAttributes().windowAnimations = R.style.DialogAnimation;
            askReplyDialog.show();
        }
    }

    @Override
    public void onReplyPosted() {

        setDefaults();
        getConversation();
    }


    public class ConversationOnScrollListener extends RecyclerView.OnScrollListener {

        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            /*final Picasso picasso = Picasso.get();

            if (newState == RecyclerView.SCROLL_STATE_IDLE || newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                picasso.resumeTag(context);
            } else {
                picasso.pauseTag(context);
            }*/
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            if (dy > 0) //check for scroll down
            {
                int visibleItemCount = linearLayoutManager.getChildCount();
                int totalItemCount = linearLayoutManager.getItemCount();
                int pastVisibleItems = linearLayoutManager.findFirstVisibleItemPosition();

                if (loading) {
                    if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                        loading = false;
                        if (hasMoreProjects) getConversation();

                    }
                }
            }
        }
    }
}
