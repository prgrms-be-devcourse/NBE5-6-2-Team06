package com.grepp.matnam.app.model.user;

import com.grepp.matnam.app.model.user.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long>, ReportRepositoryCustom {

}
