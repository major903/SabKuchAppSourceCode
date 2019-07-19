package vedam.subkuch.ui.phonebook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import vedam.subkuch.R;
import vedam.subkuch.network.models.PhoneBookDetail;

public class PhoneBookDetailsAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<PhoneBookDetail> phoneBookDetails;


    PhoneBookDetailsAdapter(Context context, ArrayList<PhoneBookDetail> phoneBookDetails) {

        inflater = LayoutInflater.from(context);
        this.phoneBookDetails = phoneBookDetails;
    }

    @Override
    public int getCount() {
        return phoneBookDetails.size();
    }

    @Override
    public Object getItem(int position) {
        return phoneBookDetails.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {

        PhoneBookDetailsAdapter.ViewHolder holder;

        if (v == null) {
            v = inflater.inflate(R.layout.fragment_phone_book_details_list_item, null);
            holder = new PhoneBookDetailsAdapter.ViewHolder();
            holder.tvName = v.findViewById(R.id.tv_name);
            holder.tvPhone = v.findViewById(R.id.tv_phone);
            v.setTag(holder);
        } else {
            holder = (PhoneBookDetailsAdapter.ViewHolder) v.getTag();
        }

        PhoneBookDetail phoneBookDetail = (PhoneBookDetail) getItem(position);

        holder.tvName.setText(phoneBookDetail.getName());
        holder.tvPhone.setText(phoneBookDetail.getNumber());

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

        private TextView tvName;
        private TextView tvPhone;
    }
}
