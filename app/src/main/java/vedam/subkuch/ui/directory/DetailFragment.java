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
import android.widget.Button;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Locale;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseFragment;
import vedam.subkuch.databinding.FragmentDetailBinding;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.ui.directory.models.Address;
import vedam.subkuch.ui.directory.models.Business;
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

        fragmentDetailBinding.rvReviews.setLayoutManager(new LinearLayoutManager(context));
        fragmentDetailBinding.rvReviews.setNestedScrollingEnabled(false);
        fragmentDetailBinding.rvReviews.setAdapter(new ReviewAdapter(directoryDetail.getReviews()));
    }

    private void bindData() {

        Picasso.with(context).load(directoryDetail.getBusinessImage())
                .placeholder(R.drawable.grey).error(R.drawable.grey).into(fragmentDetailBinding.ivEvent);

        fragmentDetailBinding.tvName.setText(directoryDetail.getBusinessName());
//        setText(fragmentDetailBinding.tvAddress, directoryDetail.getAddresses());
        setText(fragmentDetailBinding.tvPhone, "Ph :", directoryDetail.getPhone());
        setText(fragmentDetailBinding.tvMobile, "Mobile :", directoryDetail.getMobile());
        setText(fragmentDetailBinding.tvEmail, "Email :", directoryDetail.getEmail());
        setText(fragmentDetailBinding.tvWebsite, "Website :", directoryDetail.getWebsite());
        setText(fragmentDetailBinding.tvContactPerson, "Contact Person :", directoryDetail.getContactPerson());

        if (!TextUtils.isEmpty(directoryDetail.getAvegrageOfRating()) && AppUtil.isNumeric(directoryDetail.getAvegrageOfRating())) {
            fragmentDetailBinding.rbRating.setVisibility(View.VISIBLE);
            fragmentDetailBinding.rbRating.setRating(Float.valueOf(directoryDetail.getAvegrageOfRating()));
        } else
            fragmentDetailBinding.rbRating.setVisibility(View.GONE);

        if (directoryDetail.getReviews().length != 0)
            UiUtil.setTextView(fragmentDetailBinding.tvReviews, String.format(Locale.US, "( %d Reviews)", directoryDetail.getReviews().length));
        else
            fragmentDetailBinding.tvReviews.setVisibility(View.GONE);

        setAddressContainer();
//        fragmentDetailBinding.btDirection.setOnClickListener(v -> {
//            String webURL = "https://www.google.com/maps/dir/?api=1&" + "destination=" + directoryDetail.get() + "%2C" + directoryDetail.getLongitude();
//            AppUtil.openUrl(context, webURL);
//        });
    }

    private void setAddressContainer() {

        Address[] addresses = directoryDetail.getAddresses();

        for (Address address : addresses) {
            String formattedAddress = AppUtil.getFormattedAddress(address);
            View view = getLayoutInflater().inflate(R.layout.fragment_directory_details_child_list_item, fragmentDetailBinding.llContainer, false);
            view.setBackground(null);

            /*if (i != addresses.length - 1) {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams
                        (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.bottomMargin = AppUtil.dpToPx(context, 8);
                view.setLayoutParams(layoutParams);
            }*/

            TextView tvAddress = view.findViewById(R.id.tv_address);
            UiUtil.setTextView(tvAddress, formattedAddress);

            Button btDirection = view.findViewById(R.id.bt_direction);

            if (!TextUtils.isEmpty(address.getLatitude()) && !TextUtils.isEmpty(address.getLongitude())) {
                btDirection.setVisibility(View.VISIBLE);
                btDirection.setOnClickListener(v -> {
                    String webURL = "https://www.google.com/maps/dir/?api=1&" + "destination=" + address.getLatitude() + "%2C" + address.getLongitude();
                    AppUtil.openUrl(context, webURL);
                });
            } else
                btDirection.setVisibility(View.GONE);

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
