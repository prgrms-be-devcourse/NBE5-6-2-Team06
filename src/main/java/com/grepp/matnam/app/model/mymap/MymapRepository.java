package com.grepp.matnam.app.model.mymap;

import com.grepp.matnam.app.model.mymap.entity.Mymap;
import com.grepp.matnam.app.model.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MymapRepository extends JpaRepository<Mymap, Long> {

    List<Mymap> findByUserAndPinnedTrue(User user);

    List<Mymap> findByUserAndActivatedTrue(User user);

    List<Mymap> findByUserAndActivatedTrueAndPinnedTrue(User user);

    long countByUserAndActivatedTrue(User user);

    long countByUserAndActivatedTrueAndPinnedTrue(User user);

    long countByUserAndActivatedTrueAndPinnedFalse(User user);
}
