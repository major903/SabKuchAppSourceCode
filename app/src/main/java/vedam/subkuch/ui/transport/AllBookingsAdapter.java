package vedam.subkuch.ui.transport;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import vedam.subkuch.R;
import vedam.subkuch.network.models.transport.TransportBooking;
import vedam.subkuch.utils.AppUtil;
import vedam.subkuch.utils.UiUtil;

public class AllBookingsAdapter extends RecyclerView.Adapter<AllBookingsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<TransportBooking> transportBookings;

    AllBookingsAdapter(Context context, ArrayList<TransportBooking> transportBookings) {

        this.context = context;
        this.transportBookings = transportBookings;
    }

    @NonNull
    @Override
    public AllBookingsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.fragment_all_bookings_list_item, parent, false);
        return new AllBookingsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllBookingsAdapter.ViewHolder holder, int position) {

        TransportBooking transportBooking = transportBookings.get(position);

        UiUtil.setTextView("Client Name :", transportBooking.getFirstName(), holder.tvName);
        UiUtil.setTextView(holder.tvDistance, transportBooking.getDistance());
        UiUtil.setTextView("Mobile Number :", transportBooking.getMobile(), holder.tvMobileNumber);
        UiUtil.setTextView("Pick up location :", transportBooking.getPickupLocation(), holder.tvPickUp);
        UiUtil.setTextView("Destination :", transportBooking.getDropLocation(), holder.tvDestination);
        UiUtil.setTextView("Transport Material :", transportBooking.getItemType(), holder.tvTransportMaterial);
        UiUtil.setTextView("Labour Requirement :", transportBooking.getTransportCoolieName(), holder.tvLabourRequirement);
        UiUtil.setTextView("Vehicle Type :", transportBooking.getTransportTypeName(), holder.tvVehicleType);

        if (!TextUtils.isEmpty(transportBooking.getLatitude()) && !TextUtils.isEmpty(transportBooking.getLongitude())) {
            holder.ibDirection.setVisibility(View.VISIBLE);
            holder.ibDirection.setOnClickListener(view -> {
                String webURL = "https://www.google.com/maps/dir/?api=1&" + "destination=" + transportBooking.getLatitude() + "%2C" + transportBooking.getLongitude();
                AppUtil.openUrl(context, webURL);
            });
        } else
            holder.ibDirection.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return transportBookings.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName;
        private TextView tvDistance;
        private TextView tvMobileNumber;
        private TextView tvPickUp;
        private TextView tvDestination;
        private TextView tvTransportMaterial;
        private TextView tvLabourRequirement;
        private TextView tvVehicleType;
        private ImageButton ibDirection;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvDistance = itemView.findViewById(R.id.tv_distance);
            tvMobileNumber = itemView.findViewById(R.id.tv_mobile_number);
            tvPickUp = itemView.findViewById(R.id.tv_pick_up);
            tvDestination = itemView.findViewById(R.id.tv_destination);
            ibDirection = itemView.findViewById(R.id.ib_direction);
            tvTransportMaterial = itemView.findViewById(R.id.tv_transport_material);
            tvLabourRequirement = itemView.findViewById(R.id.tv_labour_requirement);
            tvVehicleType = itemView.findViewById(R.id.tv_vehicle_type);
        }

        /*public <E> void bind(final E item, final int position, final OnListViewItemClickListener listener) {

            itemView.setOnClickListener(v -> {
                if (listener != null)
                    listener.onItemClick(item, position, itemView, null);
            });
        }*/
    }
}
