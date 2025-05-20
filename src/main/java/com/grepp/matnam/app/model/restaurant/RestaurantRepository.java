package com.grepp.matnam.app.model.restaurant;

import com.grepp.matnam.app.model.restaurant.entity.Restaurant;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long>, RestaurantRepositoryCustom {
    Optional<Restaurant> findByName(String name);

    @Query("SELECT AVG(r.googleRating) FROM Restaurant r where r.activated = true")
    Double averageGoogleRating();

    @Query("SELECT SUM(r.recommendedCount) FROM Restaurant r where r.activated = true ")
    long sumRecommendedCount();

    @Query("SELECT AVG(r.googleRating) FROM Restaurant r WHERE r.category = :category and r.activated = true ")
    Double averageGoogleRatingByCategory(@Param("category") String category);

    @Query("SELECT SUM(r.recommendedCount) FROM Restaurant r WHERE r.category = :category and r.activated = true ")
    Long sumRecommendedCountByCategory(@Param("category") String category);

    long countByCategoryAndActivatedTrue(String category);

    Page<Restaurant> findAllByActivatedTrue(Pageable pageable);

    long countByActivatedTrue();

    Long countByGoodTalkAndActivatedTrue(boolean goodTalk);
    long countByManyDrinkAndActivatedTrue(boolean manyDrink);
    long countByGoodMusicAndActivatedTrue(boolean goodMusic);
    long countByCleanAndActivatedTrue(boolean clean);
    long countByGoodViewAndActivatedTrue(boolean goodView);
    long countByIsTerraceAndActivatedTrue(boolean isTerrace);
    long countByGoodPictureAndActivatedTrue(boolean goodPicture);
    long countByGoodMenuAndActivatedTrue(boolean goodMenu);
    long countByLongStayAndActivatedTrue(boolean longStay);
    long countByBigStoreAndActivatedTrue(boolean bigStore);
}
