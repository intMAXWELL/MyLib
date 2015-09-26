package bingyan.net.localphoto;

import java.util.HashMap;

/**
 * Created by Demon on 2015/7/17.
 * 保存缩略图据对路径
 */
public class ThumbnailsUtil {

    private static HashMap<Integer, String> hashMap = new HashMap<>();

    /** 根据图片id获取路径 */
    public static String get(int key,String def){
        if(null ==hashMap || !hashMap.containsKey(key)) return def;
        return hashMap.get(key);
    }

    /** 添加value */
    public static void put(Integer key,String value) {
        hashMap.put(key, value);
    }

    /** 清空hashMap */
    public static void clear(){
        hashMap.clear();
    }
}
