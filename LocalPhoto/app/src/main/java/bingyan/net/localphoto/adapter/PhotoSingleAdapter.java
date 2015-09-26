package bingyan.net.localphoto.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

import bingyan.net.localphoto.MyApplication;
import bingyan.net.localphoto.R;
import bingyan.net.localphoto.RotateImageAware;
import bingyan.net.localphoto.ThumbnailsUtil;
import bingyan.net.localphoto.UniversalImageLoaderTool;
import bingyan.net.localphoto.bean.PhotoInfo;


/**
 * Created by Demon on 2015/7/17.
 * 相片适配器
 */
public class PhotoSingleAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<PhotoInfo> list;

    /**
     * 设置每行显示三张图片
     * 为了能够正常显示,需要在Activity的onCreate方法中调用getWindowManager().
     * getDefaultDisplay().getMetrics(MyApplication.getDisplayMetrics())
     */
    private int width = MyApplication.getDisplayMetrics().widthPixels / 3;

    public PhotoSingleAdapter(Context context, List<PhotoInfo> list){
        inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_select_photo, null);
            viewHolder.image = (ImageView)convertView.findViewById(R.id.imageView);
            viewHolder.selectImage = (ImageView) convertView.findViewById(R.id.selectImage);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        /** 不显示勾选框 */
        viewHolder.selectImage.setVisibility(View.INVISIBLE);
        ViewGroup.LayoutParams layoutParams = viewHolder.image.getLayoutParams();
        layoutParams.width = width;
        //noinspection SuspiciousNameCombination
        layoutParams.height = width;
        viewHolder.image.setLayoutParams(layoutParams);

        final PhotoInfo photoInfo = list.get(position);
        if(photoInfo!=null) {
            /** 异步加载图片 */
            UniversalImageLoaderTool.disPlayLocalImage(ThumbnailsUtil.get(photoInfo.getImageId(), photoInfo.getPathFile()),
                    new RotateImageAware(viewHolder.image, photoInfo.getPathAbsolute()), R.mipmap.ic_launcher);
        }
        return convertView;
    }

    private final class ViewHolder{
        ImageView image;
        ImageView selectImage;
    }
}
