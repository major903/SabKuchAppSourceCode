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
import vedam.subkuch.utils.AppUtil;
import vedam.subkuch.utils.ImageSetter;
import vedam.subkuch.utils.UiUtil;

/**
 * Created by nadeemansari on 09/02/16.
 */
public class OffersAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<Offer> offers;


    public OffersAdapter(Context context, ArrayList<Offer> offers) {

        inflater = LayoutInflater.from(context);
        this.offers = offers;
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
            v = inflater.inflate(R.layout.fragment_offers_list_item, null);
            holder = new OffersAdapter.ViewHolder();
            holder.image = v.findViewById(R.id.imgImageView);
            v.setTag(holder);
        } else {
            holder = (OffersAdapter.ViewHolder) v.getTag();
        }
        Offer offer = (Offer) getItem(position);

        UiUtil.setImageView(new ImageSetter.ImageBuilder(parent.getContext())
                .setImageLink(offer.getImageurl())
                .setDefaults()
                .setTarget(holder.image)
                .build());

        if (!TextUtils.isEmpty(offer.getImageurl()))
            holder.image.setOnClickListener(view -> {
                AppUtil.openUrl(parent.getContext(), offer.getImageurl());
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
