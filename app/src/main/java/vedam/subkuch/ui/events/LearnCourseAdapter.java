package vedam.subkuch.ui.events;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.graphics.Paint;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.Locale;

import vedam.subkuch.R;
import vedam.subkuch.network.models.learn.LearnCourse;

class LearnCourseAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<LearnCourse> courses = new ArrayList<>();
    private final OnCourseClickListener onCourseClickListener;
    private final OnCourseClickListener onDetailsClickListener;
    private boolean enrolledCourses;

    interface OnCourseClickListener {
        void onCourseClick(LearnCourse course);
    }

    LearnCourseAdapter(
            Context context,
            OnCourseClickListener onCourseClickListener,
            OnCourseClickListener onDetailsClickListener
    ) {
        this.context = context;
        this.onCourseClickListener = onCourseClickListener;
        this.onDetailsClickListener = onDetailsClickListener;
    }

    void setCourses(ArrayList<LearnCourse> values, boolean enrolledCourses) {
        this.enrolledCourses = enrolledCourses;
        courses.clear();
        if (values != null) courses.addAll(values);
        notifyDataSetChanged();
    }

    @Override public int getCount() { return courses.size(); }
    @Override public Object getItem(int position) { return courses.get(position); }
    @Override public long getItemId(int position) { return courses.get(position).getCourseId(); }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_learn_course, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        LearnCourse course = courses.get(position);
        holder.name.setText(course.getName());
        holder.description.setText(course.getDescription());
        holder.trainer.setText(TextUtils.isEmpty(course.getTrainerName()) ? "" : "By " + course.getTrainerName());
        String ratingText = String.format(Locale.US, "\u2605 %.1f  \u2022  %s reviews", course.getRating(),
                formatReviewCount(course.getReviewCount()));
        SpannableString styledRating = new SpannableString(ratingText);
        styledRating.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.learn_rating_star)),
                0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.meta.setText(styledRating);
        boolean owned = enrolledCourses || course.isSubscribed();
        // Owned courses: price is meaningless — hide it and the strikethrough MRP.
        holder.price.setVisibility(owned ? View.GONE : View.VISIBLE);
        holder.price.setText(formatPrice(course.getPrice()));
        if (!owned && course.getMrp() > course.getPrice()) {
            holder.mrp.setVisibility(View.VISIBLE);
            holder.mrp.setText(formatPrice(course.getMrp()));
            holder.mrp.setPaintFlags(holder.mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.mrp.setVisibility(View.GONE);
        }
        // Owned courses: no button — tapping the row itself opens the course.
        holder.buy.setVisibility(owned ? View.GONE : View.VISIBLE);
        holder.buy.setText(R.string.learn_details);
        holder.buy.setOnClickListener(view -> onDetailsClickListener.onCourseClick(course));
        convertView.setOnClickListener(view -> onCourseClickListener.onCourseClick(course));
        bindThumbnail(holder, course.getImageUrl());
        return convertView;
    }

    private void bindThumbnail(ViewHolder holder, String imageUrl) {
        Glide.with(holder.thumbnail).clear(holder.thumbnail);
        holder.thumbnail.setImageDrawable(null);
        holder.thumbnailShimmer.hideShimmer();
        holder.thumbnailPlaceholder.setVisibility(View.GONE);
        holder.thumbnail.setVisibility(View.GONE);
        if (TextUtils.isEmpty(imageUrl)) {
            holder.thumbnailPlaceholder.setVisibility(View.VISIBLE);
            return;
        }

        // Keep the ImageView measured while the shimmer sits above it. Glide needs the
        // measured dimensions to resolve a size for the requested image.
        holder.thumbnail.setVisibility(View.VISIBLE);
        holder.thumbnailShimmer.showWhileLoading();

        Glide.with(holder.thumbnail)
                .load(imageUrl)
                .centerCrop()
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(GlideException exception, Object model,
                                                Target<Drawable> target, boolean isFirstResource) {
                        holder.thumbnailShimmer.hideShimmer();
                        holder.thumbnail.setVisibility(View.GONE);
                        holder.thumbnailPlaceholder.setVisibility(View.VISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model,
                                                   Target<Drawable> target, DataSource dataSource,
                                                   boolean isFirstResource) {
                        holder.thumbnailShimmer.hideShimmer();
                        holder.thumbnail.setVisibility(View.VISIBLE);
                        holder.thumbnailPlaceholder.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.thumbnail);
    }

    private String formatPrice(double price) {
        return price <= 0 ? context.getString(R.string.learn_free) : String.format(Locale.US, "\u20B9%.0f", price);
    }

    private String formatReviewCount(int count) {
        if (count >= 1000) {
            double compact = count / 1000.0;
            return compact >= 10 ? String.format(Locale.US, "%.0fK", compact)
                    : String.format(Locale.US, "%.1fK", compact);
        }
        return String.valueOf(count);
    }

    private static class ViewHolder {
        final TextView name;
        final TextView description;
        final TextView trainer;
        final TextView meta;
        final TextView price;
        final TextView mrp;
        final Button buy;
        final ImageView thumbnail;
        final ThumbnailShimmerView thumbnailShimmer;
        final TextView thumbnailPlaceholder;

        ViewHolder(View view) {
            name = view.findViewById(R.id.tv_learn_course_name);
            description = view.findViewById(R.id.tv_learn_course_description);
            trainer = view.findViewById(R.id.tv_learn_course_trainer);
            meta = view.findViewById(R.id.tv_learn_course_meta);
            price = view.findViewById(R.id.tv_learn_course_price);
            mrp = view.findViewById(R.id.tv_learn_course_mrp);
            buy = view.findViewById(R.id.btn_buy_course);
            thumbnail = view.findViewById(R.id.iv_learn_course_thumbnail);
            thumbnailShimmer = view.findViewById(R.id.shimmer_learn_course_thumbnail);
            thumbnailPlaceholder = view.findViewById(R.id.tv_learn_course_thumbnail);
        }
    }
}
