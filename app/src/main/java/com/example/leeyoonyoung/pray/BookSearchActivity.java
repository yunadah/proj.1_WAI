package com.example.leeyoonyoung.pray;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class BookSearchActivity extends AppCompatActivity {

    EditText searchKey;
    TextView text;

    String keyword;

    //네이버 도서 API 아이디 & 키
    String clientID = "r3m5vpSyYLGQfMO73VSw";
    String clientSecret = "eGoba4xmQh";

    ListView listView;
    SearchListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_search);

        searchKey = (EditText)findViewById(R.id.searchKey);
        text = (TextView)findViewById(R.id.textView);

        //keyword = searchKey.getText().toString();
        listView = (ListView)findViewById(R.id.bookData);

        ArrayList<NaverBook> list = null;
        adapter = new SearchListAdapter(BookSearchActivity.this, list);




    }

    public void mOnclick(View view){
        switch (view.getId()){
            case R.id.btnSearch:
                Log.e("버튼 눌렸다 >>> ", "검색해라!!!");
                keyword = searchKey.getText().toString();
                if(keyword != null) {
                    NaverSearchBook task = new NaverSearchBook();
                    task.execute(keyword);
                }else{
                }
                try{
                    Thread.sleep(1000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
        }
    }



    public class NaverSearchBook extends AsyncTask<String, Void, ArrayList<NaverBook>> {
        ProgressDialog mDlg = new ProgressDialog(BookSearchActivity.this);

        @Override
        protected void onPreExecute(){
            mDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mDlg.setTitle("검색중...");
            mDlg.show();
            super.onPreExecute();
        }


        @Override
        protected ArrayList<NaverBook> doInBackground(String...string) {
            ArrayList<NaverBook> list;

            list = xmlParser(keyword);

            try {
                Thread.sleep(1000);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            return list;
        }

        @Override
        protected void onPostExecute(ArrayList<NaverBook> result){
            mDlg.dismiss();
            adapter = new SearchListAdapter(BookSearchActivity.this, result);

                listView.setAdapter(adapter);

        }
    }


         private ArrayList<NaverBook> xmlParser(String keyword){  // 파싱하여 XML에서 책정보를 가져오는 메소드. 리턴값은 arraylist.

            URL url;
            ArrayList<NaverBook> list = null;
            int display = 20;
            int start = 1;

            try {
                url = new URL("https://openapi.naver.com/v1/search/book.xml?query=" + URLEncoder.encode(keyword, "UTF-8")
                        + (display != 0 ? "&display=" + display : "") + (start != 0 ? "&start=" + start : ""));
                URLConnection urlConn;

                //url 연결
                urlConn = url.openConnection();
                urlConn.setRequestProperty("X-naver-Client-Id", clientID);
                urlConn.setRequestProperty("X-naver-Client-Secret", clientSecret);

                //파싱 - 팩토리 만들고 팩토리로 파서생성(풀 파서 사용)
                XmlPullParserFactory factory;
                factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();
                parser.setInput((new InputStreamReader(urlConn.getInputStream())));

                int eventType = parser.getEventType();
                NaverBook b = null;
                while(eventType != XmlPullParser.END_DOCUMENT){
                    switch(eventType){
                        case XmlPullParser.END_DOCUMENT:
                            break;
                        case XmlPullParser.START_DOCUMENT:
                            list = new ArrayList<NaverBook>();
                            break;
                        case XmlPullParser.START_TAG:{
                            String tag = parser.getName();
                            switch (tag){
                                case "item":
                                    b = new NaverBook();
                                    break;
                                case "title":
                                    if(b != null){
                                        b.setTitle(parser.nextText());
                                    }
                                    break;
                                case "image":
                                    if (b != null)
                                        b.setImage(parser.nextText());
                                    break;
                                case "author":
                                    if (b != null)
                                        b.setAuthor(parser.nextText());
                                    break;
                                case "publisher":
                                    if (b != null)
                                        b.setPublisher(parser.nextText());
                                    break;
                                case "description":
                                    if (b != null)
                                        b.setDescription(parser.nextText());
                                    break;
                            }
                            break;
                        }
                        case XmlPullParser.END_TAG: {
                            String tag = parser.getName();
                            if (tag.equals("item")) {
                                list.add(b);
                                b = null;
                            }

                        }
                    }
                    eventType = parser.next();
                }
            }catch(MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return list;
        }


/*
    public List<NaverBook> searchBook(String keyword, int display, int start){
            URL url;
            ArrayList<NaverBook> list = null;

            try {
                url = new URL("https://openapi.naver.com/v1/search/book.xml?query=" + URLEncoder.encode(keyword, "UTF-8")
                        + (display != 0 ? "&display=" + display : "") + (start != 0 ? "&start=" + start : ""));
                URLConnection urlConn;

                //url 연결
                urlConn = url.openConnection();
                urlConn.setRequestProperty("X-naver-Client-Id", clientID);
                urlConn.setRequestProperty("X-naver-Client-Secret", clientSecret);

                //파싱 - 팩토리 만들고 팩토리로 파서생성(풀 파서 사용)
                XmlPullParserFactory factory;
                factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();
                parser.setInput((new InputStreamReader(urlConn.getInputStream())));

                int eventType = parser.getEventType();
                NaverBook b = null;
                while(eventType != XmlPullParser.END_DOCUMENT){
                    switch(eventType){
                        case XmlPullParser.END_DOCUMENT:
                            break;
                        case XmlPullParser.START_DOCUMENT:
                            list = new ArrayList<NaverBook>();
                            break;
                        case XmlPullParser.START_TAG:{
                            String tag = parser.getName();
                            switch (tag){
                                case "item":
                                    b = new NaverBook();
                                    break;
                                case "title":
                                    if(b != null){
                                        b.setTitle(parser.nextText());
                                    }
                                    break;
                                case "link":
                                    if (b != null)
                                        b.setLink(parser.nextText());
                                    break;
                                case "image":
                                    if (b != null)
                                        b.setImage(parser.nextText());
                                    break;
                                case "author":
                                    if (b != null)
                                        b.setAuthor(parser.nextText());
                                    break;
                                case "discount":
                                    if (b != null)
                                        b.setDiscount(parser.nextText());
                                    break;
                                case "publisher":
                                    if (b != null)
                                        b.setPublisher(parser.nextText());
                                    break;
                                case "pubdate":
                                    if (b != null)
                                        b.setUpdate(parser.nextText());
                                    break;
                                case "isbn":
                                    if (b != null)
                                        b.setIsbn(parser.nextText());
                                    break;
                                case "description":
                                    if (b != null)
                                        b.setDescription(parser.nextText());
                                    break;
                            }
                            break;
                        }
                        case XmlPullParser.END_TAG: {
                            String tag = parser.getName();
                            if (tag.equals("item")) {
                                list.add(b);
                                b = null;
                            }

                        }
                    }
                    eventType = parser.next();
                }
            }catch(MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return list;
        }
        */
    }
/*


    String getXmlData(){
        StringBuffer buffer = new StringBuffer();

        String str = searchKey.getText().toString();
        String key = URLEncoder.encode(str);

        String queryUrl
    }
    */

