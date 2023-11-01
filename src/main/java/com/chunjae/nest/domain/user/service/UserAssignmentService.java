package com.chunjae.nest.domain.user.service;

import com.chunjae.nest.domain.paper.entity.Paper;
import com.chunjae.nest.domain.paper.entity.PaperAssignment;
import com.chunjae.nest.domain.paper.repository.PaperRepository;
import com.chunjae.nest.domain.user.dto.AssignmentDTO;
import com.chunjae.nest.domain.user.dto.AssignmentSearchReqDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAssignmentService {
    private final PaperRepository paperRepository;

    public Page<AssignmentDTO> searchResults(AssignmentSearchReqDTO assignSearchDTO, Pageable pageable) {
        Page<Paper> papers = paperRepository.searchByWhere(assignSearchDTO, pageable);
        if (papers == null) {
            return null;
        }
        return papers.map(ele -> {
            String manager = null;
            List<PaperAssignment> paperAssignments = ele.getPaperAssignments();
            if (paperAssignments != null && !paperAssignments.isEmpty()) {
                manager = paperAssignments.get(0).getUser().getName();
            }
            return AssignmentDTO
                    .builder()
                    .id(ele.getId())
                    .year(ele.getYear())
                    .category(ele.getCategory())
                    .month(ele.getMonth())
                    .grade(ele.getGrade())
                    .area(ele.getArea())
                    .subject(ele.getSubject())
                    .name(ele.getName())
                    .ocrCount(ele.getOcrCount())
                    .uploader(ele.getUser().getName())
                    .totalCount(ele.getTotalCount())
                    .manager(manager)
                    .createdAt(ele.getCreatedAt())
                    .build();
        });
    }
}
