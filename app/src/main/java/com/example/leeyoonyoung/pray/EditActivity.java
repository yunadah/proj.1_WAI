package com.example.leeyoonyoung.pray;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;

public class EditActivity extends AppCompatActivity {

    ImageView bookImage;
    TextView bookTitle;
    TextView authorName;
    TextView publisherName;
    TextView field;
    TextView date;

    String bookImageUri_edit;

    private int myYear;
    private int myMonth;
    private int myDay;


    private int TAKE_GALLERY = 0;
    private int TAKE_CAMERA = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        bookImage = (ImageView)findViewById(R.id.bookImage);
        bookTitle = (TextView)findViewById(R.id.bookTitle_edit);
        authorName = (TextView)findViewById(R.id.author_edit);
        publisherName = (TextView)findViewById(R.id.publisher_edit);
        field = (TextView)findViewById(R.id.field_edit);
        date = (TextView)findViewById(R.id.date_edit);

        Intent intent = getIntent();

        if(intent.getStringExtra("bookImage") != null) {
            bookImage.setImageURI(Uri.parse(intent.getStringExtra("bookImage")));
            bookImageUri_edit = intent.getStringExtra("bookImage"); // 이미지 uri부분 String값으로 담아두는 부분
        }
        bookTitle.setText(intent.getStringExtra("bookTitle"));
        authorName.setText(intent.getStringExtra("authorName"));
        publisherName.setText(intent.getStringExtra("publisherName"));
        field.setText(intent.getStringExtra("field"));
        date.setText(intent.getStringExtra("date"));

        final Calendar cal = Calendar.getInstance();
        myYear = cal.get(Calendar.YEAR);
        myMonth = cal.get(Calendar.MONTH);
        myDay = cal.get(Calendar.DAY_OF_MONTH);

        final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth){
                cal.set(year, monthOfYear, dayOfMonth);
                date.setText(year+"/"+monthOfYear+"/"+dayOfMonth);
            }
        };

        date.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                new DatePickerDialog(EditActivity.this, dateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    public void mOnClick(View v){
        switch(v.getId()){
            case R.id.btnEditDone:
                Intent intent = new Intent();
                intent.putExtra("bookImage", bookImageUri_edit);
                intent.putExtra("bookTitle", bookTitle.getText().toString());
                intent.putExtra("authorName", authorName.getText().toString());
                intent.putExtra("publisherName", publisherName.getText().toString());
                intent.putExtra("field",field.getText().toString());
                intent.putExtra("date", date.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
                break;

            case R.id.btnback:
                finish();
                break;

            case R.id.btnEditImage:
                AlertDialog.Builder selectImage = new AlertDialog.Builder(EditActivity.this);
                selectImage.setTitle("Add BookImage");
                //selectImage.setMessage("Do you want to remove this book?");
                //
                selectImage.setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doGalleryAction();

                    }
                });

                selectImage.setNegativeButton("Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doTakePictureAction();
                    }
                });

                selectImage.setNeutralButton("Cancel", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        dialog.dismiss();
                    }
                });
                selectImage.show();
        }
    }


    DatePickerDialog.OnDateSetListener datepickerListner = new DatePickerDialog.OnDateSetListener(){

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth){
            myYear = year;
            myMonth = month;
            myDay = dayOfMonth;
            date.setText(year+"/"+month+"/"+dayOfMonth);
        }
    };

    public void doGalleryAction(){
        Intent intent = new Intent(Intent.ACTION_PICK);

        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, TAKE_GALLERY);
    }

    public void doTakePictureAction(){
        Intent intent = new Intent();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == RESULT_OK){
            if(requestCode == TAKE_GALLERY){
                bookImageUri_edit = data.getData().toString();
                bookImage.setImageURI(Uri.parse(bookImageUri_edit));
            }
        }
    }
        }


