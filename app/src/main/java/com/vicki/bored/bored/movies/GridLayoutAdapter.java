package com.vicki.bored.bored.movies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vicki.bored.bored.R;
import com.vicki.bored.bored.movies.data.MovieData;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GridLayoutAdapter extends RecyclerView.Adapter<GridLayoutAdapter.movieHolder> {

    private final Context context;
    private MovieData[] movieDatas;
    private final GridLayoutAdapterOnClickHandler mClickHandler;

    public GridLayoutAdapter(GridLayoutAdapterOnClickHandler clickHandler, Context c) {
        mClickHandler = clickHandler;
        context = c;
    }

    public interface GridLayoutAdapterOnClickHandler {
        void onClick(MovieData movie);
    }

    @Override
    public movieHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_image_layout, parent, false);

        return new movieHolder(view);
    }

    @Override
    public void onBindViewHolder(movieHolder holder, int position) {
        if(movieDatas[position].getPosterPath() == null){
            Picasso.with(context).load(R.drawable.no_image_icon).into(holder.imageView);

        }else{
            String posterPath = MainActivity.image_Base_URL + movieDatas[position].getPosterPath();
            Picasso.with(context).load(posterPath).into(holder.imageView);
            holder.gridTitle.setText(movieDatas[position].getOriginalTitle());
        }
    }

    @Override
    public int getItemCount() {
        if (movieDatas == null) return 0;
            return movieDatas.length;
    }

    public class movieHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        @BindView(R.id.movie_title_grid)
        TextView gridTitle;
        public movieHolder(View itemView) {
            super(itemView);
            imageView=(ImageView)itemView.findViewById(R.id.movie_image);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            MovieData movie = movieDatas[adapterPosition];
            mClickHandler.onClick(movie);
        }
    }

    public void setMovieData(MovieData[] movieData) {
        movieDatas = movieData;
        notifyDataSetChanged();
    }
}
