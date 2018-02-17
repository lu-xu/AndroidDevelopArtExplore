package com.xl.androiddevelopartexplore.ipc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.xl.androiddevelopartexplore.R;

public class IPCActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn_messenger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ipc);
//        btn_messenger =  ((Button) findViewById(R.id.btn_messenger)
        findViewById(R.id.btn_messenger).setOnClickListener(this);
        findViewById(R.id.btn_aidl).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_messenger:
                startActivity(new Intent(this,MessengerActivity.class));

                break;
            case R.id.btn_aidl:
                startActivity(new Intent(this,AIDLActivity.class));

                break;
        }
    }
}
