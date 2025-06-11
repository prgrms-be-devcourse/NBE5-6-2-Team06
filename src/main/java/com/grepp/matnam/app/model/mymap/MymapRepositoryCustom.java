package com.grepp.matnam.app.model.mymap;

import com.grepp.matnam.app.model.mymap.entity.Mymap;
import com.grepp.matnam.app.model.user.entity.User;

import java.util.Collection;
import java.util.List;

public interface MymapRepositoryCustom {
    List<Mymap> findActivatedMymapsByDynamicConditions(User user, Boolean pinned);
    List<Mymap> findActivatedMymapsByUserListAndPinned(Collection<User> users, boolean pinned);
    long countActivatedByUser(User user, Boolean pinned);
}