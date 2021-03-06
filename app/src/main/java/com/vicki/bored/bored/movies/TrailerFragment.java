package com.vicki.bored.bored.movies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vicki.bored.bored.R;
import com.vicki.bored.bored.movies.data.MovieData;
import com.vicki.bored.bored.movies.data.Trailers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.vicki.bored.bored.movies.MainActivity.MOVIE;


public class TrailerFragment extends Fragment {


    private Trailers[] mTrailers;
    private RecyclerView tRecyclerView;
    private TrailerAdapter mTrailerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        tRecyclerView = (RecyclerView) inflater.inflate(R.layout.trailer_tab_layout, container, false);

        MovieData selectedMovie = getActivity().getIntent().getParcelableExtra(MOVIE);

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(trailerUrl(selectedMovie.getId()))
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String jsonData = response.body().string();
                    if(response.isSuccessful()){
                        mTrailers = getTrailerData(jsonData);
                        if(getActivity() == null)
                            return;
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tRecyclerView.setLayoutManager(new LinearLayoutManager(tRecyclerView.getContext()));
                                mTrailerAdapter = new TrailerAdapter(getActivity(), mTrailers);
                                tRecyclerView.setAdapter(mTrailerAdapter);
                            }
                        });

                    }
                } catch (IOException | JSONException e) {
                    e.getMessage();
                }
            }
        });

        return tRecyclerView;
    }


    private static Trailers[] getTrailerData(String jsonData) throws JSONException {
        JSONObject trailers = new JSONObject(jsonData);
        JSONArray trailerResults = trailers.getJSONArray("results");
        Trailers[] movieTrailers = new Trailers[trailerResults.length()];

        for (int i = 0; i < trailerResults.length(); i++) {
            JSONObject movieTrailer = trailerResults.getJSONObject(i);
            Trailers data = new Trailers();
            data.setKey(movieTrailer.getString("key"));
            data.setName(movieTrailer.getString("name"));
            movieTrailers[i] = data;
        }

        return movieTrailers;
    }

    private String trailerUrl(int id){
        return MainActivity.base_Trailer
                + id +MainActivity.TRAILER_EXTENSION
                + MainActivity.API_KEY;
    }
}
