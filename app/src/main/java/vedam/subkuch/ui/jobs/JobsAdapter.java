package vedam.subkuch.ui.jobs;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.MetricAffectingSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import vedam.subkuch.R;
import vedam.subkuch.uicomponent.CustomTypefaceSpan;
import vedam.subkuch.utils.AppUtil;
import vedam.subkuch.utils.UiUtil;

public class JobsAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<Job> jobs;


    public JobsAdapter(Context context, ArrayList<Job> jobs) {

        inflater = LayoutInflater.from(context);
        this.jobs = jobs;
    }

    @Override
    public int getCount() {
        return jobs.size();
    }

    @Override
    public Object getItem(int position) {
        return jobs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {

        JobsAdapter.ViewHolder holder;

        if (v == null) {
            v = inflater.inflate(R.layout.fragment_jobs_list_item, null);
            holder = new JobsAdapter.ViewHolder();
            holder.tvOrganisation = v.findViewById(R.id.tv_organisation);
            holder.tvDealsIn = v.findViewById(R.id.tv_deals);
            holder.tvLocation = v.findViewById(R.id.tv_location);
            holder.tvPosition = v.findViewById(R.id.tv_position);
            holder.tvContact = v.findViewById(R.id.tv_contact);
            holder.btDirection = v.findViewById(R.id.bt_direction);
            v.setTag(holder);
        } else {
            holder = (JobsAdapter.ViewHolder) v.getTag();
        }

        Job job = (Job) getItem(position);

        holder.tvOrganisation.setText(job.getOrganisationName());
        UiUtil.setTextView("Dealing in : ", job.getDealingIn(), holder.tvDealsIn);
        UiUtil.setTextView("Job Location : ", job.getJobLocation(), holder.tvLocation);
        UiUtil.setTextView(holder.tvContact, job.getHowToContact());

        setPosition(holder.tvPosition, job.getPosts());

        if (!TextUtils.isEmpty(job.getLatitude()) && !TextUtils.isEmpty(job.getLongitude())) {
            holder.btDirection.setVisibility(View.VISIBLE);
            holder.btDirection.setOnClickListener(view -> {
                String webURL = "https://www.google.com/maps/dir/?api=1&" + "destination=" + job.getLatitude() + "%2C" + job.getLongitude();
                AppUtil.openUrl(parent.getContext(), webURL);
            });
        } else
            holder.btDirection.setVisibility(View.GONE);

        return v;
    }

    private void setPosition(TextView tvPosition, ArrayList<Post> posts) {

        final SpannableStringBuilder fullString = new SpannableStringBuilder("");
//        StringBuilder fullString = new StringBuilder("");
        for (int i = 0; i < posts.size(); i++) {
            Post post = posts.get(i);

            MetricAffectingSpan boldSpan = new CustomTypefaceSpan(Typeface.DEFAULT_BOLD);
            int currentLength = fullString.length();

            fullString.append(String.valueOf(i + 1)).append(". ")
                    .append(post.getJobTitle());
            fullString.setSpan(boldSpan, currentLength, currentLength + post.getJobTitle().length() + 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            fullString.append("\n").append(post.getRequirement());
            if (i != posts.size() - 1)
                fullString.append("\n\n");
        }

        tvPosition.setText(fullString);
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

        private TextView tvOrganisation;
        private TextView tvDealsIn;
        private TextView tvLocation;
        private TextView tvPosition;
        private TextView tvContact;
        private Button btDirection;
    }
}
