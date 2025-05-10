package com.grepp.matnam.app.controller.web.team;

import com.grepp.matnam.app.model.team.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

}
