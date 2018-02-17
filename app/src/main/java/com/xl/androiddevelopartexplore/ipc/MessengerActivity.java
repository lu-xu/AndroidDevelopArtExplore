package com.xl.androiddevelopartexplore.ipc;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.xl.androiddevelopartexplore.Constants;
import com.xl.androiddevelopartexplore.R;
import com.xl.androiddevelopartexplore.frame.BaseActivity;
import com.xl.androiddevelopartexplore.service.MessengerService;

/**
 * Created by Administrator on 2018/2/15 0015.
 */

public class MessengerActivity extends BaseActivity{
    private static final String TAG = "MessengerActivity";
    private Messenger messenger;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            messenger = new Messenger(iBinder);
            Message msg = Message.obtain(null, Constants.MSG_CLIENT);
            Bundle bundle = new Bundle();
            bundle.putString("msg","hello,this msg is from client!");
            msg.setData(bundle);
            msg.replyTo = clientmessenger;
            try {
                messenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_messenger);

        Intent intent = new Intent(this, MessengerService.class);
        bindService(intent,serviceConnection, Context.BIND_AUTO_CREATE);
    }
    private static Messenger clientmessenger = new Messenger(new msgHandlder());
    private static class msgHandlder extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Constants.MSG_SERVER:
                    Log.i(TAG, "handleMessage: receive client msg ==>> " + msg.getData().getString("reply"));
                    break;
            }
        }
    }
    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy: ==>> messengeractivity unbind");
        unbindService(serviceConnection);
        super.onDestroy();
    }
}
