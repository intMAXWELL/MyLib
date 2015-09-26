package bingyan.net.localphoto.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import bingyan.net.localphoto.CheckImageLoaderConfiguration;
import bingyan.net.localphoto.MyApplication;
import bingyan.net.localphoto.R;
import bingyan.net.localphoto.bean.PhotoInfo;
import bingyan.net.localphoto.bean.PhotoSerialzable;
import bingyan.net.localphoto.fragment.PhotoFolderFragment;
import bingyan.net.localphoto.fragment.PhotoMultipleFragment;


/**
 * 图片多选
 * @author Demon
 */

public class SelectMultiplePhotoActivity extends ActionBarActivity implements PhotoFolderFragment.OnPageLoadingClickListener
        ,PhotoMultipleFragment.OnPhotoSelectClickListener {

    private ActionBar actionBar;
    private FragmentManager manager;
    private int backInt = 0;
    private PhotoFolderFragment photoFolderFragment;
    //选中图片集合
    private List<PhotoInfo> hasList;

    @Override
    protected void onStart() {
        super.onStart();
        CheckImageLoaderConfiguration.checkImageLoaderCfg(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectphoto);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("请选择相册");
        manager = getSupportFragmentManager();
        getWindowManager().getDefaultDisplay().getMetrics(MyApplication.getDisplayMetrics());
        hasList = new ArrayList<>();
        photoFolderFragment = new PhotoFolderFragment();
        manager.beginTransaction()
                .add(R.id.body, photoFolderFragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * 当图片被点击时调用
     * @see bingyan.net.localphoto.fragment.PhotoMultipleFragment
     * @param list 被选中的图片的所有信息
     */
    @Override
    public void onPhotoSelectClickListener(List<PhotoInfo> list) {
        hasList.clear();
        for (PhotoInfo photoInfoBean : list) {
            if(photoInfoBean.isChosen()){
                hasList.add(photoInfoBean);
            }
        }
        actionBar.setTitle("已选择" + hasList.size() + "张");
    }

    /**
     * 当相册文件夹被点击时调用
     * @see bingyan.net.localphoto.fragment.PhotoFolderFragment
     * @param list 该文件夹下所有图片的信息
     */
    @Override
    public void onPageLoadingClickListener(List<PhotoInfo> list) {
        actionBar.setTitle("已选择0张");
        PhotoMultipleFragment photoFragment = new PhotoMultipleFragment();
        //将文件夹下的图片信息通过bundle传递
        Bundle args = new Bundle();
        PhotoSerialzable photoSerializable = new PhotoSerialzable();
        for (PhotoInfo photoInfoBean : list) {
            photoInfoBean.setIsChosen(false);
        }
        photoSerializable.setPhotoInfoList(list);
        args.putSerializable("list", photoSerializable);
        photoFragment.setArguments(args);
        //
        manager.beginTransaction()
                .hide(photoFolderFragment).commit();
        manager.beginTransaction()
                .add(R.id.body, photoFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(null)
                .commit();
        backInt++;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_select_multiple_photo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            if (backInt == 0) {
                finish();
            }else if (backInt == 1) {
                hasList.clear();
                backInt--;
                actionBar.setTitle("请选择相册");
                manager.beginTransaction().show(photoFolderFragment).commit();
                manager.popBackStack();
            }
            return true;
        }
        else if (id == R.id.select_photo_complete) {
            if (hasList.size() > 0) {
                Intent intent = new Intent();
                intent.putExtra("pathFiles", new PhotoSerialzable(hasList));
                setResult(RESULT_OK, intent);
                finish();
            } else {
                Toast.makeText(SelectMultiplePhotoActivity.this,
                        "至少选择一张图片", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    //当返回键被按下的时候
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK&&backInt==0){
            finish();
        }else if(keyCode == KeyEvent.KEYCODE_BACK&&backInt==1){
            backInt--;
			hasList.clear();
            actionBar.setTitle("请选择相册");
            manager.beginTransaction()
                    .show(photoFolderFragment).commit();
            manager.popBackStack(0, 0);
        }
        return false;
    }
}
