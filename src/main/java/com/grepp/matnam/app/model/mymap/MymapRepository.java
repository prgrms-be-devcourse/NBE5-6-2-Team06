package com.grepp.matnam.app.model.mymap;

import com.grepp.matnam.app.model.mymap.entity.Mymap;
import com.grepp.matnam.app.model.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MymapRepository extends JpaRepository<Mymap, Long> {

    // 현재 로그인한 유저가 고정한 맛집 리스트
    List<Mymap> findByUserAndPinnedTrue(User user);

}