package bingyan.net.localphoto.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import bingyan.net.localphoto.R;
import bingyan.net.localphoto.UniversalImageLoaderTool;
import bingyan.net.localphoto.adapter.PhotoMultipleAdapter;
import bingyan.net.localphoto.bean.PhotoInfo;
import bingyan.net.localphoto.bean.PhotoSerialzable;


/**
 * 图片多选
 */
public class PhotoMultipleFragment extends Fragment {

	public interface OnPhotoSelectClickListener {
		public void onPhotoSelectClickListener(List<PhotoInfo> list);
	}
	
	private OnPhotoSelectClickListener onPhotoSelectClickListener;
    private PhotoMultipleAdapter photoAdapter;
	private List<PhotoInfo> list;
    //记录已经选择的图片数
	private int hasSelect = 0;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if(onPhotoSelectClickListener==null){
			onPhotoSelectClickListener = (OnPhotoSelectClickListener)activity;
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_photo_select, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
        //noinspection ConstantConditions
        final GridView gridView = (GridView) getView().findViewById(R.id.gridview);
		final Bundle args = getArguments();
        //获取图片信息
		final PhotoSerialzable photoSerializable =
                (PhotoSerialzable) args.getSerializable("list");
		list = new ArrayList<>();
		list.addAll(photoSerializable.getPhotoInfoList());
		photoAdapter = new PhotoMultipleAdapter(getActivity(), list, gridView);
		gridView.setAdapter(photoAdapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (list.get(position).isChosen() && hasSelect > 0) {
                    list.get(position).setIsChosen(false);
                    hasSelect--;
                } else if (hasSelect < 9) {
                    list.get(position).setIsChosen(true);
                    hasSelect++;
                } else {
                    Toast.makeText(getActivity(), "最多选择9张图片！", Toast.LENGTH_SHORT).show();
                }
                photoAdapter.refreshView(position);
                onPhotoSelectClickListener.onPhotoSelectClickListener(list);
            }
        });

        //滑动时不加载
		gridView.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == 0) {
                    UniversalImageLoaderTool.resume();
                } else {
                    UniversalImageLoaderTool.pause();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
            }
        });
	}
}
