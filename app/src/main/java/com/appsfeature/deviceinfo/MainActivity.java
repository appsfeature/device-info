package com.appsfeature.deviceinfo;

import android.Manifest;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.deviceinfo.DeviceInfo;
import com.deviceinfo.interfaces.DeviceInfoCallback;
import com.deviceinfo.model.DeviceInfoResult;
import com.deviceinfo.util.DIResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class MainActivity extends AppCompatActivity {

    private TextView tvStatus;
    private View pbProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pbProgress = findViewById(R.id.pb_progress);
        tvStatus = findViewById(R.id.tvStatus);
        getDeviceInfo();
    }

    private void getDeviceInfo() {
        DeviceInfo.getInstance()
                .setEnablePermissionRequiredInfo(true)
                .setDebugMode(true)
                .setPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .addCallback(new DeviceInfoCallback<DeviceInfoResult>() {
                    @Override
                    public void onSuccess(DeviceInfoResult response) {
                        pbProgress.setVisibility(View.GONE);
                        printResponse(response);
                        Log.d("onSuccess",response.toString());
                    }

                    @Override
                    public void onError(Exception e) {
                        pbProgress.setVisibility(View.GONE);
                        tvStatus.setVisibility(View.VISIBLE);
                        tvStatus.setText(e.getMessage());
                        Log.d("onError",e.getMessage());
                    }
                }).fetch(this);
    }

    private void printResponse(DeviceInfoResult response) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        JsonElement je =  new JsonParser().parse(response);
        String prettyJsonString = gson.toJson(response, DeviceInfoResult.class);
        tvStatus.setVisibility(View.VISIBLE);
        tvStatus.setText(prettyJsonString);
    }
//    private void printResponse(JSONObject response) {
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        JsonElement je =  new JsonParser().parse(response.toString());
//        String prettyJsonString = gson.toJson(je);
//        tvStatus.setText(prettyJsonString);
//    }

}
