package com.chunjae.nest.domain.user.dto;

import lombok.*;
import java.sql.Date;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserReqDTO {

    private String part;
    private String name;
    private String userId;
    private Date startDate;
    private Date endDate;
    private String role;
    public Long id;

}
