package com.example.mywebshop.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.tuple.Pair;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Util {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final ThreadLocal<StringBuilder> stringBuilderLocal = ThreadLocal.withInitial(StringBuilder::new);

    public static String concat(String... strings) {
        StringBuilder builder = stringBuilderLocal.get();
        builder.setLength(0);
        for (int i = 0; i < strings.length; i++) {
            builder.append(strings[i]);
        }
        return builder.toString();
    }

    @SafeVarargs
    public static String mapToJson(Pair<String, Object>... pairs) {
        if (pairs.length == 0) return "";
        try {
            Map<String, Object> map = new HashMap<>();
            for (Pair<String, Object> pair : pairs) {
                map.put(pair.getKey(), pair.getValue());
            }
            return mapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }

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
