package com.vicki.bored.bored.movies;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vicki.bored.bored.R;
import com.vicki.bored.bored.movies.data.MovieData;
import com.vicki.bored.bored.network.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<MovieData[]>,
        GridLayoutAdapter.GridLayoutAdapterOnClickHandler {
    public static String API_KEY;

    RecyclerView recyclerView;

    TextView mErrorMessageDisplay;

    public static String image_Base_URL;
    public static String base_url;
    ProgressBar mLoadingIndicator;

    private GridLayoutAdapter gridLayoutAdapter;
    private static final String SORT_DATA_KEY = "key";
    private static final int MOVIE_LOADER_ID = 0;
    public static final String MOVIE = "movie";
    public static String REVIEW_EXTENSION;
    public static String TRAILER_EXTENSION;
    public static String Trailer_Url;
    private Bundle sortBundle;
    public static String plot;
    public static String trailers;
    public static String reviews;
    public static String base_Trailer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(MainActivity.this);
        base_Trailer=getResources().getString(R.string.trailerbase);
        API_KEY=getResources().getString(R.string.API);
        base_url=getResources().getString(R.string.base_url);
        image_Base_URL=getResources().getString(R.string.image_base_url);
        TRAILER_EXTENSION=getResources().getString(R.string.trailer);
        Trailer_Url=getResources().getString(R.string.trailerUrl);
        REVIEW_EXTENSION=getResources().getString(R.string.reviewExtension);
        plot=getResources().getString(R.string.plot);
        trailers=getResources().getString(R.string.trailers);
        reviews=getResources().getString(R.string.reviews);

        recyclerView=(RecyclerView)findViewById(R.id.rv_image);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mLoadingIndicator=(ProgressBar)findViewById(R.id.pb_loading_indicator);
        mErrorMessageDisplay=(TextView)findViewById(R.id.tv_error_message_display);
        recyclerView.setHasFixedSize(true);
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        }
        else{
            recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }
        gridLayoutAdapter = new GridLayoutAdapter(this, getApplicationContext());
        recyclerView.setAdapter(gridLayoutAdapter);

        Bundle sortBundle = new Bundle();
        sortBundle.putString(SORT_DATA_KEY, getResources().getString(R.string.popular));

        LoaderManager.LoaderCallbacks<MovieData[]> callback = MainActivity.this;

        if(NetworkUtils.isNetworkAvailable(this)) {

            mLoadingIndicator.setVisibility(View.VISIBLE);

            getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, sortBundle, callback);

        }else {
            alertUserOfError();
        }

    }



    @Override
    public Loader<MovieData[]> onCreateLoader(int id, final Bundle args) {

        return new AsyncTaskLoader<MovieData[]>(this) {

            MovieData[] cachedMovieData;

            @Override
            protected void onStartLoading() {
                if (cachedMovieData == null){
                    forceLoad();
                }else{
                    super.deliverResult(cachedMovieData);
                }
            }

            @Override
            public MovieData[] loadInBackground() {

                String sortParameter = args.getString(SORT_DATA_KEY);

                if (sortParameter == getResources().getString(R.string.favorite)){
                    return DbUtils.getFavoriteMovies(getApplicationContext());
                }

                URL movieRequestUrl = NetworkUtils.buildUrl(sortParameter);

                try {
                    String jsonMovieResponse = NetworkUtils
                            .getResponseFromHttpUrl(movieRequestUrl);


                    JSONObject movieResponse = new JSONObject(jsonMovieResponse);
                    JSONArray movieResults = movieResponse.getJSONArray(getResources().getString(R.string.moviejson));
                    MovieData[] movieData = new MovieData[movieResults.length()];

                    for (int i = 0; i < movieResults.length(); i++) {
                        JSONObject movieResult = movieResults.getJSONObject(i);
                        MovieData data = new MovieData();
                        data.setPosterPath(movieResult.getString(getResources().getString(R.string.posterpath)));
                        data.setId(movieResult.getInt(getResources().getString(R.string.movieid)));
                        data.setOriginalTitle(movieResult.getString(getResources().getString(R.string.movietitle)));
                        data.setOverView(movieResult.getString(getResources().getString(R.string.movieoverview)));
                        data.setReleaseDate(movieResult.getString(getResources().getString(R.string.release_date_moviee)));
                        data.setVoteAverage(movieResult.getDouble(getResources().getString(R.string.vote)));

                        movieData[i] = data;
                    }

                    return movieData;

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            public void deliverResult(MovieData[] data) {
                cachedMovieData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<MovieData[]> loader, MovieData[] data) {

        mLoadingIndicator.setVisibility(View.INVISIBLE);
        gridLayoutAdapter.setMovieData(data);
        if (data == null) {
            showErrorMessage();
        }else{
            showMovieDataView();
        }
    }

    @Override
    public void onLoaderReset(Loader<MovieData[]> loader) {
        gridLayoutAdapter.setMovieData(null);
    }


    private void showMovieDataView() {
        recyclerView.setVisibility(View.VISIBLE);

        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
    }

    private void showErrorMessage() {

        mErrorMessageDisplay.setVisibility(View.VISIBLE);

        recyclerView.setVisibility(View.INVISIBLE);
    }

    private void alertUserOfError(){
        AlertDialogFragment dialogFragment = new AlertDialogFragment();
        dialogFragment.show(getFragmentManager(), getResources().getString(R.string.alert));
    }

    @Override
    public void onClick(MovieData movie) {
        Intent intent = new Intent(this, MovieDetailActivity.class);

        intent.putExtra(MOVIE, movie);

        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        sortBundle = new Bundle();

        if(NetworkUtils.isNetworkAvailable(this) && item.getItemId() == R.id.popular_item) {

            sortBundle.putString(SORT_DATA_KEY, getResources().getString(R.string.popular));
            mLoadingIndicator.setVisibility(View.VISIBLE);
            getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, sortBundle, this);

        }else if(NetworkUtils.isNetworkAvailable(this) && item.getItemId() == R.id.top_rated_item) {

            sortBundle.putString(SORT_DATA_KEY, getResources().getString(R.string.top_rated));
            mLoadingIndicator.setVisibility(View.VISIBLE);
            getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, sortBundle, this);

        }else if(item.getItemId() == R.id.favorite_rated_item){

            sortBundle.putString(SORT_DATA_KEY, getResources().getString(R.string.favorite));
            mLoadingIndicator.setVisibility(View.VISIBLE);
            getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, sortBundle, this);

        }else {
            alertUserOfError();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if( sortBundle != null && sortBundle.getString(SORT_DATA_KEY) == getResources().getString(R.string.favorite)) {
            getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, sortBundle, this);
        }
    }
}
