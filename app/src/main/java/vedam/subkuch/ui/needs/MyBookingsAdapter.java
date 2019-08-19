package vedam.subkuch.ui.needs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import vedam.subkuch.R;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.models.needs.Need;
import vedam.subkuch.utils.UiUtil;

public class MyBookingsAdapter extends RecyclerView.Adapter<MyBookingsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Need> needBookings;
    private BookingCompleteListener bookingCompleteListener;

    MyBookingsAdapter(Context context, ArrayList<Need> needBookings, BookingCompleteListener bookingCompleteListener) {

        this.context = context;
        this.needBookings = needBookings;
        this.bookingCompleteListener = bookingCompleteListener;
    }

    @NonNull
    @Override
    public MyBookingsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.fragment_needs_my_bookings_list_item, parent, false);
        return new MyBookingsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyBookingsAdapter.ViewHolder holder, int position) {

        Need need = needBookings.get(position);

        UiUtil.setTextView("Work Location :", need.getWorkLocation(), holder.tvWorkLocation);
        UiUtil.setTextView("Work Details :", need.getWorkDetails(), holder.tvWorkDetails);
        UiUtil.setTextView("Status :", need.getCurrentStatus(), holder.tvStatus);

        if (need.getStatus() == Constants.STATUS_OPEN) {
            holder.btMarkComplete.setVisibility(View.VISIBLE);
            holder.btMarkComplete.setOnClickListener(v -> {
                if (bookingCompleteListener != null)
                    bookingCompleteListener.onBookingCompleteRequest(need.getNeedId());
            });
        } else {
            holder.btMarkComplete.setOnClickListener(null);
            holder.btMarkComplete.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return needBookings.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvWorkLocation;
        private TextView tvWorkDetails;
        private TextView tvStatus;
        private Button btMarkComplete;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvWorkLocation = itemView.findViewById(R.id.tv_work_location);
            tvWorkDetails = itemView.findViewById(R.id.tv_work_details);
            tvStatus = itemView.findViewById(R.id.tv_status);
            btMarkComplete = itemView.findViewById(R.id.bt_mark_complete);
        }

        /*public <E> void bind(final E item, final int position, final OnListViewItemClickListener listener) {

            itemView.setOnClickListener(v -> {
                if (listener != null)
                    listener.onItemClick(item, position, itemView, null);
            });
        }*/
    }

    public interface BookingCompleteListener {
        void onBookingCompleteRequest(String transportId);
    }
}
