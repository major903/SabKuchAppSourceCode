package vedam.subkuch.ui.events;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;

import java.util.ArrayList;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseFragment;
import vedam.subkuch.databinding.FragmentEventBinding;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.network.models.Event;
import vedam.subkuch.network.models.EventsResponse;
import vedam.subkuch.utils.UiUtil;


public class EventFragment extends BaseFragment {

    private FragmentEventBinding fragmentEventBinding;
    private EventAdapter adapter;
    private ArrayList<Event> eventsList = new ArrayList<>();
    private boolean loading = true;
    private LinearLayoutManager linearLayoutManager;
    private int pageNo = 1;
    private int pageSize = 20;
    private boolean hasMoreProjects = true;


    public EventFragment() {
        // Required empty public constructor
    }

    public static EventFragment newInstance() {

        return new EventFragment();
    }

    //Layout of BulleinFragment is used because both needed to same i.e. ListView
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentEventBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_event, container, false);
        return fragmentEventBinding.getRoot();
    }

    public void onViewCreated(@NonNull View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        initUI();
        getEvents();
        setHasOptionsMenu(true);
    }

    private void initUI() {

        linearLayoutManager = new LinearLayoutManager(context);
        fragmentEventBinding.rvEvents.setLayoutManager(linearLayoutManager);
        fragmentEventBinding.rvEvents.setHasFixedSize(true);
        adapter = new EventAdapter(context, eventsList);
        fragmentEventBinding.rvEvents.setAdapter(adapter);
        fragmentEventBinding.rvEvents.addOnScrollListener(new EventsOnScrollListener());
    }

    public void getEvents() {
        UiUtil.showProgressDialog(context, getString(R.string.please_wait));
        DataFetcher.getEvents(context, onEventsSuccessListener, EventsResponse.class, onErrorListener, pageNo, pageSize);

    }

    private Response.Listener<EventsResponse> onEventsSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (getActivity() != null)
            if (response != null && response.getReturnMessage().equals(Constants.SUCCESS)) {
                if (response.getReturnData().size() > 0) {
                    hasMoreProjects = response.getReturnData().size() >= pageSize;
                    loading = true;
                    loadValues(response.getReturnData());
                } else
                    UiUtil.showToast(context, getString(R.string.no_events_found));
            } else
                UiUtil.showToast(context, getString(R.string.err_occurred));
    };

    /*private void startEventCalendarService(JSONArray jsonArray) {

        if (jsonArray != null) {
            Intent intent = new Intent(getActivity(), EventCalendarIntentService.class);
            intent.putExtra(EXTRA_DATA, jsonArray.toString());
            getActivity().startService(intent);
        }
    }*/


    private void loadValues(ArrayList<Event> response) {

        if (response != null && !response.isEmpty()) {
            pageNo++;
            eventsList.addAll(response);
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
                startActivity(new Intent(context, AddEventActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public class EventsOnScrollListener extends RecyclerView.OnScrollListener {

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
                        if (hasMoreProjects) getEvents();

                    }
                }
            }
        }
    }
}
