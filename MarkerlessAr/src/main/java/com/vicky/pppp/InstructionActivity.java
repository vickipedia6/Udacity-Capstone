package com.vicky.pppp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.unity3d.player.*;

public class InstructionActivity extends AppCompatActivity {
Button unity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction);
    unity=(Button)findViewById(R.id.unityButton);
        unity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent unityIntent=new Intent(getApplicationContext(), UnityPlayerActivity.class);
                startActivity(unityIntent);
            }
        });


    }
}
