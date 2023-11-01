package com.chunjae.nest.domain.question.dto.res;

import com.chunjae.nest.domain.question.entity.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionResponse {

    private Long id;
    private int num;
    private QuestionType type;
    private String content;
    private String url;

}
