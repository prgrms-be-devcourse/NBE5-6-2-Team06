package com.grepp.matnam.app.model.mymap.entity;

import com.grepp.matnam.app.model.user.entity.User;
import com.grepp.matnam.infra.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mymap extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mapId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String placeName;

    private String roadAddress;

    private Double latitude;

    private Double longitude;

    private String memo;

    private Boolean pinned;

    // 향후 카테고리 필요시 enum 필드 등 추가 가능
}
