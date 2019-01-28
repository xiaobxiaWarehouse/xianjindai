package com.vxianjin.gringotts.constant;

import java.util.ResourceBundle;

/**
 * 读取配置文件
 *
 * @author simalin
 */

/**
 * 类描述：从OSS签移到api中
 * 创建人：董玉凯
 * 创建时间：2017年9月22日
 */
@Deprecated
public class SpiderConstant {

    public static ResourceBundle resourceBundle;

    public static void initConfig() {
        resourceBundle = ResourceBundle.getBundle("spider");
    }

    public static String getConstant(String key) {
        if (resourceBundle == null)
            initConfig();
        return resourceBundle.getString(key);

    }

    public static String getConstant(String key, String defaultValue) {
        String value = null;
        try {
            if (resourceBundle == null)
                initConfig();
            value = resourceBundle.getString(key);
        } catch (Exception e) {
            return defaultValue;
        }
        return value;
    }

}
