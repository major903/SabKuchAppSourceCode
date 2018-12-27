package vedam.subkuch.ui.events;

/**
 * Created by naddy on 1/1/16.
 */

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import vedam.subkuch.R;
import vedam.subkuch.network.models.Event;
import vedam.subkuch.utils.UiUtil;


public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Event> events;

    EventAdapter(Context context, ArrayList<Event> events) {

        this.context = context;
        this.events = events;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.fragment_event_list_item, parent, false);
        return new EventAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Event event = events.get(position);

        holder.tvDate.setText(String.format("Date: %s", event.getDate()));
        holder.tvTime.setText(String.format("Time: %s", event.getTime()));
        holder.tvVenue.setText(String.format("Venue: %s", event.getVenue()));
        holder.tvDetails.setText(event.getTitle());

        UiUtil.setTextView("Date:", event.getDate(), holder.tvDate);
        UiUtil.setTextView("Time:", event.getTime(), holder.tvTime);
        UiUtil.setTextView("Venue:", event.getVenue(), holder.tvVenue);
        UiUtil.setTextView("Entry Fee:", event.getEntryFee(), holder.tvCost);

        if (!TextUtils.isEmpty(event.getEventImage())) {
            holder.ivEvent.setVisibility(View.VISIBLE);
            Picasso.with(context).load(event.getEventImage()).placeholder(R.drawable.grey)
                    .error(R.drawable.grey).into(holder.ivEvent, new Callback() {
                @Override
                public void onSuccess() {
                    holder.ivEvent.setVisibility(View.VISIBLE);
                }

                @Override
                public void onError() {
                    holder.ivEvent.setVisibility(View.GONE);
                }
            });
        } else
            holder.ivEvent.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTime;
        private TextView tvDate;
        private TextView tvDetails;
        private TextView tvVenue;
        private TextView tvCost;
        private ImageView ivEvent;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDetails = itemView.findViewById(R.id.tvDetails);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvVenue = itemView.findViewById(R.id.tvVenue);
            tvCost = itemView.findViewById(R.id.tvCost);
            ivEvent = itemView.findViewById(R.id.iv_event);
        }

        /*public <E> void bind(final E item, final int position, final OnListViewItemClickListener listener) {

            itemView.setOnClickListener(v -> {
                if (listener != null)
                    listener.onItemClick(item, position, itemView, null);
            });
        }*/
    }
}



