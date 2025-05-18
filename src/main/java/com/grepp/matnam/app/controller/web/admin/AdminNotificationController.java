package com.grepp.matnam.app.controller.web.admin;

import com.grepp.matnam.app.model.notification.entity.Notice;
import com.grepp.matnam.app.model.notification.service.NotificationService;
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
@RequestMapping("/admin/notification")
@RequiredArgsConstructor
public class AdminNotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public String notification(@RequestParam(required = false, defaultValue = "") String keyword,
        @RequestParam(required = false, defaultValue = "newest") String sort,
        @Valid PageParam param, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            throw new CommonException(ResponseCode.BAD_REQUEST);
        }

        Sort sortOption;
        switch (sort) {
            case "oldest":
                sortOption = Sort.by(Sort.Order.asc("createdAt")); // 오래된순
                break;
            default:
                sortOption = Sort.by(Sort.Order.desc("createdAt")); // 최신순
        }

        Pageable pageable = PageRequest.of(param.getPage() - 1, param.getSize(), sortOption);

        Page<Notice> page = notificationService.findByFilter(keyword, pageable);

        if (param.getPage() != 1 && page.getContent().isEmpty()) {
            throw new CommonException(ResponseCode.BAD_REQUEST);
        }
        PageResponse<Notice> response = new PageResponse<>("/admin/notification?&sort=" + sort + "&keyword=" + keyword, page, 5);


        model.addAttribute("pageTitle", "알림 관리");
        model.addAttribute("currentPage", "notification");
        model.addAttribute("page", response);
        model.addAttribute("sort", sort);

        return "admin/notification";
    }

}