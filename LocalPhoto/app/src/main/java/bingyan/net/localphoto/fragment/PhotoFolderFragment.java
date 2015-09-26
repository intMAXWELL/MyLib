package bingyan.net.localphoto.fragment;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Thumbnails;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import bingyan.net.localphoto.R;
import bingyan.net.localphoto.ThumbnailsUtil;
import bingyan.net.localphoto.adapter.PhotoFolderAdapter;
import bingyan.net.localphoto.bean.AlbumInfo;
import bingyan.net.localphoto.bean.PhotoInfo;


/**
 * @author Demon
 * 相册文件夹
 */
public class PhotoFolderFragment extends Fragment {
    //提供借口外部activity调用
	public interface OnPageLoadingClickListener {
		public void onPageLoadingClickListener(List<PhotoInfo> list);
	}
	private OnPageLoadingClickListener onPageLoadingClickListener;
	private ListView listView;
	private ContentResolver cr;
	private List<AlbumInfo> listImageInfo = new ArrayList<>();
    private LinearLayout loadingLay;

    //当Fragment与Activity发生关联时调用
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if(onPageLoadingClickListener ==null){
			onPageLoadingClickListener = (OnPageLoadingClickListener)activity;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_photo_folder, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		//listView可能为null
        //noinspection ConstantConditions
        listView = (ListView) getView().findViewById(R.id.listView);
        //显示progressbar和提示文字的父布局
		loadingLay = (LinearLayout)getView().findViewById(R.id.loadingLay);
        //获取contentResolver
		cr = getActivity().getContentResolver(); 
		//将listImageInfo清空
        listImageInfo.clear();
        //异步执行加载任务
		new ImageAsyncTask().execute();
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				onPageLoadingClickListener.onPageLoadingClickListener
                        (listImageInfo.get(position).getPhotoInfoList());
			}
		});
	}

    //异步加载图片
	private class ImageAsyncTask extends AsyncTask<Void, Void, Object>{
		@Override
		protected Object doInBackground(Void... params) {
			//获取缩略图
            //http://blog.csdn.net/java2009cgh/article/details/8364735
			ThumbnailsUtil.clear();
			//Thumbnails._ID------缩略图ID
            //Thumbnails.IMAGE_ID------与真实图片的ID images._id对应
            //Thumbnails.DATA------存放路径
            String[] projection = { Thumbnails._ID, Thumbnails.IMAGE_ID, Thumbnails.DATA };
            Cursor cur = cr.query(Thumbnails.EXTERNAL_CONTENT_URI, projection, null, null, null);
            if (cur != null && cur.moveToFirst()) {
                int imageId;
                String imagePath;
                do {
                    imageId = cur.getInt(cur.getColumnIndex(Thumbnails.IMAGE_ID));
                    imagePath = cur.getString(cur.getColumnIndex(Thumbnails.DATA));
                    ThumbnailsUtil.put(imageId, "file://" + imagePath);
                } while (cur.moveToNext());
            }
            if (cur != null) {
                cur.close();
            }
            //获取原图，按修改时间递减
			Cursor cursor = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, "date_modified DESC");  
            //原图路径
			String _path="_data";
			//图片所在的文件夹
            String _album="bucket_display_name";
            //记录文件夹名与文件夹信息的hash表
			HashMap<String,AlbumInfo> myHash = new HashMap<>();
            //用来存放文件夹信息
			AlbumInfo albumInfo;
            //用来存放图片文件信息
			PhotoInfo photoInfo;
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int index = 0;
                    //id
                    int _id = cursor.getInt(cursor.getColumnIndex("_id"));
                    //path
                    String path = cursor.getString(cursor.getColumnIndex(_path));
                    //所在文件夹
                    String album = cursor.getString(cursor.getColumnIndex(_album));
                    List<PhotoInfo> photoInfos = new ArrayList<>();
                    photoInfo = new PhotoInfo();
                    if (myHash.containsKey(album)) {
                        //若文件夹名存在则更新
                        albumInfo = myHash.remove(album);
                        if (listImageInfo.contains(albumInfo))
                            index = listImageInfo.indexOf(albumInfo);
                        photoInfo.setImageId(_id);
                        photoInfo.setPathFile("file://" + path);
                        photoInfo.setPathAbsolute(path);
                        albumInfo.getPhotoInfoList().add(photoInfo);
                        listImageInfo.set(index, albumInfo);
                        myHash.put(album, albumInfo);
                    } else {
                        albumInfo = new AlbumInfo();
                        photoInfos.clear();
                        photoInfo.setImageId(_id);
                        photoInfo.setPathFile("file://" + path);
                        photoInfo.setPathAbsolute(path);
                        photoInfos.add(photoInfo);
                        albumInfo.setImageId(_id);
                        albumInfo.setPathFile("file://" + path);
                        albumInfo.setPathAbsolute(path);
                        albumInfo.setNameAlbum(album);
                        albumInfo.setPhotoInfoList(photoInfos);
                        listImageInfo.add(albumInfo);
                        myHash.put(album, albumInfo);
                    }
                } while (cursor.moveToNext());
                cursor.close();
            }
            return null;
		}

		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			loadingLay.setVisibility(View.GONE);
			if(getActivity()!=null){
                PhotoFolderAdapter listAdapter = new PhotoFolderAdapter(getActivity(), listImageInfo);
				listView.setAdapter(listAdapter);
			}
		}
	} 

}
