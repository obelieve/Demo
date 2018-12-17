package io.rong.imkit;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import io.rong.common.RLog;
import io.rong.eventbus.EventBus;
import io.rong.imkit.manager.AudioPlayManager;
import io.rong.imkit.manager.AudioRecordManager;
import io.rong.imkit.manager.IAudioPlayListener;
import io.rong.imkit.model.Event;
import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.destruct.MessageBufferPool;
import io.rong.imlib.model.Message;
import io.rong.message.DestructionCmdMessage;
import io.rong.message.VoiceMessage;

/**
 * Created by zxy on 2018/12/13 15:38.
 */
@ProviderTag(messageContent = VoiceMessage.class, showReadState = true)
public class DefVoiceMessageProvider extends IContainerItemProvider.MessageProvider<VoiceMessage>
{
    private static final String TAG = "DefVoiceMessageProvider";

    public View newView(Context context, ViewGroup group) {
        View view = LayoutInflater.from(context).inflate(R.layout.def_item_voice_message, (ViewGroup)null);
        DefVoiceMessageProvider.ViewHolder holder = new DefVoiceMessageProvider.ViewHolder();
        holder.left = (TextView)view.findViewById(R.id.rc_left);
        holder.right = (TextView)view.findViewById(R.id.rc_right);
        holder.img = (ImageView)view.findViewById(R.id.rc_img);
        holder.unread = (ImageView)view.findViewById(R.id.rc_voice_unread);
        view.setTag(holder);
        return view;
    }

    public void bindView(View v, int position, VoiceMessage content, UIMessage message) {
        DefVoiceMessageProvider.ViewHolder holder = (DefVoiceMessageProvider.ViewHolder)v.getTag();
        Uri playingUri;
        boolean listened;
        if(message.continuePlayAudio) {
            playingUri = AudioPlayManager.getInstance().getPlayingUri();
            if(playingUri == null || !playingUri.equals(content.getUri())) {
                listened = message.getMessage().getReceivedStatus().isListened();
                this.sendDestructReceiptMessage(message);
                AudioPlayManager.getInstance().startPlay(v.getContext(), content.getUri(), new DefVoiceMessageProvider.VoiceMessagePlayListener(v.getContext(), message, holder, listened));
            }
        } else {
            playingUri = AudioPlayManager.getInstance().getPlayingUri();
            if(playingUri != null && playingUri.equals(content.getUri())) {
                this.setLayout(v.getContext(), holder, message, true);
                listened = message.getMessage().getReceivedStatus().isListened();
                AudioPlayManager.getInstance().setPlayListener(new DefVoiceMessageProvider.VoiceMessagePlayListener(v.getContext(), message, holder, listened));
            } else {
                this.setLayout(v.getContext(), holder, message, false);
            }
        }

    }

    public void onItemClick(View view, int position, VoiceMessage content, UIMessage message) {
        RLog.d("DefVoiceMessageProvider", "Item index:" + position);
        if(content != null) {
            this.sendDestructReceiptMessage(message);
            DefVoiceMessageProvider.ViewHolder holder = (DefVoiceMessageProvider.ViewHolder)view.getTag();
            if(AudioPlayManager.getInstance().isPlaying()) {
                if(AudioPlayManager.getInstance().getPlayingUri().equals(content.getUri())) {
                    AudioPlayManager.getInstance().stopPlay();
                    return;
                }

                AudioPlayManager.getInstance().stopPlay();
            }

            if(!AudioPlayManager.getInstance().isInNormalMode(view.getContext()) && AudioPlayManager.getInstance().isInVOIPMode(view.getContext())) {
                Toast.makeText(view.getContext(), view.getContext().getString(R.string.rc_voip_occupying), Toast.LENGTH_SHORT).show();
            } else {
                holder.unread.setVisibility(View.GONE);
                boolean listened = message.getMessage().getReceivedStatus().isListened();
                AudioPlayManager.getInstance().startPlay(view.getContext(), content.getUri(), new DefVoiceMessageProvider.VoiceMessagePlayListener(view.getContext(), message, holder, listened));
            }
        }
    }

    private void sendDestructReceiptMessage(UIMessage message) {
        if(message.getContent().isDestruct() && message.getMessageDirection() == Message.MessageDirection.RECEIVE && message.getMessage().getReadTime() <= 0L && !TextUtils.isEmpty(message.getUId())) {
            long currentTimeMillis = System.currentTimeMillis();
            RongIMClient.getInstance().setMessageReadTime((long)message.getMessageId(), currentTimeMillis, (RongIMClient.OperationCallback)null);
            message.getMessage().setReadTime(currentTimeMillis);
            DestructionCmdMessage destructionCmdMessage = new DestructionCmdMessage();
            destructionCmdMessage.addBurnMessageUId(message.getUId());
            MessageBufferPool.getInstance().putMessageInBuffer(Message.obtain(message.getTargetId(), message.getConversationType(), destructionCmdMessage));
            EventBus.getDefault().post(message.getMessage());
        }

    }

    private void setLayout(Context context, DefVoiceMessageProvider.ViewHolder holder, UIMessage message, boolean playing) {
        VoiceMessage content = (VoiceMessage)message.getContent();
        int minWidth = 70;
        int maxWidth = 204;
        float scale = context.getResources().getDisplayMetrics().density;
        minWidth = (int)((float)minWidth * scale + 0.5F);
        maxWidth = (int)((float)maxWidth * scale + 0.5F);
        int duration = AudioRecordManager.getInstance().getMaxVoiceDuration();
        holder.img.getLayoutParams().width = minWidth + (maxWidth - minWidth) / duration * content.getDuration();
        AnimationDrawable animationDrawable;
        if(message.getMessageDirection() == Message.MessageDirection.SEND) {
            holder.left.setText(String.format("%s\"", new Object[]{Integer.valueOf(content.getDuration())}));
            holder.left.setVisibility(View.VISIBLE);
            holder.right.setVisibility(View.GONE);
            holder.unread.setVisibility(View.GONE);
            holder.img.setScaleType(ImageView.ScaleType.FIT_END);
            holder.img.setBackgroundResource(R.drawable.rc_ic_bubble_right);
            animationDrawable = (AnimationDrawable)context.getResources().getDrawable(R.drawable.rc_an_voice_sent);
            if(playing) {
                holder.img.setImageDrawable(animationDrawable);
                if(animationDrawable != null) {
                    animationDrawable.start();
                }
            } else {
                holder.img.setImageDrawable(holder.img.getResources().getDrawable(R.drawable.rc_ic_voice_sent));
                if(animationDrawable != null) {
                    animationDrawable.stop();
                }
            }
        } else {
            holder.right.setText(String.format("%s\"", new Object[]{Integer.valueOf(content.getDuration())}));
            holder.right.setVisibility(View.VISIBLE);
            holder.left.setVisibility(View.GONE);
            if(!message.getReceivedStatus().isListened()) {
                holder.unread.setVisibility(View.VISIBLE);
            } else {
                holder.unread.setVisibility(View.GONE);
            }

            holder.img.setBackgroundResource(R.drawable.rc_ic_bubble_left);
            animationDrawable = (AnimationDrawable)context.getResources().getDrawable(R.drawable.rc_an_voice_receive);
            if(playing) {
                holder.img.setImageDrawable(animationDrawable);
                if(animationDrawable != null) {
                    animationDrawable.start();
                }
            } else {
                holder.img.setImageDrawable(holder.img.getResources().getDrawable(R.drawable.rc_ic_voice_receive));
                if(animationDrawable != null) {
                    animationDrawable.stop();
                }
            }

            holder.img.setScaleType(ImageView.ScaleType.FIT_START);
        }

    }

    public Spannable getContentSummary(VoiceMessage data) {
        return null;
    }

    public Spannable getContentSummary(Context context, VoiceMessage data) {
        return new SpannableString(context.getString(R.string.rc_message_content_voice));
    }

    @TargetApi(8)
    private boolean muteAudioFocus(Context context, boolean bMute) {
        if(context == null) {
            RLog.d("DefVoiceMessageProvider", "muteAudioFocus context is null.");
            return false;
        } else if(Build.VERSION.SDK_INT < 8) {
            RLog.d("DefVoiceMessageProvider", "muteAudioFocus Android 2.1 and below can not stop music");
            return false;
        } else {
            boolean bool = false;
            @SuppressLint("WrongConstant") AudioManager am = (AudioManager)context.getSystemService("audio");
            int result;
            if(bMute) {
                result = am.requestAudioFocus((AudioManager.OnAudioFocusChangeListener)null, 3, 2);
                bool = result == 1;
            } else {
                result = am.abandonAudioFocus((AudioManager.OnAudioFocusChangeListener)null);
                bool = result == 1;
            }

            RLog.d("DefVoiceMessageProvider", "muteAudioFocus pauseMusic bMute=" + bMute + " result=" + bool);
            return bool;
        }
    }

    private class VoiceMessagePlayListener implements IAudioPlayListener
    {
        private Context context;
        private UIMessage message;
        private DefVoiceMessageProvider.ViewHolder holder;
        private boolean listened;

        public VoiceMessagePlayListener(Context context, UIMessage message, DefVoiceMessageProvider.ViewHolder holder, boolean listened) {
            this.context = context;
            this.message = message;
            this.holder = holder;
            this.listened = listened;
        }

        public void onStart(Uri uri) {
            this.message.continuePlayAudio = false;
            this.message.setListening(true);
            this.message.getReceivedStatus().setListened();
            RongIMClient.getInstance().setMessageReceivedStatus(this.message.getMessageId(), this.message.getReceivedStatus(), (RongIMClient.ResultCallback)null);
            DefVoiceMessageProvider.this.setLayout(this.context, this.holder, this.message, true);
            EventBus.getDefault().post(new Event.AudioListenedEvent(this.message.getMessage()));
        }

        public void onStop(Uri uri) {
            if(this.message.getContent() instanceof VoiceMessage) {
                this.message.setListening(false);
                DefVoiceMessageProvider.this.setLayout(this.context, this.holder, this.message, false);
            }

        }

        public void onComplete(Uri uri) {
            Event.PlayAudioEvent event = Event.PlayAudioEvent.obtain();
            event.messageId = this.message.getMessageId();
            if(this.message.isListening() && this.message.getMessageDirection().equals(Message.MessageDirection.RECEIVE)) {
                try {
                    event.continuously = this.context.getResources().getBoolean(R.bool.rc_play_audio_continuous);
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                }
            }

            if(event.continuously) {
                EventBus.getDefault().post(event);
            }

            this.message.setListening(false);
            DefVoiceMessageProvider.this.setLayout(this.context, this.holder, this.message, false);
        }
    }

    private static class ViewHolder {
        ImageView img;
        TextView left;
        TextView right;
        ImageView unread;

        private ViewHolder() {
        }
    }
}
