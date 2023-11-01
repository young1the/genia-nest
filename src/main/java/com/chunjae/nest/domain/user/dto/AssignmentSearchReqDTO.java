package com.chunjae.nest.domain.user.dto;

import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Data
public class AssignmentSearchReqDTO {
    private String startDate;
    private String endDate;
    private String searchOption;
    private String searchKeyword;
}
