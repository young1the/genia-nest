package com.chunjae.nest.domain.paper.dto;

import com.chunjae.nest.common.excel.ExcelColumn;
import com.chunjae.nest.domain.paper.entity.PaperStatus;
import com.chunjae.nest.domain.user.entity.User;
import lombok.*;

import java.time.LocalDateTime;


@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaperExcelDTO {
    @ExcelColumn(headerName = "ID")
    private Long id;
    @ExcelColumn(headerName = "출제년도")
    private int year;
    @ExcelColumn(headerName = "월")
    private int month;
    @ExcelColumn(headerName = "시험종류")
    private String category;
    @ExcelColumn(headerName = "학년")
    private int grade;
    @ExcelColumn(headerName = "영역")
    private String area;
    @ExcelColumn(headerName = "과목")
    private String subject;
    @ExcelColumn(headerName = "시험지명")
    private String name;
    @ExcelColumn(headerName = "등록자")
    private String userId;
    @ExcelColumn(headerName = "등록일")
    private LocalDateTime createdAt;
    @ExcelColumn(headerName = "업로드상태")
    private PaperStatus paperStatus;
}
