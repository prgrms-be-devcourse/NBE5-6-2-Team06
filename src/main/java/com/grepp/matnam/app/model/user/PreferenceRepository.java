package com.grepp.matnam.app.model.user;

import com.grepp.matnam.app.model.user.entity.Preference;
import com.grepp.matnam.app.model.user.entity.User;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PreferenceRepository extends JpaRepository<Preference, Long> {

    Preference findByUser(User user);

    @Query(value = "SELECT " +
        "SUM(CASE p.bigStore WHEN true THEN 1 ELSE 0 END) AS bigStoreCount, " +
        "SUM(CASE p.clean WHEN true THEN 1 ELSE 0 END) AS cleanCount, " +
        "SUM(CASE p.goodMenu WHEN true THEN 1 ELSE 0 END) AS goodMenuCount, " +
        "SUM(CASE p.goodMusic WHEN true THEN 1 ELSE 0 END) AS goodMusicCount, " +
        "SUM(CASE p.goodPicture WHEN true THEN 1 ELSE 0 END) AS goodPictureCount, " +
        "SUM(CASE p.goodTalk WHEN true THEN 1 ELSE 0 END) AS goodTalkCount, " +
        "SUM(CASE p.goodView WHEN true THEN 1 ELSE 0 END) AS goodViewCount, " +
        "SUM(CASE p.isTerrace WHEN true THEN 1 ELSE 0 END) AS isTerraceCount, " +
        "SUM(CASE p.longStay WHEN true THEN 1 ELSE 0 END) AS longStayCount, " +
        "SUM(CASE p.manyDrink WHEN true THEN 1 ELSE 0 END) AS manyDrinkCount " +
        "FROM Preference p")
    Map<String, Long> getPreferenceCounts();
}
