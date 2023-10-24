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

    public Paper createNewPaper(User user) {

        return Paper.builder()
                .user(user)
                .year(year)
                .month(month)
                .grade(grade)
                .name(name)
                .totalCount(totalCount)
                .category(category)
                .area(area)
                .subject(subject)
                .ocrCount(0)
                .paperStatus(PaperStatus.TO_DO)
                .build();
    }
}
