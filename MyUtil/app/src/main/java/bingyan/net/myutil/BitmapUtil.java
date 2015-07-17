package bingyan.net.myutil;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;

import java.io.IOException;

/**
 * @author Demon
 * 图片处理工具类
 *
 * <p> 简单介绍 </p>
 * {@link bingyan.net.myutil.BitmapUtil#reviewPicRotate(android.graphics.Bitmap, String)} 重新生成未旋转角度图片
 *
 * {@link bingyan.net.myutil.BitmapUtil#drawableToBitmap(android.graphics.drawable.Drawable)} drawable转Bitmap
 *
 * {@link bingyan.net.myutil.BitmapUtil#getPicRotate(String)} 获取图片旋转角度信息
 *
 * {@link bingyan.net.myutil.BitmapUtil#calculateInSampleSize(android.graphics.BitmapFactory.Options, int, int)}
 * 计算图片的缩小倍数，返回为2的倍数
 *
 * {@link bingyan.net.myutil.BitmapUtil#decodeSampledBitmapFromFile(String, int, int)}
 * 从文件读取图片,并缩放
 *
 * {@link bingyan.net.myutil.BitmapUtil#decodeSampledBitmapFromResource(android.content.res.Resources, int, int, int)}
 * 从资源读取图片,并缩放
 */

public class BitmapUtil {

    /**
     * 获取图片文件的信息，是否旋转了90度，如果是则反转
     * @param bitmap 需要旋转的图片
     * @param path   图片的路径
     */
    public static Bitmap reviewPicRotate(Bitmap bitmap,String path){
        int degree = getPicRotate(path);
        if(degree!=0){
            Matrix m = new Matrix();
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            m.setRotate(degree); // 旋转angle度
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,m, true);// 重新生成图片
        }
        return bitmap;
    }


    /**
     * 读取图片文件旋转的角度
     * @param path 图片绝对路径
     * @return 图片旋转的角度
     */
    public static int getPicRotate(String path) {
        int degree  = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.
                    getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * drawable转Bitmap
     * @param drawable 需要转换的drawable
     * @return 所求的bitmap
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        /** 取 drawable 的长宽 */
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        /** 取 drawable 的颜色格式 */
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        /** 建立对应 bitmap */
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        /** 建立对应 bitmap 的画布 */
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        /** 把 drawable 内容画到画布中 */
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 计算图片的缩小倍数，返回为2的倍数
     * @param options target Bitmap options
     * @param reqHeight 决定图片质量，压缩后图片的高度，介于reqHeight和2*reqHeight
     * @param reqWidth 决定图片质量，压缩后图片的宽度，介于reqWidth和2*reqWidth
     * @return 缩小倍数
     */
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    /**
     * 从文件中读取图片
     * @param path 文件绝对路径
     * @param reqHeight 决定图片质量，压缩后图片的高度，介于reqHeight和2*reqHeight
     * @param reqWidth 决定图片质量，压缩后图片的宽度，介于reqWidth和2*reqWidth
     * @return target Bitmap
     */
    public static Bitmap decodeSampledBitmapFromFile(String path,
                                                     int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    /**
     * 从资源文件中读取图片
     * @param res 资源
     * @param resId 资源id
     * @param reqHeight 决定图片质量，压缩后图片的高度，介于reqHeight和2*reqHeight
     * @param reqWidth 决定图片质量，压缩后图片的宽度，介于reqWidth和2*reqWidth
     * @return target Bitmap
     */
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

}
