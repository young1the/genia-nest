package com.chunjae.nest.domain.question.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OCRTextAPIResDTO {

    private String version;
    private String requestId;
    private long timestamp;
    private List<Image> images;

    @Getter
    @Setter
    public static class Image {
        private String uid;
        private String name;
        private String inferResult;
        private String message;
        private List<Field> fields;
        private ValidationResult validationResult;

        @Getter
        @Setter
        public static class Field {
            private String inferText;
            private double inferConfidence;
        }
    }

    @Getter
    @Setter
    public static class ValidationResult {
        private String result;

    }

}
