package com.vxianjin.gringotts.web.util.qiniu;

import java.io.File;


/**
 * 图片上传接口_七牛服务器
 *
 * @author wangyudong
 */
public interface UploadQn {


    /**
     * 图片上传
     */
    String uploadImage(File file, String key);

    /**
     * 图片下载
     */
    String downloadImage(String fileName);

    /**
     * 图片删除
     */
    String deleteImage(String key);

    String createKey();


}
