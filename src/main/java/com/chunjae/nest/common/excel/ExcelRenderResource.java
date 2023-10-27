package com.chunjae.nest.common.excel;

import java.lang.reflect.Field;
import java.util.*;

public class ExcelRenderResource {
    private final PriorityQueue<ExcelColumnInfo> infos;

    public ExcelRenderResource(Class<?> clazz) {
        infos = new PriorityQueue<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            ExcelColumn excelColumn = field.getAnnotation(ExcelColumn.class);
            if (excelColumn == null) continue;
            ExcelColumnInfo excelColumnInfo = new ExcelColumnInfo(field, excelColumn);
            infos.add(excelColumnInfo);
        }
    }

    public List<String> getHeaderNames() {
        List<String> headerNames = new LinkedList<>();
        for (ExcelColumnInfo info: infos) {
            headerNames.add(info.getHeaderName());
        }
        return headerNames;
    }

    public List<String> getFieldNames() {
        List<String> fieldNames = new LinkedList<>();
        for (ExcelColumnInfo info: infos) {
            fieldNames.add(info.getFieldName());
        }
        return fieldNames;
    }

}
