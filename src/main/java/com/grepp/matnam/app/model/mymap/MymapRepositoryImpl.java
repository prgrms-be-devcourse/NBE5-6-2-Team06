package com.grepp.matnam.app.model.mymap;

import com.grepp.matnam.app.model.mymap.entity.Mymap;
import com.grepp.matnam.app.model.mymap.entity.QMymap;
import com.grepp.matnam.app.model.user.entity.User;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MymapRepositoryImpl implements MymapRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Mymap> findActivatedMymapsByDynamicConditions(User user, Boolean pinned) {
        QMymap mymap = QMymap.mymap;

        BooleanBuilder builder = new BooleanBuilder()
                .and(mymap.user.eq(user))
                .and(mymap.activated.isTrue());

        if (pinned != null) {
            builder.and(mymap.pinned.eq(pinned));
        }

        return queryFactory
                .selectFrom(mymap)
                .where(builder)
                .fetch();
    }

    @Override
    public List<Mymap> findActivatedMymapsByUserListAndPinned(Collection<User> users, boolean pinned) {
        QMymap mymap = QMymap.mymap;

        return queryFactory
                .selectFrom(mymap)
                .where(
                        mymap.user.in(users),
                        mymap.activated.isTrue(),
                        mymap.pinned.eq(pinned)
                )
                .fetch();
    }
}
