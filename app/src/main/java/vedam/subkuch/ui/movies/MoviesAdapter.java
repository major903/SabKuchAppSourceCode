package vedam.subkuch.ui.movies;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;

import java.util.ArrayList;

import vedam.subkuch.R;
import vedam.subkuch.network.models.Movie;
import vedam.subkuch.network.models.Venue;
import vedam.subkuch.utils.AppUtil;
import vedam.subkuch.utils.ImageSetter;
import vedam.subkuch.utils.UiUtil;

public class MoviesAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<Movie> movies;


    public MoviesAdapter(Context context, ArrayList<Movie> movies) {

        inflater = LayoutInflater.from(context);
        this.movies = movies;
    }

    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public Object getItem(int position) {
        return movies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {

        MoviesAdapter.ViewHolder holder;

        if (v == null) {
            v = inflater.inflate(R.layout.fragment_movies_list_item, null);
            holder = new MoviesAdapter.ViewHolder();
            holder.flMovie = v.findViewById(R.id.fl_movie);
            holder.rlSubContainer = v.findViewById(R.id.rl_sub_container);
            holder.tvMovieTitle = v.findViewById(R.id.tv_movie_title);
            holder.tvVenue = v.findViewById(R.id.tv_venue);
            holder.ivMovie = v.findViewById(R.id.iv_movie);
            holder.ivTriangle = v.findViewById(R.id.iv_triangle);
            holder.ivYoutube = v.findViewById(R.id.iv_youtube);
            v.setTag(holder);
        } else {
            holder = (MoviesAdapter.ViewHolder) v.getTag();
        }

        Movie movie = (Movie) getItem(position);

        holder.tvMovieTitle.setText(movie.getName());

        setVenueListener(holder.tvVenue, holder.ivTriangle, holder.rlSubContainer, movie);
        setVenue(holder.tvVenue, holder.ivTriangle, movie);
        setYoutubeButton(parent.getContext(), holder.ivYoutube, movie.getMovievideo());

        holder.tvMovieTitle.setText(movie.getName());

        if (!TextUtils.isEmpty(movie.getMovieposter())) {
            holder.flMovie.setVisibility(View.VISIBLE);
            UiUtil.setImageView(new ImageSetter.ImageBuilder(parent.getContext())
                    .setImageLink(movie.getMovieposter())
                    .setDefaults()
                    .setTarget(holder.ivMovie)
                    .setCallback(new Callback() {
                        @Override
                        public void onSuccess() {
                            holder.flMovie.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError(Exception e) {
                            holder.flMovie.setVisibility(View.GONE);
                        }
                    })
                    .build());
        } else
            holder.flMovie.setVisibility(View.GONE);

        return v;
    }

    private void setYoutubeButton(Context context, ImageView ivYoutube, String movievideo) {

        ivYoutube.setOnClickListener(v -> {
            AppUtil.openUrl(context, movievideo);
        });
    }

    private void setVenueListener(TextView tvVenue, ImageView ivTriangle, RelativeLayout rlSubContainer, Movie movie) {
        if (movie.getObj_Venue().size() > 4) {
            ivTriangle.setVisibility(View.VISIBLE);
            rlSubContainer.setOnClickListener(v -> {
                if (movie.isExpanded()) {
                    movie.setExpanded(false);
                    setVenue(tvVenue, ivTriangle, movie);
                } else {
                    movie.setExpanded(true);
                    setVenue(tvVenue, ivTriangle, movie);
                }
            });
        } else
            ivTriangle.setVisibility(View.GONE);
    }

    private void setVenue(TextView tvVenue, ImageView ivTriangle, Movie movie) {

        if (movie.getObj_Venue().size() > 4)
            if (movie.isExpanded()) {
                tvVenue.setText(getFullVenueString(movie.getObj_Venue()));
                ivTriangle.setImageResource(R.drawable.baseline_expand_less_black_24dp);
            } else {
                ivTriangle.setImageResource(R.drawable.baseline_expand_more_black_24dp);
                if (movie.getObj_Venue().size() > 0)
                    tvVenue.setText(movie.getObj_Venue().get(0).getVenuewithtime());
                else
                    tvVenue.setText("");
            }
        else {
            tvVenue.setText(getFullVenueString(movie.getObj_Venue()));
        }
    }

    private String getFullVenueString(ArrayList<Venue> venues) {
        StringBuilder fullVenues = new StringBuilder();
        for (int i = 0; i < venues.size(); i++) {
            Venue venue = venues.get(i);
            if (i == venues.size() - 1)
                fullVenues.append(venue.getVenuewithtime());
            else
                fullVenues.append(venue.getVenuewithtime()).append("\n\n");
        }
        return fullVenues.toString();
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

        private FrameLayout flMovie;
        private RelativeLayout rlSubContainer;
        private TextView tvMovieTitle;
        private TextView tvVenue;
        private ImageView ivMovie;
        private ImageView ivTriangle;
        private ImageView ivYoutube;
    }
}
