package com.example.leeyoonyoung.pray;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


    }

    public void mOnClick(View view){
        Intent intent;
        switch (view.getId()){
            case R.id.btnShowRandomNote:
                intent = new Intent(ProfileActivity.this, ShowNoteActivity.class);
                startActivity(intent);
                break;

            case R.id.btnSearchBook:
                intent = new Intent(ProfileActivity.this, BookSearchActivity.class);
                startActivity(intent);
                break;


        }
    }
}
