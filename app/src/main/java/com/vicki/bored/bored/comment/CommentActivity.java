package com.vicki.bored.bored.comment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vicki.bored.bored.R;


public class CommentActivity extends AppCompatActivity {
    EditText comment;
    Button submit;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        AdView mAdview=(AdView)findViewById(R.id.adView);
        AdRequest adRequest=new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        mAdview.loadAd(adRequest);
        comment=(EditText)findViewById(R.id.commentSection);
        databaseReference= FirebaseDatabase.getInstance().getReference().child(getResources().getString(R.string.child));
        submit=(Button)findViewById(R.id.button2);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),getResources().getString(R.string.sent_to_firebase),Toast.LENGTH_LONG).show();
                AddData();
            }
        });
    }
    public void AddData()
    {
        String comments=comment.getText().toString();
        Save save=new Save(comments);
        databaseReference.setValue(save);
    }
}
