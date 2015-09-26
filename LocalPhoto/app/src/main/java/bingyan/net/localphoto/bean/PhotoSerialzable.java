package bingyan.net.localphoto.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Demon on 2015/7/17.
 *
 */
public class PhotoSerialzable implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 存储相片信息 */
    private List<PhotoInfo> photoInfoList;

    public PhotoSerialzable(List<PhotoInfo> photoInfoList) {
        this.photoInfoList = photoInfoList;
    }

    public PhotoSerialzable(){}

    public List<PhotoInfo> getPhotoInfoList() {
        return photoInfoList;
    }

    public void setPhotoInfoList(List<PhotoInfo> photoInfoList) {
        this.photoInfoList = photoInfoList;
    }
}
