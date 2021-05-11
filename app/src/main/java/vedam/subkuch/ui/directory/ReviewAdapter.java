package vedam.subkuch.ui.directory;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import vedam.subkuch.R;
import vedam.subkuch.ui.directory.models.Review;
import vedam.subkuch.utils.AppUtil;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder> {

    private final ArrayList<Review> reviews;

    ReviewAdapter(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public ReviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_review, parent, false);
        return new ReviewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewHolder holder, int position) {

        Review review = reviews.get(position);

        holder.tvName.setText(review.getUserName());
        holder.tvReviewComments.setText(review.getBusinessReview());

        if (!TextUtils.isEmpty(review.getRating()) && AppUtil.isNumeric(review.getRating())) {
            holder.rbRating.setVisibility(View.VISIBLE);
            holder.rbRating.setRating(Float.valueOf(review.getRating()));
        } else
            holder.rbRating.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    class ReviewHolder extends RecyclerView.ViewHolder {

        private final TextView tvName;
        private final TextView tvReviewComments;
        private final RatingBar rbRating;

        ReviewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvReviewComments = itemView.findViewById(R.id.tv_review_comments);
            rbRating = itemView.findViewById(R.id.rb_rating);
        }
    }
}
