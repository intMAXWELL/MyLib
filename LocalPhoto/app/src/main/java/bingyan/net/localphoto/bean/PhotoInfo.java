package bingyan.net.localphoto.bean;

import java.io.Serializable;

/**
 * Created by Demon on 2015/7/17.
 * 本地相册,记录本地图片信息
 */
public class PhotoInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 图片id*/
    private int imageId;

    /** 图片路径 格式为"file:"+ path */
    private String pathFile;

    /** 图片绝对路径 */
    private String pathAbsolute;

    /** 图片是否被选中 */
    private boolean isChosen = false;

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

    public boolean isChosen() {
        return isChosen;
    }

    public void setIsChosen(boolean isChosen) {
        this.isChosen = isChosen;
    }
}
