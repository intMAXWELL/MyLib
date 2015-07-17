package bingyan.net.myutil;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.os.Environment.isExternalStorageRemovable;

/**
 * Created by Demon on 2015/7/17.
 * 文件处理工具类
 */
public class FileUtil {

    /**
     * 获取缓存目录
     * @param context 上下文
     * @param uniqueName 系统默认缓存目录下文件夹名，如用image,video区分不同的缓存类型
     * @return File
     */
    public static File getDiskCacheDir(Context context, String uniqueName) {
        // Check if media is mounted or storage is built-in, if so, try and use external cache dir
        // otherwise use internal cache dir
        @SuppressWarnings("ConstantConditions")
        final String cachePath =
                Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ||
                        !isExternalStorageRemovable() ?
                        context.getExternalCacheDir().getPath()
                        : context.getCacheDir().getPath();
        return new File(cachePath + File.separator + uniqueName);
    }

    /** 创建文件夹 */
    public static boolean createNewFolder(String folderName) {
        Boolean bool = false;
        File file = new File
                (Environment.getExternalStorageDirectory() + folderName);
        if (!file.exists())
            bool = file.mkdir();
        return bool;
    }

    /** 使用系统当前日期加以调整作为照片的名称 */
    public static String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HH_mm_ss", Locale.CHINA);
        return dateFormat.format(date) + ".jpg";
    }
}
