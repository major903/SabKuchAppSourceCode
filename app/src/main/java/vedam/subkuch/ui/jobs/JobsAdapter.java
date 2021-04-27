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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

import vedam.subkuch.R;
import vedam.subkuch.ui.jobs.models.Job;
import vedam.subkuch.ui.jobs.models.Post;
import vedam.subkuch.uicomponent.CustomTypefaceSpan;
import vedam.subkuch.utils.AppUtil;
import vedam.subkuch.utils.ShareUtils;
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
        UiUtil.setTextView("Job Location : ", String.format("%s", job.getJobLocation()), holder.tvLocation);
        UiUtil.setTextView(holder.tvContact, job.getHowToContact());

        if (job.getPosts() != null && !job.getPosts().isEmpty())
            setPosition(holder.tvPosition, job.getPosts());
        else
            holder.tvPosition.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(job.getLatitude()) && !TextUtils.isEmpty(job.getLongitude())) {
            holder.ibDirection.setVisibility(View.VISIBLE);
            holder.ibDirection.setOnClickListener(view -> {
                String webURL = "https://www.google.com/maps/dir/?api=1&" + "destination=" + job.getLatitude() + "%2C" + job.getLongitude();
                AppUtil.openUrl(context, webURL);
            });
        } else
            holder.ibDirection.setVisibility(View.GONE);

        holder.ibShare.setOnClickListener(view -> {
            ShareUtils.shareMessage(context, String.format("Sharing this job ad with you. If you want to find jobs near your home install Sabkuch App from the link given below. \n\n" +
                    "https://play.google.com/store/apps/details?id=vedam.subkuch&referrer={0}\n\n%s", getShareJobPost(job)), null);
        });
    }

    private void setPosition(TextView tvPosition, ArrayList<Post> posts) {
        tvPosition.setVisibility(View.VISIBLE);
//        StringBuilder fullString = new StringBuilder("");
        CharSequence fullJobPost = getJobPost(posts);

        tvPosition.setText(fullJobPost);
    }

    private CharSequence getJobPost(ArrayList<Post> posts) {

        final SpannableStringBuilder fullString = new SpannableStringBuilder("");
        for (int i = 0; i < posts.size(); i++) {
            Post post = posts.get(i);

            MetricAffectingSpan boldSpan = new CustomTypefaceSpan(Typeface.DEFAULT_BOLD);
            int currentLength = fullString.length();

            fullString.append(String.valueOf(i + 1)).append(". ")
                    .append(AppUtil.deNull(post.getJobTitle()));
            fullString.setSpan(boldSpan, currentLength, currentLength + post.getJobTitle().length() + 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (!TextUtils.isEmpty(post.getRequirement()))
                fullString.append("\n").append(post.getRequirement());
            if (i != posts.size() - 1)
                fullString.append("\n\n");
        }
        return fullString;
    }

    private CharSequence getShareJobPost(Job job) {

        if (job == null || job.getPosts() == null || job.getPosts().isEmpty())
            return null;

        StringBuilder sbPost = new StringBuilder();

        if (job.getPosts().size() == 1)
            sbPost.append(job.getPosts().get(0).getJobTitle());
        else
            for (int i = 0; i < job.getPosts().size(); i++) {
                Post post = job.getPosts().get(i);
                sbPost.append(String.format(Locale.US, "%d) %s ", i + 1, post.getJobTitle()));
            }

        return String.format(Locale.US, "%s dealing in %s is looking for %s\n%s", job.getOrganisationName(),
                job.getDealingIn(), sbPost.toString().trim(), job.getHowToContact());
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
        private ImageView ibShare;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDistance = itemView.findViewById(R.id.tvDistance);
            tvOrganisation = itemView.findViewById(R.id.tv_organisation);
            tvDealsIn = itemView.findViewById(R.id.tv_deals);
            tvLocation = itemView.findViewById(R.id.tv_address);
            tvPosition = itemView.findViewById(R.id.tv_position);
            tvContact = itemView.findViewById(R.id.tv_contact);
            ibDirection = itemView.findViewById(R.id.ib_direction);
            ibShare = itemView.findViewById(R.id.ib_share);
        }

        /*public <E> void bind(final E item, final int position, final OnListViewItemClickListener listener) {

            itemView.setOnClickListener(v -> {
                if (listener != null)
                    listener.onItemClick(item, position, itemView, null);
            });
        }*/
    }
}
