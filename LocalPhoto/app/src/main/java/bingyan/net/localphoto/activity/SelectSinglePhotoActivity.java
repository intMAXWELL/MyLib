package bingyan.net.localphoto.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.MenuItem;

import java.util.List;

import bingyan.net.localphoto.CheckImageLoaderConfiguration;
import bingyan.net.localphoto.MyApplication;
import bingyan.net.localphoto.R;
import bingyan.net.localphoto.bean.PhotoInfo;
import bingyan.net.localphoto.bean.PhotoSerialzable;
import bingyan.net.localphoto.fragment.PhotoFolderFragment;
import bingyan.net.localphoto.fragment.PhotoSingleFragment;


/**
 * @author Demon
 * 选择一张图片
 */
public class SelectSinglePhotoActivity extends ActionBarActivity implements PhotoFolderFragment.OnPageLoadingClickListener
				,PhotoSingleFragment.OnPhotoSelectClickListener {
	private PhotoFolderFragment photoFolderFragment;
	private FragmentManager manager;
    private android.support.v7.app.ActionBar actionBar;
    private int backInt = 0;

    @Override
    protected void onStart() {
        super.onStart();
        CheckImageLoaderConfiguration.checkImageLoaderCfg(this);
    }

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectphoto);
		getWindowManager().getDefaultDisplay().getMetrics(MyApplication.getDisplayMetrics());
		manager = getSupportFragmentManager();
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("请选择相册");
		photoFolderFragment = new PhotoFolderFragment();
        manager.beginTransaction()
                .add(R.id.body, photoFolderFragment)
                .addToBackStack(null).commit();
	}

	@Override
	public void onPageLoadingClickListener(List<PhotoInfo> list) {
		PhotoSingleFragment photoSingleFragment = new PhotoSingleFragment();
		Bundle args = new Bundle();
		PhotoSerialzable photoSerializable = new PhotoSerialzable();
        photoSerializable.setPhotoInfoList(list);
        //将该文件夹下所有图片信息传递
        args.putSerializable("list", photoSerializable);
        photoSingleFragment.setArguments(args);
        //隐藏图片文件夹
        manager.beginTransaction().hide(photoFolderFragment).commit();
        //显示图片
        manager.beginTransaction().add(R.id.body, photoSingleFragment)
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
        actionBar.setTitle("请选择图片");
        backInt++;
	}

    @Override
    public void onPhotoSelectClickListener(String pathFile) {
        Intent intent = new Intent();
        intent.putExtra("pathFile", pathFile);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && backInt == 0) {
            finish();
        } else if (keyCode == KeyEvent.KEYCODE_BACK && backInt == 1) {
            backInt--;
            manager.beginTransaction()
                    .show(photoFolderFragment).commit();
            manager.popBackStack(0, 0);
            actionBar.setTitle("请选择相册");
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (backInt == 0) {
                finish();
            } else if (backInt == 1) {
                backInt--;
                manager.beginTransaction()
                        .show(photoFolderFragment).commit();
                manager.popBackStack(0, 0);
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
