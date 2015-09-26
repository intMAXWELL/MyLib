package bingyan.net.localphoto;


import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Demon on 2015/7/17.
 * 图片加载辅助工具
 */
public class UniversalImageLoaderTool {

    /** 单例模式 */
    private static ImageLoader imageLoader = ImageLoader.getInstance();

    /** 获取单例 */
    public static ImageLoader getImageLoader(){
        return imageLoader;
    }

    /** 检查是否初始化 */
    public static boolean checkImageLoader() {
        return imageLoader.isInited();
    }

    /** 清理缓存 */
    public static void clear(){
        imageLoader.clearDiskCache();
        imageLoader.clearMemoryCache();
    }

    /** 继续加载 */
    public static void resume(){
        imageLoader.resume();
    }

    /** 暂停加载 */
    public static void pause(){
        imageLoader.pause();
    }

    /** 停止加载 */
    public static void stop(){
        imageLoader.stop();
    }

    /** 销毁加载 */
    public static void destroy(){
        imageLoader.destroy();
    }

    /**
     * 显示本地本地图片
     * @param uri 图片uri
     * @param imageAware 实现ImageAware
     * @param default_pic 加载期间和加载失败显示的图片
     */
    public static void disPlayLocalImage(String uri, ImageAware imageAware, int default_pic) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                /** 设置图片在下载期间显示的图片 */
                .showImageOnLoading(default_pic)
                        /** 设置图片Uri为空或是错误的时候显示的图片 */
                .showImageForEmptyUri(default_pic)
                        /** 设置图片加载/解码过程中错误时候显示的图片 */
                .showImageOnFail(default_pic)
                        /** 设置下载的图片是否缓存在内存中 */
                .cacheInMemory(true)
                        /** 设置下载的图片是否缓存在SD卡中 */
                .cacheOnDisk(true)
                        /** 设置图片的解码类型 */
                .bitmapConfig(Bitmap.Config.RGB_565)
                        /** 显示方式 */
                .displayer(new SimpleBitmapDisplayer())
                        /** 构建完成 */
                .build();

        /** imageUrl代表图片的URL地址，imageView代表承载图片的IMAGEVIEW控件 */
        imageLoader.displayImage(uri, imageAware, options,
                new SampleImageLoadingListener());

    }

    /**
     * @author Demon
     * 加载图片的回调
     */
    static class SampleImageLoadingListener extends SimpleImageLoadingListener{

        /** 链表保存加载过的图片 */
        public static final List<String> displayedImages =
                Collections.synchronizedList(new LinkedList<String>());

        /** 加载之前调用 */
        @Override
        public void onLoadingStarted(String imageUri, View view) {
            super.onLoadingStarted(imageUri, view);
        }

        /** 加载失败调用 */
        @Override
        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
            super.onLoadingFailed(imageUri, view, failReason);
        }

        /** 加载完成调用 */
        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean isFirstDisplay = !displayedImages.contains(imageUri);
                if (isFirstDisplay) {
                    /** 图片的淡入效果 */
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }

        /** 加载取消调用 */
        @Override
        public void onLoadingCancelled(String imageUri, View view) {
            super.onLoadingCancelled(imageUri, view);
        }
    }

    /**
     * 加载本地图片
     * @param uri 图片uri
     * @param imageAware 实现ImageAware接口
     * @param default_pic 加载过程及加载失败显示的图片
     * @param listener 回调接口
     */
    public static void disPlayLocalImage(String uri, ImageAware imageAware, int default_pic, ImageLoadingListener listener) {

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                /** 设置图片在下载期间显示的图片 */
                .showImageOnLoading(default_pic)
                        /** 设置图片Uri为空或是错误的时候显示的图片 */
                .showImageForEmptyUri(default_pic)
                        /** 设置图片加载/解码过程中错误时候显示的图片 */
                .showImageOnFail(default_pic)
                        /** 设置下载的图片是否缓存在内存中 */
                .cacheInMemory(true)
                        /** 设置下载的图片是否缓存在SD卡中 */
                .cacheOnDisk(true)
                        /** 设置图片的解码类型 */
                .bitmapConfig(Bitmap.Config.RGB_565)
                        /** 显示方式 */
                .displayer(new SimpleBitmapDisplayer())
                        /** 构建完成 */
                .build();

        /** imageUrl代表图片的URL地址，imageView代表承载图片的IMAGEVIEW控件 */
        imageLoader.displayImage(uri, imageAware, options,
                listener);

    }

}
