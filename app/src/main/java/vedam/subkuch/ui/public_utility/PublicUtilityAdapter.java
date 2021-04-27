package vedam.subkuch.ui.public_utility;

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
import vedam.subkuch.network.models.public_utility.PublicUtility;
import vedam.subkuch.utils.AppUtil;
import vedam.subkuch.utils.UiUtil;


public class PublicUtilityAdapter extends RecyclerView.Adapter<PublicUtilityAdapter.PublicUtilityViewHolder> {

    private ArrayList<PublicUtility> publicUtilities;
    private Context context;

    PublicUtilityAdapter(Context context, ArrayList<PublicUtility> publicUtilities) {

        this.publicUtilities = publicUtilities;
        this.context = context;
    }

    @NonNull
    @Override
    public PublicUtilityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.fragment_utilities_details_list_item, parent, false);
        return new PublicUtilityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PublicUtilityViewHolder holder, int position) {
        PublicUtility publicUtility = publicUtilities.get(position);

        setText(holder.tvWebsite, "Website :", publicUtility.getWebsite());

        holder.tvName.setText(publicUtility.getName());

        UiUtil.setTextView(holder.tvDistance, publicUtility.getDistance());
        setText(holder.tvDealingIn, "Dealing in :", publicUtility.getDealingIn());
        String formattedAddress = AppUtil.getFormattedAddress(publicUtility);
        UiUtil.setTextView(holder.tvAddress, formattedAddress);
        setText(holder.tvPhone, "Ph :", publicUtility.getPhoneNo());
        setText(holder.tvMobile, "Mobile :", publicUtility.getMobile1());
        setText(holder.tvEmail, "Email :", publicUtility.getEmailId());
        setText(holder.tvContactPerson, "Contact Person :", publicUtility.getContactPerson());
        UiUtil.setTextView(holder.tvLine1, publicUtility.getInfoLine1());
        UiUtil.setTextView(holder.tvLine2, publicUtility.getInfoLine2());


        if (!TextUtils.isEmpty(publicUtility.getLatitude()) && !TextUtils.isEmpty(publicUtility.getLongitude())) {
            holder.ibDirection.setVisibility(View.VISIBLE);
            holder.ibDirection.setOnClickListener(view -> {
                String webURL = "https://www.google.com/maps/dir/?api=1&" + "destination=" + publicUtility.getLatitude() + "%2C" + publicUtility.getLongitude();
                AppUtil.openUrl(context, webURL);
            });
        } else
            holder.ibDirection.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return publicUtilities.size();
    }

    private void setText(TextView tv, String prefix, String text) {
        if (TextUtils.isEmpty(text)) {
            tv.setVisibility(View.GONE);
        } else {
            tv.setVisibility(View.VISIBLE);
            tv.setText(String.format("%s %s", prefix, text));
        }
    }


    static class PublicUtilityViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName;
        private TextView tvDistance;
        private TextView tvDealingIn;
        private TextView tvAddress;
        private TextView tvPhone;
        private TextView tvMobile;
        private TextView tvEmail;
        private TextView tvWebsite;
        private TextView tvContactPerson;
        private TextView tvLine1;
        private TextView tvLine2;
        private ImageButton ibDirection;

        PublicUtilityViewHolder(@NonNull View v) {
            super(v);
            tvName = v.findViewById(R.id.tvName);
            tvDistance = v.findViewById(R.id.tvDistance);
            tvDealingIn = v.findViewById(R.id.tvDealingIn);
            tvAddress = v.findViewById(R.id.tv_address);
            tvPhone = v.findViewById(R.id.tvPhone);
            tvMobile = v.findViewById(R.id.tvMobile);
            tvEmail = v.findViewById(R.id.tvEmail);
            tvWebsite = v.findViewById(R.id.tvWebsite);
            tvContactPerson = v.findViewById(R.id.tvContactPerson);
            tvLine1 = v.findViewById(R.id.tvLine1);
            tvLine2 = v.findViewById(R.id.tvLine2);
            ibDirection = v.findViewById(R.id.ib_direction);
        }
    }
}
