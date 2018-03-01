package com.vicki.bored.bored;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vicki.bored.bored.comment.CommentActivity;
import com.vicki.bored.bored.joke.JokeDisplayActivity;
import com.vicki.bored.bored.movies.MainActivity;
import com.vicky.pppp.InstructionActivity;

public class OptionsDisplayActivity extends AppCompatActivity implements View.OnClickListener{
CardView mSleepCardView,mSuggestMovies,mAugmentedReality,mTellaJoke;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options_display);
        mSleepCardView=(CardView)findViewById(R.id.makemesleep);
        mTellaJoke=(CardView)findViewById(R.id.jokes);
        mSuggestMovies=(CardView)findViewById(R.id.movies);
        mAugmentedReality=(CardView)findViewById(R.id.augmentedreality);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAugmentedReality.setOnClickListener(this);
        mSleepCardView.setOnClickListener(this);
        mTellaJoke.setOnClickListener(this);
        mSuggestMovies.setOnClickListener(this);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        
    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId())
        {
            case R.id.makemesleep: i=new Intent(getApplicationContext(),MakeMeSleep.class);
                startActivity(i);
                break;

            case R.id.jokes: i=new Intent(getApplicationContext(), JokeDisplayActivity.class);
                startActivity(i);
                break;

            case R.id.movies: i=new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                break;

            case R.id.augmentedreality: i=new Intent(getApplicationContext(), InstructionActivity.class);
                startActivity(i);
             break;


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
     getMenuInflater().inflate(R.menu.review_menu, menu);
     return true;
    }

    @Override
     public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.comment)
       {
            Intent i=new Intent(getApplicationContext(), CommentActivity.class);
            startActivity(i);
       }
        return true;

     }
}
