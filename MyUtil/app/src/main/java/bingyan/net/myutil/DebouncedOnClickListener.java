package bingyan.net.myutil;

import android.os.SystemClock;
import android.view.View;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by Demon on 2015/7/17.
 * 防止猴子操作
 */
public abstract class DebouncedOnClickListener implements View.OnClickListener {

    /** 最短时间间隔 */
    private final long minimumInterval;

    /** 存储点击信息 */
    private Map<View, Long> lastClickMap;

    /** 构造方法 */
    public DebouncedOnClickListener(long minimumInterval) {
        this.minimumInterval = minimumInterval;
        this.lastClickMap = new WeakHashMap<>();
    }

    /** 构造方法，默认时间间隔是500ms */
    public DebouncedOnClickListener() {
        this(500);
    }

    public abstract void onDebouncedClick(View view);

    @Override
    public void onClick(View v) {
        Long previousClickTime = lastClickMap.get(v);
        long currentTime = SystemClock.uptimeMillis();

        lastClickMap.put(v, currentTime);
        if (previousClickTime == null || currentTime - previousClickTime > minimumInterval) {
            onDebouncedClick(v);
        }
    }
}
