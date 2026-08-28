package vedam.subkuch.ui.phonebook;

import android.content.Context;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import vedam.subkuch.R;
import vedam.subkuch.network.models.PhoneBookCategory;

public class PhoneBookAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<PhoneBookCategory> phoneBookCategories;


    public PhoneBookAdapter(Context context, ArrayList<PhoneBookCategory> phoneBookCategories) {

        inflater = LayoutInflater.from(context);
        this.phoneBookCategories = phoneBookCategories;
    }

    @Override
    public int getCount() {
        return phoneBookCategories.size();
    }

    @Override
    public Object getItem(int position) {
        return phoneBookCategories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {

        PhoneBookAdapter.ViewHolder holder;

        if (v == null) {
            v = inflater.inflate(R.layout.fragment_phone_book_list_item, parent, false);
            holder = new PhoneBookAdapter.ViewHolder();
            holder.rlContainer = v.findViewById(R.id.rl_container);
            holder.tvCategory = v.findViewById(R.id.tv_category);
            v.setTag(holder);
        } else {
            holder = (PhoneBookAdapter.ViewHolder) v.getTag();
        }

        PhoneBookCategory phoneBookCategory = (PhoneBookCategory) getItem(position);

        holder.tvCategory.setText(phoneBookCategory.getName());
        if (position % 2 == 0)
            holder.rlContainer.setBackgroundColor(ContextCompat.getColor(parent.getContext(), R.color.light_phone_book));
        else
            holder.rlContainer.setBackgroundColor(ContextCompat.getColor(parent.getContext(), R.color.dark_phone_book));

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

        private RelativeLayout rlContainer;
        private TextView tvCategory;
    }
}
