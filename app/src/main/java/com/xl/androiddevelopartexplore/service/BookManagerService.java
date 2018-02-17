package com.xl.androiddevelopartexplore.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import com.xl.androiddevelopartexplore.aidl.Book;
import com.xl.androiddevelopartexplore.aidl.IBookManager;
import com.xl.androiddevelopartexplore.aidl.IOnNewBookArrivedListener;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class BookManagerService extends Service {
    public BookManagerService() {
    }

    private static final String TAG = "BookManagerService";
    private CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<>();
    //    private Binder binder = new IBookManager.Stub(){
//
//        @Override
//        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {
//
//        }
//
//        @Override
//        public List<Book> getBookList() throws RemoteException {
//            return mBookList;
//        }
//
//        @Override
//        public void addBook(Book book) throws RemoteException {
//            mBookList.add(book);
//        }
//    };
    private AtomicBoolean mIsServiceDestroyed = new AtomicBoolean(false);
    private RemoteCallbackList<IOnNewBookArrivedListener> mListenerList = new RemoteCallbackList<>();
    private Binder binder = new IBookManager.Stub() {
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public List<Book> getBookList() throws RemoteException {
            return mBookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            mBookList.add(book);
        }

        @Override
        public void regisListener(IOnNewBookArrivedListener listener) throws RemoteException {
//            if(mListenerList.contains(listener)){
//                Log.i(TAG, "regisListener: ==>> listener already exists");
//            }else{
//                mListenerList.add(listener);
//            }
//            Log.i(TAG, "regisListener: ==>> listenerlist size = "+mListenerList.size());
            mListenerList.register(listener);
            Log.i(TAG, "regisListener: ==>> listenerlist size = " + mListenerList.beginBroadcast());
            mListenerList.finishBroadcast();

        }

        @Override
        public void unRegisListener(IOnNewBookArrivedListener listener) throws RemoteException {
//            if(mListenerList.contains(listener)){
//                mListenerList.remove(listener);
//                Log.i(TAG, "unRegisListener: ==>> unregis succeed!");
//            }else{
//                Log.i(TAG, "unRegisListener: ==>> unregis failed");
//            }
//            Log.i(TAG, "unRegisListener: ==>> listenerlist size = "+mListenerList.size());
            mListenerList.unregister(listener);
            Log.i(TAG, "unRegisListener: ==>> listenerlist size = " + mListenerList.beginBroadcast());
            mListenerList.finishBroadcast();

        }
    };

    private void onNewBookArrived(Book book) throws RemoteException {
        mBookList.add(book);
//        for(IOnNewBookArrivedListener listener:mListenerList){
//            listener.onNewBookArrived(book);
//            Log.i(TAG, "onNewBookArrived: ==>> notify new Book "+listener);
//        }
        final int N = mListenerList.beginBroadcast();
        for (int i = 0; i < N; i++) {
            IOnNewBookArrivedListener listener = mListenerList.getBroadcastItem(i);
            if (listener != null) {
                listener.onNewBookArrived(book);
            }
        }
        mListenerList.finishBroadcast();
    }

    private class ServiceWorker implements Runnable {

        @Override
        public void run() {
            while (!mIsServiceDestroyed.get()) {
                try {
                    Thread.sleep(5000);
                    int bookid = mBookList.size();
                    Book book = new Book("book-" + bookid, "author-" + bookid, bookid);
                    onNewBookArrived(book);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mBookList.add(new Book("Android", "Andy Rubin", 0));
        mBookList.add(new Book("IOS", "Jobs", 1));
        new Thread(new ServiceWorker()).start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
        return binder;
    }

    @Override
    public void onDestroy() {
        mIsServiceDestroyed.set(true);
        super.onDestroy();
    }
}
