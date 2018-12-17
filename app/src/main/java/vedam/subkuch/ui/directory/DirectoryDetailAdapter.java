package vedam.subkuch.ui.directory;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import vedam.subkuch.R;
import vedam.subkuch.interfaces.OnListViewItemClickListener;
import vedam.subkuch.ui.directory.models.Address;
import vedam.subkuch.ui.directory.models.Business;
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
        return directoryDetails.get(listPosition).getAddresses().length;
    }

    @Override
    public Object getGroup(int listPosition) {
        return directoryDetails.get(listPosition);
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return directoryDetails.get(listPosition).getAddresses()[expandedListPosition];
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded, View v, ViewGroup parent) {

        DirectoryDetailAdapter.ViewHolder holder;

        if (v == null) {
            v = inflater.inflate(R.layout.fragment_directory_details_list_item, null);
            holder = new DirectoryDetailAdapter.ViewHolder();
            holder.tvName = v.findViewById(R.id.tvName);
            holder.tvDistance = v.findViewById(R.id.tvDistance);
            holder.rbRating = v.findViewById(R.id.rb_rating);
            holder.tvReviews = v.findViewById(R.id.tv_reviews);
            holder.tvPhone = v.findViewById(R.id.tvPhone);
            holder.tvMobile = v.findViewById(R.id.tvMobile);
            holder.tvEmail = v.findViewById(R.id.tvEmail);
            holder.tvWebsite = v.findViewById(R.id.tvWebsite);
            holder.tvContactPerson = v.findViewById(R.id.tvContactPerson);
            holder.ivTriangle = v.findViewById(R.id.iv_triangle);
            holder.rlBranchesContainer = v.findViewById(R.id.rl_branches_container);
            v.setTag(holder);
        } else {
            holder = (DirectoryDetailAdapter.ViewHolder) v.getTag();
        }

        Business directoryDetail = (Business) getGroup(listPosition);

        holder.tvName.setText(directoryDetail.getBusinessName());

        UiUtil.setTextView(holder.tvDistance, directoryDetail.getDistance());
        setText(holder.tvPhone, "Ph :", directoryDetail.getPhone());
        setText(holder.tvMobile, "Mobile :", directoryDetail.getMobile());
        setText(holder.tvEmail, "Email :", directoryDetail.getEmail());
        setText(holder.tvWebsite, "Website :", directoryDetail.getWebsite());
        setText(holder.tvContactPerson, "Contact Person :", directoryDetail.getContactPerson());

        if (!TextUtils.isEmpty(directoryDetail.getAvegrageOfRating()) && AppUtil.isNumeric(directoryDetail.getAvegrageOfRating())) {
            holder.rbRating.setVisibility(View.VISIBLE);
            holder.rbRating.setRating(Float.valueOf(directoryDetail.getAvegrageOfRating()));
        } else
            holder.rbRating.setVisibility(View.GONE);

        if (directoryDetail.getReviews().length != 0)
            UiUtil.setTextView(holder.tvReviews, String.format(Locale.US, "( %d Reviews)", directoryDetail.getReviews().length));
        else
            holder.tvReviews.setVisibility(View.GONE);

        int imageResourceId = isExpanded ? R.drawable.baseline_expand_less_black_24dp : R.drawable.baseline_expand_more_black_24dp;
        holder.ivTriangle.setImageResource(imageResourceId);

        holder.rlBranchesContainer.setOnClickListener(view -> {
            if (isExpanded) ((ExpandableListView) parent).collapseGroup(listPosition);
            else ((ExpandableListView) parent).expandGroup(listPosition, true);
        });

        v.setOnClickListener(view -> {
            if (onListViewItemClickListener != null)
                onListViewItemClickListener.onItemClick(directoryDetail, listPosition, view, ListItemClickAction.SELECT);
        });

        return v;
    }

    @Override
    public View getChildView(int listPosition, int expandedListPosition, boolean isExpanded, View v, ViewGroup viewGroup) {
        DirectoryDetailAdapter.ChildViewHolder holder;

        if (v == null) {
            v = inflater.inflate(R.layout.fragment_directory_details_child_list_item, null);
//            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams
//                    (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            int dps = AppUtil.dpToPx(viewGroup.getContext(), 16);
//            layoutParams.leftMargin = dps;
//            layoutParams.rightMargin = dps;
//            v.setLayoutParams(layoutParams);

            holder = new DirectoryDetailAdapter.ChildViewHolder();
            holder.tvAddress = v.findViewById(R.id.tv_address);
            holder.btDirection = v.findViewById(R.id.bt_direction);
            v.setTag(holder);
        } else {
            holder = (DirectoryDetailAdapter.ChildViewHolder) v.getTag();
        }

        Business directoryDetail = (Business) getGroup(listPosition);
        Address address = directoryDetail.getAddresses()[expandedListPosition];
        String formattedAddress = AppUtil.getFormattedAddress(address);
        UiUtil.setTextView(holder.tvAddress, formattedAddress);

        if (!TextUtils.isEmpty(address.getLatitude()) && !TextUtils.isEmpty(address.getLongitude())) {
            holder.btDirection.setVisibility(View.VISIBLE);
            holder.btDirection.setOnClickListener(view -> {
                String webURL = "https://www.google.com/maps/dir/?api=1&" + "destination=" + address.getLatitude() + "%2C" + address.getLongitude();
                AppUtil.openUrl(viewGroup.getContext(), webURL);
            });
        } else
            holder.btDirection.setVisibility(View.GONE);

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
        private TextView tvPhone;
        private TextView tvMobile;
        private TextView tvEmail;
        private TextView tvWebsite;
        private TextView tvContactPerson;
        private ImageView ivTriangle;
        private RelativeLayout rlBranchesContainer;
    }

    private static class ChildViewHolder {
        private TextView tvAddress;
        private Button btDirection;
    }
}
