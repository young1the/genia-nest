package com.chunjae.nest.domain.question.dto.res;

import com.chunjae.nest.domain.paper.entity.Paper;
import com.chunjae.nest.domain.question.entity.Question;
import com.chunjae.nest.domain.question.entity.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionResponse {

    private int num;
    private QuestionType type;
    private String content;
    private String url;

}
