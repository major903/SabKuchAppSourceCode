package vedam.subkuch.ui.realestate;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vedam.subkuch.R;
import vedam.subkuch.network.models.EstateListItem;

/** RecyclerView adapter for the public real estate listings browse screen. */
public class RealEstateListingsAdapter
        extends RecyclerView.Adapter<RealEstateListingsAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<EstateListItem> listings = new ArrayList<>();
    private final Map<Integer, String> estateTypes = new HashMap<>();

    RealEstateListingsAdapter(Context context) {
        this.context = context;
    }

    void setListings(List<EstateListItem> values) {
        listings.clear();
        if (values != null) listings.addAll(values);
        notifyDataSetChanged();
    }

    void setEstateTypes(Map<Integer, String> types) {
        estateTypes.clear();
        if (types != null) estateTypes.putAll(types);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_real_estate_listing, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EstateListItem item = listings.get(position);

        String typeLabel = item.getEstateTypeId() == null ? null
                : estateTypes.get(item.getEstateTypeId());
        if (typeLabel == null || typeLabel.trim().isEmpty()) {
            holder.tvEstateType.setVisibility(View.GONE);
        } else {
            holder.tvEstateType.setText(typeLabel);
            holder.tvEstateType.setVisibility(View.VISIBLE);
        }

        holder.tvLocation.setText(item.getLocation());

        if (item.getDetails() == null || item.getDetails().trim().isEmpty()) {
            holder.tvDetails.setVisibility(View.GONE);
        } else {
            holder.tvDetails.setText(item.getDetails().trim());
            holder.tvDetails.setVisibility(View.VISIBLE);
        }

        holder.tvName.setText(item.getName());
        holder.tvMobile.setText(item.getMobile());

        View.OnClickListener dialListener = v -> {
            String mobile = item.getMobile();
            if (mobile == null || mobile.trim().isEmpty()) return;
            context.startActivity(new Intent(Intent.ACTION_DIAL,
                    Uri.parse("tel:" + mobile.trim())));
        };
        holder.tvMobile.setOnClickListener(dialListener);
        holder.ibCall.setOnClickListener(dialListener);
    }

    @Override
    public int getItemCount() {
        return listings.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvEstateType;
        private final TextView tvLocation;
        private final TextView tvDetails;
        private final TextView tvName;
        private final TextView tvMobile;
        private final ImageButton ibCall;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEstateType = itemView.findViewById(R.id.tv_estate_type);
            tvLocation = itemView.findViewById(R.id.tv_location);
            tvDetails = itemView.findViewById(R.id.tv_details);
            tvName = itemView.findViewById(R.id.tv_name);
            tvMobile = itemView.findViewById(R.id.tv_mobile);
            ibCall = itemView.findViewById(R.id.ib_call);
        }
    }
}
