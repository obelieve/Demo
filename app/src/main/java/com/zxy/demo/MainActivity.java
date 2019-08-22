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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.zxy.utility.LogUtil;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String SERVER_ACCEPT_ACTION = "SERVER_ACCEPT_ACTION";
    public static final String SERVER_ACCEPT_ACTION_EXTRA_STRING = "SERVER_ACCEPT_ACTION_EXTRA_STRING";

    private BluetoothAdapter mBluetoothAdapter;
    private BtReceiver mBtReceiver = new BtReceiver();
    private TextView mTvLog;
    private ListView mLvContent;

    private boolean isStart;
    private Button mBtnScan;
    private Button mBtnWrite;
    private Button mBtnServer;
    private BluetoothConnection mBluetoothConnection;
    private String mConnect;
    private int mInt;
    private ArrayAdapter<String> mAdapter;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_scan:
                if (!isStart) {
                    mBluetoothAdapter.startDiscovery();//发现设备，异步进程，通过广播接收数据。
                    mTvLog.append("发现设备信息：\n");
                    isStart = true;
                    mBtnScan.setText("停止");
                } else {
                    mBtnScan.setText("扫描");
                    isStart = false;
                    mBluetoothAdapter.cancelDiscovery();//在找到需要连接设备的MAC地址后，可关闭发现设备（发现设备操作会消耗大量资源）
                    mTvLog.append("关闭发现设备。\n");
                }
                break;
            case R.id.btn_server:
                mConnect = "Server";
                mBluetoothConnection.closeClient();
                mBluetoothConnection.startServer();
                break;
            case R.id.btn_write:
                if (mConnect != null) {
                    if ("Server".equals(mConnect)) {
                        mInt++;
                        mBluetoothConnection.writeServer("客户端写到 随机Int:" + mInt);
                    } else if ("Client".equals(mConnect)) {
                        mInt++;
                        mBluetoothConnection.writeClient("写到服务端随机Int:" + mInt);
                    }
                }
                break;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTvLog = findViewById(R.id.tv_log);
        mBtnScan = findViewById(R.id.btn_scan);
        mBtnWrite = findViewById(R.id.btn_write);
        mBtnServer = findViewById(R.id.btn_server);
        mLvContent = findViewById(R.id.lv_content);
        mAdapter = new ArrayAdapter<String>(this, R.layout.message);
        mLvContent.setAdapter(mAdapter);
        mBtnScan.setOnClickListener(this);
        mBtnWrite.setOnClickListener(this);
        mBtnServer.setOnClickListener(this);
        mLvContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mConnect = "Client";
                mBluetoothConnection.closeServer();
                String info = ((TextView) view).getText().toString();
                String address = info.substring(info.length() - 17);
                mBluetoothConnection.startClient(mBluetoothAdapter.getRemoteDevice(address));
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
        mBluetoothConnection = new BluetoothConnection(this, mBluetoothAdapter);
    }

    /**
     * 显示已配对蓝牙信息
     */
    private void showPairedDeviceInfo() {
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices != null && pairedDevices.size() > 0) {
            mTvLog.setText("已配对设备信息：\n");
            for (BluetoothDevice device : pairedDevices) {
                mAdapter.add("device:" + device.getName() + "  " + device.getAddress());
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

        final String msgFinal = s;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTvLog.append(msgFinal + "\n");
                ToastUtil.show(msgFinal);
            }
        });
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
    public class BluetoothConnection {

        public static final String UUID = "00001101-0000-1000-8000-00805F9B34FB";

        private ExecutorService mExecutorServer;
        private ExecutorService mExecutorClient;
        private BluetoothAdapter mBluetoothAdapter;
        private BluetoothServerSocket mBluetoothServerSocket = null;
        private BluetoothSocket mBluetoothSocketS = null;
        private BluetoothSocket mBluetoothSocketC = null;
        private Context mContext;

        public BluetoothConnection(Context context, BluetoothAdapter adapter) {
            mContext = context;
            mExecutorServer = Executors.newSingleThreadExecutor();
            mExecutorClient = Executors.newSingleThreadExecutor();
            mBluetoothAdapter = adapter;
        }

        public void startServer() {
            logger("蓝牙服务器监听：开始设置");
            mExecutorServer.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        try {
                            mBluetoothServerSocket = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("blue", java.util.UUID.fromString(UUID));
                        } catch (IOException e) {
                            e.printStackTrace();
                            logger("蓝牙服务器监听：初始化异常");
                        }
                        mBluetoothSocketS = mBluetoothServerSocket.accept();
                        logger("蓝牙服务器监听：接收到请求");
                    } catch (IOException e) {
                        e.printStackTrace();
                        logger("蓝牙服务器监听：请求错误");
                    }
                    logger("蓝牙服务器监听：开始读取数据");
                    readServer();
                    try {
                        mBluetoothServerSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }


        /**
         *
         */
        public void readServer() {
            //服务端接收
            try {
                byte[] buffer = new byte[1024];
                StringBuilder stringBuilder = new StringBuilder();
                while (mBluetoothSocketS != null) {
                    int b = mBluetoothSocketS.getInputStream().read(buffer);
                    stringBuilder.append(new String(buffer, 0, b) + "\n");
                    logger("蓝牙服务器监听：读取数据-完成-发送广播");
                    Intent intent = new Intent(SERVER_ACCEPT_ACTION);
                    intent.putExtra(SERVER_ACCEPT_ACTION_EXTRA_STRING, stringBuilder.toString());
                    mContext.sendBroadcast(intent);//发送接收的数据
                }
            } catch (IOException e) {
                e.printStackTrace();
                logger("蓝牙服务器监听：读取数据-异常");
            }
        }

        public void writeServer(String str) {
            if (mBluetoothSocketS != null) {
                try {
                    mBluetoothSocketS.getOutputStream().write(str.getBytes());
                    logger("写入到服务端信息："+str);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        public void closeServer() {
            if (mBluetoothSocketS != null) {
                try {
                    mBluetoothSocketS.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mBluetoothSocketS = null;
            }
        }


        public void startClient(final BluetoothDevice device) {
            mExecutorClient.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        mBluetoothSocketC = device.createRfcommSocketToServiceRecord(java.util.UUID.fromString(UUID));
                        if (mBluetoothSocketC != null) {
                            mBluetoothAdapter.cancelDiscovery();
                            mBluetoothSocketC.connect();
                            logger("蓝牙客户端主动连接：成功");
                            Intent intent = new Intent(SERVER_ACCEPT_ACTION);
                            intent.putExtra(SERVER_ACCEPT_ACTION_EXTRA_STRING, "蓝牙客户端主动连接：成功");
                            mContext.sendBroadcast(intent);//发送接收的数据
                            readClient();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        if (mBluetoothSocketC != null) {
                            try {
                                mBluetoothSocketC.close();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                }
            });
        }

        public void writeClient(String data) {
            try {
                if (mBluetoothSocketC != null) {
                    mBluetoothSocketC.getOutputStream().write(data.getBytes());
                    logger("客户端写入：" + data);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         *
         */
        public void readClient() {
            //客户端接收
            try {
                logger("蓝牙客户端监听：读取数据开始");
                byte[] buffer = new byte[1024];
                StringBuilder stringBuilder = new StringBuilder();
                while (mBluetoothSocketC != null) {
                    int b = mBluetoothSocketC.getInputStream().read(buffer);
                    stringBuilder.append(new String(buffer, 0, b) + "\n");
                    logger("蓝牙客户端监听：读取数据-完成-发送广播");
                    Intent intent = new Intent(SERVER_ACCEPT_ACTION);
                    intent.putExtra(SERVER_ACCEPT_ACTION_EXTRA_STRING, stringBuilder.toString());
                    mContext.sendBroadcast(intent);//发送接收的数据
                }
            } catch (IOException e) {
                e.printStackTrace();
                logger("蓝牙客户端监听：读取数据-异常");
            }
        }

        public void closeClient() {
            if (mBluetoothSocketC != null) {
                try {
                    mBluetoothSocketC.close();
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
            intentFilter.addAction(SERVER_ACCEPT_ACTION);//获取蓝牙通信信息
            return intentFilter;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                showBluetoothState(intent);
            } else if (intent.getAction().equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                BluetoothClass bluetoothClass = intent.getParcelableExtra(BluetoothDevice.EXTRA_CLASS);
                mAdapter.add("device:" + device.getName() + "  " + device.getAddress());
            } else if (intent.getAction().equals(SERVER_ACCEPT_ACTION)) {
                String s = intent.getStringExtra(SERVER_ACCEPT_ACTION_EXTRA_STRING);
                mTvLog.append("接收数据：========================:\n");
                mTvLog.append(s);
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
