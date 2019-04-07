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
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.models.TransportBooking;
import vedam.subkuch.utils.UiUtil;

public class MyBookingsAdapter extends RecyclerView.Adapter<MyBookingsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<TransportBooking> transportBookings;
    private BookingCompleteListener bookingCompleteListener;

    MyBookingsAdapter(Context context, ArrayList<TransportBooking> transportBookings, BookingCompleteListener bookingCompleteListener) {

        this.context = context;
        this.transportBookings = transportBookings;
        this.bookingCompleteListener = bookingCompleteListener;
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
        UiUtil.setTextView("Transport Material :", transportBooking.getItemType(), holder.tvTransportMaterial);
        UiUtil.setTextView("Status :", transportBooking.getCurrentStatus(), holder.tvStatus);

        if (transportBooking.getStatus() == Constants.STATUS_OPEN) {
            holder.btMarkComplete.setVisibility(View.VISIBLE);
            holder.btMarkComplete.setOnClickListener(v -> {
                if (bookingCompleteListener != null)
                    bookingCompleteListener.onBookingCompleteRequest(transportBooking.getTransportId());
            });
        } else {
            holder.btMarkComplete.setOnClickListener(null);
            holder.btMarkComplete.setVisibility(View.GONE);
        }

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
        private TextView tvTransportMaterial;
        private Button btMarkComplete;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvFrom = itemView.findViewById(R.id.tv_from);
            tvTo = itemView.findViewById(R.id.tv_to);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvTransportMaterial = itemView.findViewById(R.id.tv_transport_material);
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
