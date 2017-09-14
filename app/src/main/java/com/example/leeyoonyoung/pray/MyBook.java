package com.example.leeyoonyoung.pray;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by leeyoonyoung on 2017. 9. 1..
 */

public class MyBook implements Serializable {

    private String bookImage;
    private String bookTitle;
    private String authorName;
    private String publisherName;
    private String field;
    private String date;
    private ArrayList<MyNote> myNoteArrayList;


    public MyBook(String bookImage, String bookTitle, String authorName, String publisherName, String field, String date, ArrayList<MyNote> myNoteArrayList) {
        this.bookImage = bookImage;
        this.bookTitle = bookTitle;
        this.authorName = authorName;
        this.publisherName = publisherName;
        this.field = field;
        this.date = date;
        this.myNoteArrayList = myNoteArrayList;
    }

    /*
    public String getBookImage() {
        return bookImage;
    }

    public void setBookImage(String bookImage) {
        this.bookImage = bookImage;
    }
    */

    public String getBookImage() {
        return bookImage;
    }

    public void setBookImage(String bookImage) {
        this.bookImage = bookImage;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }


    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<MyNote> getMyNoteArrayList() {
        return myNoteArrayList;
    }

    public void setMyNoteArrayList(ArrayList<MyNote> myNoteArrayList) {
        this.myNoteArrayList = myNoteArrayList;
    }


}
