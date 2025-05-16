package com.grepp.matnam.app.model.restaurant;

import com.grepp.matnam.app.model.restaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long>, RestaurantRepositoryCustom {

    long countByCategory(String category);
    long countByGoodTalk(boolean goodTalk);
    long countByManyDrink(boolean manyDrink);
    long countByGoodMusic(boolean goodMusic);
    long countByClean(boolean clean);
    long countByGoodView(boolean goodView);
    long countByIsTerrace(boolean isTerrace);
    long countByGoodPicture(boolean goodPicture);
    long countByGoodMenu(boolean goodMenu);
    long countByLongStay(boolean longStay);
    long countByBigStore(boolean bigStore);

    @Query("SELECT AVG(r.googleRating) FROM Restaurant r")
    Double averageGoogleRating();

    @Query("SELECT SUM(r.recommendedCount) FROM Restaurant r")
    long sumRecommendedCount();

    @Query("SELECT AVG(r.googleRating) FROM Restaurant r WHERE r.category = :category")
    Double averageGoogleRatingByCategory(@Param("category") String category);

    @Query("SELECT SUM(r.recommendedCount) FROM Restaurant r WHERE r.category = :category")
    Long sumRecommendedCountByCategory(@Param("category") String category);
}
