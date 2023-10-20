package com.chunjae.nest.domain.paper.dto.req;

import com.chunjae.nest.domain.paper.entity.Paper;
import com.chunjae.nest.domain.paper.entity.PaperStatus;
import com.chunjae.nest.domain.user.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaperRequest {

    private int year;
    private int month;
    private int grade;
    private String name;
    private int totalCount;
    private String category;
    private String area;
    private String subject;

    public Paper toEntity(PaperRequest paperRequest, User user) {

        return Paper.builder()
                .user(user)
                .year(paperRequest.getYear())
                .month(paperRequest.getMonth())
                .grade(paperRequest.getGrade())
                .name(paperRequest.getName())
                .totalCount(paperRequest.getTotalCount())
                .category(paperRequest.getCategory())
                .area(paperRequest.getArea())
                .subject(paperRequest.getSubject())
                .ocrCount(0)
                .paperStatus(PaperStatus.TO_DO)
                .build();
    }
}
