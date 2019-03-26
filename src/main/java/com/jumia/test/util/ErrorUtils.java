package com.jumia.test.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public final class ErrorUtils {
    
    private ErrorUtils() {} // prevents this class from being initialized
    
    public static Map<String,Object> getErrorMap(String message) {
        Map<String, Object> map = new HashMap<>();
        map.put("timestamp",new Date().getTime());
        map.put("message", message);

        return map;
    }
}
