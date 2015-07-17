package bingyan.net.RecordButton;

/**
 * Created by Demon on 2015/7/16.
 * 录音过程接口
 */
public interface OnMediaRecord {

    /** 录音开始 */
    public void startVoice();

    /** 录音中止 */
    public void stopVoice();

    /** 录音正常结束 */
    public void finishRecord();

    /** 取消录音 */
    public void cancelRecord();

    /** 分贝处理*/
    public void dealWithVolume(int decibel);
}
