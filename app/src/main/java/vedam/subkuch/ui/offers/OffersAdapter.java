package vedam.subkuch.ui.offers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import vedam.subkuch.R;

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

        Picasso.with(parent.getContext()).load(offer.getImageurl()).
                placeholder(R.drawable.grey).error(R.drawable.grey).into(holder.image);

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
