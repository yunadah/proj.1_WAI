package com.example.leeyoonyoung.pray;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    String bookImageUri_main;
    String bookTitle_main;
    String authorName_main;

    // Request Code Num.
    int ACT_ADD = 0;
    int ACT_NOTE = 1;
    MyBook mybook;

    ListView listView;
    MyListAdapter myListAdapter;
    public static ArrayList<MyBook> myBookList;

    int bookListIndexNum; // 노트액티비티에서 결과를 받은 후, 해당 인덱스의 값의 arraylist자리에 값을 세팅해주기 위함

    public static ArrayList<MyBook> getMyBookList(){
        return myBookList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("Main ", "onCreate() 호출");

//        bookImage = (ImageView) findViewById(R.id.bookImage);
//        bookTitle = (TextView) findViewById(R.id.bookTitle);
//        authorName = (TextView) findViewById(R.id.author);
        listView = (ListView) findViewById(R.id.myBookList);

        myBookList = new ArrayList<MyBook>();
//        myListAdapter = new MyListAdapter(MainActivity.this, myBookList);
//        listView.setAdapter(myListAdapter);

        SharedPreferences sf = getSharedPreferences("myBookList", MODE_PRIVATE);
        String str = sf.getString("myBookList_JSONArray","등록된 책이 없습니다.");
        Log.e("SharedPref. 받기(크리에잇)", str);

        try {
            JSONArray jArray = new JSONArray(str);//str을 JSONArray로 변경
            Log.e("create JSONarray 체크 >> ", String.valueOf(jArray));

            for(int i = 0; i < jArray.length(); i++){
                JSONObject obj = jArray.getJSONObject(i);
                Log.e("jArray에서 JSON객체 추출", obj.toString());


                String bookImage = obj.getString("bookImage");
                String bookTitle = obj.getString("bookTitle");
                String authorName = obj.getString("authorName");
                String publisherName = obj.getString("publisherName");
                String field = obj.getString("field");
                String date = obj.getString("date");

                ArrayList<MyNote> myNoteArrayList = new ArrayList<MyNote>();
                // JSONArray에 담아둔 JsonObject를 꺼내 myNoteArrayList를 만들어주고 그것을 myBook 데이터 객체의 arraylist로 저장시킨다.
                //JSONArray jsonArrayNote = obj.getJSONArray("note");
                if(obj.has("note")) {
                    JSONArray jsonArrayNote = obj.getJSONArray("note");

                    for (int j = 0; j < jsonArrayNote.length(); j++) {
                        JSONObject objNote = jsonArrayNote.getJSONObject(j);
                        String myLine = objNote.getString("myLine");
                        String myWhyLine = objNote.getString("myWhyLine");

                        MyNote myNote = new MyNote(myLine, myWhyLine);
                        myNote.setMyLine(myLine);
                        myNote.setWhyLine(myWhyLine);

                        myNoteArrayList.add(myNote);
                    }
                }else{
                    myNoteArrayList = null;
                }

                MyBook myBook = new MyBook(bookImage, bookTitle, authorName, publisherName, field, date, myNoteArrayList);

                myBook.setBookImage(bookImage);
                myBook.setBookTitle(bookTitle);
                myBook.setAuthorName(authorName);
                myBook.setPublisherName(publisherName);
                myBook.setField(field);
                myBook.setDate(date);
                myBook.setMyNoteArrayList(myNoteArrayList);

                myBookList.add(myBook);
                //OnCreate()에서는 myBookList에 아무 값이 없으므로 저장된 값을 불러와 새롭게 myBookList를 만들기 위해 ADD를 한다.
                //OnResume()에서는 위와 동일하게 SharedPreference에서 값을 불러오지만 기존의 myBookList가 있기 때문에 SET으로 값을 설정한다.

            }
        }catch(JSONException e){
            e.printStackTrace();
        }

        myListAdapter = new MyListAdapter(MainActivity.this, myBookList);
        listView.setAdapter(myListAdapter);

        // 리스트뷰의 특정 항목을 클릭했을 때 실행되는 메소드
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mybook = myBookList.get(position); // 클릭한 항목을 가져와 myBook 객체에 넣어줌
                bookListIndexNum = position;
                Log.e("리스트 뷰 선택항목 Index >>>> ", position + " 번째 항목 ");
                Log.e("리스트 뷰 선택항목 Info >>>> ", mybook.getBookTitle() + " / " + mybook.getAuthorName());

                Intent intent = new Intent(MainActivity.this, NoteActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("myBook", mybook);
                intent.putExtras(bundle);

                startActivityForResult(intent, ACT_NOTE);

            }
        });


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                AlertDialog.Builder remove = new AlertDialog.Builder(MainActivity.this);
                remove.setTitle("선택한 도서 및 도서 노트 삭제");
                remove.setMessage("삭제 시, 저장한 도서 노트의 데이터가 사라져요. 그래도 삭제를 원하나요?");
                MyBook myBook1 = myBookList.get(position);
                //
                remove.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i("삭제여부 ", "Yes Btn Click ");
                        myBookList.remove(position);
                        myListAdapter.notifyDataSetChanged();
                    }
                });


                remove.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i("삭제여부 ", "No Btn Click ");
                        dialog.dismiss();
                    }
                });

                remove.show();
                return true;
            }
        });
    }



    public void mOnClick(View v){
        switch(v.getId()){
            case R.id.btnAdd:
                Intent intent = new Intent(MainActivity.this, BookRegistrationActivity.class);
                startActivityForResult(intent, ACT_ADD);
                break;

            case R.id.btnback:
                finish();
                break;

            case R.id.profileActvity:
                Intent intent1 = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent1);
                break;
        }
    }

    private boolean ACT_NOTE_status = false;
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            if(requestCode == ACT_ADD){
                //Intent intent = getIntent();
                MyBook myBook;
                myBook = (MyBook) data.getExtras().getSerializable("myBook");

                Log.e("[메인서재]리스트뷰 추가항목 >>>> ", myBook.getBookTitle()+" / "+myBook.getAuthorName());
                myBookList.add(myBook);
                myListAdapter.notifyDataSetChanged();
            }else if(requestCode == ACT_NOTE){
                ACT_NOTE_status = true;
                mybook = (MyBook) data.getExtras().getSerializable("myBook");
                //Log.e("myBook의 arraylist확인 ", mybook.getMyNoteArrayList().toString());
                myListAdapter.notifyDataSetChanged();

                Log.e("[메인서재]리스트뷰 수정된 항목 >>>> ", mybook.getBookTitle()+" / "+mybook.getAuthorName());
            }
        }
    }


    @Override
    public void onStart(){
        super.onStart();
        Log.i("Main", "onStart() 호출");
        Toast toast = Toast.makeText(this, "메인 서재 >>> onStart ", Toast.LENGTH_SHORT);
        toast.show();
    }


    @Override
    public void onStop(){
        super.onStop();
        Log.i("Main", "onStop() 호출");
        Toast toast = Toast.makeText(this, "메인 서재 >>> onStop ", Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("Main", "onResume() 호출");
        Toast toast = Toast.makeText(this, "메인 서재 >>> onResume ", Toast.LENGTH_SHORT);
        toast.show();

        SharedPreferences sf = getSharedPreferences("myBookList", MODE_PRIVATE);
        String str = sf.getString("myBookList_JSONArray", "등록된 책이 없습니다.");
        /*sf에 "myBookList_~~"를 키값으로 받는 String화 시킨 jArray를 str이 받는 부분이다.
        jArray형태를 가지는 str로 jArray를 아래 try 문 안에서 생성하여 jArray 각각의 jsonObject를 꺼내어
        obj라는 JSONObject 객체로 담는다.
        JSONObject에서 각 데이터 태그별로 데이터를 꺼내와 bookTtile 과 authorName 변수에 넣어준다.
        그 변수를 사용해 데이터 클래스인 MyBook의 객체를 만들어 myBook 객체가 갖는 정보를 set 해준다.
        세팅을 완료한 myBook 객체는 myBookList에 새롭게 들어간다.
         */
        Log.e("SharedPref. 받기(리쥼)", str);
        //if (ACT_NOTE_status == false){
            try {
                Log.e("들어왔나", "들어왔다");
                JSONArray jArray = new JSONArray(str);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject obj = jArray.getJSONObject(i);


                    String bookImage = obj.getString("bookImage");
                    String bookTitle = obj.getString("bookTitle");
                    String authorName = obj.getString("authorName");
                    String publisherName = obj.getString("publisherName");
                    String field = obj.getString("field");
                    String date = obj.getString("date");

                    ArrayList<MyNote> myNoteArrayList = new ArrayList<MyNote>();
                    // JSONArray에 담아둔 JsonObject를 꺼내 myNoteArrayList를 만들어주고 그것을 myBook 데이터 객체의 arraylist로 저장시킨다.
                    //JSONArray jsonArrayNote = obj.getJSONArray("note");
                    if(obj.has("note")) {
                        JSONArray jsonArrayNote = obj.getJSONArray("note");

                        //ArrayList<MyNote> myNoteArrayList = new ArrayList<MyNote>();
                        for (int j = 0; j < jsonArrayNote.length(); j++) {
                            JSONObject objNote = jsonArrayNote.getJSONObject(j);
                            Log.e("리쥽 노트Json객체에서 값추출", objNote.getString("myLine") + "/" + objNote.getString("myWhyLine"));
                            String myLine = objNote.getString("myLine");
                            String myWhyLine = objNote.getString("myWhyLine");

                            MyNote myNote = new MyNote(myLine, myWhyLine);
                            myNote.setMyLine(myLine);
                            myNote.setWhyLine(myWhyLine);
                            myNoteArrayList.add(myNote);
                        }
                    }else{
                        myNoteArrayList = null;
                    }

                    MyBook myBook = new MyBook(bookImage, bookTitle, authorName, publisherName, field, date, myNoteArrayList);
                    myBook.setBookImage(bookImage);
                    myBook.setBookTitle(bookTitle);
                    myBook.setAuthorName(authorName);
                    myBook.setPublisherName(publisherName);
                    myBook.setField(field);
                    myBook.setDate(date);
                    myBook.setMyNoteArrayList(myNoteArrayList);
                    if (myBookList.size() != 0) {
                        myBookList.set(i, myBook);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            /* 노트 액티비티에서 메인으로 넘어올 때,  Result를 받는데  onStart()보다 먼저 실행된다.
            따라서  결과값을 받더라도 그 뒤에  reference 저장값이 나와서 변화된 값이 아닌 이전의 저장된 값이 보여진다.
            따라서 결과값을 받았을 때를 표시할 수 있는 ACT_NOTE_status라는 boolean 변수를 만들어
            노트 액티비티에서 결과값을 받는 단계 뒤에 오는 onResume()에서는 다시한번 변경된 인덱스와 결과로 받은 mybook객체를
            세팅해준다.
            */
            if (ACT_NOTE_status == true) {
                //
                myBookList.set(bookListIndexNum, mybook);
                Log.e("책정보 수정된 값", bookListIndexNum + mybook.toString());
                ACT_NOTE_status = false;
            }
    //}
    }

    @Override
    public void onPause(){ // SharedPreference 저장하는 부분
        super.onPause();
        Log.i("Main", "onPause() 호출");
        Toast toast = Toast.makeText(this, "메인 서재 >>> onPause ", Toast.LENGTH_SHORT);
        toast.show();

        SharedPreferences sf = getSharedPreferences("myBookList", MODE_PRIVATE);
        SharedPreferences.Editor editor = sf.edit();


        JSONArray jArray = new JSONArray();
        try{
            for(int i = 0; i < myBookList.size();i++){
                JSONObject obj = new JSONObject();
                if(myBookList.get(i).getBookImage() != null) {
                    obj.put("bookImage", myBookList.get(i).getBookImage());
                    Log.e("json >> ", "이미지 있음 / 이미지 저장됨");
                }else if(myBookList.get(i).getBookImage() == null){
                    obj.put("bookImage", ""); // 이미지가 없을 경우
                    Log.e("json >> ", "이미지 없음 / 이미 값 null");
                }
                obj.put("bookTitle", myBookList.get(i).getBookTitle());
                obj.put("authorName", myBookList.get(i).getAuthorName());
                obj.put("publisherName", myBookList.get(i).getPublisherName());
                obj.put("field", myBookList.get(i).getField());
                obj.put("date", myBookList.get(i).getDate());

                //myNoteArrayList 라는 ArrayList를 안에 담긴 Note 객체를 JsonObject로 변환 후, JsonArray에 넣고
                //완성된 JsonArray를 obj에 넣어서 보낸다.
                ArrayList<MyNote> myNoteArrayList;
                if(myBookList.get(i).getMyNoteArrayList() != null) {
                    myNoteArrayList = myBookList.get(i).getMyNoteArrayList();

                    JSONArray jArray_note = new JSONArray();
                    if (myNoteArrayList != null) {
                        try {
                            for (int j = 0; j < myNoteArrayList.size(); j++) {
                                JSONObject objNote = new JSONObject();
                                objNote.put("myLine", myNoteArrayList.get(j).getMyLine());
                                objNote.put("myWhyLine", myNoteArrayList.get(j).getWhyLine());
                                jArray_note.put(objNote);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        obj.put("note", jArray_note);
                    }
                }else{

                }
                jArray.put(obj);

            }
        }catch(JSONException e){
            e.printStackTrace();
        }


        editor.putString("myBookList_JSONArray", ""+jArray);
        editor.commit();

        Log.e("저장된 jArray", jArray.toString());
    }



    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.i("Main", "onDestroy() 호출");
        Toast toast = Toast.makeText(this, "메인 서재 >>> onDestroy ", Toast.LENGTH_SHORT);
        toast.show();
    }

}
