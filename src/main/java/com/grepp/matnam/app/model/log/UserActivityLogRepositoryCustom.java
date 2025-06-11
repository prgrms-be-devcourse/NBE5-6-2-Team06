package com.grepp.matnam.app.model.log;

import com.grepp.matnam.app.controller.api.admin.payload.StatLongResponse;
import java.time.LocalDate;
import java.util.List;

public interface UserActivityLogRepositoryCustom {

    List<StatLongResponse> findMonthlyUserActivity(LocalDate startDate);
    List<StatLongResponse> findWeekUserActivity(LocalDate startDate);
}
