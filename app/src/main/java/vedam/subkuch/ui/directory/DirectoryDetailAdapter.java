package vedam.subkuch.ui.directory;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import vedam.subkuch.R;
import vedam.subkuch.interfaces.OnListViewItemClickListener;
import vedam.subkuch.ui.directory.models.Business;
import vedam.subkuch.ui.directory.models.BusinessAddress;
import vedam.subkuch.utils.AppUtil;
import vedam.subkuch.utils.ListItemClickAction;
import vedam.subkuch.utils.UiUtil;


public class DirectoryDetailAdapter extends BaseExpandableListAdapter {

    private LayoutInflater inflater;
    private ArrayList<Business> directoryDetails;
    private OnListViewItemClickListener onListViewItemClickListener;

    public DirectoryDetailAdapter(Context context, ArrayList<Business> directoryDetails, OnListViewItemClickListener onListViewItemClickListener) {

        inflater = LayoutInflater.from(context);
        this.directoryDetails = directoryDetails;
        this.onListViewItemClickListener = onListViewItemClickListener;
    }


    @Override
    public int getGroupCount() {
        return directoryDetails.size();
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return directoryDetails.get(listPosition).getBusinessAddresses().length - 1;
    }

    @Override
    public Object getGroup(int listPosition) {
        return directoryDetails.get(listPosition);
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return directoryDetails.get(listPosition).getBusinessAddresses()[expandedListPosition + 1];
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition + 1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded, View v, ViewGroup parent) {

        DirectoryDetailAdapter.ViewHolder holder;

        if (v == null) {
            v = inflater.inflate(R.layout.fragment_directory_details_list_item, parent, false);
            holder = new DirectoryDetailAdapter.ViewHolder();
            holder.tvName = v.findViewById(R.id.tvName);
            holder.tvDistance = v.findViewById(R.id.tvDistance);
            holder.rbRating = v.findViewById(R.id.rb_rating);
            holder.tvReviews = v.findViewById(R.id.tv_reviews);
            holder.tvDealingIn = v.findViewById(R.id.tvDealingIn);
            holder.tvAddress = v.findViewById(R.id.tv_address);
            holder.tvPhone = v.findViewById(R.id.tvPhone);
            holder.tvMobile = v.findViewById(R.id.tvMobile);
            holder.tvEmail = v.findViewById(R.id.tvEmail);
            holder.tvWebsite = v.findViewById(R.id.tvWebsite);
            holder.tvContactPerson = v.findViewById(R.id.tvContactPerson);
            holder.tvLine1 = v.findViewById(R.id.tvLine1);
            holder.tvLine2 = v.findViewById(R.id.tvLine2);
            holder.ibDirection = v.findViewById(R.id.ib_direction);
            holder.ivTriangle = v.findViewById(R.id.iv_triangle);
            holder.rlBranchesContainer = v.findViewById(R.id.rl_branches_container);
            holder.llRatings = v.findViewById(R.id.ll_ratings);
            v.setTag(holder);
        } else {
            holder = (DirectoryDetailAdapter.ViewHolder) v.getTag();
        }

        Business directoryDetail = (Business) getGroup(listPosition);
        if (directoryDetail.getBusinessAddresses().length > 0) {
            BusinessAddress businessAddress = directoryDetail.getBusinessAddresses()[0];
            businessAddress.setCity(directoryDetail.getCity());
            setText(holder.tvWebsite, "Website :", directoryDetail.getWebsite());

            holder.tvName.setText(directoryDetail.getBusinessName());

            UiUtil.setTextView(businessAddress.getDistance(), "KMs away", holder.tvDistance);
            setText(holder.tvDealingIn, "Dealing in :", businessAddress.getDealingIn());
            String formattedAddress = AppUtil.getFormattedAddress(businessAddress);
            UiUtil.setTextView(holder.tvAddress, formattedAddress);
            setText(holder.tvPhone, "Ph :", businessAddress.getPhoneNo());
            setText(holder.tvMobile, "Mobile :", businessAddress.getMobile1());
            setText(holder.tvEmail, "Email :", businessAddress.getEmail());
            setText(holder.tvContactPerson, "Contact Person :", businessAddress.getContactPerson());
            UiUtil.setTextView(holder.tvLine1, businessAddress.getInfoLine1());
            UiUtil.setTextView(holder.tvLine2, businessAddress.getInfoLine2());

            int noOfReviews = directoryDetail.getReviews().length;
            if (noOfReviews != 0) {
                holder.llRatings.setVisibility(View.VISIBLE);
                UiUtil.setTextView(holder.tvReviews, String.format(Locale.US, "(%d %s)", noOfReviews,
                        AppUtil.getSingularOrPluralString("Review", noOfReviews)));
                if (!TextUtils.isEmpty(directoryDetail.getAvegrageOfRating()) && AppUtil.isNumeric(directoryDetail.getAvegrageOfRating())) {
                    holder.rbRating.setVisibility(View.VISIBLE);
                    holder.rbRating.setRating(Float.valueOf(directoryDetail.getAvegrageOfRating()));
                } else
                    holder.rbRating.setVisibility(View.GONE);
            } else
                holder.llRatings.setVisibility(View.GONE);

            if (directoryDetail.getBusinessAddresses().length > 1) {
                holder.rlBranchesContainer.setVisibility(View.VISIBLE);
                int imageResourceId = isExpanded ? R.drawable.baseline_expand_less_black_24dp : R.drawable.baseline_expand_more_black_24dp;
                holder.ivTriangle.setImageResource(imageResourceId);
                holder.rlBranchesContainer.setOnClickListener(view -> {
                    if (isExpanded) ((ExpandableListView) parent).collapseGroup(listPosition);
                    else ((ExpandableListView) parent).expandGroup(listPosition, true);
                });
            } else
                holder.rlBranchesContainer.setVisibility(View.GONE);

            v.setOnClickListener(view -> {
                if (onListViewItemClickListener != null)
                    onListViewItemClickListener.onItemClick(directoryDetail, listPosition, view, ListItemClickAction.SELECT);
            });

            if (!TextUtils.isEmpty(businessAddress.getLatitude()) && !TextUtils.isEmpty(businessAddress.getLongitude())) {
                holder.ibDirection.setVisibility(View.VISIBLE);
                holder.ibDirection.setOnClickListener(view -> {
                    String webURL = "https://www.google.com/maps/dir/?api=1&" + "destination=" + businessAddress.getLatitude() + "%2C" + businessAddress.getLongitude();
                    AppUtil.openUrl(parent.getContext(), webURL);
                });
            } else
                holder.ibDirection.setVisibility(View.GONE);
        }
        return v;
    }

    @Override
    public View getChildView(int listPosition, int expandedListPosition, boolean isExpanded, View v, ViewGroup viewGroup) {
        DirectoryDetailAdapter.ChildViewHolder holder;

        if (v == null) {
            v = inflater.inflate(R.layout.fragment_directory_details_child_list_item, viewGroup, false);
//            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams
//                    (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            int dps = AppUtil.dpToPx(viewGroup.getContext(), 16);
//            layoutParams.leftMargin = dps;
//            layoutParams.rightMargin = dps;
//            v.setLayoutParams(layoutParams);

            holder = new DirectoryDetailAdapter.ChildViewHolder();
            holder.tvAddress = v.findViewById(R.id.tv_address);
            holder.tvDistance = v.findViewById(R.id.tvDistance);
            holder.tvDealingIn = v.findViewById(R.id.tvDealingIn);
            holder.tvPhone = v.findViewById(R.id.tvPhone);
            holder.tvMobile = v.findViewById(R.id.tvMobile);
            holder.tvEmail = v.findViewById(R.id.tvEmail);
            holder.tvContactPerson = v.findViewById(R.id.tvContactPerson);
            holder.tvLine1 = v.findViewById(R.id.tvLine1);
            holder.tvLine2 = v.findViewById(R.id.tvLine2);
            holder.ibDirection = v.findViewById(R.id.ib_direction);
            v.setTag(holder);
        } else {
            holder = (DirectoryDetailAdapter.ChildViewHolder) v.getTag();
        }

        Business directoryDetail = (Business) getGroup(listPosition);
        BusinessAddress businessAddress = directoryDetail.getBusinessAddresses()[expandedListPosition + 1];

        UiUtil.setTextView(holder.tvDistance, businessAddress.getDistance());
        setText(holder.tvDealingIn, "Dealing in :", businessAddress.getDealingIn());
        String formattedAddress = AppUtil.getFormattedAddress(businessAddress);
        UiUtil.setTextView(holder.tvAddress, formattedAddress);
        setText(holder.tvPhone, "Ph :", businessAddress.getPhoneNo());
        setText(holder.tvMobile, "Mobile :", businessAddress.getMobile1());
        setText(holder.tvEmail, "Email :", businessAddress.getEmail());
        setText(holder.tvContactPerson, "Contact Person :", businessAddress.getContactPerson());
        UiUtil.setTextView(holder.tvLine1, businessAddress.getInfoLine1());
        UiUtil.setTextView(holder.tvLine2, businessAddress.getInfoLine2());

        if (!TextUtils.isEmpty(businessAddress.getLatitude()) && !TextUtils.isEmpty(businessAddress.getLongitude())) {
            holder.ibDirection.setVisibility(View.VISIBLE);
            holder.ibDirection.setOnClickListener(view -> {
                String webURL = "https://www.google.com/maps/dir/?api=1&" + "destination=" + businessAddress.getLatitude() + "%2C" + businessAddress.getLongitude();
                AppUtil.openUrl(viewGroup.getContext(), webURL);
            });
        } else
            holder.ibDirection.setVisibility(View.GONE);

        return v;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return false;
    }

    private void setText(TextView tv, String prefix, String text) {
        if (TextUtils.isEmpty(text)) {
            tv.setVisibility(View.GONE);
        } else {
            tv.setVisibility(View.VISIBLE);
            tv.setText(String.format("%s %s", prefix, text));
        }
    }


    private static class ViewHolder {

        private TextView tvName;
        private TextView tvDistance;
        private RatingBar rbRating;
        private TextView tvReviews;
        private LinearLayout llRatings;
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
        private ImageView ivTriangle;
        private RelativeLayout rlBranchesContainer;
    }

    private static class ChildViewHolder {
        private TextView tvDistance;
        private TextView tvDealingIn;
        private TextView tvAddress;
        private TextView tvPhone;
        private TextView tvMobile;
        private TextView tvEmail;
        private TextView tvContactPerson;
        private TextView tvLine1;
        private TextView tvLine2;
        private ImageButton ibDirection;
    }
}
