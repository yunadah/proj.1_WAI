package com.example.leeyoonyoung.pray;

import android.app.DatePickerDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class BookRegistrationActivity extends AppCompatActivity {

    ImageView bookImage;
    EditText date;
    EditText bookTitle;
    EditText authorName;
    EditText publisherName;
    TextView field;

    EditText searchInput;


    String bookImageUri_add;
    Uri bookImageUri;

    ArrayList<String> fieldList;


    private int myYear;
    private int myMonth;
    private int myDay;

    private int TAKE_CAMERA = 0;
    private int TAKE_GALLERY = 1;

    Spinner spinnerField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_registration);

        bookImage = (ImageView)findViewById(R.id.bookImage);
        date = (EditText)findViewById(R.id.dayInput);
        bookTitle = (EditText) findViewById(R.id.bookTitleInput);
        authorName = (EditText) findViewById(R.id.authorInput);
        publisherName = (EditText)findViewById(R.id.publisherInput);
        field = (TextView) findViewById(R.id.fieldInput);
        searchInput = (EditText)findViewById(R.id.searchKey);

        fieldList = new ArrayList<String>();
        fieldList.add("경제/경영");
        fieldList.add("건강/취미");
        fieldList.add("문학");
        fieldList.add("역사");
        fieldList.add("직접입력");

        spinnerField = (Spinner)findViewById(R.id.spinnerField);
        final ArrayAdapter<String> fieldAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, fieldList);
        //fieldAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerField.setAdapter(fieldAdapter);
        //spinnerField.setOnItemSelectedListener(mItemSelected);

        spinnerField.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            // 스피너에서 직접입력을 선택했을 때 입력 다이얼로그 띄워지고 입력된 값이 스피너에 추가되도록 하는 부분
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, final int position, long l){
                String str1 = spinnerField.getItemAtPosition(position).toString();
                final int pos = position +1;
                Log.e("position", pos+"");
                if(str1 == "직접입력"){
                    Context mContext = getApplicationContext();
                    LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
                    View layout = inflater.inflate(R.layout.dialog_add, (ViewGroup)findViewById(R.id.fieldInput_Layout));
                    final EditText field_dial = layout.findViewById(R.id.field_input_dial);

                    AlertDialog.Builder fieldAdd = new AlertDialog.Builder(BookRegistrationActivity.this);
                    fieldAdd.setTitle("도서 분류 항목 추가");
                    fieldAdd.setView(R.layout.dialog_add);
                    fieldAdd.setNegativeButton("입력", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i){
                            String inputField = field_dial.getText().toString();
                            Log.e("field 추가된 항목명 >> ", inputField);
                            fieldList.add(inputField);
                            fieldAdapter.notifyDataSetChanged();
                            spinnerField.setSelection(pos);
                        }
                    }); // 여기까지가 negative버튼
                    fieldAdd.setPositiveButton("취소", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int i){
                            dialog.dismiss();
                        }
                    });
                    fieldAdd.show();
                }
                field.setText(str1);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView){

            }
        });

        // 날짜 선택 다이얼로그를 위한 선택 창. 또한, 책 등록 액티비티가 실행될 때 당일 날짜 텍스트 지정.
        final Calendar cal = Calendar.getInstance();
        myYear = cal.get(Calendar.YEAR);
        myMonth = cal.get(Calendar.MONTH);
        myDay = cal.get(Calendar.DAY_OF_MONTH);
        date.setText(myYear+"/"+(myMonth+1)+"/"+myDay);

        final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth){
                cal.set(year, monthOfYear, dayOfMonth);
                date.setText(year+"/"+(monthOfYear+1)+"/"+dayOfMonth);
            }
        };

        date.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                new DatePickerDialog(BookRegistrationActivity.this, dateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

/*
    AdapterView.OnItemSelectedListener mItemSelected = new AdapterView.OnItemSelectedListener() {
        // 스피너에서 직접입력을 선택했을 때 입력 다이얼로그 띄워지고 입력된 값이 스피너에 추가되도록 하는 부분
        @Override
        public void onItemSelected(final AdapterView<?> adapterView, View view, int i, long l) {
            //String str = adapterView.getItemAtPosition(i).toString();
            String str1 = spinnerField.getSelectedItem().toString();
            final int pos = i+1;
            Log.e("position", pos + "");
            if(str1 == "직접입력"){
                Context mContext = getApplicationContext();
                LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.dialog_add, (ViewGroup)findViewById(R.id.fieldInput_Layout));
                final EditText field_dial = layout.findViewById(R.id.field_input_dial);

                new AlertDialog.Builder(BookRegistrationActivity.this)
                        .setTitle("사용자 도서 분류 입력창")
                        .setView(R.layout.dialog_add)
                        .setNegativeButton("입력", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String inputField = field_dial.getText().toString();
                                Log.e("field 추가된 항목명 >>> ", inputField);
                                fieldList.add(inputField);

                                //spinneerField.setSelection(pos); // 새로 세팅된 값을 스피너가 선택
                            }
                        }) // 여기까지가 negative버튼
                        .setPositiveButton("취소", null)
                        .show();
            }else {
                field.setText(str1);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }; // 스피너 동적 추가
*/

    public void mOnClick(View v){
        Intent intent;
        switch (v.getId()) {
            case R.id.btnAddBook:
                MyBook myBook = null ;
                ArrayList<MyNote> myNoteArrayList = null;
                if(bookImageUri_add != null) {
                    myBook = new MyBook(bookImageUri_add, bookTitle.getText().toString(), authorName.getText().toString(), publisherName.getText().toString(), field.getText().toString(), date.getText().toString(), myNoteArrayList);
                }else if(bookImageUri_add == null){
                    Uri uri = Uri.parse("android.resource://com.example.leeyoonyoung.pray/drawable/book");
                    myBook = new MyBook(uri.toString(), bookTitle.getText().toString(), authorName.getText().toString(), publisherName.getText().toString(), field.getText().toString(), date.getText().toString(), myNoteArrayList);
                }
                intent = new Intent();
                Log.e("책 추가 >>>> ", myBook.getBookTitle() + " / " + myBook.getAuthorName()+ " / " +myBook.getPublisherName()+ " / " +myBook.getField()+ " / " +myBook.getDate());

                Bundle bundle = new Bundle();
                bundle.putSerializable("myBook", myBook);
                intent.putExtras(bundle);

                setResult(RESULT_OK, intent);
                finish();
                break;

            case R.id.btnback:
                finish();
                break;

            case R.id.btnAddImage:
                AlertDialog.Builder selectImage = new AlertDialog.Builder(BookRegistrationActivity.this);
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

                selectImage.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                selectImage.show();
                break;

            case R.id.btnSearch:
                intent = new Intent();
                intent.setAction(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, searchInput.getText().toString());
                startActivity(intent);
                break;

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
        //intent.setAction(Intent.ACTION_PICK);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, TAKE_GALLERY);
    }


    public void doTakePictureAction(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, TAKE_CAMERA);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == RESULT_OK){
            if(requestCode == TAKE_GALLERY){
                bookImageUri_add = data.getData().toString(); // 이미지 uri를 string으로 담아두는 부분이다. bookImageUri_add에는 String형의 uri가 담겨있음.
                bookImage.setImageURI(Uri.parse(bookImageUri_add));
            }else if(requestCode == TAKE_CAMERA){
                if(data != null){
                    Log.e("카메라 getResult ", data.toString());
                    Bitmap bm = (Bitmap)data.getExtras().get("data");
                    if(bm != null){
                        bookImage.setImageBitmap(bm);
                        bookImageUri_add = data.toString();
                    }
                }
            }
        }
    }




}
