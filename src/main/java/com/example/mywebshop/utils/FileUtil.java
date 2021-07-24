package com.example.mywebshop.utils;

import java.nio.file.Paths;

public class FileUtil {

    public static String getFileFormat(String fileName) {
        if (fileName == null) return null;
        fileName = getFileNameFromPath(fileName);
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex <= 0)
            return "";
        return fileName.substring(dotIndex + 1);
    }

    public static String getFileNameFromPath(String path) {
        return Paths.get(path)
                .getFileName()
                .toString();
    }
}
