package com.jumia.test.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FilterUtils {

    private final static ObjectMapper mapper = new ObjectMapper();

    public static void writeErrorMap(Map<String,Object> errorMap, HttpServletResponse response, int httpStatus) throws IOException {
        String content = mapper.writeValueAsString(errorMap);
        response.setStatus(httpStatus);
        response.setContentType("application/json");
        response.getWriter().write(content);
    }

    public static void doErrorResponse(HttpServletResponse httpServletResponse, String message, int scUnauthorized) throws IOException {
        Map<String, Object> map = ErrorUtils.getErrorMap(message);
        writeErrorMap(map, httpServletResponse, scUnauthorized);
    }
}
