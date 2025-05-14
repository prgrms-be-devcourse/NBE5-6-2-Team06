package com.grepp.matnam.app.controller.web.admin;

import com.grepp.matnam.app.model.team.TeamService;
import com.grepp.matnam.app.model.team.code.Status;
import com.grepp.matnam.app.model.team.entity.Team;
import com.grepp.matnam.infra.error.exceptions.CommonException;
import com.grepp.matnam.infra.payload.PageParam;
import com.grepp.matnam.infra.response.PageResponse;
import com.grepp.matnam.infra.response.ResponseCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
@RequestMapping("/admin/team")
@RequiredArgsConstructor
public class AdminTeamController {

    private final TeamService teamService;

    @GetMapping
    public String teamManagement(@RequestParam(required = false) Status status,
        @RequestParam(required = false, defaultValue = "") String keyword,
        @RequestParam(required = false, defaultValue = "newest") String sort,
        @Valid PageParam param, BindingResult bindingResult, Model model) {

        Sort sortOption;
        switch (sort) {
            case "oldest":
                sortOption = Sort.by(Sort.Order.asc("createdAt")); // 오래된순
                break;
            default:
                sortOption = Sort.by(Sort.Order.desc("createdAt")); // 최신순
        }

        Pageable pageable = PageRequest.of(param.getPage() - 1, param.getSize(), sortOption);
        String statusName = "";
        if (status != null) {
            statusName = status.name();
        }
        Page<Team> page = teamService.findByFilter(statusName, keyword, pageable);

        if (param.getPage() != 1 && page.getContent().isEmpty()) {
            throw new CommonException(ResponseCode.BAD_REQUEST);
        }
        PageResponse<Team> response = new PageResponse<>("/admin/team?status=" + statusName + "&keyword=" + keyword + "&sort=" + sort, page, 5);

        model.addAttribute("pageTitle", "모임 관리");
        model.addAttribute("currentPage", "team-management");
        model.addAttribute("page", response);
        model.addAttribute("status", statusName);
        model.addAttribute("sort", sort);

        return "admin/team-management";
    }

}