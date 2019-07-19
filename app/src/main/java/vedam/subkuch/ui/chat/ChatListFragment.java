package vedam.subkuch.ui.chat;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;

import java.util.ArrayList;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseFragment;
import vedam.subkuch.databinding.FragmentChatListBinding;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.interfaces.OnListViewItemClickListener;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.ui.matrimonial.models.DatingProfile;
import vedam.subkuch.ui.matrimonial.models.DatingProfileResponse;
import vedam.subkuch.utils.AppUtil;
import vedam.subkuch.utils.ListItemClickAction;
import vedam.subkuch.utils.UiUtil;


public class ChatListFragment extends BaseFragment implements OnListViewItemClickListener {
    private FragmentChatListBinding fragmentChatListBinding;
    private ChatListAdapter adapter;
    private ArrayList<DatingProfile> datingProfiles = new ArrayList<>();
    private boolean loading = true;
    private LinearLayoutManager linearLayoutManager;
    private int pageNo = 1;
    private int pageSize = 20;
    private boolean hasMoreProjects = true;
    private boolean isDating;

    public ChatListFragment() {
        // Required empty public constructor
    }


    public static ChatListFragment newInstance(boolean isDating) {
        ChatListFragment chatListFragment = new ChatListFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.EXTRA_IS_DATING, isDating);
        chatListFragment.setArguments(bundle);
        return chatListFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            isDating = getArguments().getBoolean(Constants.EXTRA_IS_DATING);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentChatListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat_list, container, false);
        return fragmentChatListBinding.getRoot();
    }

    public void onViewCreated(@NonNull View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        setTitle(getString(R.string.matches));
        initUI();
        getMatchedProfiles();
    }

    private void initUI() {

        linearLayoutManager = new LinearLayoutManager(context);
        fragmentChatListBinding.rvChatList.setLayoutManager(linearLayoutManager);
        fragmentChatListBinding.rvChatList.setHasFixedSize(true);
        adapter = new ChatListAdapter(context, datingProfiles, this);
        fragmentChatListBinding.rvChatList.setAdapter(adapter);
        fragmentChatListBinding.rvChatList.addOnScrollListener(new ProfilesOnScrollListener());

    }

    public void getMatchedProfiles() {
        UiUtil.showProgressDialog(context, getString(R.string.please_wait));
        if (isDating)
            DataFetcher.getDatingMatchedChatProfiles(context, onMatchedProfilesSuccessListener, DatingProfileResponse.class, onErrorListener, pageNo, pageSize);
        else
            DataFetcher.getMatrimonialMatchedChatProfiles(context, onMatchedProfilesSuccessListener, DatingProfileResponse.class, onErrorListener, pageNo, pageSize);

    }

    private void startChatActivity(DatingProfile datingProfile) {

        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(Constants.EXTRA_NAME, AppUtil.deNull(datingProfile.getFirstName()));
        intent.putExtra(Constants.EXTRA_CHAT_TO_ID, datingProfile.getProfileId());
        intent.putExtra(Constants.EXTRA_IS_DATING, isDating);
        startActivityForResult(intent, Constants.REQUEST_CHAT);
    }

    private Response.Listener<DatingProfileResponse> onMatchedProfilesSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (getActivity() != null)
            if (response != null && response.getReturnMessage().equals(Constants.SUCCESS)) {
                if (response.getReturnData().size() > 0) {
                    hasMoreProjects = response.getReturnData().size() >= pageSize;
                    loading = true;
                    loadValues(response.getReturnData());
                } else
                    UiUtil.showToast(context, getString(R.string.no_matches_found));
            } else
                UiUtil.showToast(context, getString(R.string.err_occurred));
    };

    private void loadValues(ArrayList<DatingProfile> response) {

        if (response != null && !response.isEmpty()) {
            pageNo++;
            datingProfiles.addAll(response);
            adapter = new ChatListAdapter(context, datingProfiles, this);
            fragmentChatListBinding.rvChatList.setAdapter(adapter);
        }
    }

    @Override
    public <E> void onItemClick(E item, int position, View view, ListItemClickAction action) {
        if (item != null) {
            DatingProfile datingProfile = (DatingProfile) item;
            startChatActivity(datingProfile);
        }
    }

    public class ProfilesOnScrollListener extends RecyclerView.OnScrollListener {

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
                        if (hasMoreProjects) getMatchedProfiles();

                    }
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CHAT) {
            refreshData();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void refreshData() {
        pageNo = 1;
        hasMoreProjects = true;
        loading = true;
        datingProfiles.clear();
        getMatchedProfiles();
    }

    public void changeData() {
        adapter.notifyDataSetChanged();
    }

}
