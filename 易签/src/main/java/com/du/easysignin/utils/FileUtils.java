package com.du.easysignin.utils;

import android.os.Environment;

import java.io.File;
import java.util.ArrayList;

/**
 * 文件工具类
 * 作者：DragonDu
 * Email: 807110586@qq.com
 */
public class FileUtils {


    /**
     * SD卡下的目录获取及创建
     *
     * @param path
     * @return
     */
    public static File getfilepath(String path) {

        File file = new File(Environment.getExternalStorageDirectory() + path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }


    /**
     * 获取文件夹路径名
     */

    public static String[] getFileDirName(File file) {

        String[] list = file.list();
        return list;
    }
    /**
     * 获取文件夹路径名
     */

    public static File[] getFileName(File file) {

        File[] list = file.listFiles();
        return list;
    }


}
