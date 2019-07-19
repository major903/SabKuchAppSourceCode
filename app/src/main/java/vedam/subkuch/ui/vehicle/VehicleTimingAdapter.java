package vedam.subkuch.ui.vehicle;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import vedam.subkuch.R;
import vedam.subkuch.ui.vehicle.models.VehicleTiming;
import vedam.subkuch.utils.UiUtil;

public class VehicleTimingAdapter extends RecyclerView.Adapter<VehicleTimingAdapter.ViewHolder> {
    private Context context;
    private ArrayList<VehicleTiming> vehicleTimings;

    VehicleTimingAdapter(Context context, ArrayList<VehicleTiming> vehicleTimings) {

        this.context = context;
        this.vehicleTimings = vehicleTimings;
    }

    @NonNull
    @Override
    public VehicleTimingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.fragment_vehicle_timing_list_item, parent, false);
        return new VehicleTimingAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleTimingAdapter.ViewHolder holder, int position) {

        VehicleTiming vehicleTiming = vehicleTimings.get(position);

        UiUtil.setTextView(holder.tvTitle, vehicleTiming.getTitle());
        UiUtil.setTextView("Vehicle Number :", vehicleTiming.getVehicleNumber(), holder.tvVehicleNumber);
        UiUtil.setTextView("Departure :", vehicleTiming.getDepartureTime(), holder.tvDeparture);
        UiUtil.setTextView("Arrival :", vehicleTiming.getArrivalTime(), holder.tvArrival);
        UiUtil.setTextView("Via :", vehicleTiming.getVia(), holder.tvVia);
        UiUtil.setTextView("Frequency :", vehicleTiming.getFrequency(), holder.tvFrequency);
        UiUtil.setTextView("Fare :", vehicleTiming.getFare(), holder.tvFare);
    }

    @Override
    public int getItemCount() {
        return vehicleTimings.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle;
        private TextView tvVehicleNumber;
        private TextView tvVia;
        private TextView tvFrequency;
        private TextView tvDeparture;
        private TextView tvArrival;
        private TextView tvFare;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvVehicleNumber = itemView.findViewById(R.id.tv_no);
            tvVia = itemView.findViewById(R.id.tv_via);
            tvFrequency = itemView.findViewById(R.id.tv_frequency);
            tvDeparture = itemView.findViewById(R.id.tv_departure);
            tvArrival = itemView.findViewById(R.id.tv_arrival);
            tvFare = itemView.findViewById(R.id.tv_fare);
        }

        /*public <E> void bind(final E item, final int position, final OnListViewItemClickListener listener) {

            itemView.setOnClickListener(v -> {
                if (listener != null)
                    listener.onItemClick(item, position, itemView, null);
            });
        }*/
    }
}
