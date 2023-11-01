package com.chunjae.nest.common.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.List;

public class ExcelFile<T> {
    private static final int ROW_START_INDEX = 0;
    private static final int COLUMN_START_INDEX = 0;
    private static final SpreadsheetVersion excelVersion = SpreadsheetVersion.EXCEL2007;
    private final Workbook workbook;
    private Sheet sheet;
    private int currentRowIndex = ROW_START_INDEX;
    ExcelRenderResource renderResource;

    public ExcelFile(List<T> data, Class<T> clazz) throws ExcelFileException {
        validateData(data);
        renderResource = new ExcelRenderResource(clazz);
        this.workbook = new SXSSFWorkbook();
        renderExcel(data, clazz);
    }

    private void validateData(List<T> data) throws ExcelFileException {
        int maxRows = excelVersion.getMaxRows();
        if (data.size() > maxRows) {
            throw new ExcelFileException(String.format("This concrete ExcelFile does not support over %s rows", maxRows));
        }
    }

    private void renderExcel(List<T> data, Class<T> clazz) throws ExcelFileException {
        sheet = workbook.createSheet();
        renderHeader();
        if (data.isEmpty()) return;
        renderBody(data, clazz);
    }

    private void renderHeader() {
        Row row = sheet.createRow(currentRowIndex++);
        int columnIndex = COLUMN_START_INDEX;
        for (String headerName : renderResource.getHeaderNames()) {
            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setBorderTop(BorderStyle.THIN); //테두리 위쪽
            cellStyle.setBorderBottom(BorderStyle.THIN); //테두리 아래쪽
            cellStyle.setBorderLeft(BorderStyle.THIN); //테두리 왼쪽
            cellStyle.setBorderRight(BorderStyle.THIN); //테두리 오른쪽
            cellStyle.setFillForegroundColor(IndexedColors.BRIGHT_GREEN.getIndex());
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);	//채우기 적용
            Cell cell = row.createCell(columnIndex++);
            cell.setCellValue(headerName);
            cell.setCellStyle(cellStyle);
        }
    }

    private void renderBody(List<T> data, Class<T> clazz) throws ExcelFileException {
        for (T element : data) {
            renderRow(element, clazz);
        }
    }

    private void renderRow(T element, Class<T> clazz) throws ExcelFileException {
        Row row = sheet.createRow(currentRowIndex++);
        int columnIndex = COLUMN_START_INDEX;
        for (String fieldName : renderResource.getFieldNames()) {
            try {
                Cell cell = row.createCell(columnIndex++);
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                Object cellValue = field.get(element);
                renderCell(cell, cellValue);
            } catch (Exception e) {
                throw new ExcelFileException(e.getMessage());
            }
        }
    }

    private void renderCell(Cell cell, Object cellValue) {
        if (cellValue instanceof Number number) {
            cell.setCellValue(number.doubleValue());
            return;
        }
        cell.setCellValue(cellValue == null ? "" : cellValue.toString());
    }

    public void write(OutputStream stream) throws IOException {
        workbook.write(stream);
        workbook.close();
    }

}