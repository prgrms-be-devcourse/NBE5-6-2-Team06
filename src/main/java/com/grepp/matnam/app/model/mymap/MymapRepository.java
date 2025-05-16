package com.grepp.matnam.app.model.mymap;

import com.grepp.matnam.app.model.mymap.entity.Mymap;
import com.grepp.matnam.app.model.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MymapRepository extends JpaRepository<Mymap, Long> {

    // pinned = true
    List<Mymap> findByUserAndPinnedTrue(User user);

    // activated = true
    List<Mymap> findByUserAndActivatedTrue(User user);

    // 팀페이지용 공개, 활성화 핀
    List<Mymap> findByUserAndActivatedTrueAndPinnedTrue(User user);
}
