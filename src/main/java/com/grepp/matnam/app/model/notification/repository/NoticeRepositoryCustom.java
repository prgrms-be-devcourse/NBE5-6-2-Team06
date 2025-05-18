package com.grepp.matnam.app.model.notification.repository;

import com.grepp.matnam.app.model.notification.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeRepositoryCustom {

    Page<Notice> findByKeywordContaining(String keyword, Pageable pageable);

    Page<Notice> findAllNotices(Pageable pageable);
}
