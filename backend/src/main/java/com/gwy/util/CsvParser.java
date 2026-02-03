package com.gwy.util;

import java.util.ArrayList;
import java.util.List;

/**
 * CSV解析工具类
 */
public class CsvParser {
    
    /**
     * 解析CSV行，正确处理包含逗号的字段
     */
    public static String[] parseLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder currentField = new StringBuilder();
        boolean inQuotes = false;
        char[] chars = line.toCharArray();
        
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            
            if (c == '"') {
                // 检查是否是转义的引号（""）
                if (i + 1 < chars.length && chars[i + 1] == '"') {
                    currentField.append('"');
                    i++; // 跳过下一个引号
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                fields.add(currentField.toString());
                currentField.setLength(0); // 清空StringBuilder
            } else {
                currentField.append(c);
            }
        }
        
        // 添加最后一个字段
        fields.add(currentField.toString());
        
        return fields.toArray(new String[0]);
    }
}