package vedam.subkuch.ui.events;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;

import java.util.ArrayList;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseListFragment;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.network.models.Event;
import vedam.subkuch.network.models.EventsResponse;
import vedam.subkuch.utils.UiUtil;


public class EventFragment extends BaseListFragment {

    public EventFragment() {
        // Required empty public constructor
    }

    public static EventFragment newInstance() {

        return new EventFragment();
    }

    //Layout of BulleinFragment is used because both needed to same i.e. ListView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event, container, false);
    }

    public void onViewCreated(@NonNull View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        getEvents();
        setHasOptionsMenu(true);
    }

    public void getEvents() {
        UiUtil.showProgressDialog(context, getString(R.string.please_wait));
        DataFetcher.getEvents(context, onEventsSuccessListener, EventsResponse.class, onErrorListener);

    }

    private Response.Listener<EventsResponse> onEventsSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (response != null && response.getReturnMessage().equals(Constants.SUCCESS) && response.getReturnData().size() > 0) {
            loadValues(response.getReturnData());
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

        EventAdapter eventAdapter = new EventAdapter(context, response);
        getListView().setAdapter(eventAdapter);
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
                startActivityForResult(new Intent(context, AddEventActivity.class), Constants.REQUEST_ADD_EVENT);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case Constants.REQUEST_ADD_EVENT:
                if (resultCode == Activity.RESULT_OK)
                    getEvents();
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
