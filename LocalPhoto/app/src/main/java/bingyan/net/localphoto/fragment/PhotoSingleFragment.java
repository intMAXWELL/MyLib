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

import java.util.ArrayList;
import java.util.List;

import bingyan.net.localphoto.R;
import bingyan.net.localphoto.UniversalImageLoaderTool;
import bingyan.net.localphoto.adapter.PhotoSingleAdapter;
import bingyan.net.localphoto.bean.PhotoInfo;
import bingyan.net.localphoto.bean.PhotoSerialzable;


/**
 * 选择一张图片
 * @author Demon
 */
public class PhotoSingleFragment extends Fragment {

	public interface OnPhotoSelectClickListener {
        void onPhotoSelectClickListener(String pathFile);
    }

	private OnPhotoSelectClickListener onPhotoSelectClickListener;
    private List<PhotoInfo> list;

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
        @SuppressWarnings("ConstantConditions")
        final GridView gridView = (GridView) getView().findViewById(R.id.gridview);
        Bundle args = getArguments();

		PhotoSerialzable photoSerializable = (PhotoSerialzable)
                args.getSerializable("list");
        list = new ArrayList<>();
        list.addAll(photoSerializable.getPhotoInfoList());

        PhotoSingleAdapter photoSingleAdapter =
                new PhotoSingleAdapter(getActivity(), list);
        gridView.setAdapter(photoSingleAdapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                onPhotoSelectClickListener.onPhotoSelectClickListener
                        (list.get(position).getPathFile());
            }
        });
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
