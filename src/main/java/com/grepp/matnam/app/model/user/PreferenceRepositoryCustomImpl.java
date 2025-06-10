package com.grepp.matnam.app.model.user;

import com.grepp.matnam.app.model.user.entity.QPreference;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Collections;
import java.util.Map;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PreferenceRepositoryCustomImpl implements PreferenceRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QPreference preference = QPreference.preference;


    @Override
    public Map<String, Long> getPreferenceCounts() {

        NumberExpression<Long> bigStoreCount = new CaseBuilder().when(preference.bigStore.isTrue()).then(1L).otherwise(0L).sum();
        NumberExpression<Long> cleanCount = new CaseBuilder().when(preference.clean.isTrue()).then(1L).otherwise(0L).sum();
        NumberExpression<Long> goodMenuCount = new CaseBuilder().when(preference.goodMenu.isTrue()).then(1L).otherwise(0L).sum();
        NumberExpression<Long> goodMusicCount = new CaseBuilder().when(preference.goodMusic.isTrue()).then(1L).otherwise(0L).sum();
        NumberExpression<Long> goodPictureCount = new CaseBuilder().when(preference.goodPicture.isTrue()).then(1L).otherwise(0L).sum();
        NumberExpression<Long> goodTalkCount = new CaseBuilder().when(preference.goodTalk.isTrue()).then(1L).otherwise(0L).sum();
        NumberExpression<Long> goodViewCount = new CaseBuilder().when(preference.goodView.isTrue()).then(1L).otherwise(0L).sum();
        NumberExpression<Long> isTerraceCount = new CaseBuilder().when(preference.isTerrace.isTrue()).then(1L).otherwise(0L).sum();
        NumberExpression<Long> longStayCount = new CaseBuilder().when(preference.longStay.isTrue()).then(1L).otherwise(0L).sum();
        NumberExpression<Long> manyDrinkCount = new CaseBuilder().when(preference.manyDrink.isTrue()).then(1L).otherwise(0L).sum();

        Tuple result = queryFactory
            .select(
                bigStoreCount,
                cleanCount,
                goodMenuCount,
                goodMusicCount,
                goodPictureCount,
                goodTalkCount,
                goodViewCount,
                isTerraceCount,
                longStayCount,
                manyDrinkCount
            )
            .from(preference)
            .fetchOne();

        if (result == null) {
            return Collections.emptyMap();
        }

        return Map.of(
            "bigStoreCount", result.get(bigStoreCount),
            "cleanCount", result.get(cleanCount),
            "goodMenuCount", result.get(goodMenuCount),
            "goodMusicCount", result.get(goodMusicCount),
            "goodPictureCount", result.get(goodPictureCount),
            "goodTalkCount", result.get(goodTalkCount),
            "goodViewCount", result.get(goodViewCount),
            "isTerraceCount", result.get(isTerraceCount),
            "longStayCount", result.get(longStayCount),
            "manyDrinkCount", result.get(manyDrinkCount)
        );
    }
}
