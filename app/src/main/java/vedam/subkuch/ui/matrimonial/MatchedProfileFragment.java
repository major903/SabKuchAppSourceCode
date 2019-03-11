package vedam.subkuch.ui.matrimonial;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;

import java.util.ArrayList;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseFragment;
import vedam.subkuch.databinding.FragmentMatchedProfileBinding;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.interfaces.OnListViewItemClickListener;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.ui.chat.ChatActivity;
import vedam.subkuch.ui.matrimonial.models.DatingProfile;
import vedam.subkuch.ui.matrimonial.models.DatingProfileResponse;
import vedam.subkuch.utils.AppUtil;
import vedam.subkuch.utils.ListItemClickAction;
import vedam.subkuch.utils.UiUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class MatchedProfileFragment extends BaseFragment implements OnListViewItemClickListener {

    private FragmentMatchedProfileBinding fragmentMatchedProfileBinding;
    private MatchedProfileAdapter adapter;
    private ArrayList<DatingProfile> datingProfiles = new ArrayList<>();
    private boolean loading = true;
    private LinearLayoutManager linearLayoutManager;
    private int pageNo = 1;
    private int pageSize = 20;
    private boolean hasMoreProjects = true;

    public MatchedProfileFragment() {
        // Required empty public constructor
    }

    public static MatchedProfileFragment newInstance() {

        return new MatchedProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        fragmentMatchedProfileBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_matched_profile, container, false);
        return fragmentMatchedProfileBinding.getRoot();
    }

    public void onViewCreated(@NonNull View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        setTitle(getString(R.string.matches));
        initUI();
        getMatchedProfiles();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.clear();
    }

    private void initUI() {

        linearLayoutManager = new LinearLayoutManager(context);
        fragmentMatchedProfileBinding.rvMatchedProfile.setLayoutManager(linearLayoutManager);
        fragmentMatchedProfileBinding.rvMatchedProfile.setHasFixedSize(true);
        adapter = new MatchedProfileAdapter(context, datingProfiles, this);
        fragmentMatchedProfileBinding.rvMatchedProfile.setAdapter(adapter);
        fragmentMatchedProfileBinding.rvMatchedProfile.addOnScrollListener(new ProfilesOnScrollListener());

    }

    public void getMatchedProfiles() {
        UiUtil.showProgressDialog(context, getString(R.string.please_wait));
        DataFetcher.getMatchedProfiles(context, onMatchedProfilesSuccessListener, DatingProfileResponse.class, onErrorListener, pageNo, pageSize);

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
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public <E> void onItemClick(E item, int position, View view, ListItemClickAction action) {
        if (item != null) {
            DatingProfile datingProfile = (DatingProfile) item;
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra(Constants.EXTRA_NAME, AppUtil.getFullName(datingProfile.getFirstName(), datingProfile.getLastName()));
            intent.putExtra(Constants.EXTRA_CHAT_TO_ID, datingProfile.getProfileId());
            startActivity(intent);
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

}
