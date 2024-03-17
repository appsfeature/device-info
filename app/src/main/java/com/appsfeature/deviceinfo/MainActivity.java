package com.appsfeature.deviceinfo;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.TextView;

import com.deviceinfo.DeviceInfo;
import com.deviceinfo.interfaces.DeviceInfoCallback;
import com.deviceinfo.model.DeviceInfoResult;
import com.deviceinfo.util.DILogger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class MainActivity extends AppCompatActivity {

    private TextView tvStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvStatus = findViewById(R.id.tvStatus);

        getDeviceInfo();
    }

    private void getDeviceInfo() {
        DeviceInfo.getInstance()
                .setEnablePermissionRequiredInfo(true)
                .setDebugMode(true)
                .addCallback(new DeviceInfoCallback<DeviceInfoResult>() {
                    @Override
                    public void onSuccess(DeviceInfoResult response) {
                        printResponse(response);
                        DILogger.d("onSuccess",response.toString());
                    }

                    @Override
                    public void onError(Exception e) {
                        tvStatus.setText(e.getMessage());
                        DILogger.d("onError",e.getMessage());
                    }
                }).fetch(this);
    }
    private void printResponse(DeviceInfoResult response) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        JsonElement je =  new JsonParser().parse(response);
        String prettyJsonString = gson.toJson(response, DeviceInfoResult.class);
        tvStatus.setText(prettyJsonString);
    }
//    private void printResponse(JSONObject response) {
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        JsonElement je =  new JsonParser().parse(response.toString());
//        String prettyJsonString = gson.toJson(je);
//        tvStatus.setText(prettyJsonString);
//    }

}
