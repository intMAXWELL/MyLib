package bingyan.net.localphoto;

import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * Created by Demon on 2015/7/17.
 * 配置ImageLoader属性
 */
public class CheckImageLoaderConfiguration {

    /** 最大磁盘缓存 */
    private static final int diskCacheLimitSize = 80 * 1024 * 1024;

    /**
     *  配置ImageLoader
     *  一般在Activity初始化时调用
     */
    public static void checkImageLoaderCfg(Context context) {
        if(!UniversalImageLoaderTool.checkImageLoader()) {
            /**
             * 自定义图片加载配置
             * 使用ImageLoaderConfiguration.createDefault(this)使用默认配置
             */
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                    .threadPriority(Thread.NORM_PRIORITY - 2)//设置正在运行任务的所有线程在系统中的优先级
                    .denyCacheImageMultipleSizesInMemory()
                    .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                    .diskCacheSize(diskCacheLimitSize)
                    .tasksProcessingOrder(QueueProcessingType.LIFO)
                    .writeDebugLogs()
                    .build();
            ImageLoader.getInstance().init(config);
        }
    }
}
