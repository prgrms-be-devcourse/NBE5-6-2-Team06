CREATE TABLE mymap (
                       map_id        BIGINT NOT NULL AUTO_INCREMENT,
                       activated     BIT(1) NOT NULL DEFAULT 1,
                       created_at    DATETIME NULL,
                       modified_at   DATETIME NULL,
                       user_id       VARCHAR(255) NOT NULL,
                       place_name    VARCHAR(255) NOT NULL,
                       road_address  VARCHAR(255) NOT NULL,
                       latitude      DOUBLE NOT NULL,
                       longitude     DOUBLE NOT NULL,
                       memo          VARCHAR(255),
                       pinned        BIT(1) NOT NULL DEFAULT 1,
                       CONSTRAINT pk_mymap PRIMARY KEY (map_id),
                       CONSTRAINT FK_MYMAP_ON_USER FOREIGN KEY (user_id) REFERENCES user (user_id)
);

CREATE INDEX idx_mymap_user_id ON mymap (user_id);
CREATE INDEX idx_mymap_user_activated_pinned ON mymap (user_id, activated, pinned);