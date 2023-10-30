package com.chunjae.nest.domain.user.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.time.LocalDateTime;

@Getter
@Setter
@Data
public class AssignmentSearchReqDTO {
    private Date startDate;
    private Date endDate;
    private String searchOption;
    private String searchKeyword;
}
