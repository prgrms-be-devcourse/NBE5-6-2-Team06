package com.grepp.matnam.app.model.notification.repository;

import com.grepp.matnam.app.model.notification.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long>, NoticeRepositoryCustom {

}
