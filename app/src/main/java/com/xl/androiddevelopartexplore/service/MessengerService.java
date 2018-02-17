package com.xl.androiddevelopartexplore.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.xl.androiddevelopartexplore.Constants;

import static com.xl.androiddevelopartexplore.Constants.MSG_CLIENT;

/**
 * Created by Administrator on 2018/2/15 0015.
 */

public class MessengerService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind: current pid ==>> " + android.os.Process.myPid());
        return messenger.getBinder();
    }

    private static Context context;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        context = getApplicationContext();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    private static final String TAG = "MessengerService";

    private static class MsgHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_CLIENT:
                    Log.i(TAG, "handleMessage: receive client msg ==>> " + msg.getData().getString("msg"));
//                    Toast.makeText(context, "remeoteService toast==>>" + msg.getData().getString("msg"), Toast.LENGTH_SHORT).show();
                    Messenger toclient = msg.replyTo;
                    Message message = Message.obtain(null, Constants.MSG_SERVER);
                    Bundle bundle = new Bundle();
                    bundle.putString("reply","ok,This is Server,I've received your msg");
                    message.setData(bundle);
                    try {
                        toclient.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private final Messenger messenger = new Messenger(new MsgHandler());

}
