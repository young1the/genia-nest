package com.chunjae.nest.domain.paper.entity;

public enum PaperStatus {

    //TO_DO, IN_PROGRESS, DONE, DELETED // 변환 전, 변환 중, 변환 완료, 삭제상태
    TO_DO("변환 전"),
    IN_PROGRESS("변환 중"),
    DONE("변환 완료"),
    DELETED("삭제상태");

    private String displayValue;

    PaperStatus(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
