package com.chunjae.nest.domain.user.service;

import com.chunjae.nest.domain.paper.entity.Paper;
import com.chunjae.nest.domain.paper.repository.PaperRepository;
import com.chunjae.nest.domain.user.dto.AssignmentDTO;
import com.chunjae.nest.domain.user.dto.AssignmentSearchReqDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAssignmentService {
    private final PaperRepository paperRepository;

    public Page<Paper> searchResults(AssignmentSearchReqDTO assignSearchDTO, Pageable pageable) {
        return paperRepository.searchByWhere(assignSearchDTO, pageable);
    }
}
