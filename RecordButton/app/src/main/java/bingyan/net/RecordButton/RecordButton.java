package bingyan.net.RecordButton;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import static android.os.Environment.isExternalStorageRemovable;

/**
 * @author Demon
 * <p>{@inheritDoc}</p>
 *
 * <pre>
 * 能够录音的按钮
 * </pre>
 * <pre>
 * 使用{@link #setPicCancelVoice(int)} 设置取消录音对话框显示的图片资源
 * 使用{@link #setPicStartVoice(int)} 设置按钮按下的背景状态
 * 使用{@link #setPicStopVoice(int)} 设置按钮松开的背景状态
 * 使用{@link #setSavePath(String)} 设置音频保存的文件夹
 * 使用{@link #setMinIntervalTime(int)} 设置最短录音时间，默认1s
 * 如果你关心录音的结果
 * 使用{@link #setOnFinishRecordListener(bingyan.net.RecordButton.RecordButton.OnFinishRecordListener)} 设置录音结果回调
 * </pre>
 *
 * <pre>
 * 如果你对音频格式比较关心
 * 使用{@link #setDefaultAudioEncoder(int)} or
 *    {@link #setDefaultAudioSource(int)} or
 *    {@link #setDefaultOutputFormat(int)}
 * 设置对应的音频参数
 * </pre>
 *
 * <p> Xml 属性</p>
 * @see {@link bingyan.net.RecordButton.R.styleable#RecordButton },
 * {@link bingyan.net.RecordButton.R.styleable#RecordButton_pic_voice_cancel},
 * {@link bingyan.net.RecordButton.R.styleable#RecordButton_pic_voice_start},
 * {@link bingyan.net.RecordButton.R.styleable#RecordButton_min_interval_time},
 * {@link bingyan.net.RecordButton.R.styleable#RecordButton_pic_voice_stop}
 *
 * <p> 使用例子 </p>
 *
 * <p> 代码 </p>
 * <pre>
 * final RecordButton recordButton = (RecordButton) findViewById(R.id.rec);
 * recordButton.setOnFinishRecordListener(new RecordButton.OnFinishRecordListener() {
 *       public void onFinishedRecord(String fileName, int lastTime) {
 *           //自定义录音结束回调
 *       }});
 * </pre>
 *
 * <p> Xml 文件</p>
 * <pre>
 * xmlns:app="http://schemas.android.com/apk/res-auto"
 * app:pic_voice_cancel="@mipmap/speaking_cancel"
 * </pre>
 *
 * <p> 加入权限 </p>
 *     <uses-permission android:name="android.permission.RECORD_AUDIO" />
 *     <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
 */

public class RecordButton extends Button implements OnMediaRecord {

    /** 保存录音文件路径 */
    private String fileName;

    /**
     * @see android.media.MediaRecorder
     *
     * <p>A common case of using MediaRecorder to record audio works as follows:
     *
     * <pre>MediaRecorder recorder = new MediaRecorder();
     * recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
     * recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
     * recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
     * recorder.setOutputFile(PATH_NAME);
     * recorder.prepare();
     * recorder.start();   // Recording is now started
     * ...
     * recorder.stop();
     * recorder.reset();   // You can reuse the object by going back to setAudioSource() step
     * recorder.release(); // Now the object cannot be reused
     * </pre>
     *
     * 使用{@link android.media.MediaRecorder.OnErrorListener}
     * 或者 {@link android.media.MediaRecorder.OnErrorListener} 获取错误信息
     */
    private MediaRecorder mediaRecorder;

    /** 录音是否手动取消 */
    private boolean isCancel;

    /** 日志标志 */
    private String LOG_TAG = this.getClass().getName();

    /**
     * 录音状态显示图片资源
     * 可自定义图片资源(包括张数)
     * 可在{@link bingyan.net.RecordButton.RecordButton.ObtainDecibelThread}
     * 完成对显示图片的显示处理
     */
    private static int[] picVoiceChange = {R.mipmap.mic_1, R.mipmap.mic_2, R.mipmap.mic_3,
            R.mipmap.mic_4};

    /** 取消时显示的图片 */
    private  int picCancelVoice = R.mipmap.speaking_cancel;

    /** 按下按钮的背景图 */
    private  int picStartVoice = R.mipmap.voice_btn_hover;

    /** 松开按钮的背景图 */
    private  int picStopVoice = R.mipmap.voice_btn;

    /** 设置取消录音的图片资源 */
    public  void setPicCancelVoice(int picCancelVoice) {
        this.picCancelVoice = picCancelVoice;
    }

    /** 设置开始录音按钮的背景图 */
    public  void setPicStartVoice(int picStartVoice) {
        this.picStartVoice = picStartVoice;
    }

    /** 设置停止录音按钮的背景图 */
    public  void setPicStopVoice(int picStopVoice) {
        this.picStopVoice = picStopVoice;
    }

    /** 显示语音音量view,可自定义 */
    private  static ImageView view;

    /** 构造函数 */
    public RecordButton(Context context) {
        super(context);
        init(context, null, 0);
    }

    public RecordButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public RecordButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RecordButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr);
    }

    /** 初始化操作 */
    private void init(Context context,AttributeSet attrs,int defStyle){
        volumeHandler = new ShowVolumeHandler();

        //noinspection ConstantConditions
        fileName = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ||
                !isExternalStorageRemovable() ?
                context.getExternalCacheDir().getPath()
                : context.getCacheDir().getPath();

        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.RecordButton, defStyle, 0);

        /** Load attributes */
        this.picCancelVoice = a.getResourceId(R.styleable.RecordButton_pic_voice_cancel, R.mipmap.speaking_cancel);
        this.picStartVoice = a.getResourceId(R.styleable.RecordButton_pic_voice_start, R.mipmap.voice_btn_hover);
        this.picStopVoice = a.getResourceId(R.styleable.RecordButton_pic_voice_stop, R.mipmap.voice_btn);
        this.minIntervalTime = a.getInteger(R.styleable.RecordButton_min_interval_time, 1000);

        /**
         * Recycle the TypedArray, to be re-used by a later caller. After calling
         * this function you must not ever touch the typed array again.
         */
        a.recycle();

        /** 设置初始化的背景 */
        this.setBackgroundResource(picStopVoice);
    }

    /**
     * @see android.media.MediaRecorder
     * 获语音分贝数线程
     *
     * <pre>
     * 启动线程
     * ObtainDecibelThread thread = new ObtainDecibelThread();
     * thread.start();
     * 中途结束进程
     * thread.exit();
     * </pre>
     */
    private final class ObtainDecibelThread extends Thread{

        /** 线程运行循环条件 */
        private volatile boolean running = true;

        @Override
        public void run() {

            while (running) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (mediaRecorder == null || !running) {
                    break;
                }
                if (isCancel) {
                    continue;
                }
                int maxAmplitude = mediaRecorder.getMaxAmplitude();
                if (maxAmplitude != 0) {
                    int decibel = (int) (10 * Math.log(maxAmplitude) / Math.log(10));//分贝

                    /** 分贝信息的处理，可自定义*/
                    dealWithVolume(decibel);
                }
            }
        }

        /** 通过改变循环条件中途结束进程*/
        public void exit() {running = false;}
    }

    /** 处理分贝数 */
    @Override
    public void dealWithVolume(int decibel){
        if (decibel != 0) {
            if (decibel < 26)
                volumeHandler.sendEmptyMessage(0);
            else if (decibel < 32)
                volumeHandler.sendEmptyMessage(1);
            else if (decibel < 38)
                volumeHandler.sendEmptyMessage(2);
            else
                volumeHandler.sendEmptyMessage(3);
        }
    }

    /** 获取分贝数 */
    private ObtainDecibelThread obtainDecibelThread;

    /** 处理UI */
    private final static class ShowVolumeHandler extends Handler{

        @Override
        public void handleMessage(Message msg) {
            /** 更新语音音量对应UI */
            view.setImageResource(picVoiceChange[msg.what]);
        }
    }

    /**
     * @see bingyan.net.RecordButton.RecordButton.ShowVolumeHandler
     * 处理语音音量
     */
    private Handler volumeHandler;

    /**
     * @author Demon
     * 录音结束回调函数
     */
    public interface OnFinishRecordListener{
        /**
         * 处理录音文件
         * @param fileName 录音文件路径
         * @param lastTime 录音持续时间
         */
        public void onFinishedRecord(String fileName, int lastTime);
    }

    /** 完成录音回调 */
    private OnFinishRecordListener finishedListener;

    /** 修改语音文件路径 */
    public void setSavePath(String path){
        fileName = path;
    }

    /** 设置回调 */
    public void setOnFinishRecordListener(OnFinishRecordListener listener) {
        finishedListener = listener;
    }

    /** 录音开始时间 */
    private long startTime;

    /**
     * AudioSource
     * @see android.media.MediaRecorder.AudioSource
     */
    private int defaultAudioSource = MediaRecorder.AudioSource.MIC;

    /**
     * OutputFormat
     * @see android.media.MediaRecorder.AudioSource
     */
    private int defaultOutputFormat = MediaRecorder.AudioSource.MIC;

    /**
     * AudioEncoder
     * @see android.media.MediaRecorder.AudioEncoder
     */
    private int defaultAudioEncoder = MediaRecorder.AudioEncoder.DEFAULT;

    /** 设置AudioSource */
    public void setDefaultAudioSource(int defaultAudioSource) {
        this.defaultAudioSource = defaultAudioSource;
    }

    /** 设置OutputFormat */
    public void setDefaultOutputFormat(int defaultOutputFormat) {
        this.defaultOutputFormat = defaultOutputFormat;
    }

    /** 设置AudioEncoder */
    public void setDefaultAudioEncoder(int defaultAudioEncoder) {
        this.defaultAudioEncoder = defaultAudioEncoder;
    }

    /**
     * @see bingyan.net.RecordButton.OnMediaRecord
     * 录音开始
     */
    @Override
    public void startVoice() {

        fileName += UUID.randomUUID().toString() + ".amr";
        String state = Environment.getExternalStorageState();

        if (!state.equals(Environment.MEDIA_MOUNTED)) {
            Log.e(LOG_TAG, "SD Card is not mounted,It is  " + state + ".");
        }
        File directory = new File(fileName).getParentFile();
        if (!directory.exists() && !directory.mkdirs()) {
            Log.e(LOG_TAG, "Path to file could not be created");
        }

        /** 标准初始化格式，可修改 */
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(defaultAudioSource);
        mediaRecorder.setOutputFormat(defaultOutputFormat);
        mediaRecorder.setOutputFile(fileName);
        mediaRecorder.setAudioEncoder(defaultAudioEncoder);

        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
        mediaRecorder.start();
        obtainDecibelThread = new ObtainDecibelThread();
        obtainDecibelThread.start();
    }

    /**
     * @see bingyan.net.RecordButton.OnMediaRecord
     * 录音中止
     */
    @Override
    public void stopVoice() {
        if (mediaRecorder != null) {
            /** 标准释放资源 */
            mediaRecorder.stop();
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;
        }

        if (obtainDecibelThread != null) {
            obtainDecibelThread.exit();
            obtainDecibelThread = null;
        }
    }

    /** 最短录音时间 */
    private int minIntervalTime = 1000;// 1s

    /** 设置最短时间 */
    public void setMinIntervalTime(int minIntervalTime) {
        this.minIntervalTime = minIntervalTime;
    }

    /** 对话框显示录音音量信息 */
    private Dialog recordIndicator;

    /**
     * @see bingyan.net.RecordButton.OnMediaRecord
     * 录音结束，若设置了回调则调用回调函数
     */
    @Override
    public void finishRecord() {
        stopVoice();

        if (recordIndicator != null && recordIndicator.isShowing()) {
            recordIndicator.dismiss();
        }

        long intervalTime = System.currentTimeMillis() - startTime;

        if (intervalTime < minIntervalTime) {
            Toast.makeText(getContext(), "时间太短！", Toast.LENGTH_SHORT).show();
            File file = new File(fileName);
            if(!file.delete()) Log.e(LOG_TAG,"delete failed!");
            return;
        }

        if (finishedListener != null)
            finishedListener.onFinishedRecord(fileName, (int) (intervalTime / 1000));
    }

    /**
     * @see bingyan.net.RecordButton.OnMediaRecord
     * 录音取消,对应的录音文件也删除
     */
    @Override
    public void cancelRecord() {
        stopVoice();

        if (recordIndicator != null && recordIndicator.isShowing()) {
            recordIndicator.dismiss();
        }

        Toast.makeText(getContext(), "取消说话！", Toast.LENGTH_SHORT).show();
        File file = new File(fileName);
        if(!file.delete()) Log.e(LOG_TAG,"delete failed!");
    }

    /** 记录按下的坐标 */
    private float touchDownY,touchDownX;

    /** 监听触摸事件 */
    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        try {
            switch (event.getAction()) {
                /** 按下时调用 */
                case MotionEvent.ACTION_DOWN:
                    /** 记录按下坐标 */
                    touchDownX = event.getX();
                    touchDownY = event.getY();
                    initDialogAndStartRecord();
                    this.setBackgroundResource(picStartVoice);
                    break;

                /** 根据抬起点的坐标判断是取消还是正常结束 */
                case MotionEvent.ACTION_UP:
                    this.setBackgroundResource(picStopVoice);
                    if (isCancel) {
                        cancelRecord();
                    } else {
                        finishRecord();
                    }
                    break;

                /** 当手指移动到view外面，会cancel */
                case MotionEvent.ACTION_CANCEL:
                    cancelRecord();
                    break;

                /** 监听是否移出 */
                case MotionEvent.ACTION_MOVE:
                    isCancel = Math.abs(event.getX() - touchDownX) > getMeasuredWidth()
                            || Math.abs(event.getY() - touchDownY) > getMeasuredHeight();
                    if (isCancel) {
                        view.setImageResource(picCancelVoice);
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 当按下按钮时调用
     * 开启音量提示对话框
     * @see #onTouchEvent(android.view.MotionEvent)
     */
    private void initDialogAndStartRecord() {

        startTime = System.currentTimeMillis();
        recordIndicator = new Dialog(getContext(),
                R.style.like_toast_dialog_style);
        view = new ImageView(getContext());
        view.setImageResource(R.mipmap.mic_1);

        recordIndicator.setContentView(view, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        recordIndicator.setOnDismissListener(onDismiss);
        WindowManager.LayoutParams lp = recordIndicator.getWindow().getAttributes();
        lp.gravity = Gravity.CENTER;

        startVoice();
        recordIndicator.show();
    }

    /** Dialog消失回调 */
    private DialogInterface.OnDismissListener onDismiss = new DialogInterface.OnDismissListener() {
        @Override
        public void onDismiss(DialogInterface dialog) {
            stopVoice();
        }
    };
}
