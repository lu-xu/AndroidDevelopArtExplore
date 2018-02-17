package com.xl.androiddevelopartexplore.ipc;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.xl.androiddevelopartexplore.Constants;
import com.xl.androiddevelopartexplore.R;
import com.xl.androiddevelopartexplore.aidl.Book;
import com.xl.androiddevelopartexplore.aidl.IBookManager;
import com.xl.androiddevelopartexplore.aidl.IOnNewBookArrivedListener;
import com.xl.androiddevelopartexplore.service.BookManagerService;

import java.util.List;

public class AIDLActivity extends AppCompatActivity {
    private static final String TAG = "AIDLActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidl);
        Intent intent = new Intent(this, BookManagerService.class);

        bindService(intent,connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        if(mRemoteBookManager!=null&&mRemoteBookManager.asBinder().isBinderAlive()){
            try {
                Log.i(TAG, "onDestroy: ==>>unregis listener");
                mRemoteBookManager.unRegisListener(listener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        unbindService(connection);
        super.onDestroy();
    }
    private Handler BookHander = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Constants.MSG_NEWBOOK:
                    Log.i(TAG, "handleMessage: receive new Book ==>>"+msg.obj);
                    break;
            }
        }
    };
    private IBookManager mRemoteBookManager;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            IBookManager iBookManager = IBookManager.Stub.asInterface(iBinder);
            try {
                mRemoteBookManager = iBookManager;
                List<Book> list = iBookManager.getBookList();
                Log.i(TAG, "onServiceConnected: booklist type==>>"+list.getClass().getCanonicalName());
                Log.i(TAG, "onServiceConnected: booklist content==>>"+list.toString());

                Book book = new Book("AndroidDevelopArtExplore","RenZhiGang",2);
                iBookManager.addBook(book);
                list = iBookManager.getBookList();
                Log.i(TAG, "onServiceConnected: booklist content add a new book==>>"+list.toString());
                iBookManager.regisListener(listener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mRemoteBookManager = null;
            Log.i(TAG, "onServiceDisconnected: ==>> binder disconnect");
        }
    };
    private IOnNewBookArrivedListener listener = new IOnNewBookArrivedListener.Stub(){

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public void onNewBookArrived(Book newbook) throws RemoteException {
            BookHander.obtainMessage(Constants.MSG_NEWBOOK,newbook).sendToTarget();
        }
    };
}
