package vedam.subkuch.ui.classifieds;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;

import java.util.ArrayList;

import vedam.subkuch.R;
import vedam.subkuch.network.models.classifieds.Classified;
import vedam.subkuch.utils.AppUtil;
import vedam.subkuch.utils.DateTimeUtils;
import vedam.subkuch.utils.ImageSetter;
import vedam.subkuch.utils.UiUtil;

public class ClassifiedDetailsAdapter extends RecyclerView.Adapter<ClassifiedDetailsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Classified> classifieds;

    ClassifiedDetailsAdapter(Context context, ArrayList<Classified> classifieds) {

        this.context = context;
        this.classifieds = classifieds;
    }

    @NonNull
    @Override
    public ClassifiedDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.fragment_classified_details_list_item, parent, false);
        return new ClassifiedDetailsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassifiedDetailsAdapter.ViewHolder holder, int position) {

        Classified classified = classifieds.get(position);

        UiUtil.setTextView(holder.tvUserName, getNameAndDate(classified));
        UiUtil.setTextView(holder.tvTitle, classified.getTitle());
        UiUtil.setTextView(holder.tvAbout, classified.getAbout());
        UiUtil.setTextView(holder.tvDistance, classified.getFormattedDistance());
        UiUtil.setTextView(holder.tvContact, classified.getContact());
        UiUtil.setTextView("Location :", getLocation(classified), holder.tvAddress);
        UiUtil.setTextView("Advertised Price : Rs.", classified.getFormattedRate(), holder.tvAdPrice);
        UiUtil.setTextView("Current Price : Rs.", classified.getTodaysPrice(), holder.tvCurrentPrice);
        UiUtil.setTextView("Daily Discount : Rs.", classified.getDailyDiscount(), holder.tvDailyDiscount);
        UiUtil.setTextView(holder.tvDistance, classified.getFormattedDistance());

        if (!TextUtils.isEmpty(classified.getImageUrl())) {
            holder.ivAd.setVisibility(View.VISIBLE);
            UiUtil.setImageView(new ImageSetter.ImageBuilder(context)
                    .setImageLink(classified.getImageUrl())
                    .setDefaults()
                    .setTarget(holder.ivAd)
                    .setCallback(new Callback() {
                        @Override
                        public void onSuccess() {
                            holder.ivAd.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError(Exception e) {
                            holder.ivAd.setVisibility(View.GONE);
                        }
                    })
                    .build());
        } else
            holder.ivAd.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(classified.getLatitude()) && !TextUtils.isEmpty(classified.getLongitude())) {
            holder.ibDirection.setVisibility(View.VISIBLE);
            holder.ibDirection.setOnClickListener(view -> {
                String webURL = "https://www.google.com/maps/dir/?api=1&" + "destination=" + classified.getLatitude() + "%2C" + classified.getLongitude();
                AppUtil.openUrl(context, webURL);
            });
        } else
            holder.ibDirection.setVisibility(View.GONE);
    }

    private String getNameAndDate(Classified classified) {
        StringBuilder sb = new StringBuilder();
        if (!TextUtils.isEmpty(classified.getUserName()))
            sb.append(classified.getUserName());
        if (!TextUtils.isEmpty(classified.getUpdatedAt()))
            sb.append(" ").append(DateTimeUtils.getFormattedDate(classified.getUpdatedAt(),
                    DateTimeUtils.DATE_TIME_FORMAT_1, DateTimeUtils.DATE_FORMAT_5));
        return sb.toString().trim();
    }

    private String getLocation(Classified classified) {
        StringBuilder sb = new StringBuilder();
        if (!TextUtils.isEmpty(classified.getLocality()))
            sb.append(classified.getLocality());
        if (!TextUtils.isEmpty(classified.getCityName()))
            sb.append(", ").append(classified.getCityName());
        return sb.toString();
    }

    @Override
    public int getItemCount() {
        return classifieds.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvUserName;
        private TextView tvTitle;
        private TextView tvAbout;
        private TextView tvAddress;
        private TextView tvDistance;
        private TextView tvContact;
        private TextView tvAdPrice;
        private TextView tvCurrentPrice;
        private TextView tvDailyDiscount;
        private ImageView ivAd;
        private ImageView ibDirection;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvUserName = itemView.findViewById(R.id.tv_user_name);
            tvAbout = itemView.findViewById(R.id.tv_about);
            tvAddress = itemView.findViewById(R.id.tv_address);
            tvDistance = itemView.findViewById(R.id.tv_distance);
            tvContact = itemView.findViewById(R.id.tv_contact);
            tvAdPrice = itemView.findViewById(R.id.tv_ad_price);
            tvCurrentPrice = itemView.findViewById(R.id.tv_current_price);
            tvDailyDiscount = itemView.findViewById(R.id.tv_daily_discount);
            ivAd = itemView.findViewById(R.id.iv_ad);
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
