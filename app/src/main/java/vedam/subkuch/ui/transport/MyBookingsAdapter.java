package vedam.subkuch.ui.transport;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import vedam.subkuch.R;
import vedam.subkuch.network.models.TransportBooking;
import vedam.subkuch.utils.UiUtil;

public class MyBookingsAdapter extends RecyclerView.Adapter<MyBookingsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<TransportBooking> transportBookings;

    MyBookingsAdapter(Context context, ArrayList<TransportBooking> transportBookings) {

        this.context = context;
        this.transportBookings = transportBookings;
    }

    @NonNull
    @Override
    public MyBookingsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.fragment_my_bookings_list_item, parent, false);
        return new MyBookingsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyBookingsAdapter.ViewHolder holder, int position) {

        TransportBooking transportBooking = transportBookings.get(position);

        UiUtil.setTextView("Date :", transportBooking.getDate(), holder.tvDate);
        UiUtil.setTextView("From :", transportBooking.getPickupLocation(), holder.tvFrom);
        UiUtil.setTextView("To :", transportBooking.getDropLocation(), holder.tvTo);
        UiUtil.setTextView("Status :", transportBooking.getStatus(), holder.tvStatus);

        holder.btMarkComplete.setOnClickListener(v -> markComplete(transportBooking.getTransportId()));

    }

    private void markComplete(String transportId) {


    }

    @Override
    public int getItemCount() {
        return transportBookings.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvDate;
        private TextView tvFrom;
        private TextView tvTo;
        private TextView tvStatus;
        private Button btMarkComplete;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvFrom = itemView.findViewById(R.id.tv_from);
            tvTo = itemView.findViewById(R.id.tv_to);
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
}
