package com.grepp.matnam.app.model.user;

import com.grepp.matnam.app.model.user.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepositoryCustom {

    Page<UserDto> findAllUsers(Pageable pageable);

    Page<UserDto> findByStatusAndKeywordContaining(String status, String keyword, Pageable pageable);

    Page<UserDto> findByStatus(String status, Pageable pageable);

    Page<UserDto> findByKeywordContaining(String keyword, Pageable pageable);
}
