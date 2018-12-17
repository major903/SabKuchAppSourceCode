package vedam.subkuch.ui.movies;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Response;

import java.util.ArrayList;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseListFragment;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.network.models.Movie;
import vedam.subkuch.network.models.MoviesResponse;
import vedam.subkuch.utils.UiUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoviesFragment extends BaseListFragment {


    public MoviesFragment() {
        // Required empty public constructor
    }

    public static MoviesFragment newInstance() {

        Bundle args = new Bundle();

        MoviesFragment fragment = new MoviesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movies, container, false);
    }

    public void onViewCreated(@NonNull View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        getMovies();
    }

    private void getMovies() {

        UiUtil.showProgressDialog(context, getString(R.string.please_wait));
        DataFetcher.getMovies(context, onMoviesSuccessListener, MoviesResponse.class, onErrorListener);
    }

    private Response.Listener<MoviesResponse> onMoviesSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (response != null && response.getReturnMessage().equals(Constants.SUCCESS) && response.getReturnData().size() > 0) {
            loadValues(response.getReturnData());
        } else
            UiUtil.showToast(context, getString(R.string.err_occurred));
    };

    private void loadValues(ArrayList<Movie> response) {

        MoviesAdapter moviesAdapter = new MoviesAdapter(context, response);
        getListView().setAdapter(moviesAdapter);
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    }
}
