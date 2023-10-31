package com.chunjae.nest.domain.user.dto;

import com.chunjae.nest.common.excel.ExcelColumn;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
public class AssignmentDTO {
    @ExcelColumn(headerName = "ID")
    private Long id;
    @ExcelColumn(headerName = "출제년도")
    private int year;
    @ExcelColumn(headerName = "출제타입")
    private String category;
    @ExcelColumn(headerName = "월")
    private int month;
    @ExcelColumn(headerName = "학년")
    private int grade;
    @ExcelColumn(headerName = "영역")
    private String area;
    @ExcelColumn(headerName = "과목")
    private String subject;
    @ExcelColumn(headerName = "시험지명")
    private String name;
    @ExcelColumn(headerName = "OCR 현황 수")
    private int ocrCount;
    @ExcelColumn(headerName = "문항수")
    private int totalCount;
    @ExcelColumn(headerName = "등록자")
    private String uploader;
    @ExcelColumn(headerName = "등록일")
    private LocalDateTime createdAt;
    @ExcelColumn(headerName = "담당자")
    private String manager;

}
