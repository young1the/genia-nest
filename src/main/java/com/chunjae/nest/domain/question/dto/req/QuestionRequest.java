package com.chunjae.nest.domain.question.dto.req;

import com.chunjae.nest.domain.paper.entity.Paper;
import com.chunjae.nest.domain.question.entity.Question;
import com.chunjae.nest.domain.question.entity.QuestionStatus;
import com.chunjae.nest.domain.question.entity.QuestionType;
import com.chunjae.nest.domain.user.entity.User;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
public class QuestionRequest {

    private int num;
    private QuestionType type;
    private Paper paper;
    private MultipartFile multipartFile;
    private String numExpression;

    public Question createQuestion(User user) {
        return Question.builder()
                .num(num)
                .type(type)
                .paper(paper)
                .user(user)
                .questionStatus(QuestionStatus.BEFORE)
                .build();
    }
}
