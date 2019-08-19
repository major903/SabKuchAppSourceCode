package vedam.subkuch.ui.needs;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import vedam.subkuch.R;
import vedam.subkuch.network.models.needs.Need;
import vedam.subkuch.utils.AppUtil;
import vedam.subkuch.utils.UiUtil;

public class AllBookingsAdapter extends RecyclerView.Adapter<AllBookingsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Need> needs;

    AllBookingsAdapter(Context context, ArrayList<Need> needs) {

        this.context = context;
        this.needs = needs;
    }

    @NonNull
    @Override
    public AllBookingsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.fragment_needs_all_booking_list_item, parent, false);
        return new AllBookingsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllBookingsAdapter.ViewHolder holder, int position) {

        Need need = needs.get(position);

        UiUtil.setTextView("Client Name :", need.getFirstName(), holder.tvName);
        UiUtil.setTextView("Mobile Number :", need.getMobile(), holder.tvMobileNumber);
        UiUtil.setTextView("Work Location :", need.getWorkLocation(), holder.tvWorkLocation);
        UiUtil.setTextView("Work Details :", need.getWorkDetails(), holder.tvWorkDetails);
        UiUtil.setTextView(holder.tvDistance, need.getDistance());
        if (!TextUtils.isEmpty(need.getLatitude()) && !TextUtils.isEmpty(need.getLongitude())) {
            holder.ibDirection.setVisibility(View.VISIBLE);
            holder.ibDirection.setOnClickListener(view -> {
                String webURL = "https://www.google.com/maps/dir/?api=1&" + "destination=" + need.getLatitude() + "%2C" + need.getLongitude();
                AppUtil.openUrl(context, webURL);
            });
        } else
            holder.ibDirection.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return needs.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName;
        private TextView tvWorkLocation;
        private TextView tvWorkDetails;
        private TextView tvDistance;
        private TextView tvMobileNumber;
        private ImageButton ibDirection;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvMobileNumber = itemView.findViewById(R.id.tv_mobile_number);
            tvWorkLocation = itemView.findViewById(R.id.tv_work_location);
            tvWorkDetails = itemView.findViewById(R.id.tv_work_details);
            tvDistance = itemView.findViewById(R.id.tv_distance);
            ibDirection = itemView.findViewById(R.id.ib_direction);
        }

        /*public <E> void bind(final E item, final int position, final OnListViewItemClickListener listener) {

            itemView.setOnClickListener(v -> {
                if (listener != null)
                    listener.onItemClick(item, position, itemView, null);
            });
        }*/
    }
}
