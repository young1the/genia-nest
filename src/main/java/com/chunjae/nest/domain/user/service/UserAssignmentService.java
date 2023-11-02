package com.chunjae.nest.domain.user.service;

import com.chunjae.nest.domain.paper.entity.Paper;
import com.chunjae.nest.domain.paper.entity.PaperAssignment;
import com.chunjae.nest.domain.paper.repository.PaperRepository;
import com.chunjae.nest.domain.user.dto.AssignmentDTO;
import com.chunjae.nest.domain.user.dto.AssignmentSearchReqDTO;
import com.chunjae.nest.domain.user.dto.ManagerDTO;
import com.chunjae.nest.domain.user.dto.ManagerSearchDTO;
import com.chunjae.nest.domain.user.entity.User;
import com.chunjae.nest.domain.user.repository.UserAssignmentRepository;
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
    private final UserAssignmentRepository userAssignmentRepository;

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

    public Page<ManagerDTO> searchQuestionManagers(ManagerSearchDTO searchDTO, Pageable pageable) {
        Page<User> users = userAssignmentRepository.getQuestionManagerByOption(searchDTO, pageable);
        if (users == null) {return null;}
        return users.map(ele-> {
            String startDate = ele.getRole().getStartDate().toString();
            String endDate = ele.getRole().getEndDate().toString();
            return ManagerDTO.builder()
                    .id(ele.getId())
                    .userId(ele.getUserId())
                    .name(ele.getName())
                    .part(ele.getPart())
                    .startDate(startDate)
                    .endDate(endDate)
                    .build();
        });
    }
}
