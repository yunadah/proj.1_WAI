package com.example.leeyoonyoung.pray;

import android.app.Activity;
import android.app.Notification;
import android.net.Uri;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class ShowNoteActivity extends Activity {

    ImageView bookImage;
    TextView bookTitle;
    TextView date;
    TextView line;
    ArrayList<MyBook> myBookArrayList;

    String bookImageStr;
    String bookTitleStr;
    String dateStr;
    String lineStr;

    Random random = new Random();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
         setContentView(R.layout.activity_show_note);

        bookImage = (ImageView)findViewById(R.id.bookImage_showNote);
        bookTitle = (TextView)findViewById(R.id.bookTitle_showNote);
        date = (TextView)findViewById(R.id.date_showNote);
        line = (TextView)findViewById(R.id.line_showNote);

        MainActivity mainActivity = new MainActivity();
        myBookArrayList = mainActivity.getMyBookList();
        Log.e("책리스트 들어왔니???? ", myBookArrayList.toString());
        Log.e("책리스트 값이 잘 들어왔나?? >>> ", myBookArrayList.get(0).getBookTitle());

        final android.os.Handler handler = new android.os.Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.what == 0) {
                    bookImage.setImageURI(Uri.parse(bookImageStr));
                    bookTitle.setText(bookTitleStr);
                    date.setText(dateStr);
                    line.setText(lineStr);
                }

            }
        };



        Thread showRandom = new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < 5; i++){
                    Log.e("서브 쓰레드  ", "진입했다!!!");
                    int num = random.nextInt(myBookArrayList.size());

                    bookImageStr = myBookArrayList.get(num).getBookImage();
                    bookTitleStr = myBookArrayList.get(num).getBookTitle();
                    dateStr = myBookArrayList.get(num).getDate();
                    int lineNum = random.nextInt(myBookArrayList.get(num).getMyNoteArrayList().size());
                    lineStr = myBookArrayList.get(num).getMyNoteArrayList().get(lineNum).getMyLine();

                    try{
                        handler.sendEmptyMessage(0);
                        Thread.sleep(2000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }

            }
        });
        showRandom.start();



    }
}
