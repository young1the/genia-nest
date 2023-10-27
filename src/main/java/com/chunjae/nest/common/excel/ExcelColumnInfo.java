package com.chunjae.nest.common.excel;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Field;

@AllArgsConstructor
@Getter
public class ExcelColumnInfo implements Comparable<ExcelColumnInfo> {
    private String fieldName;
    private String headerName;
    private int priority;

    public ExcelColumnInfo(Field field, ExcelColumn excelColumn) {
        this.fieldName = field.getName();
        this.headerName = excelColumn.headerName();
        this.priority = excelColumn.priority();
    }

    @Override
    public int compareTo(ExcelColumnInfo other) {
        return other.priority - this.priority;
    }
}
