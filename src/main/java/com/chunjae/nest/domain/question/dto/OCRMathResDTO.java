package com.chunjae.nest.domain.question.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OCRMathResDTO {
    private String request_id;
    private String text;
}
