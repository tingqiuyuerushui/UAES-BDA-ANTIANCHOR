package uaes.bda.antianchor.demo.utils;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/3/1.
 */
public class HashMapUtil {


    private static HashMap<String, String> hashMap;
    private static HashMap<String, Integer> hashMap_int;

    public static void saveString(String key, String value) {
        if (hashMap == null) {
            hashMap = new HashMap();
        }
        hashMap.put(key, value);

    }

    public static void saveInt(String key, Integer value) {
        if (hashMap_int == null) {
            hashMap_int = new HashMap();
        }
        hashMap_int.put(key, value);
    }

    public static void clear() {
        hashMap.clear();
    }

    public static String getString(String key) {
        if (hashMap == null) {
            hashMap = new HashMap();
        }
        //  String o = hashMap.get(key);
        return hashMap.get(key);
    }

    public static Integer getInt(String key) {
        if (hashMap_int == null) {
            hashMap_int = new HashMap();
        }
        //  String o = hashMap.get(key);
        return hashMap_int.get(key);
    }

}
