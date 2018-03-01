package com.vicki.bored.bored.movies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vicki.bored.bored.R;
import com.vicki.bored.bored.movies.data.MovieData;

import static com.vicki.bored.bored.movies.MainActivity.MOVIE;


public class OverviewFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.overview_tab_layout, container, false);
        MovieData selectedMovie = getActivity().getIntent().getParcelableExtra(MOVIE);
        String overview_text = selectedMovie.getOverView();

        TextView mOverview = view.findViewById(R.id.overviewTextView);
        mOverview.setText(overview_text);

        return view;
    }


}
