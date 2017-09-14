package com.example.leeyoonyoung.pray;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class WriteNoteActivity extends AppCompatActivity {

    EditText myLine;
    EditText myWhyLine;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_note);

        myLine = (EditText)findViewById(R.id.myLine);
        myWhyLine = (EditText)findViewById(R.id.myWhyLine);

        Intent intent = getIntent();
        if(intent.hasExtra("editNoteItem") == true) {
            MyNote myNote = (MyNote) intent.getExtras().getSerializable("myNote");
            myLine.setText(myNote.getMyLine());
            myWhyLine.setText(myNote.getWhyLine());
        }


    }

    public void mOnClick(View v){
        switch(v.getId()){
            case R.id.btnback:

                MyNote myNote = new MyNote(myLine.getText().toString(), myWhyLine.getText().toString());
                myNote.setMyLine(myLine.getText().toString());
                myNote.setWhyLine(myWhyLine.getText().toString());

                Bundle bundle = new Bundle();
                bundle.putSerializable("myNote", myNote);

                Intent intent = new Intent();
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
                break;

        }
    }
}
