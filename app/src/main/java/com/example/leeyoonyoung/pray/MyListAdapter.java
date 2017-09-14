package com.example.leeyoonyoung.pray;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by leeyoonyoung on 2017. 9. 1..
 */

public class MyListAdapter extends BaseAdapter {

    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<MyBook>  myBookList = new ArrayList<MyBook>();
    private int mLayout;
    private Context mContext;
    //private LayoutInflater mInflater;
    RadioButton radiobutton;

    // ListViewAdapter 의 생성자
    public MyListAdapter(Context context, ArrayList<MyBook> myBookList){

        this.mContext = context;
        //this.mLayout = layout;
        this.myBookList = myBookList;
    }

    //
    @Override
    public int getCount(){
        return myBookList.size();
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return myBookList.get(position) ;
    }


    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View v, ViewGroup parent){

        myBookViewHolder viewHolder;

        if(v == null){
            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.mybook, parent, false);

            viewHolder = new myBookViewHolder();
            viewHolder.bookImage = v.findViewById(R.id.bookImage);
            viewHolder.bookTitle = v.findViewById(R.id.bookTitle);
            viewHolder.authorName = v.findViewById(R.id.author);
            viewHolder.field = v.findViewById(R.id.field);
            viewHolder.date = v.findViewById(R.id.date);
            radiobutton = v.findViewById(R.id.radioButton);
            //viewHolder.radiobutton = v.findViewById(R.id.radioButton);


            v.setTag(viewHolder);
        }
        else{
            viewHolder = (myBookViewHolder) v.getTag();
        }

        if(myBookList.get(position).getBookImage() != null) {
            viewHolder.bookImage.setImageURI(Uri.parse(myBookList.get(position).getBookImage()));
        }
        viewHolder.bookTitle.setText(myBookList.get(position).getBookTitle());
        viewHolder.authorName.setText(myBookList.get(position).getAuthorName());
        viewHolder.field.setText(myBookList.get(position).getField());
        viewHolder.date.setText(myBookList.get(position).getDate());
        //viewHolder.radiobutton

        Log.e("어댑터 getView() 결과값 리턴>> ", "customView 생성");
        return v;

    }


}
