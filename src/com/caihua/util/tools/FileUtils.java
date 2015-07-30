package com.caihua.util.tools;

import java.io.File;

public class FileUtils {
	
	/**
	 * 判断文件目录是否存在，不存在则创建文件目录
	 * @param strFolder
	 * @return true:文件目录存在或者创建成功  false:文件目录创建失败
	 */
	public static boolean isFolderExists(String strFolder) {
        File file = new File(strFolder);        
        if (!file.exists()) {
            if (file.mkdirs()) {                
                return true;
            } else {
                return false;
            }
        }
        return true;
	}
	
}
