package com.chunjae.nest.domain.paper.dto.req;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaperAssignmentRequest {

    private Long userId;
    private Long paperId;
}
