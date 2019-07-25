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

import android.os.Bundle;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import com.ainirobot.coreservice.client.module.ModuleCallbackApi;

import com.ainirobot.sdk_demo.control.MessageManager;
import com.ainirobot.sdk_demo.utils.Constants;

/**
 * 去掉了onCmdResponse 回调
 */
public class ModuleCallback extends ModuleCallbackApi {

    private static final String TAG = "ModuleCallback";

    public ModuleCallback() {
    }

    @Override
    public boolean onSendRequest(int reqId, String reqType, String reqText, String reqParam) {
        Log.d(TAG, "New request: " + " type is:" + reqType + " text is:" + reqText + " reqParam = " + reqParam);
        Message msg = Message.obtain();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.BUNDLE_ID, reqId);
        bundle.putString(Constants.BUNDLE_REQUEST_TYPE, reqType);
        bundle.putString(Constants.BUNDLE_REQUEST_TEXT, reqText);
        bundle.putString(Constants.BUNDLE_REQUEST_PARAM, reqParam);
        msg.setData(bundle);
        msg.what = Constants.MSG_REQUEST;
        MessageManager.getInstance().handleMessage(msg);
        return true;
    }

    /**
     * Handle message received from HW service
     *
     * @param hwFunction
     * @param hwReport
     * @throws RemoteException
     */
    @Override
    public void onHWReport(int hwFunction, String command, String hwReport)
            throws RemoteException {
        Log.d(TAG, "HW report: " + " hwFunction is:" + hwFunction + " command:" + command +
                " hwReport is:" + hwReport);
        Message msg = Message.obtain();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.BUNDLE_ID, hwFunction);
        bundle.putString(Constants.BUNDLE_CMD_COMMAND, command);
        bundle.putString(Constants.BUNDLE_CMD_STATUS, hwReport);
        msg.setData(bundle);
        msg.what = Constants.MSG_HWREPORT;
        MessageManager.getInstance().handleMessage(msg);
    }

    /**
     * 挂起状态：不会再处理任何请求，当前执行得任务会被挂起
     */
    @Override
    public void onSuspend() throws RemoteException {
        Log.d(TAG, "onSuspend: ");
        Message msg = Message.obtain();
        msg.what = Constants.MSG_SUSPEND;
        MessageManager.getInstance().handleMessage(msg);
    }

    /**
     * 状态回复（如果任务被挂起时，有任务在执行，需要自行记录任务及状态，并重新执行）
     */
    @Override
    public void onRecovery() throws RemoteException {
        Log.d(TAG, "onRecovery: ");
        Message msg = Message.obtain();
        msg.what = Constants.MSG_RECOVERY;
        MessageManager.getInstance().handleMessage(msg);
    }
}
