package vedam.subkuch.ui.jobs;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import vedam.subkuch.R;
import vedam.subkuch.ui.jobs.models.JobCategory;

public class JobCategoryAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<JobCategory> jobCategories;


    public JobCategoryAdapter(Context context, ArrayList<JobCategory> jobCategories) {

        inflater = LayoutInflater.from(context);
        this.jobCategories = jobCategories;
    }

    @Override
    public int getCount() {
        return jobCategories.size();
    }

    @Override
    public Object getItem(int position) {
        return jobCategories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {

        JobCategoryAdapter.ViewHolder holder;

        if (v == null) {
            v = inflater.inflate(R.layout.fragment_phone_book_list_item, null);
            holder = new JobCategoryAdapter.ViewHolder();
            holder.rlContainer = v.findViewById(R.id.rl_container);
            holder.tvCategory = v.findViewById(R.id.tv_category);
            v.setTag(holder);
        } else {
            holder = (JobCategoryAdapter.ViewHolder) v.getTag();
        }

        JobCategory jobCategory = (JobCategory) getItem(position);

        holder.tvCategory.setText(jobCategory.getJobCategoryName());
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
