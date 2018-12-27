package vedam.subkuch.ui.directory;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseFragment;
import vedam.subkuch.databinding.FragmentDetailBinding;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.ui.directory.models.Business;
import vedam.subkuch.ui.directory.models.BusinessAddress;
import vedam.subkuch.utils.AppUtil;
import vedam.subkuch.utils.UiUtil;

public class DetailFragment extends BaseFragment {

    private FragmentDetailBinding fragmentDetailBinding;
    private Business directoryDetail;

    public static DetailFragment newInstance(Business directoryDetail) {

        Bundle args = new Bundle();

        DetailFragment fragment = new DetailFragment();
        args.putParcelable(Constants.EXTRA_DIRECTORY_DETAIL, directoryDetail);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            directoryDetail = getArguments().getParcelable(Constants.EXTRA_DIRECTORY_DETAIL);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentDetailBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false);

        return fragmentDetailBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI();
        bindData();
    }

    private void initUI() {

        if (directoryDetail.getReviews().length > 0) {
            fragmentDetailBinding.rvReviews.setLayoutManager(new LinearLayoutManager(context));
            fragmentDetailBinding.rvReviews.setNestedScrollingEnabled(false);
            fragmentDetailBinding.rvReviews.setAdapter(new ReviewAdapter(directoryDetail.getReviews()));
        } else {
            fragmentDetailBinding.llReviews.setVisibility(View.GONE);
        }
    }

    private void bindData() {

        if (!TextUtils.isEmpty(directoryDetail.getBusinessImage())) {
            fragmentDetailBinding.ivEvent.setVisibility(View.VISIBLE);
            Picasso.with(context).load(directoryDetail.getBusinessImage())
                    .placeholder(R.drawable.grey).error(R.drawable.grey).into(fragmentDetailBinding.ivEvent, new Callback() {
                @Override
                public void onSuccess() {
                    fragmentDetailBinding.ivEvent.setVisibility(View.VISIBLE);
                }

                @Override
                public void onError() {
                    fragmentDetailBinding.ivEvent.setVisibility(View.GONE);
                }
            });
        } else
            fragmentDetailBinding.ivEvent.setVisibility(View.GONE);

        fragmentDetailBinding.tvName.setText(directoryDetail.getBusinessName());
        UiUtil.setTextView(fragmentDetailBinding.tvWebsite, directoryDetail.getWebsite());

        if (!TextUtils.isEmpty(directoryDetail.getAvegrageOfRating()) && AppUtil.isNumeric(directoryDetail.getAvegrageOfRating())) {
            fragmentDetailBinding.rbRating.setVisibility(View.VISIBLE);
            fragmentDetailBinding.rbRating.setRating(Float.valueOf(directoryDetail.getAvegrageOfRating()));
        } else
            fragmentDetailBinding.rbRating.setVisibility(View.GONE);

        int noOfReviews = directoryDetail.getReviews().length;
        if (noOfReviews != 0)
            UiUtil.setTextView(fragmentDetailBinding.tvReviews, String.format(Locale.US, "( %d %s)", noOfReviews,
                    AppUtil.getSingularOrPluralString("Review", noOfReviews)));
        else
            fragmentDetailBinding.tvReviews.setVisibility(View.GONE);

        setAddressContainer();
//        fragmentDetailBinding.btDirection.setOnClickListener(v -> {
//            String webURL = "https://www.google.com/maps/dir/?api=1&" + "destination=" + directoryDetail.get() + "%2C" + directoryDetail.getLongitude();
//            AppUtil.openUrl(context, webURL);
//        });
    }

    private void setAddressContainer() {

        BusinessAddress[] businessAddresses = directoryDetail.getBusinessAddresses();

        for (BusinessAddress businessAddress : businessAddresses) {
            businessAddress.setCity(directoryDetail.getCity());
            String formattedAddress = AppUtil.getFormattedAddress(businessAddress);
            View view = getLayoutInflater().inflate(R.layout.fragment_directory_details_child_list_item, fragmentDetailBinding.llContainer, false);
            view.setBackground(null);

            /*if (i != businessAddresses.length - 1) {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams
                        (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.bottomMargin = AppUtil.dpToPx(context, 8);
                view.setLayoutParams(layoutParams);
            }*/

            TextView tvAddress = view.findViewById(R.id.tv_address);
            TextView tvDistance = view.findViewById(R.id.tvDistance);
            TextView tvDealingIn = view.findViewById(R.id.tvDealingIn);
            TextView tvPhone = view.findViewById(R.id.tvPhone);
            TextView tvMobile = view.findViewById(R.id.tvMobile);
            TextView tvEmail = view.findViewById(R.id.tvEmail);
            TextView tvContactPerson = view.findViewById(R.id.tvContactPerson);
            TextView tvLine1 = view.findViewById(R.id.tvLine1);
            TextView tvLine2 = view.findViewById(R.id.tvLine2);

            UiUtil.setTextView(tvAddress, formattedAddress);
            UiUtil.setTextView(businessAddress.getDistance(), "Kms away", tvDistance);
            setText(tvDealingIn, "Dealing In :", businessAddress.getDealingIn());
            setText(tvPhone, "Ph :", businessAddress.getPhoneNo());
            setText(tvMobile, "Mobile :", businessAddress.getMobile1());
            setText(tvEmail, "Email :", businessAddress.getEmail());
//            setText(tvWebsite, "Website :", businessAddress.getWebsite());
            setText(tvContactPerson, "Contact Person :", businessAddress.getContactPerson());
            UiUtil.setTextView(tvLine1, businessAddress.getInfoLine1());
            UiUtil.setTextView(tvLine2, businessAddress.getInfoLine2());

            ImageButton ibDirection = view.findViewById(R.id.ib_direction);

            if (!TextUtils.isEmpty(businessAddress.getLatitude()) && !TextUtils.isEmpty(businessAddress.getLongitude())) {
                ibDirection.setVisibility(View.VISIBLE);
                ibDirection.setOnClickListener(v -> {
                    String webURL = "https://www.google.com/maps/dir/?api=1&" + "destination=" + businessAddress.getLatitude() + "%2C" + businessAddress.getLongitude();
                    AppUtil.openUrl(context, webURL);
                });
            } else
                ibDirection.setVisibility(View.GONE);

            fragmentDetailBinding.llBranches.addView(view);
        }
    }

    private void setText(TextView tv, String prefix, String text) {
        if (TextUtils.isEmpty(text)) {
            tv.setVisibility(View.GONE);
        } else {
            tv.setVisibility(View.VISIBLE);
            tv.setText(String.format("%s %s", prefix, text));
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.add_review, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_add_review:
                startActivity(new Intent(getActivity(), AddReviewActivity.class)
                        .putExtra(Constants.EXTRA_BUSINESS_ID, directoryDetail.getBusinessID()));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
