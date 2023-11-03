package com.chunjae.nest.domain.user.dto;

import com.chunjae.nest.common.excel.ExcelColumn;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ManagerDTO {
    private Long id;
    @ExcelColumn(headerName = "아이디")
    private String userId;
    @ExcelColumn(headerName = "이름")
    private String name;
    @ExcelColumn(headerName = "부서")
    private String part;
    @ExcelColumn(headerName = "시작")
    private String startDate;
    @ExcelColumn(headerName = "종료")
    private String endDate;
}
