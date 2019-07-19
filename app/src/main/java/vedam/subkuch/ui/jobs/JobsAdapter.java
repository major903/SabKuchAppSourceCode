package vedam.subkuch.ui.jobs;

import android.content.Context;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.MetricAffectingSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import vedam.subkuch.R;
import vedam.subkuch.ui.jobs.models.Job;
import vedam.subkuch.ui.jobs.models.Post;
import vedam.subkuch.uicomponent.CustomTypefaceSpan;
import vedam.subkuch.utils.AppUtil;
import vedam.subkuch.utils.UiUtil;

public class JobsAdapter extends RecyclerView.Adapter<JobsAdapter.ViewHolder> {

    private ArrayList<Job> jobs;
    private Context context;


    JobsAdapter(Context context, ArrayList<Job> jobs) {
        this.jobs = jobs;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.fragment_jobs_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Job job = jobs.get(position);

        holder.tvOrganisation.setText(job.getOrganisationName());
        UiUtil.setTextView(job.getDistance(), "away", holder.tvDistance);
        UiUtil.setTextView("Dealing in : ", job.getDealingIn(), holder.tvDealsIn);
        UiUtil.setTextView("Job Location : ", String.format("%s, %s", job.getJobLocation(),
                AppUtil.deNull(job.getCity())), holder.tvLocation);
        UiUtil.setTextView(holder.tvContact, job.getHowToContact());

        setPosition(holder.tvPosition, job.getPosts());

        if (!TextUtils.isEmpty(job.getLatitude()) && !TextUtils.isEmpty(job.getLongitude())) {
            holder.ibDirection.setVisibility(View.VISIBLE);
            holder.ibDirection.setOnClickListener(view -> {
                String webURL = "https://www.google.com/maps/dir/?api=1&" + "destination=" + job.getLatitude() + "%2C" + job.getLongitude();
                AppUtil.openUrl(context, webURL);
            });
        } else
            holder.ibDirection.setVisibility(View.GONE);
    }

    private void setPosition(TextView tvPosition, ArrayList<Post> posts) {

        final SpannableStringBuilder fullString = new SpannableStringBuilder("");
//        StringBuilder fullString = new StringBuilder("");
        for (int i = 0; i < posts.size(); i++) {
            Post post = posts.get(i);

            MetricAffectingSpan boldSpan = new CustomTypefaceSpan(Typeface.DEFAULT_BOLD);
            int currentLength = fullString.length();

            fullString.append(String.valueOf(i + 1)).append(". ")
                    .append(AppUtil.deNull(post.getJobTitle()));
            fullString.setSpan(boldSpan, currentLength, currentLength + post.getJobTitle().length() + 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            fullString.append("\n").append(post.getRequirement());
            if (i != posts.size() - 1)
                fullString.append("\n\n");
        }

        tvPosition.setText(fullString);
    }

    @Override
    public int getItemCount() {
        return jobs.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvOrganisation;
        private TextView tvDealsIn;
        private TextView tvDistance;
        private TextView tvLocation;
        private TextView tvPosition;
        private TextView tvContact;
        private ImageView ibDirection;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDistance = itemView.findViewById(R.id.tvDistance);
            tvOrganisation = itemView.findViewById(R.id.tv_organisation);
            tvDealsIn = itemView.findViewById(R.id.tv_deals);
            tvLocation = itemView.findViewById(R.id.tv_address);
            tvPosition = itemView.findViewById(R.id.tv_position);
            tvContact = itemView.findViewById(R.id.tv_contact);
            ibDirection = itemView.findViewById(R.id.ib_direction);
        }

        /*public <E> void bind(final E item, final int position, final OnListViewItemClickListener listener) {

            itemView.setOnClickListener(v -> {
                if (listener != null)
                    listener.onItemClick(item, position, itemView, null);
            });
        }*/
    }
}
