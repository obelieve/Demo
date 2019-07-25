/*
 *
 * Copyright (C) 2017 OrionStar Technology Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.ainirobot.sdk_demo.service;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.ainirobot.coreservice.client.ApiListener;
import com.ainirobot.coreservice.client.RobotApi;

import com.ainirobot.sdk_demo.RobotApplication;
import com.ainirobot.sdk_demo.control.Status;


public class ModuleService extends android.app.Service {

    private static final String TAG = "ModuleService";

    private static final int NOTIFICATION_ID = 1001;

    private ModuleCallback mModuleCallback;

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        mModuleCallback = new ModuleCallback();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int result = super.onStartCommand(intent, flags, startId);
        Log.d(TAG, "onStartCommand this result=" + result);

        Notification notification = new Notification();
        startForeground(NOTIFICATION_ID, notification);

        ConnectivityManager network = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        network.requestNetwork(
                new NetworkRequest.Builder().build(),
                new ConnectivityManager.NetworkCallback() {
                    @Override
                    public void onAvailable(Network network) {
                        super.onAvailable(network);
                        initRobotApi();
                    }
                });
        return START_NOT_STICKY;
    }

    public void initRobotApi() {
        Log.d(TAG, "initRobotApi");
        RobotApi.getInstance().connectServer(this, new ApiListener() {
            @Override
            public void handleApiDisabled() {
                Log.e(TAG, "handleApiDisabled: connectServer");
            }

            @Override
            public void handleApiConnected() {
                Log.d(TAG, "handleApiConnected: connectServer");
                Toast.makeText(ModuleService.this, "服务连接", Toast.LENGTH_SHORT).show();
                addApiCallBack();
            }

            @Override
            public void handleApiDisconnected() {
                Log.e(TAG, "handleApiDisconnected: connectServer");
                Toast.makeText(ModuleService.this, "服务断开", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void addApiCallBack() {
        Log.d(TAG, "addApiCallBack: registerModule");
        // TODO api 调整
        RobotApi.getInstance().setCallback(mModuleCallback);
        // 自定义添加
        Status.getInstance().registerPoseStatus();
        Status.getInstance().registerEmergencyStatus();
        Status.getInstance().registerEstimateStatus();
        /*RobotApi.getInstance().getHeadStatus(0, new CommandListener() {
            @Override
            public void onResult(int result, String message) {
                Log.d(TAG, "onResult: " + result + ", " + message);
            }
        });*/
        startService(new Intent(RobotApplication.getInstance(), SpeechService.class));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

}
