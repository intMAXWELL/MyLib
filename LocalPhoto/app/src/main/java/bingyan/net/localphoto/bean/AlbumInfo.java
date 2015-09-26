package bingyan.net.localphoto.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Demon on 2015/7/17.
 * 相册
 */
public class AlbumInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 图片id */
    private int imageId;

    /** 显示路径 */
    private String pathFile;

    /** 绝对路径 */
    private String pathAbsolute;

    /** 相册名称 */
    private String nameAlbum;

    /** 该相册下的所有相片信息 */
    private List<PhotoInfo> photoInfoList;

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getPathFile() {
        return pathFile;
    }

    public void setPathFile(String pathFile) {
        this.pathFile = pathFile;
    }

    public String getPathAbsolute() {
        return pathAbsolute;
    }

    public void setPathAbsolute(String pathAbsolute) {
        this.pathAbsolute = pathAbsolute;
    }

    public List<PhotoInfo> getPhotoInfoList() {
        return photoInfoList;
    }

    public void setPhotoInfoList(List<PhotoInfo> photoInfoList) {
        this.photoInfoList = photoInfoList;
    }

    public String getNameAlbum() {
        return nameAlbum;
    }

    public void setNameAlbum(String nameAlbum) {
        this.nameAlbum = nameAlbum;
    }
}
