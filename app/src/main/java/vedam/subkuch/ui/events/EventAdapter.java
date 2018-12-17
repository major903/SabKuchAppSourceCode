package vedam.subkuch.ui.events;

/**
 * Created by naddy on 1/1/16.
 */

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import vedam.subkuch.R;
import vedam.subkuch.network.models.Event;


public class EventAdapter extends BaseAdapter {


    private LayoutInflater inflater;
    private ArrayList<Event> events;


    EventAdapter(Context context, ArrayList<Event> events) {

        inflater = LayoutInflater.from(context);
        this.events = events;
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public Object getItem(int position) {
        return events.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {

        ViewHolder holder;

        if (v == null) {
            v = inflater.inflate(R.layout.fragment_event_list_item, null);
            holder = new ViewHolder();
            holder.tvDetails = v.findViewById(R.id.tvDetails);
            holder.tvDate = v.findViewById(R.id.tvDate);
            holder.tvTime = v.findViewById(R.id.tvTime);
            holder.tvVenue = v.findViewById(R.id.tvVenue);
            holder.tvCost = v.findViewById(R.id.tvCost);
            holder.ivEvent = v.findViewById(R.id.iv_event);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        Event event = (Event) getItem(position);

        holder.tvDate.setText(String.format("Date: %s", event.getDate()));
        holder.tvTime.setText(String.format("Time: %s", event.getTime()));
        holder.tvVenue.setText(String.format("Venue: %s", event.getVenue()));
        holder.tvDetails.setText(event.getTitle());

        setTextView("Date:", event.getDate(), holder.tvDate);
        setTextView("Time:", event.getTime(), holder.tvTime);
        setTextView("Venue:", event.getVenue(), holder.tvVenue);
        setTextView("Entry Fee:", event.getEntryFee(), holder.tvCost);

        if (!TextUtils.isEmpty(event.getEventImage())) {
            holder.ivEvent.setVisibility(View.VISIBLE);
            Picasso.with(parent.getContext()).load(event.getEventImage()).placeholder(R.drawable.grey).error(R.drawable.grey).into(holder.ivEvent);
        } else
            holder.ivEvent.setVisibility(View.GONE);


        return v;
    }

    private void setTextView(String prefix, String suffix, TextView tv) {
        if (!TextUtils.isEmpty(suffix)) {
            tv.setText(String.format("%s %s", prefix, suffix));
            tv.setVisibility(View.VISIBLE);
        } else
            tv.setVisibility(View.GONE);

    }

    @Override
    public int getItemViewType(int position) {

        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    private static class ViewHolder {

        private TextView tvTime;
        private TextView tvDate;
        private TextView tvDetails;
        private TextView tvVenue;
        private TextView tvCost;
        private ImageView ivEvent;
    }
}



