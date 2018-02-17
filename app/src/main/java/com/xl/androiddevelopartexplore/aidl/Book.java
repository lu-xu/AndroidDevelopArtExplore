package com.xl.androiddevelopartexplore.aidl;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2018/2/16 0016.
 */

public class Book implements Parcelable {
    String BookName;
    String Author;
    int BookId;

    public int getBookId() {
        return BookId;
    }

    public void setBookId(int bookId) {
        BookId = bookId;
    }

    public String getBookName() {
        return BookName;
    }

    public void setBookName(String bookName) {
        BookName = bookName;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.BookName);
        dest.writeString(this.Author);
        dest.writeInt(this.BookId);
    }

    public Book() {
    }

    public Book(String bookName, String author, int bookId) {
        BookName = bookName;
        Author = author;
        BookId = bookId;
    }

    @Override
    public String toString() {
        return "Book{" +
                "BookName='" + BookName + '\'' +
                ", Author='" + Author + '\'' +
                ", BookId=" + BookId +
                '}';
    }

    protected Book(Parcel in) {
        this.BookName = in.readString();
        this.Author = in.readString();
        this.BookId = in.readInt();
    }

    public static final Parcelable.Creator<Book> CREATOR = new Parcelable.Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel source) {
            return new Book(source);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
}
