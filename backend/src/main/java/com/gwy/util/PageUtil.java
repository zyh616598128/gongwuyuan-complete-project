package com.gwy.util;

import org.springframework.data.domain.Page;

import java.util.HashMap;
import java.util.Map;

public class PageUtil {
    
    public static <T> Map<String, Object> toPageResult(Page<T> page) {
        Map<String, Object> result = new HashMap<>();
        result.put("content", page.getContent());
        result.put("page", page.getNumber());
        result.put("size", page.getSize());
        result.put("totalElements", page.getTotalElements());
        result.put("totalPages", page.getTotalPages());
        result.put("first", page.isFirst());
        result.put("last", page.isLast());
        return result;
    }
}