package com.example.leeyoonyoung.pray;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

public class NoteActivity extends AppCompatActivity {

    ImageView bookImage;
    TextView bookTitle;
    TextView authorName;
    TextView publisherName;
    TextView field;
    TextView date;

    ListView listView;
    ArrayList<MyNote> myNoteList = null;
    MyNoteListAdapter myNoteListAdapter;
    MyNote myNote;
    int noteListIndexNum;


    // RequestCode num
    int ACT_EDIT = 0;
    int ACT_ADD_NOTE = 1;
    int ACT_EDIT_NOTE_ITEM = 2;

    String bookImageUri_note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        bookImage = (ImageView)findViewById(R.id.bookImage);
        bookTitle = (TextView)findViewById(R.id.bookTitle_note);
        authorName = (TextView)findViewById(R.id.author_note);
        publisherName = (TextView)findViewById(R.id.publisher_note);
        field = (TextView)findViewById(R.id.field_note);
        date = (TextView)findViewById(R.id.date_note);
        listView = (ListView)findViewById(R.id.notelist);


        Intent intent = getIntent();
        MyBook myBook = (MyBook)intent.getExtras().getSerializable("myBook");
        Log.e("선택된 항목 info >>>> ", myBook.getBookTitle() + " / "+ myBook.getAuthorName());

        if(myBook.getBookImage() != null) {
            bookImage.setImageURI(Uri.parse(myBook.getBookImage().toString()));
            bookImageUri_note = myBook.getBookImage().toString(); // 메인 리스트 뷰에서 받은 이미지 데이터의 uri를 string으로 받아오는 부분
        }
        bookTitle.setText(myBook.getBookTitle());
        authorName.setText(myBook.getAuthorName());
        publisherName.setText(myBook.getPublisherName());
        field.setText(myBook.getField());
        date.setText(myBook.getDate());
        if(myBook.getMyNoteArrayList() != null) {
            myNoteList = myBook.getMyNoteArrayList();
        }else{
            myNoteList = new ArrayList<MyNote>();
            Log.e("어레이 없을 때, null로 만들자",">> 만들어졌다");
        }

        //myNoteList = new ArrayList<MyNote>();
        Log.e("Note onCreate()", myNoteList.toString());
        myNoteListAdapter = new MyNoteListAdapter(NoteActivity.this, myNoteList);
        listView.setAdapter(myNoteListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                myNote = myNoteList.get(position);
                noteListIndexNum = position;
                Log.e("노트 리스트뷰 선택항목 ", position+"번째 항목 "+myNote.getMyLine()+"/ "+myNote.getWhyLine());

                Intent intent = new Intent(NoteActivity.this, WriteNoteActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("myNote",myNote);
                intent.putExtras(bundle);
                intent.putExtra("editNoteItem", true);
                startActivityForResult(intent, ACT_EDIT_NOTE_ITEM);
            }
        });
    }



    public void mOnClick(View v){
        String bookTitle_note = bookTitle.getText().toString();
        String authorName_note = authorName.getText().toString();
        String publisherName_note = publisherName.getText().toString();
        String field_note = field.getText().toString();
        String date_note = date.getText().toString();

        Intent intent;
        switch(v.getId()){
            case R.id.btnEdit:
                intent = new Intent(NoteActivity.this, EditActivity.class);
                intent.putExtra("bookImage", bookImageUri_note);
                intent.putExtra("bookTitle", bookTitle_note);
                intent.putExtra("authorName", authorName_note);
                intent.putExtra("publisherName", publisherName_note);
                intent.putExtra("field", field_note);
                intent.putExtra("date", date_note);
                startActivityForResult(intent, ACT_EDIT);
                break;

            case R.id.btnback:
                MyBook myBook1 = new MyBook(bookImageUri_note, bookTitle_note, authorName_note, publisherName_note, field_note, date_note, myNoteList);

                myBook1.setBookImage(bookImageUri_note);
                myBook1.setBookTitle(bookTitle_note);
                myBook1.setAuthorName(authorName_note);
                myBook1.setPublisherName(publisherName_note);
                myBook1.setField(field_note);
                myBook1.setDate(date_note);
                myBook1.setMyNoteArrayList(myNoteList); // 현재까지 만들어져 있는 노트arraylist를 myBook1 객체에 넣어 세팅한다.


                intent = new Intent();

                Bundle bundle = new Bundle();
                bundle.putSerializable("myBook", myBook1);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
                break;

            case R.id.addnote:
                intent = new Intent(this,WriteNoteActivity.class);
                startActivityForResult(intent, ACT_ADD_NOTE);
                break;

            case R.id.btnShare:
                String noteWritten ="";
                for(int i = 0; i < myNoteList.size(); i++){
                    String myLine = myNoteList.get(i).getMyLine();
                    String myWhyLine = myNoteList.get(i).getWhyLine();

                    noteWritten = noteWritten + (i+1)+". " + "\n- 나의 밑줄 : "+ myLine+"\n- 밑줄 이유 : "+myWhyLine+"\n\n";
                }

                //각 텍스트를 모두 전달해주기 위해 noteContext라는 객체에 합치는 부분이다.
                String noteContext = "- 도서 : "+bookTitle_note+"\n- 저자 : "+authorName_note+"\n- 출판사 : "+publisherName_note+"\n- 분야 : "+field_note +"\n\n[나의 독서 기록과 나 연구 관찰 보고서]\n\n"+ noteWritten;

                //share 메소드의 인텐트 설정하는 부분
                intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT,"[WhoAmI]"+" '"+bookTitle_note+"' "+"독서 노트 공유");
                intent.putExtra(Intent.EXTRA_TEXT, noteContext);

                Intent chooser = Intent.createChooser(intent, "공유");
                startActivity(chooser);
                break;


        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == ACT_EDIT){
                bookImage.setImageURI(Uri.parse(data.getStringExtra("bookImage")));
                bookImageUri_note = data.getStringExtra("bookImage");
                bookTitle.setText(data.getStringExtra("bookTitle"));
                authorName.setText(data.getStringExtra("authorName"));
                publisherName.setText(data.getStringExtra("publisherName"));
                field.setText(data.getStringExtra("field"));
                date.setText(data.getStringExtra("date"));
            }

            if(requestCode == ACT_ADD_NOTE){
                MyNote myNote;
                myNote = (MyNote)data.getExtras().getSerializable("myNote");

                Log.e("[도서노트]리스트뷰 추가항목 >>> ", myNote.getMyLine()+"/"+myNote.getWhyLine());
                Log.e("Note onCreate()", myNoteList.toString());
                myNoteList.add(myNote);
                myNoteListAdapter.notifyDataSetChanged();
            }
            if(requestCode == ACT_EDIT_NOTE_ITEM){
                MyNote myNote;
                myNote = (MyNote)data.getExtras().getSerializable("myNote");

                Log.e("[도서노트]리스트뷰 수정된 항목 >>> ", myNote.getMyLine()+"/"+myNote.getWhyLine());
                Log.e("Note", myNoteList.toString());
                myNoteList.set(noteListIndexNum, myNote);
                myNoteListAdapter.notifyDataSetChanged();
            }
        }
    }
}


class MyNote implements Serializable {

    private String myLine;
    private String whyLine;

    public MyNote(String myLine, String whyLine) {
        this.myLine = myLine;
        this.whyLine = whyLine;
    }

    public String getMyLine() {
        return myLine;
    }

    public void setMyLine(String myLine) {
        this.myLine = myLine;
    }

    public String getWhyLine() {
        return whyLine;
    }

    public void setWhyLine(String whyLine) {
        this.whyLine = whyLine;
    }


}

class MyNoteListAdapter extends BaseAdapter{

    private ArrayList<MyNote> myNoteList = new ArrayList<>();
    private Context mContext;

    public MyNoteListAdapter(Context context, ArrayList<MyNote> myNoteList){
        this.mContext = context;
        this.myNoteList = myNoteList;
    }

    @Override
    public int getCount() {
        return myNoteList.size();
    }

    @Override
    public Object getItem(int position) {
        return myNoteList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View v, ViewGroup viewGroup) {

        MyNoteViewHolder viewHolder;

        if(v == null){
            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.booknote, viewGroup, false);

            viewHolder = new MyNoteViewHolder();
            viewHolder.myLine = v.findViewById(R.id.myLine);
            viewHolder.whyLine = v.findViewById(R.id.myWhyLine);

            v.setTag(viewHolder);
        }else{
            viewHolder = (MyNoteViewHolder)v.getTag();
        }

        viewHolder.myLine.setText(myNoteList.get(position).getMyLine().toString());
        viewHolder.whyLine.setText(myNoteList.get(position).getWhyLine().toString());

        Log.e("myNote 어댑터", " getView() 결과값 리턴");

        return v;
    }


}

class MyNoteViewHolder{
    public TextView indexNum;
    public TextView myLine;
    public TextView whyLine;
}