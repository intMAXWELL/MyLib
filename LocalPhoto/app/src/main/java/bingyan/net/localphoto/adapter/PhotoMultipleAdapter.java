package bingyan.net.localphoto.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.List;

import bingyan.net.localphoto.MyApplication;
import bingyan.net.localphoto.R;
import bingyan.net.localphoto.RotateImageAware;
import bingyan.net.localphoto.ThumbnailsUtil;
import bingyan.net.localphoto.UniversalImageLoaderTool;
import bingyan.net.localphoto.bean.PhotoInfo;


/**
 * @author Demon
 * 多选相片适配器
 */
public class PhotoMultipleAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<PhotoInfo> list;
    private GridView gridView;

	/** 每行显示三幅图 */
	private int width = MyApplication.getDisplayMetrics().widthPixels / 3;

	public PhotoMultipleAdapter(Context context, List<PhotoInfo> list, GridView gridView){
		mInflater = LayoutInflater.from(context);
		this.list = list;
		this.gridView = gridView;
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

    /**
     * @param index View的绝对索引
     */
	public void refreshView(int index) {
        int visiblePos = gridView.getFirstVisiblePosition();
		/** getChildAt 返回当前可见区域的第几个元素 */
        View view = gridView.getChildAt(index - visiblePos);
        ViewHolder holder = (ViewHolder) view.getTag();
		/** 更新选中状态 */
        if (list.get(index).isChosen()) {
            holder.selectImage.setImageResource(R.mipmap.gou_selected);
        } else {
            holder.selectImage.setImageResource(R.mipmap.gou_normal);
        }
    }

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_select_photo, null);
			viewHolder.image = (ImageView)convertView.findViewById(R.id.imageView);
			viewHolder.selectImage = (ImageView)convertView.findViewById(R.id.selectImage);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if(list.get(position).isChosen()){
			viewHolder.selectImage.setImageResource(R.mipmap.gou_selected);
		}else{
			viewHolder.selectImage.setImageResource(R.mipmap.gou_normal);
		}

		LayoutParams layoutParams = viewHolder.image.getLayoutParams();
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

    private final class ViewHolder {
        ImageView image;
        ImageView selectImage;
    }
}
