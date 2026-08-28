package vedam.subkuch.ui.offers;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

import vedam.subkuch.R;
import vedam.subkuch.interfaces.OnListViewItemClickListener;
import vedam.subkuch.ui.offers.models.Offer;
import vedam.subkuch.utils.ImageSetter;
import vedam.subkuch.utils.UiUtil;

/**
 * Created by nadeemansari on 09/02/16.
 */
public class OffersAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<Offer> offers;
    private OnListViewItemClickListener listener;

    OffersAdapter(Context context, ArrayList<Offer> offers, OnListViewItemClickListener listener) {

        inflater = LayoutInflater.from(context);
        this.offers = offers;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return offers.size();
    }

    @Override
    public Object getItem(int position) {
        return offers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {

        OffersAdapter.ViewHolder holder;

        if (v == null) {
            v = inflater.inflate(R.layout.fragment_offers_list_item, parent, false);
            holder = new OffersAdapter.ViewHolder();
            holder.image = v.findViewById(R.id.imgImageView);
            v.setTag(holder);
        } else {
            holder = (OffersAdapter.ViewHolder) v.getTag();
        }
        Offer offer = (Offer) getItem(position);

        UiUtil.setImageView(new ImageSetter.ImageBuilder(parent.getContext())
                .setImageLink(offer.getOfferImage())
                .setDefaults()
                .setTarget(holder.image)
                .build());

        if (!TextUtils.isEmpty(offer.getOfferURL()))
            holder.image.setOnClickListener(view -> {
                if (listener != null)
                    listener.onItemClick(offer, position, null, null);
            });
        return v;
    }

    @Override
    public int getItemViewType(int position) {

        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    private static class ViewHolder {

        private ImageView image;
    }

}
