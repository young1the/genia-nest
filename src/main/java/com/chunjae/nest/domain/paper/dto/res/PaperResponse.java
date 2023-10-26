package com.chunjae.nest.domain.paper.dto.res;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaperResponse {

    private int year;
    private int month;
    private int grade;
    private String name;
    private int totalCount;
    private String category;
    private String area;
    private String subject;
    private String url;

}
