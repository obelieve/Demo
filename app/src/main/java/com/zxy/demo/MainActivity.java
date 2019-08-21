package com.zxy.demo;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zxy.utility.LogUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    public static final String SERVER_ACCEPT_ACTION = "SERVER_ACCEPT_ACTION";
    public static final String SERVER_ACCEPT_ACTION_EXTRA_STRING = "SERVER_ACCEPT_ACTION_EXTRA_STRING";

    private BluetoothAdapter mBluetoothAdapter;
    private BtReceiver mBtReceiver = new BtReceiver();
    private TextView mTv;
    private Button mBtn;

    private boolean isStart;
    private Button mBtnServer;
    private Button mBtnClient;
    private BluetoothDevice mBluetoothDeviceConnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtn = findViewById(R.id.btn);
        mTv = findViewById(R.id.tv);
        mTv.setMovementMethod(ScrollingMovementMethod.getInstance());
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isStart) {
                    mBluetoothAdapter.startDiscovery();//发现设备，异步进程，通过广播接收数据。
                    mTv.append("发现设备信息：\n");
                    isStart = true;
                    mBtn.setText("停止");
                } else {
                    mBtn.setText("扫描");
                    isStart = false;
                    mBluetoothAdapter.cancelDiscovery();//在找到需要连接设备的MAC地址后，可关闭发现设备（发现设备操作会消耗大量资源）
                    mTv.append("关闭发现设备。\n");
                }
            }
        });
        ToastUtil.init(getApplicationContext());
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            logger("蓝牙不可用");
            finish();
        } else {
            if (mBluetoothAdapter.isEnabled()) {
                logger("判断蓝牙开启");
                showPairedDeviceInfo();
            } else {
                logger("判断蓝牙关闭");
                openBluetooth();
            }
        }

        mBtnServer = findViewById(R.id.btn_server);
        mBtnClient = findViewById(R.id.btn_client);
        final BluetoothConnection bluetoothConnection = new BluetoothConnection(this, mBluetoothAdapter);
        mBtnServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetoothConnection.startServer();
            }
        });
        mBtnClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBluetoothDeviceConnect != null)
                    bluetoothConnection.startClient(mBluetoothDeviceConnect, mTv.getText().toString());
            }
        });
    }

    /**
     * 显示已配对蓝牙信息
     */
    private void showPairedDeviceInfo() {
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices != null && pairedDevices.size() > 0) {
            mTv.setText("已配对设备信息：\n");
            for (BluetoothDevice device : pairedDevices) {
                if("QCOM-BTD".equals(device.getName())){
                    mBluetoothDeviceConnect = device;
                }
                mTv.append("device:" + device.getName() + "  " + device.getAddress() + "\n");
            }
        }
    }

    /**
     * 开启蓝牙
     */
    private void openBluetooth() {
        startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), 1);//开启蓝牙intent
//                Intent discoverableIntent = new
//                        Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
//                discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 250);
//                startActivity(discoverableIntent);//设置自定义检测时间
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(mBtReceiver, mBtReceiver.getIntentFilter());
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mBtReceiver);
    }

    private void logger(String s) {
        LogUtil.e(s);
        ToastUtil.show(s);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                logger("蓝牙已开启");
            } else {
                logger("蓝牙已关闭");
            }
        }
    }

    /**
     * 蓝牙连接
     */
    public static class BluetoothConnection {

        public static final String UUID = "00001101-0000-1000-8000-00805F9B34FB";

        private ExecutorService mExecutorServer;
        private ExecutorService mExecutorClient;
        private BluetoothAdapter mBluetoothAdapter;
        private BluetoothServerSocket mBluetoothServerSocket = null;
        private BluetoothSocket mBluetoothSocketServer = null;
        private BluetoothSocket mBluetoothSocketClient = null;
        private Context mContext;

        public BluetoothConnection(Context context, BluetoothAdapter adapter) {
            mContext = context;
            mExecutorServer = Executors.newSingleThreadExecutor();
            mExecutorClient = Executors.newSingleThreadExecutor();
            mBluetoothAdapter = adapter;
        }

        public void startServer() {
            mExecutorServer.execute(new Runnable() {
                @Override
                public void run() {

                    try {
                        mBluetoothServerSocket = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("蓝牙", java.util.UUID.fromString(UUID));
                    } catch (IOException e) {
                        e.printStackTrace();
                        LogUtil.e("服务ServerSocket初始化异常");
                    }
                    LogUtil.e("服务ServerSocket开始接收");
                    while (true) {
                        try {
                            mBluetoothSocketServer = mBluetoothServerSocket.accept();
                        } catch (IOException e) {
                            e.printStackTrace();
                            break;
                        }
                        try {
                            mBluetoothServerSocket.close();
                            LogUtil.e("服务ServerSocket读取数据");
                            readServer(mBluetoothSocketServer);
                        } catch (IOException e) {
                            e.printStackTrace();
                            break;
                        }
                    }
                }
            });
        }

        /**
         * @param socket
         */
        private void readServer(BluetoothSocket socket) {
            //服务端接收
            LogUtil.e("readLine:"+socket);
            try {

                InputStreamReader streamReader = new InputStreamReader(socket.getInputStream());
                BufferedReader reader = new BufferedReader(streamReader);
                String s = null;
                StringBuilder stringBuilder = new StringBuilder();
                LogUtil.e("readLine:"+reader.readLine());
                while ((s = reader.readLine()) != null) {
                    stringBuilder.append(s + "\n");
                }
                Intent intent = new Intent(SERVER_ACCEPT_ACTION);
                intent.putExtra(SERVER_ACCEPT_ACTION_EXTRA_STRING,stringBuilder.toString());
                mContext.sendBroadcast(intent);//发送接收的数据
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        public void startClient(final BluetoothDevice device, final String data) {
            mExecutorClient.execute(new Runnable() {
                @Override
                public void run() {
                    if (mBluetoothSocketClient != null) {
                        try {
                            mBluetoothSocketClient.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        mBluetoothSocketClient = device.createRfcommSocketToServiceRecord(java.util.UUID.fromString(UUID));
                        mBluetoothAdapter.cancelDiscovery();
                        mBluetoothSocketClient.connect();
                        writeClient(mBluetoothSocketClient, data);
                    } catch (IOException e) {
                        e.printStackTrace();
                        if (mBluetoothSocketClient != null) {
                            try {
                                mBluetoothSocketClient.close();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                }
            });
        }

        private void writeClient(BluetoothSocket socket, String data) {
            try {
                OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream());
                LogUtil.e("客户端写入："+data);
                writer.write(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void stopClient() {
            if (mBluetoothSocketClient != null) {
                try {
                    mBluetoothSocketClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void stopServer() {
            if (mBluetoothSocketServer != null) {
                try {
                    mBluetoothSocketServer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    /**
     * 监听蓝牙状态广播
     */
    public class BtReceiver extends BroadcastReceiver {

        public IntentFilter getIntentFilter() {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);//蓝牙连接状态
            intentFilter.addAction(BluetoothDevice.ACTION_FOUND);//发现设备信息
            intentFilter.addAction(SERVER_ACCEPT_ACTION);
            return intentFilter;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                showBluetoothState(intent);
            } else if (intent.getAction().equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                BluetoothClass bluetoothClass = intent.getParcelableExtra(BluetoothDevice.EXTRA_CLASS);
                if ("QCOM-BTD".equals(device.getName())) {
                    mBluetoothDeviceConnect = device;
                    mTv.append("【存在】发现device:" + device.getName() + " addr:" + device.getAddress() + "\n");
                } else {
                    mTv.append("发现device:" + device.getName() + " addr:" + device.getAddress() + "\n");
                }

            } else if (intent.getAction().equals(SERVER_ACCEPT_ACTION)) {
                String s = intent.getStringExtra(SERVER_ACCEPT_ACTION_EXTRA_STRING);
                mTv.append("接收数据：========================:\n");
                mTv.append(s);
            }

        }

        /**
         * 显示蓝牙状态
         *
         * @param intent
         */
        private void showBluetoothState(Intent intent) {
            int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
            int preState = intent.getIntExtra(BluetoothAdapter.EXTRA_PREVIOUS_STATE, -1);
            switch (state) {
                case BluetoothAdapter.STATE_TURNING_ON:
                    logger("STATE_TURNING_ON");
                    break;
                case BluetoothAdapter.STATE_ON:
                    logger("STATE_ON");
                    break;
                case BluetoothAdapter.STATE_TURNING_OFF:
                    logger("STATE_TURNING_OFF");
                    break;
                case BluetoothAdapter.STATE_OFF:
                    logger("STATE_OFF");
                    break;
            }
            switch (preState) {
                case BluetoothAdapter.STATE_TURNING_ON:
                    logger("preState STATE_TURNING_ON");
                    break;
                case BluetoothAdapter.STATE_ON:
                    logger("preState STATE_ON");
                    break;
                case BluetoothAdapter.STATE_TURNING_OFF:
                    logger("preState STATE_TURNING_OFF");
                    break;
                case BluetoothAdapter.STATE_OFF:
                    logger("preState STATE_OFF");
                    break;
            }
        }
    }
}
