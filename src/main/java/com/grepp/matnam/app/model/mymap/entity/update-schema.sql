CREATE TABLE mymap
(
    map_id       BIGINT       NOT NULL,
    activated    BIT(1)       NULL,
    created_at   datetime     NULL,
    modified_at  datetime     NULL,
    user_id      VARCHAR(255) NOT NULL,
    place_name   VARCHAR(255) NULL,
    road_address VARCHAR(255) NULL,
    latitude     DOUBLE       NULL,
    longitude    DOUBLE       NULL,
    memo         VARCHAR(255) NULL,
    pinned       BIT(1)       NULL,
    CONSTRAINT pk_mymap PRIMARY KEY (map_id)
);
ALTER TABLE mymap MODIFY map_id BIGINT NOT NULL AUTO_INCREMENT;

ALTER TABLE mymap
    ADD CONSTRAINT FK_MYMAP_ON_USER FOREIGN KEY (user_id) REFERENCES user (user_id);