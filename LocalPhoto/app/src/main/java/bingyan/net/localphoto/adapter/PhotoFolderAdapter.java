package bingyan.net.localphoto.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import bingyan.net.localphoto.R;
import bingyan.net.localphoto.RotateImageAware;
import bingyan.net.localphoto.ThumbnailsUtil;
import bingyan.net.localphoto.UniversalImageLoaderTool;
import bingyan.net.localphoto.bean.AlbumInfo;

/**
 * Created by Demon on 2015/7/17.
 * 相册适配器
 */
public class PhotoFolderAdapter extends BaseAdapter {

    private LayoutInflater inflater;

    private Context context;

    /** 存储相册信息 */
    private List<AlbumInfo> albumInfoList;

    /** 构造函数 */
    public PhotoFolderAdapter(Context context, List<AlbumInfo> list) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.albumInfoList = list;
    }

    @Override
    public int getCount() {
        return albumInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return albumInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (null == convertView) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_photo_folder, new RelativeLayout(context));
            viewHolder.image = (ImageView) convertView.findViewById(R.id.imageView);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.info);
            viewHolder.num = (TextView) convertView.findViewById(R.id.num);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final AlbumInfo albumInfo = albumInfoList.get(position);

        /**
         * 异步加载图片
         * 显示的是缩略图缓存
         * 若加载失败或者在加载过程则则显示R.mipmap.ic_launcher
         * @see UniversalImageLoaderTool
         */
        UniversalImageLoaderTool.disPlayLocalImage(ThumbnailsUtil.get(albumInfo.getImageId(), albumInfo.getPathFile())
                , new RotateImageAware(viewHolder.image, albumInfo.getPathAbsolute()), R.mipmap.ic_launcher);

        /** 显示相册文件夹信息 */
        viewHolder.textView.setText(albumInfo.getNameAlbum());
        viewHolder.num.setText("(" + albumInfoList.get(position).getPhotoInfoList().size() + "张)");
        return convertView;
    }

    private final class ViewHolder{
        /** 显示图片 */
        ImageView image;
        /** 显示相册名 */
        TextView textView;
        /** 显示相册含有照片数 */
        TextView num;
    }

}
