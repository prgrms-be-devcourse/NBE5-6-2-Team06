package com.grepp.matnam.app.model.restaurant;

import com.grepp.matnam.app.model.restaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
