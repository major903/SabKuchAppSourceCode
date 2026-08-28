package vedam.subkuch.ui.matrimonial;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import vedam.subkuch.network.Response;

import java.util.ArrayList;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseFragment;
import vedam.subkuch.databinding.FragmentMatchedProfileBinding;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.interfaces.OnListViewItemClickListener;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.ui.matrimonial.models.DatingProfile;
import vedam.subkuch.ui.matrimonial.models.DatingProfileResponse;
import vedam.subkuch.ui.matrimonial.viewProfile.ViewProfileActivity;
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
    private boolean isDating;
    private final ActivityResultLauncher<Intent> viewProfileLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == android.app.Activity.RESULT_OK) refreshData();
            });

    public MatchedProfileFragment() {
        // Required empty public constructor
    }

    public static MatchedProfileFragment newInstance(boolean isDating) {

        MatchedProfileFragment matchedProfileFragment = new MatchedProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.EXTRA_IS_DATING, isDating);
        matchedProfileFragment.setArguments(bundle);
        return matchedProfileFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            isDating = getArguments().getBoolean(Constants.EXTRA_IS_DATING);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

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

    private void initUI() {

        linearLayoutManager = new LinearLayoutManager(mContext);
        fragmentMatchedProfileBinding.rvMatchedProfile.setLayoutManager(linearLayoutManager);
        fragmentMatchedProfileBinding.rvMatchedProfile.setHasFixedSize(true);
        adapter = new MatchedProfileAdapter(mContext, datingProfiles, this);
        fragmentMatchedProfileBinding.rvMatchedProfile.setAdapter(adapter);
        fragmentMatchedProfileBinding.rvMatchedProfile.addOnScrollListener(new ProfilesOnScrollListener());

    }

    public void getMatchedProfiles() {
        UiUtil.showProgressDialog(mContext, getString(R.string.please_wait));
        if (isDating)
            DataFetcher.getDatingMatchedProfiles(mContext, onMatchedProfilesSuccessListener, DatingProfileResponse.class, onErrorListener, pageNo, pageSize);
        else
            DataFetcher.getMatrimonialMatchedProfiles(mContext, onMatchedProfilesSuccessListener, DatingProfileResponse.class, onErrorListener, pageNo, pageSize);


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
                    UiUtil.showToast(mContext, getString(R.string.no_matches_found));
            } else
                UiUtil.showToast(mContext, getString(R.string.err_occurred));
    };

    private void loadValues(ArrayList<DatingProfile> response) {

        if (response != null && !response.isEmpty()) {
            pageNo++;
            int previousSize = datingProfiles.size();
            datingProfiles.addAll(response);
            adapter.notifyItemRangeInserted(previousSize, response.size());
        }
    }

    @Override
    public <E> void onItemClick(E item, int position, View view, ListItemClickAction action) {
        if (item != null) {
            DatingProfile datingProfile = (DatingProfile) item;
            startViewProfileActivity(datingProfile);
        }
    }

    private void startViewProfileActivity(DatingProfile datingProfile) {

        Intent intent = new Intent(mContext, ViewProfileActivity.class);
        intent.putExtra(Constants.EXTRA_NAME, AppUtil.deNull(datingProfile.getFirstName()));
        intent.putExtra(Constants.EXTRA_DATA, datingProfile);
        intent.putExtra(Constants.EXTRA_IS_DATING, isDating);
        viewProfileLauncher.launch(intent);
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

    private void refreshData() {
        pageNo = 1;
        hasMoreProjects = true;
        loading = true;
        int previousSize = datingProfiles.size();
        datingProfiles.clear();
        if (previousSize > 0) adapter.notifyItemRangeRemoved(0, previousSize);
        getMatchedProfiles();
    }
}
