package com.m7.imkfsdk.chat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.m7.imkfsdk.R;
import com.m7.imkfsdk.chat.adapter.ChatAdapter;
import com.m7.imkfsdk.recordbutton.AudioRecorderButton;
import com.m7.imkfsdk.utils.FaceConversionUtil;
import com.m7.imkfsdk.utils.FileUtils;
import com.m7.imkfsdk.utils.PermissionUtils;
import com.m7.imkfsdk.utils.PickUtils;
import com.m7.imkfsdk.utils.ToastUtils;
import com.m7.imkfsdk.view.ChatListView;
import com.moor.imkf.AcceptOtherAgentListener;
import com.moor.imkf.ChatListener;
import com.moor.imkf.GetPeersListener;
import com.moor.imkf.IMChat;
import com.moor.imkf.IMChatManager;
import com.moor.imkf.IMMessage;
import com.moor.imkf.OnConvertManualListener;
import com.moor.imkf.OnSessionBeginListener;
import com.moor.imkf.db.dao.GlobalSetDao;
import com.moor.imkf.db.dao.MessageDao;
import com.moor.imkf.model.construct.JsonBuild;
import com.moor.imkf.model.entity.CardInfo;
import com.moor.imkf.model.entity.ChatEmoji;
import com.moor.imkf.model.entity.ChatMore;
import com.moor.imkf.model.entity.FromToMessage;
import com.moor.imkf.model.entity.GlobalSet;
import com.moor.imkf.model.entity.Investigate;
import com.moor.imkf.model.entity.Peer;
import com.moor.imkf.utils.LogUtils;
import com.moor.imkf.utils.NullUtil;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 聊天界面
 *
 * @author LongWei
 */
public class ChatActivity extends MyBaseActivity implements OnClickListener,
        OnItemClickListener, ChatListView.OnRefreshListener, AudioRecorderButton.RecorderFinishListener {
    private ChatListView mChatList;
    private Button mChatSend, mChatMore, mChatSetModeVoice,
            mChatSetModeKeyboard;
    TextView chat_tv_back, chat_tv_convert;
    private EditText mChatInput;
    private ChatAdapter chatAdapter;
    private RelativeLayout mChatEdittextLayout,
            mChatMoreContainer;
    private LinearLayout mMore;
    private AudioRecorderButton mRecorderButton;
    //	private SpeechRecorderButton mRecorderButton;
    private RelativeLayout mChatFaceContainer;
    private ImageView mChatEmojiNormal, mChatEmojiChecked;
    private InputMethodManager manager;
    private TextView mOtherName;
    private OnCorpusSelectedListener mListener;
    private ViewPager mChatEmojiVPager, mChatMoreVPager;
    private ArrayList<View> facePageViews;
    private ArrayList<View> morePageViews;
    private LinearLayout mChatIvImageMore, mChatIvImageFace;
    private ArrayList<ImageView> pointViewsFace, pointViewsMore;
    private List<List<ChatEmoji>> emojis;
    private List<FaceAdapter> faceAdapters;
    private List<MoreAdapter> moreAdapters;
    private int current = 0;
    private ArrayList<ChatMore> moreList;
    // 表情分页的结果集合
    public List<List<ChatMore>> moreLists = new ArrayList<List<ChatMore>>();
    private List<FromToMessage> fromToMessage;
    private Boolean JZflag = true;
    private View header;
    private int i = 2;
    private int height;
    private List<FromToMessage> descFromToMessage = new ArrayList<FromToMessage>();
    private static final String tag = "ChatActivity";
    private static final int PICK_IMAGE_ACTIVITY_REQUEST_CODE = 200;
    private static final int PICK_FILE_ACTIVITY_REQUEST_CODE = 300;
    private String picFileFullName;
    MsgReceiver msgReceiver;
    KeFuStatusReceiver keFuStatusReceiver;
    private String peerId;
    LinearLayout chat_queue_ll;
    TextView chat_queue_tv;
    LinearLayout bar_bottom;
    private static final int HANDLER_MSG = 1;
    private static final int HANDLER_MSG_MORE = 2;
    private static final int HANDLER_ROBOT = 0x111;
    private static final int HANDLER_ONLINE = 0x222;
    private static final int HANDLER_OFFNLINE = 0x333;
    private static final int HANDLER_INVESTIGATE = 0x444;
    private static final int HANDLER_QUEUENUM = 0x555;
    private static final int HANDLER_CLIAM = 0x666;
    private static final int HANDLER_FINISH = 0x777;
    private static final int HANDLER_BREAK = 0x888;
    private static final int HANDLER_BREAK_TIP = 0x999;
    private static final int HANDLER_VIPASSIGNFAIL = 0x1000;
    private static final int HANDLER_LEAVEMSG = 0x1100;
    private static final int HANDLER_WRITING = 0x1200;
    private static final int HANDLER_NO_WRITING = 0x1300;
    private boolean isRobot = false;
    private String type = "";
    private String scheduleId = "";
    private String processId = "";
    private String currentNodeId = "";
    private String schedule_id = "";
    private String schedule_topeer = "";
    private String processType = "";
    private String titleName = "";
    private String entranceId = "";
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == HANDLER_MSG) {
                updateMessage();
            } else if (msg.what == HANDLER_MSG_MORE) {
                // 加载更多的时候
                JZMoreMessage();
            }
            if (msg.what == HANDLER_ROBOT) {
                //当前是机器人
                ToastUtils.showShort(R.string.now_robit);
                chat_tv_convert.setVisibility(View.VISIBLE);
                bar_bottom.setVisibility(View.VISIBLE);
                isRobot = true;
            }
            if (msg.what == HANDLER_ONLINE) {
                //当前是客服
                chat_tv_convert.setVisibility(View.GONE);
            }
            if (msg.what == HANDLER_WRITING) {
                //对方当前正在输入
                mOtherName.setText(R.string.other_writing);
            }
            if (msg.what == HANDLER_NO_WRITING) {
                mOtherName.setText(titleName);
            }
            if (msg.what == HANDLER_OFFNLINE) {
                ToastUtils.showShort(R.string.people_not_online);
                chat_tv_convert.setVisibility(View.VISIBLE);
                if (isRobot) {
                    bar_bottom.setVisibility(View.VISIBLE);
                } else {
                    bar_bottom.setVisibility(View.VISIBLE);
                }
                showOffineDialog();
            }
            if (msg.what == HANDLER_INVESTIGATE) {
                sendInvestigate();
            }
            if (msg.what == HANDLER_QUEUENUM) {
                String queueNem = (String) msg.obj;
                showQueueNumLabel(queueNem);
            }
            if (msg.what == HANDLER_CLIAM) {
                chat_queue_ll.setVisibility(View.GONE);
                chat_tv_convert.setVisibility(View.GONE);
                bar_bottom.setVisibility(View.VISIBLE);
                isRobot = false;
                Toast.makeText(getApplicationContext(), R.string.people_now, Toast.LENGTH_SHORT).show();
            }
            if (msg.what == HANDLER_FINISH) {
                //  showSessionFinishDialog();
                mOtherName.setText(R.string.people_isleave);
                titleName = getString(R.string.people_isleave);
                bar_bottom.setVisibility(View.GONE);
            }
            if (msg.what == HANDLER_LEAVEMSG) {
                //跳留言
                Intent intent = new Intent(ChatActivity.this, ScheduleOfflineMessageActivity.class);
                intent.putExtra("LeavemsgNodeId", schedule_id);
                intent.putExtra("ToPeer", schedule_topeer);
                startActivity(intent);
                finish();
            }
            if (msg.what == HANDLER_BREAK) {
                //断开会话
                IMChatManager.getInstance().quitSDk();
                finish();
            }
            if (msg.what == HANDLER_BREAK_TIP) {
                //断开会话前提示
                IMChat.getInstance().createBreakTipMsg(break_tips);
                updateMessage();
            }
            if (msg.what == HANDLER_VIPASSIGNFAIL) {
                //专属座席不在线
                showVipAssignFailDialog();
            }
            if (getString(R.string.chat_img).equals(msg.obj)) {
                openAlbum();
            } else if (getString(R.string.chat_evaluate).equals(msg.obj)) {
                openInvestigateDialog();
            } else if (getString(R.string.chat_file).equals(msg.obj)) {
                openFile();
            }
        }
    };

    /**
     * 打开文件选择
     */
    private void openFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");//设置类型
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, PICK_FILE_ACTIVITY_REQUEST_CODE);
    }

    private void showSessionFinishDialog() {
        new AlertDialog.Builder(this).setTitle(R.string.warm_prompt)
                .setMessage(R.string.doyouneed)
                .setPositiveButton(R.string.continuecall, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        IMChatManager.getInstance().getPeers(new GetPeersListener() {
                            @Override
                            public void onSuccess(List<Peer> peers) {
                                if (peers.size() > 1) {
                                    PeerDialog dialog = new PeerDialog();
                                    Bundle b = new Bundle();
                                    b.putSerializable("Peers", (Serializable) peers);
                                    b.putString("type", "chat");
                                    dialog.setArguments(b);
                                    dialog.show(getFragmentManager(), "");
                                } else if (peers.size() == 1) {
                                    beginSession(peers.get(0).getId());
                                } else {
                                    beginSession("");
                                }
                            }

                            @Override
                            public void onFailed() {

                            }
                        });
                    }
                })
                .setNegativeButton(R.string.back, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        IMChatManager.getInstance().quitSDk();
                        finish();
                    }
                })
                .setCancelable(false)
                .create()
                .show();

    }

    private void showVipAssignFailDialog() {
        new AlertDialog.Builder(this).setTitle(R.string.warm_prompt)
                .setMessage(R.string.doyouneedother)
                .setPositiveButton(R.string.need, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        IMChatManager.getInstance().acceptOtherAgent(peerId, new AcceptOtherAgentListener() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(ChatActivity.this, "已通知其它座席为你服务", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailed() {
                                Toast.makeText(ChatActivity.this, "通知其它座席失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setNegativeButton(R.string.noneed, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        IMChatManager.getInstance().quitSDk();
                        finish();
                    }
                })
                .setCancelable(false)
                .create()
                .show();

    }

    /**
     * 显示排队数
     */
    private void showQueueNumLabel(String queueNum) {
        if (Integer.parseInt(queueNum) > 0) {
            chat_queue_ll.setVisibility(View.VISIBLE);
            chat_queue_tv.setText(queueNum);
        } else {
            chat_queue_ll.setVisibility(View.GONE);
        }
    }

    public static void startActivity(Context mContext, String type, String scheduleId, String processId, String processTo, String processType, String id, CardInfo card) {
        Intent chatIntent = new Intent(mContext, ChatActivity.class);
        chatIntent.putExtra("type", type);
        chatIntent.putExtra("scheduleId", scheduleId);
        chatIntent.putExtra("processId", processId);
        chatIntent.putExtra("currentNodeId", processTo);
        chatIntent.putExtra("processType", processType);
        chatIntent.putExtra("entranceId", id);
        chatIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(chatIntent);
    }

    public static void startActivity(Context mContext, String type, String scheduleId, String processId, String processTo, String processType, String id) {
        startActivity(mContext, type, scheduleId, processId, processTo, processType, id, null);
    }

    public static void startActivity(Context mContext, String type, String peerId, CardInfo card) {
        //卡片不为空 则传入数据库
        if (card != null) {
            FromToMessage cardMsg = new FromToMessage();
            cardMsg.msgType = FromToMessage.MSG_TYPE_CARD;
            cardMsg.cardInfo = JsonBuild.getCardInfo(card);
            cardMsg.userType = "0";
            cardMsg.when = System.currentTimeMillis();
            LogUtils.aTag("cardinfo", JsonBuild.getCardInfo(card));
            MessageDao.getInstance().insertSendMsgsToDao(cardMsg);
        }
        Intent chatIntent = new Intent(mContext, ChatActivity.class);
        chatIntent.putExtra("type", type);
        chatIntent.putExtra("PeerId", peerId);
        chatIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(chatIntent);
    }

    public static void startActivity(Context mContext, String type, String peerId) {
        startActivity(mContext, type, peerId, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.kf_activity_chat);

        //获取技能组id
        Intent intent = getIntent();
        if (intent.getStringExtra("PeerId") != null) {
            peerId = intent.getStringExtra("PeerId");
        }
        if (intent.getStringExtra("type") != null) {
            type = intent.getStringExtra("type");
        }
        if (intent.getStringExtra("scheduleId") != null) {
            scheduleId = intent.getStringExtra("scheduleId");
        }
        if (intent.getStringExtra("processId") != null) {
            processId = intent.getStringExtra("processId");
        }
        if (intent.getStringExtra("currentNodeId") != null) {
            currentNodeId = intent.getStringExtra("currentNodeId");
        }
        if (intent.getStringExtra("entranceId") != null) {
            entranceId = intent.getStringExtra("entranceId");
        }
        if (intent.getStringExtra("processType") != null) {
            processType = intent.getStringExtra("processType");
        }

        IntentFilter intentFilter = new IntentFilter("com.m7.imkfsdk.msgreceiver");
        msgReceiver = new MsgReceiver();
        registerReceiver(msgReceiver, intentFilter);

        IntentFilter kefuIntentFilter = new IntentFilter();
        kefuIntentFilter.addAction(IMChatManager.ROBOT_ACTION);
        kefuIntentFilter.addAction(IMChatManager.ONLINE_ACTION);
        kefuIntentFilter.addAction(IMChatManager.OFFLINE_ACTION);
        kefuIntentFilter.addAction(IMChatManager.CLIAM_ACTION);
        kefuIntentFilter.addAction(IMChatManager.INVESTIGATE_ACTION);
        kefuIntentFilter.addAction(IMChatManager.QUEUENUM_ACTION);
        kefuIntentFilter.addAction(IMChatManager.LEAVEMSG_ACTION);
        kefuIntentFilter.addAction(IMChatManager.FINISH_ACTION);
        kefuIntentFilter.addAction(IMChatManager.USERINFO_ACTION);
        kefuIntentFilter.addAction(IMChatManager.VIPASSIGNFAIL_ACTION);
        kefuIntentFilter.addAction(IMChatManager.CANCEL_ROBOT_ACCESS_ACTION);
        kefuIntentFilter.addAction(IMChatManager.WITHDRAW_ACTION);
        kefuIntentFilter.addAction(IMChatManager.WRITING_ACTION);
        kefuIntentFilter.addAction(IMChatManager.ROBOT_SWITCH_ACTION);
        kefuIntentFilter.addAction(IMChatManager.TCP_ACTION);
        keFuStatusReceiver = new KeFuStatusReceiver();
        registerReceiver(keFuStatusReceiver, kefuIntentFilter);
        init();
        initPermission();
        registerListener();
        initEmojiViewPager();
        initEmojiPoint();
        initEmojiData();
        initMoreViewPager();
        initMorePoint();
        initMoreData();
        updateMessage();
        if (type.equals("peedId")) {
            beginSession(peerId);
        }
        if (type.equals("schedule")) {
            beginScheduleSession(scheduleId, processId, currentNodeId, entranceId);
        }
        //设置全局配置
        setGlobalConfig();
    }

    private void initPermission() {
        if (PermissionUtils.hasAlwaysDeniedPermission(this, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            PermissionUtils.requestPermissions(this, 0x11, new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionUtils.OnPermissionListener() {
                @Override
                public void onPermissionGranted() {
                }

                @Override
                public void onPermissionDenied(String[] deniedPermissions) {
                    Toast.makeText(ChatActivity.this, R.string.notpermession, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    Timer break_timer;
    Timer break_tip_timer;
    long breakTime = 0;
    long breakTipTime = 0;
    String break_tips;
    BreakTimerTask breakTimerTask;
    BreakTipTimerTask breakTipTimerTask;

    /**
     * 设置断开定时器
     */
    private void setGlobalConfig() {
        GlobalSet globalSet = GlobalSetDao.getInstance().getGlobalSet();
        if (globalSet != null) {
            String break_len = globalSet.break_len;
            String break_tips_len = globalSet.break_tips_len;
            break_tips = globalSet.break_tips;
            try {
                breakTime = Integer.parseInt(break_len) * 60 * 1000;
            } catch (Exception e) {
            }
            try {
                breakTipTime = breakTime - Integer.parseInt(break_tips_len) * 60 * 1000;
            } catch (Exception e) {
            }
            if (breakTime > 0) {
                break_timer = new Timer();
                breakTimerTask = new BreakTimerTask();
                break_timer.schedule(breakTimerTask, breakTime);
            }
            if (breakTipTime > 0) {
                break_tip_timer = new Timer();
                breakTipTimerTask = new BreakTipTimerTask();
                break_tip_timer.schedule(breakTipTimerTask, breakTipTime);
            }
        }
    }

    /**
     * 发送文本消息
     *
     * @param msgStr
     */
    public void sendTextMsg(String msgStr) {
        LogUtils.aTag("send", msgStr);
        FromToMessage fromToMessage = IMMessage.createTxtMessage(msgStr);

        //界面显示
        descFromToMessage.add(fromToMessage);
        chatAdapter.notifyDataSetChanged();
        mChatList.setSelection(descFromToMessage.size());
        mChatInput.setText("");

        resetBreakTimer();

        //发送消息
        IMChat.getInstance().sendMessage(fromToMessage, new ChatListener() {
            @Override
            public void onSuccess() {
                //消息发送成功
                updateMessage();
            }

            @Override
            public void onFailed() {
                //消息发送失败
                updateMessage();
            }

            @Override
            public void onProgress(int progress) {

            }
        });
    }

    /**
     * 断开定时器任务
     */
    class BreakTimerTask extends TimerTask {
        @Override
        public void run() {
            handler.sendEmptyMessage(HANDLER_BREAK);
            break_timer.cancel();
        }
    }

    /**
     * 断开前提示定时器任务
     */
    class BreakTipTimerTask extends TimerTask {
        @Override
        public void run() {
            handler.sendEmptyMessage(HANDLER_BREAK_TIP);
            break_tip_timer.cancel();
        }
    }

    /**
     * 重置断开提示定时器
     */
    private void resetBreakTimer() {
        if (break_timer != null) {
            break_timer.cancel();
            break_timer = null;
        }
        if (break_tip_timer != null) {
            break_tip_timer.cancel();
            break_tip_timer = null;
        }

        if (breakTimerTask != null) {
            breakTimerTask.cancel();
        }
        if (breakTipTimerTask != null) {
            breakTipTimerTask.cancel();
        }

        if (breakTime > 0) {
            break_timer = new Timer();
            breakTimerTask = new BreakTimerTask();
            break_timer.schedule(breakTimerTask, breakTime);
        }
        if (breakTipTime > 0) {
            break_tip_timer = new Timer();
            breakTipTimerTask = new BreakTipTimerTask();
            break_tip_timer.schedule(breakTipTimerTask, breakTipTime);
        }
    }

    /**
     * 查询数据库更新页面
     */
    public void updateMessage() {
        fromToMessage = IMChatManager.getInstance().getMessages(1);
        descFromToMessage.clear();
        for (int i = fromToMessage.size() - 1; i >= 0; i--) {
            descFromToMessage.add(fromToMessage.get(i));
        }
        // 是否有数据
        if (IMChatManager.getInstance().isReachEndMessage(
                descFromToMessage.size())) {
            mChatList.dismiss();
        }
        chatAdapter = new ChatAdapter(ChatActivity.this, descFromToMessage);
        mChatList.setAdapter(chatAdapter);
        chatAdapter.notifyDataSetChanged();
        mChatList.setSelection(fromToMessage.size() + 1);

        //重置未读消息数量
        IMChatManager.getInstance().resetMsgUnReadCount();
        //刷新标题
        mOtherName.setText(titleName);
        if (handler.hasMessages(HANDLER_NO_WRITING)) {
            handler.removeMessages(HANDLER_NO_WRITING);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        updateMessage();
    }

    // 分页加载更多
    public void JZMoreMessage() {
        fromToMessage = IMChatManager.getInstance().getMessages(i);
        descFromToMessage.clear();
        for (int i = fromToMessage.size() - 1; i >= 0; i--) {
            descFromToMessage.add(fromToMessage.get(i));
        }

        chatAdapter.notifyDataSetChanged();

        if (mChatList.getHeaderViewsCount() > 0) {
            mChatList.removeHeaderView(header);
        }

        // 是否有数据
        if (IMChatManager.getInstance().isReachEndMessage(
                descFromToMessage.size())) {
            mChatList.setSelectionFromTop(fromToMessage.size() - (i - 1) * 15,
                    height);
            mChatList.dismiss();
        } else {
            mChatList.setSelectionFromTop(fromToMessage.size() - (i - 1) * 15
                    + 1, height);
        }

        mChatList.onRefreshFinished();
        JZflag = true;
        i++;

    }

    // 初始化方法
    public void init() {
        // 设置进来时间软键盘不弹出的默认状态
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mChatSend = (Button) this.findViewById(R.id.chat_send);
        chat_tv_back = (TextView) this.findViewById(R.id.chat_tv_back);
        mRecorderButton = (AudioRecorderButton) findViewById(R.id.chat_press_to_speak);
        mRecorderButton.setRecordFinishListener(this);
        mChatInput = (EditText) this.findViewById(R.id.chat_input);
        mChatEdittextLayout = (RelativeLayout) this
                .findViewById(R.id.chat_edittext_layout);
        mMore = (LinearLayout) this.findViewById(R.id.more);
        mChatEmojiNormal = (ImageView) this
                .findViewById(R.id.chat_emoji_normal);
        mChatEmojiChecked = (ImageView) this
                .findViewById(R.id.chat_emoji_checked);
        mChatFaceContainer = (RelativeLayout) this
                .findViewById(R.id.chat_face_container);
        mChatMoreContainer = (RelativeLayout) this
                .findViewById(R.id.chat_more_container);
        mChatMore = (Button) this.findViewById(R.id.chat_more);

        mChatSetModeVoice = (Button) this
                .findViewById(R.id.chat_set_mode_voice);
        mChatSetModeKeyboard = (Button) this
                .findViewById(R.id.chat_set_mode_keyboard);

        //转人工服务按钮，判断是否需要显示
        chat_tv_convert = (TextView) this.findViewById(R.id.chat_tv_convert);

        if (type.equals("schedule") && !processType.equals("robot")) {
            chat_tv_convert.setVisibility(View.GONE);
        }

        mOtherName = (TextView) this.findViewById(R.id.other_name);
        mChatInput.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mChatEdittextLayout
                            .setBackgroundResource(R.drawable.kf_input_bar_bg_active);
                } else {
                    mChatEdittextLayout
                            .setBackgroundResource(R.drawable.kf_input_bar_bg_normal);
                }

            }
        });

        mChatInput.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mChatEdittextLayout
                        .setBackgroundResource(R.drawable.kf_input_bar_bg_active);
                mChatEmojiNormal.setVisibility(View.VISIBLE);
                mChatEmojiChecked.setVisibility(View.GONE);

                mMore.setVisibility(View.GONE);
                mChatFaceContainer.setVisibility(View.GONE);
                mChatMoreContainer.setVisibility(View.GONE);
            }
        });

        // 监听文字框
        mChatInput.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (!TextUtils.isEmpty(s)) {
                    mChatMore.setVisibility(View.GONE);
                    mChatSend.setVisibility(View.VISIBLE);
                } else {
                    mChatMore.setVisibility(View.VISIBLE);
                    mChatSend.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mChatList = (ChatListView) this.findViewById(R.id.chat_list);
        header = View.inflate(this, R.layout.kf_chatlist_header, null);
        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        header.measure(w, h);
        height = header.getMeasuredHeight();

        mChatList.setOnTouchListener(new OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                mMore.setVisibility(View.GONE);
                mChatEmojiNormal.setVisibility(View.VISIBLE);
                mChatEmojiChecked.setVisibility(View.GONE);
                mChatFaceContainer.setVisibility(View.GONE);
                mChatMoreContainer.setVisibility(View.GONE);
                return false;
            }
        });

        emojis = FaceConversionUtil.getInstace().emojiLists;
        moreList = new ArrayList<ChatMore>();
        ChatMore chatMore2 = new ChatMore(2, R.drawable.kf_icon_chat_pic + "",
                getString(R.string.chat_img));
        ChatMore chatMore3 = new ChatMore(3, R.drawable.kf_icon_chat_file + "",
                getString(R.string.chat_file));
        ChatMore chatMore4 = new ChatMore(4, R.drawable.kf_icon_chat_investigate + "",
                getString(R.string.chat_evaluate));
        moreList.add(chatMore2);
        moreList.add(chatMore3);
        moreList.add(chatMore4);
        int pageCount = (int) Math.ceil(moreList.size() / 8 + 0.1);
        for (int i = 0; i < pageCount; i++) {
            moreLists.add(getData(i));
        }
        mChatEmojiVPager = (ViewPager) findViewById(R.id.chat_emoji_vPager);
        mChatMoreVPager = (ViewPager) findViewById(R.id.chat_more_vPager);
        mChatInput = (EditText) findViewById(R.id.chat_input);
        mChatIvImageFace = (LinearLayout) findViewById(R.id.chat_iv_image_face);
        mChatIvImageMore = (LinearLayout) findViewById(R.id.chat_iv_image_more);
        chat_queue_ll = (LinearLayout) findViewById(R.id.chat_queue_ll);
        chat_queue_tv = (TextView) findViewById(R.id.chat_queue_tv);
        bar_bottom = (LinearLayout) findViewById(R.id.bar_bottom);
    }

    // 注册监听方法
    public void registerListener() {
        mChatSend.setOnClickListener(this);
        chat_tv_back.setOnClickListener(this);
        mChatSetModeVoice.setOnClickListener(this);
        mChatSetModeKeyboard.setOnClickListener(this);
        mChatEmojiNormal.setOnClickListener(this);
        mChatEmojiChecked.setOnClickListener(this);
        mChatMore.setOnClickListener(this);
        mChatList.setOnRefreshListener(this);
        chat_tv_convert.setOnClickListener(this);
    }

    // 获取分页数据
    private List<ChatMore> getData(int page) {
        int startIndex = page * 8;
        int endIndex = startIndex + 8;

        if (endIndex > moreList.size()) {
            endIndex = moreList.size();
        }
        List<ChatMore> list = new ArrayList<ChatMore>();
        list.addAll(moreList.subList(startIndex, endIndex));
        if (list.size() < 8) {
            for (int i = list.size(); i < 8; i++) {
                ChatMore object = new ChatMore();
                list.add(object);
            }
        }
        return list;
    }

    // 隐藏软键盘
    private void hideKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onClick(View v) {
        int i1 = v.getId();
        if (i1 == R.id.chat_tv_back)
        {//断开长连接
            IMChatManager.getInstance().quitSDk();
            //保持长连接
            // IMChatManager.getInstance().onLineQuitSDK();
            finish();

        } else if (i1 == R.id.chat_tv_convert)
        {//转人工服务
            IMChatManager.getInstance().convertManual(new OnConvertManualListener()
            {
                @Override
                public void onLine()
                {
                    if (!type.equals("schedule"))
                    {
                        //有客服在线,隐藏转人工按钮
                        chat_tv_convert.setVisibility(View.GONE);
                        bar_bottom.setVisibility(View.VISIBLE);
                        mOtherName.setText(R.string.wait_link);
                        titleName = getString(R.string.wait_link);
                        Toast.makeText(getApplicationContext(), R.string.topeoplesucceed, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void offLine()
                {
                    //当前没有客服在线
                    if (!type.equals("schedule"))
                    {
                        showOffineDialog();
                    }

                }
            });

        } else if (i1 == R.id.chat_send)
        {
            String txt = mChatInput.getText().toString();
            sendTextMsg(txt);

        } else if (i1 == R.id.chat_set_mode_voice)
        {
            if (Build.VERSION.SDK_INT < 23)
            {
                showVoice();
            } else
            {
                //6.0
                if (ContextCompat.checkSelfPermission(ChatActivity.this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)
                {
                    //该权限已经有了
                    showVoice();
                } else
                {
                    //申请该权限
                    ActivityCompat.requestPermissions(ChatActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, 0x4444);
                }
            }


        } else if (i1 == R.id.chat_set_mode_keyboard)
        {
            mChatEdittextLayout.setVisibility(View.VISIBLE);
            mChatSetModeKeyboard.setVisibility(View.GONE);
            mChatSetModeVoice.setVisibility(View.VISIBLE);
            mChatInput.requestFocus();
            mRecorderButton.setVisibility(View.GONE);
            mChatFaceContainer.setVisibility(View.GONE);

            if (TextUtils.isEmpty(mChatInput.getText()))
            {
                mChatMore.setVisibility(View.VISIBLE);
                mChatSend.setVisibility(View.GONE);
            } else
            {
                mChatMore.setVisibility(View.GONE);
                mChatSend.setVisibility(View.VISIBLE);
            }


        } else if (i1 == R.id.chat_emoji_normal)
        {
            hideKeyboard();
            mMore.setVisibility(View.VISIBLE);
            mChatEmojiNormal.setVisibility(View.GONE);
            mChatEmojiChecked.setVisibility(View.VISIBLE);
            mChatMoreContainer.setVisibility(View.GONE);
            mChatFaceContainer.setVisibility(View.VISIBLE);
            mChatMoreVPager.setVisibility(View.GONE);
            mChatEmojiVPager.setVisibility(View.VISIBLE);

        } else if (i1 == R.id.chat_emoji_checked)
        {
            mChatEmojiNormal.setVisibility(View.VISIBLE);
            mChatEmojiChecked.setVisibility(View.GONE);
            mChatMoreContainer.setVisibility(View.GONE);
            mChatFaceContainer.setVisibility(View.GONE);
            mMore.setVisibility(View.GONE);

        } else if (i1 == R.id.chat_more)
        {
            if (mChatMoreVPager.getVisibility() == View.VISIBLE)
            {
                mChatMoreVPager.setVisibility(View.GONE);
                mMore.setVisibility(View.GONE);
            } else
            {
                mChatMoreVPager.setVisibility(View.VISIBLE);
                mMore.setVisibility(View.VISIBLE);
                mChatEmojiNormal.setVisibility(View.VISIBLE);
                mChatEmojiChecked.setVisibility(View.GONE);
                mChatFaceContainer.setVisibility(View.GONE);
                mChatMoreContainer.setVisibility(View.VISIBLE);
                mChatEmojiVPager.setVisibility(View.GONE);
                hideKeyboard();
            }

        } else
        {
        }
    }

    private void showVoice() {
        hideKeyboard();
        mChatEdittextLayout.setVisibility(View.GONE);
        mMore.setVisibility(View.GONE);
        mChatSetModeVoice.setVisibility(View.GONE);
        mChatSetModeKeyboard.setVisibility(View.VISIBLE);
        mChatSend.setVisibility(View.GONE);
        mChatMore.setVisibility(View.VISIBLE);
        mRecorderButton.setVisibility(View.VISIBLE);
        mChatEmojiNormal.setVisibility(View.VISIBLE);
        mChatEmojiChecked.setVisibility(View.GONE);
        mChatMoreContainer.setVisibility(View.VISIBLE);
        mChatFaceContainer.setVisibility(View.GONE);
    }

    // 表情选择监听器
    public interface OnCorpusSelectedListener {

        void onCorpusSelected(ChatEmoji emoji);

        void onCorpusDeleted();
    }

    // 初始化文件的viewpager
    private void initMoreViewPager() {

        morePageViews = new ArrayList<View>();
        // 左侧添加空页
        View nullView1 = new View(this);
        // 设置透明背景
        nullView1.setBackgroundColor(Color.TRANSPARENT);
        morePageViews.add(nullView1);

        // 中间添加表情页
        moreAdapters = new ArrayList<MoreAdapter>();
        for (int i = 0; i < moreLists.size(); i++) {
            GridView view = new GridView(this);
            MoreAdapter adapter = new MoreAdapter(this, moreLists.get(i),
                    handler);
            view.setAdapter(adapter);
            moreAdapters.add(adapter);
            view.setOnItemClickListener(this);
            view.setNumColumns(4);
            view.setBackgroundColor(Color.TRANSPARENT);
            view.setHorizontalSpacing(1);
            view.setVerticalSpacing(1);
            view.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
            view.setCacheColorHint(0);
            view.setPadding(5, 0, 5, 0);
            view.setSelector(new ColorDrawable(Color.TRANSPARENT));
            view.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
                    LayoutParams.WRAP_CONTENT));
            view.setGravity(Gravity.CENTER);
            morePageViews.add(view);

        }

        // 右侧添加空页面
        View nullView2 = new View(this);
        // 设置透明背景
        nullView2.setBackgroundColor(Color.TRANSPARENT);
        morePageViews.add(nullView2);

    }

    // 初始化游标
    private void initMorePoint() {

        pointViewsMore = new ArrayList<ImageView>();
        ImageView imageView;
        mChatIvImageMore.removeAllViews();
        for (int i = 0; i < morePageViews.size(); i++) {
            imageView = new ImageView(this);
            imageView.setBackgroundResource(R.drawable.kf_d1);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,
                            LayoutParams.WRAP_CONTENT));
            layoutParams.leftMargin = 10;
            layoutParams.rightMargin = 10;
            layoutParams.width = 8;
            layoutParams.height = 8;
            mChatIvImageMore.addView(imageView, layoutParams);
            if (i == 0 || i == morePageViews.size() - 1) {
                imageView.setVisibility(View.GONE);
            }
            if (i == 1) {
                imageView.setBackgroundResource(R.drawable.kf_d2);
            }
            pointViewsMore.add(imageView);

        }
    }

    // 填充数据
    private void initMoreData() {
        mChatMoreVPager.setAdapter(new ViewPagerAdapter(morePageViews));

        mChatMoreVPager.setCurrentItem(1);
        current = 0;
        mChatMoreVPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                current = arg0 - 1;
                // 描绘分页点
                drawMorePoint(arg0);
                // 如果是第一屏或者是最后一屏禁止滑动，其实这里实现的是如果滑动的是第一屏则跳转至第二屏，如果是最后一屏则跳转到倒数第二屏.
                if (arg0 == pointViewsMore.size() - 1 || arg0 == 0) {
                    if (arg0 == 0) {
                        mChatMoreVPager.setCurrentItem(arg0 + 1);// 第二屏
                        // 会再次实现该回调方法实现跳转.
                        pointViewsMore.get(1).setBackgroundResource(
                                R.drawable.kf_d2);
                    } else {
                        mChatMoreVPager.setCurrentItem(arg0 - 1);// 倒数第二屏
                        pointViewsMore.get(arg0 - 1).setBackgroundResource(
                                R.drawable.kf_d2);
                    }
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

    }

    // 绘制游标背景
    public void drawMorePoint(int index) {
        for (int i = 1; i < pointViewsMore.size(); i++) {
            if (index == i) {
                pointViewsMore.get(i).setBackgroundResource(R.drawable.kf_d2);
            } else {
                pointViewsMore.get(i).setBackgroundResource(R.drawable.kf_d1);
            }
        }
    }

    // 初始化显示表情的viewpager
    private void initEmojiViewPager() {
        facePageViews = new ArrayList<View>();
        // 左侧添加空页
        View nullView1 = new View(this);
        // 设置透明背景
        nullView1.setBackgroundColor(Color.TRANSPARENT);
        facePageViews.add(nullView1);
        // 中间添加表情页
        faceAdapters = new ArrayList<FaceAdapter>();
        for (int i = 0; i < emojis.size(); i++) {
            GridView view = new GridView(this);
            FaceAdapter adapter = new FaceAdapter(this, emojis.get(i));
            view.setAdapter(adapter);
            faceAdapters.add(adapter);
            view.setOnItemClickListener(this);
            view.setNumColumns(7);
            view.setBackgroundColor(Color.TRANSPARENT);
            view.setHorizontalSpacing(1);
            view.setVerticalSpacing(1);
            view.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
            view.setCacheColorHint(0);
            view.setPadding(5, 0, 5, 0);
            view.setSelector(new ColorDrawable(Color.TRANSPARENT));
            view.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
                    LayoutParams.WRAP_CONTENT));
            view.setGravity(Gravity.CENTER);
            facePageViews.add(view);
        }

        // 右侧添加空页面
        View nullView2 = new View(this);
        // 设置透明背景
        nullView2.setBackgroundColor(Color.TRANSPARENT);
        facePageViews.add(nullView2);

    }

    // 初始化游标
    private void initEmojiPoint() {
        pointViewsFace = new ArrayList<ImageView>();
        ImageView imageView;
        for (int i = 0; i < facePageViews.size(); i++) {
            imageView = new ImageView(this);
            imageView.setBackgroundResource(R.drawable.kf_d1);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,
                            LayoutParams.WRAP_CONTENT));
            layoutParams.leftMargin = 10;
            layoutParams.rightMargin = 10;
            layoutParams.width = 8;
            layoutParams.height = 8;
            mChatIvImageFace.addView(imageView, layoutParams);
            if (i == 0 || i == facePageViews.size() - 1) {
                imageView.setVisibility(View.GONE);
            }
            if (i == 1) {
                imageView.setBackgroundResource(R.drawable.kf_d2);
            }
            pointViewsFace.add(imageView);
        }
    }

    // 填充数据
    private void initEmojiData() {
        mChatEmojiVPager.setAdapter(new ViewPagerAdapter(facePageViews));

        mChatEmojiVPager.setCurrentItem(1);
        current = 0;
        mChatEmojiVPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                current = arg0 - 1;
                // 描绘分页点
                drawFacePoint(arg0);
                // 如果是第一屏或者是最后一屏禁止滑动，其实这里实现的是如果滑动的是第一屏则跳转至第二屏，如果是最后一屏则跳转到倒数第二屏.
                if (arg0 == pointViewsFace.size() - 1 || arg0 == 0) {
                    if (arg0 == 0) {
                        mChatEmojiVPager.setCurrentItem(arg0 + 1);// 第二屏
                        // 会再次实现该回调方法实现跳转.
                        pointViewsFace.get(1).setBackgroundResource(
                                R.drawable.kf_d2);
                    } else {
                        mChatEmojiVPager.setCurrentItem(arg0 - 1);// 倒数第二屏
                        pointViewsFace.get(arg0 - 1).setBackgroundResource(
                                R.drawable.kf_d2);
                    }
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

    }

    // 绘制游标背景
    public void drawFacePoint(int index) {
        for (int i = 1; i < pointViewsFace.size(); i++) {
            if (index == i) {
                pointViewsFace.get(i).setBackgroundResource(R.drawable.kf_d2);
            } else {
                pointViewsFace.get(i).setBackgroundResource(R.drawable.kf_d1);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        if (mChatFaceContainer.getVisibility() == View.VISIBLE
                && mChatMoreContainer.getVisibility() == View.GONE) {

            ChatEmoji emoji = (ChatEmoji) faceAdapters.get(current).getItem(
                    arg2);
            if (emoji.getId() == R.drawable.kf_face_del_icon) {
                int selection = mChatInput.getSelectionStart();
                String text = mChatInput.getText().toString();
                if (selection > 0) {
                    String text2 = text.substring(selection - 1);
                    if (":".equals(text2)) {
                        String str = text.substring(0, selection - 1);
                        int start = str.lastIndexOf(":");
                        int end = selection;
                        mChatInput.getText().delete(start, end);
                        return;
                    }
                    mChatInput.getText().delete(selection - 1, selection);
                }

            }
            if (!TextUtils.isEmpty(emoji.getCharacter())) {
                if (mListener != null)
                    mListener.onCorpusSelected(emoji);
                SpannableString spannableString = FaceConversionUtil
                        .getInstace().addFace(this, emoji.getId(),
                                emoji.getCharacter(), mChatInput);
                mChatInput.append(spannableString);
            }
        }
    }

    // 打开本地相册
    public void openAlbum() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        this.startActivityForResult(intent, PICK_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    /**
     * 打开评价对话框
     */
    private void openInvestigateDialog() {
        InvestigateDialog dialog = new InvestigateDialog();
        dialog.show(getFragmentManager(), "InvestigateDialog");
    }

    // 拍照回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                if (uri != null) {
                    String realPath = PickUtils.getPath(ChatActivity.this, uri);
                    picFileFullName = realPath;
                    Log.d("发送图片消息了", "图片的本地路径是：" + picFileFullName);
                    //准备发送图片消息
                    FromToMessage fromToMessage = IMMessage.createImageMessage(picFileFullName);
                    ArrayList fromTomsgs = new ArrayList<FromToMessage>();
                    fromTomsgs.add(fromToMessage);
                    descFromToMessage.addAll(fromTomsgs);
                    chatAdapter.notifyDataSetChanged();
                    mChatList.setSelection(descFromToMessage.size());
                    resetBreakTimer();
                    IMChat.getInstance().sendMessage(fromToMessage, new ChatListener() {
                        @Override
                        public void onSuccess() {
                            updateMessage();
                        }

                        @Override
                        public void onFailed() {
                            updateMessage();
                        }

                        @Override
                        public void onProgress(int progress) {

                        }
                    });
                } else {
                    Log.e(tag, "从相册获取图片失败");
                }
            }
        } else if (requestCode == PICK_FILE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();//得到uri，后面就是将uri转化成file的过程。
            String path = PickUtils.getPath(ChatActivity.this, uri);
            File file = new File(path);
            String fileSizeStr = "";
            if (file.exists()) {
                long fileSize = file.length();
                if ((fileSize / 1024 / 1024) > 20.0) {
                    //大于20M不能上传
                    Toast.makeText(ChatActivity.this, R.string.sendfiletoobig, Toast.LENGTH_SHORT).show();
                } else {
                    fileSizeStr = FileUtils.formatFileLength(fileSize);
                    String fileName = path.substring(path.lastIndexOf("/") + 1);
                    //发送文件
                    FromToMessage fromToMessage = IMMessage.createFileMessage(path, fileName, fileSizeStr);
                    ArrayList fromTomsgs = new ArrayList<FromToMessage>();
                    fromTomsgs.add(fromToMessage);
                    descFromToMessage.addAll(fromTomsgs);
                    chatAdapter.notifyDataSetChanged();
                    mChatList.setSelection(descFromToMessage.size());
                    resetBreakTimer();
                    IMChat.getInstance().sendMessage(fromToMessage, new ChatListener() {
                        @Override
                        public void onSuccess() {
                            System.out.println("文件消息发送成功");
                            updateMessage();
                        }

                        @Override
                        public void onFailed() {
                            System.out.println("文件消息发送失败");
                            updateMessage();
                        }

                        @Override
                        public void onProgress(int progress) {
                            System.out.println("文件消息正在发送");
                            updateMessage();
                        }
                    });
                }
            }
        }

    }

    // 获取字符
    public String getRealPathFromURI(Uri contentUri) {
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            // Do not call Cursor.close() on a cursor obtained using this
            // method,
            // because the activity will do that for you at the appropriate time
            Cursor cursor = this.managedQuery(contentUri, proj, null, null,
                    null);
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            return contentUri.getPath();
        }
    }

    // 覆盖手机返回键
    @Override
    public void onBackPressed() {
        if (mMore.getVisibility() == View.VISIBLE) {
            mMore.setVisibility(View.GONE);
            mChatEmojiNormal.setVisibility(View.VISIBLE);
            mChatEmojiChecked.setVisibility(View.INVISIBLE);
        } else {
            finish();
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(msgReceiver);
        unregisterReceiver(keFuStatusReceiver);
        mRecorderButton.cancelListener();
        if (break_timer != null) {
            break_timer.cancel();
            break_timer = null;
        }
        if (break_tip_timer != null) {
            break_tip_timer.cancel();
            break_tip_timer = null;
        }
        if (breakTimerTask != null) {
            breakTimerTask.cancel();
        }
        if (breakTipTimerTask != null) {
            breakTipTimerTask.cancel();
        }
        //删除卡片信息
        MessageDao.getInstance().delecteCardMsgs();

    }

    @Override
    protected void onPause() {
        super.onPause();
        chatAdapter.onPause();
    }

    @Override
    public void toRefresh() {
        if (JZflag) {
            JZflag = false;
            new Thread() {
                public void run() {
                    try {
                        sleep(300);
                        handler.sendEmptyMessage(HANDLER_MSG_MORE);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

                ;
            }.start();
        }
    }

    /**
     * 新消息接收器,用来通知界面进行更新
     */
    class MsgReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            handler.sendEmptyMessage(HANDLER_MSG);
        }
    }

    /**
     * 客服状态接收器
     */
    class KeFuStatusReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (IMChatManager.ROBOT_ACTION.equals(action)) {
                //当前是机器人
                handler.sendEmptyMessage(HANDLER_ROBOT);
            } else if (IMChatManager.ONLINE_ACTION.equals(action)) {
                //当前是客服在线
                handler.sendEmptyMessage(HANDLER_ONLINE);
            } else if (IMChatManager.OFFLINE_ACTION.equals(action)) {
                //当前是客服离线
                handler.sendEmptyMessage(HANDLER_OFFNLINE);
            } else if (IMChatManager.INVESTIGATE_ACTION.equals(action)) {
                //客服发起了评价
                handler.sendEmptyMessage(HANDLER_INVESTIGATE);
            } else if (IMChatManager.QUEUENUM_ACTION.equals(action)) {
                //技能组排队数
                if (intent.getStringExtra(IMChatManager.QUEUENUM_ACTION) != null) {
                    String queueNum = intent.getStringExtra(IMChatManager.QUEUENUM_ACTION);
                    Message queueMsg = Message.obtain();
                    queueMsg.what = HANDLER_QUEUENUM;
                    queueMsg.obj = queueNum;
                    handler.sendMessage(queueMsg);
                }
            } else if (IMChatManager.CLIAM_ACTION.equals(action)) {
                //客服领取了会话
                handler.sendEmptyMessage(HANDLER_CLIAM);
            } else if (IMChatManager.LEAVEMSG_ACTION.equals(action)) {
                //schedule 跳留言
                schedule_id = intent.getStringExtra(IMChatManager.CONSTANT_ID);
                schedule_topeer = intent.getStringExtra(IMChatManager.CONSTANT_TOPEER);
                handler.sendEmptyMessage(HANDLER_LEAVEMSG);
            } else if (IMChatManager.FINISH_ACTION.equals(action)) {
                //客服关闭了会话
                handler.sendEmptyMessage(HANDLER_FINISH);
            } else if (IMChatManager.USERINFO_ACTION.equals(action)) {
                //客服信息
                String type = intent.getStringExtra(IMChatManager.CONSTANT_TYPE);
                String exten = intent.getStringExtra(IMChatManager.CONSTANT_EXTEN);
                String userName = intent.getStringExtra(IMChatManager.CONSTANT_USERNAME);
                String userIcon = intent.getStringExtra(IMChatManager.CONSTANT_USERICON);

                if ("claim".equals(type)) {
                    mOtherName.setText(NullUtil.checkNull(userName) + getString(R.string.seiveceforyou));
                    titleName = NullUtil.checkNull(userName) + getString(R.string.seiveceforyou);
                }
                if ("redirect".equals(type)) {
                    mOtherName.setText(NullUtil.checkNull(userName) + getString(R.string.seiveceforyou));
                    titleName = NullUtil.checkNull(userName) + getString(R.string.seiveceforyou);
                }
                if ("robot".equals(type)) {
                    mOtherName.setText(NullUtil.checkNull(userName) + getString(R.string.seiveceforyou));
                    titleName = NullUtil.checkNull(userName) + getString(R.string.seiveceforyou);
                }
            } else if (IMChatManager.VIPASSIGNFAIL_ACTION.equals(action)) {
                //专属座席不在线
                handler.sendEmptyMessage(HANDLER_VIPASSIGNFAIL);
            } else if (IMChatManager.CANCEL_ROBOT_ACCESS_ACTION.equals(action)) {
                //人工干预
                Toast.makeText(ChatActivity.this, R.string.receivepeopleaction, Toast.LENGTH_SHORT).show();
            } else if (IMChatManager.WITHDRAW_ACTION.equals(action)) {
                //消息撤回
                String id = intent.getStringExtra(IMChatManager.WITHDEAW_ID);
                MessageDao.getInstance().updateMsgWithDrawStatus(id);
                handler.sendEmptyMessage(HANDLER_MSG);
            } else if (IMChatManager.WRITING_ACTION.equals(action)) {
                //对方正在输入
                handler.sendEmptyMessage(HANDLER_WRITING);
                handler.sendEmptyMessageDelayed(HANDLER_NO_WRITING, 5000);
            } else if (IMChatManager.ROBOT_SWITCH_ACTION.equals(action)) {
                //robotSwitch
                String status = intent.getStringExtra(IMChatManager.CONSTANT_ROBOT_SWITCH);
                String sessionId = intent.getStringExtra(IMChatManager.CONSTANT_SESSIONID);
            } else if (IMChatManager.TCP_ACTION.equals(action)) {
                //tcp状态
                String tcpstatus = intent.getStringExtra(IMChatManager.TCPSTATUS);
            }
        }
    }

    private void beginSession(String peerId) {
        IMChatManager.getInstance().beginSession(peerId, new OnSessionBeginListener() {
            @Override
            public void onSuccess() {
                if (IMChatManager.getInstance().isInvestigateOn()) {
                    //显示评价按钮
                    moreList.clear();
                    ChatMore chatMore2 = new ChatMore(2, R.drawable.kf_icon_chat_pic + "",
                            getString(R.string.chat_img));
                    ChatMore chatMore3 = new ChatMore(3, R.drawable.kf_icon_chat_file + "",
                            getString(R.string.chat_file));
                    ChatMore chatMore4 = new ChatMore(4, R.drawable.kf_icon_chat_investigate + "",
                            getString(R.string.chat_evaluate));
                    moreList.add(chatMore2);
                    moreList.add(chatMore3);
                    moreList.add(chatMore4);
                    moreLists.clear();
                    int pageCount = (int) Math.ceil(moreList.size() / 8 + 0.1);
                    for (int i = 0; i < pageCount; i++) {
                        moreLists.add(getData(i));
                    }
                    initMoreViewPager();
                    initMorePoint();
                    initMoreData();
                } else {
                    //隐藏评价按钮
                    moreList.clear();
                    ChatMore chatMore2 = new ChatMore(2, R.drawable.kf_icon_chat_pic + "",
                            getString(R.string.chat_img));
                    ChatMore chatMore3 = new ChatMore(3, R.drawable.kf_icon_chat_file + "",
                            getString(R.string.chat_file));
                    moreList.add(chatMore2);
                    moreList.add(chatMore3);

                    moreLists.clear();
                    int pageCount = (int) Math.ceil(moreList.size() / 8 + 0.1);
                    for (int i = 0; i < pageCount; i++) {
                        moreLists.add(getData(i));
                    }
                    initMoreViewPager();
                    initMorePoint();
                    initMoreData();
                }
            }

            @Override
            public void onFailed() {
                chat_tv_convert.setVisibility(View.GONE);
                showOffineDialog();
            }
        });
    }

    private void beginScheduleSession(String scheduleId, String processId, String currentNodeId, String entranceId) {
        IMChatManager.getInstance().beginScheduleSession(scheduleId, processId, currentNodeId, entranceId, new OnSessionBeginListener() {

            @Override
            public void onSuccess() {

                if (IMChatManager.getInstance().isInvestigateOn()) {
                    //显示评价按钮
                    moreList.clear();
                    ChatMore chatMore2 = new ChatMore(2, R.drawable.kf_icon_chat_pic + "",
                            getString(R.string.chat_img));
                    ChatMore chatMore3 = new ChatMore(3, R.drawable.kf_icon_chat_file + "",
                            getString(R.string.chat_file));
                    ChatMore chatMore4 = new ChatMore(4, R.drawable.kf_icon_chat_investigate + "",
                            getString(R.string.chat_evaluate));
                    moreList.add(chatMore2);
                    moreList.add(chatMore3);
                    moreList.add(chatMore4);

                    moreLists.clear();
                    int pageCount = (int) Math.ceil(moreList.size() / 8 + 0.1);
                    for (int i = 0; i < pageCount; i++) {
                        moreLists.add(getData(i));
                    }
                    initMoreViewPager();
                    initMorePoint();
                    initMoreData();
                } else {
                    //隐藏评价按钮
                    moreList.clear();
                    ChatMore chatMore2 = new ChatMore(2, R.drawable.kf_icon_chat_pic + "",
                            getString(R.string.chat_img));
                    ChatMore chatMore3 = new ChatMore(3, R.drawable.kf_icon_chat_file + "",
                            getString(R.string.chat_file));
                    moreList.add(chatMore2);
                    moreList.add(chatMore3);

                    moreLists.clear();
                    int pageCount = (int) Math.ceil(moreList.size() / 8 + 0.1);
                    for (int i = 0; i < pageCount; i++) {
                        moreLists.add(getData(i));
                    }
                    initMoreViewPager();
                    initMorePoint();
                    initMoreData();
                }
            }

            @Override
            public void onFailed() {
                chat_tv_convert.setVisibility(View.GONE);
                showOffineDialog();
            }
        });
    }

    private void showOffineDialog() {
        if (isRobot) {
            bar_bottom.setVisibility(View.VISIBLE);
        } else {
            bar_bottom.setVisibility(View.GONE);
        }
        GlobalSet globalSet = GlobalSetDao.getInstance().getGlobalSet();
        if ("1".equals(NullUtil.checkNull(globalSet.isLeaveMsg))) {
            Intent intent = new Intent(ChatActivity.this, OfflineMessageActicity.class);
            intent.putExtra("PeerId", peerId);
            intent.putExtra("leavemsgTip", NullUtil.checkNull(globalSet.leavemsgTip));
            startActivity(intent);
            finish();
        } else {
            new AlertDialog.Builder(this).setTitle(R.string.warm_prompt)
                    .setMessage(NullUtil.checkNull(globalSet.msg))
                    .setPositiveButton(R.string.iknow, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    })
                    .setCancelable(false)
                    .create()
                    .show();
        }
    }


    public ChatAdapter getChatAdapter() {
        return chatAdapter;
    }

    public void resendMsg(FromToMessage msg, int position) {
        resetBreakTimer();
        IMChat.getInstance().reSendMessage(msg, new ChatListener() {
            @Override
            public void onSuccess() {
                updateMessage();
            }

            @Override
            public void onFailed() {
                updateMessage();
            }

            @Override
            public void onProgress(int progress) {
                updateMessage();
            }
        });
    }

    public void sendMsg(FromToMessage msg) {
        FromToMessage fromToMessage = new FromToMessage();
        fromToMessage.userType = "0";
        fromToMessage.message = "";
        fromToMessage.msgType = FromToMessage.MSG_TYPE_CARDINFO;
        fromToMessage.when = System.currentTimeMillis();
        fromToMessage.sessionId = IMChat.getInstance().getSessionId();
        fromToMessage.tonotify = IMChat.getInstance().get_id();
        fromToMessage.type = "User";
        fromToMessage.from = IMChat.getInstance().get_id();
        fromToMessage.cardInfo = msg.cardInfo;

        //界面显示
        descFromToMessage.add(fromToMessage);
        chatAdapter.notifyDataSetChanged();
        mChatList.setSelection(descFromToMessage.size());
        mChatInput.setText("");

        resetBreakTimer();

        //发送消息
        IMChat.getInstance().sendMessage(fromToMessage, new ChatListener() {
            @Override
            public void onSuccess() {
                //消息发送成功
                updateMessage();
            }

            @Override
            public void onFailed() {
                //消息发送失败
                updateMessage();
            }

            @Override
            public void onProgress(int progress) {

            }
        });


    }

    /**
     * 客服主动发起评价
     */
    private void sendInvestigate() {
        ArrayList<Investigate> investigates = (ArrayList<Investigate>) IMChatManager.getInstance().getInvestigate();
        IMMessage.createInvestigateMessage(investigates);
        updateMessage();
    }

    public ChatListView getChatListView() {
        return mChatList;
    }


    @Override
    public void onRecordFinished(float mTime, String filePath, String pcmFilePath) {

        //先在界面上显示出来
        FromToMessage fromToMessage = IMMessage.createAudioMessage(mTime, filePath, "");
        descFromToMessage.add(fromToMessage);
        chatAdapter.notifyDataSetChanged();
        mChatList.setSelection(descFromToMessage.size());
        sendVoiceMsg("", fromToMessage);
    }

    /**
     * 发送录音消息
     *
     * @param voiceText
     */
    private void sendVoiceMsg(String voiceText, FromToMessage fromToMessage) {
        fromToMessage.voiceText = voiceText;
        resetBreakTimer();
        IMChat.getInstance().sendMessage(fromToMessage, new ChatListener() {
            @Override
            public void onSuccess() {
                updateMessage();
            }

            @Override
            public void onFailed() {
                updateMessage();
            }

            @Override
            public void onProgress(int progress) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtils.onRequestPermissionsResult(this, 0x11, new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, grantResults);
    }
}
