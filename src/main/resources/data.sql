INSERT INTO user (user_id, password, age, address, email, nickname, gender, temperature, created_at, modified_at, activated, status, role)
VALUES
    ('admin', '{bcrypt}$2a$10$DLmC62iFtw6t/XTcLHhU/ONfwogNazrbae9PsnZ9DWz4/lIFr2jFK', 28, '서울시 강남구', 'admin@matnam.com', '관리자', 'MAN', 36.5, '2024-05-01 10:00:00', '2024-05-01 10:00:00', 1,'ACTIVE','ROLE_ADMIN'),
    ('user1', '{bcrypt}$2a$10$DLmC62iFtw6t/XTcLHhU/ONfwogNazrbae9PsnZ9DWz4/lIFr2jFK', 28, '서울시 강남구', 'user1@example.com', '맛집탐험가', 'MAN', 36.5, '2024-05-01 10:00:00', '2024-05-01 10:00:00', 1, 'ACTIVE','ROLE_USER'),
    ('user2', '{bcrypt}$2a$10$DLmC62iFtw6t/XTcLHhU/ONfwogNazrbae9PsnZ9DWz4/lIFr2jFK', 32, '서울시 서초구', 'user2@example.com', '미식가레이디', 'WOMAN', 37.2, '2024-05-01 11:30:00', '2024-05-01 11:30:00', 1, 'ACTIVE','ROLE_USER'),
    ('user3', '{bcrypt}$2a$10$DLmC62iFtw6t/XTcLHhU/ONfwogNazrbae9PsnZ9DWz4/lIFr2jFK', 25, '서울시 마포구', 'user3@example.com', '힙스터푸디', 'MAN', 36.8, '2024-05-02 09:15:00', '2024-05-02 09:15:00', 1, 'ACTIVE','ROLE_USER'),
    ('user4', '{bcrypt}$2a$10$DLmC62iFtw6t/XTcLHhU/ONfwogNazrbae9PsnZ9DWz4/lIFr2jFK', 30, '서울시 용산구', 'user4@example.com', '맛있으면짖는개', 'WOMAN', 30.9, '2024-05-02 14:20:00', '2024-05-02 14:20:00', 1, 'ACTIVE', 'ROLE_USER'),
    ('user5', '{bcrypt}$2a$10$DLmC62iFtw6t/XTcLHhU/ONfwogNazrbae9PsnZ9DWz4/lIFr2jFK', 27, '서울시 동대문구', 'user5@example.com', '동대문핫플러', 'MAN', 36.7, '2024-05-03 16:45:00', '2024-05-03 16:45:00', 1, 'ACTIVE','ROLE_USER'),
    ('user6', '{bcrypt}$2a$10$DLmC62iFtw6t/XTcLHhU/ONfwogNazrbae9PsnZ9DWz4/lIFr2jFK', 35, '서울시 성북구', 'user6@example.com', '맛집여행자', 'WOMAN', 36.6, '2024-05-04 10:30:00', '2024-05-04 10:30:00', 1, 'ACTIVE', 'ROLE_USER'),
    ('user7', '{bcrypt}$2a$10$DLmC62iFtw6t/XTcLHhU/ONfwogNazrbae9PsnZ9DWz4/lIFr2jFK', 29, '서울시 송파구', 'user7@example.com', '송파구맛집왕', 'MAN', 36.4, '2024-05-04 13:45:00', '2024-05-04 13:45:00', 1, 'ACTIVE', 'ROLE_USER'),
    ('user8', '{bcrypt}$2a$10$DLmC62iFtw6t/XTcLHhU/ONfwogNazrbae9PsnZ9DWz4/lIFr2jFK', 31, '서울시 중구', 'user8@example.com', '미식의여왕', 'WOMAN', 37.0, '2024-05-05 09:20:00', '2024-05-05 09:20:00', 1, 'ACTIVE', 'ROLE_USER'),
    ('user9', '{bcrypt}$2a$10$DLmC62iFtw6t/XTcLHhU/ONfwogNazrbae9PsnZ9DWz4/lIFr2jFK', 26, '서울시 광진구', 'user9@example.com', '혼밥마스터', 'MAN', 36.8, '2024-05-05 11:50:00', '2024-05-05 11:50:00', 1, 'ACTIVE', 'ROLE_USER'),
    ('user10', '{bcrypt}$2a$10$DLmC62iFtw6t/XTcLHhU/ONfwogNazrbae9PsnZ9DWz4/lIFr2jFK', 33, '서울시 종로구', 'user10@example.com', '종로맛집헌터', 'WOMAN', 36.5, '2024-05-06 14:15:00', '2024-05-06 14:15:00', 1,  'ACTIVE', 'ROLE_USER');

-- 식당(restaurant) 데이터
INSERT INTO restaurant (activated, google_rating, kakao_rating, naver_rating, recommended_count, created_at, modified_at, address, category, main_food, mood, name, open_time, summary, tel)
VALUES
    (1, 4.5, 4.7, 4.6, 120, '2024-04-10 09:00:00', '2024-05-01 10:30:00', '서울시 강남구 테헤란로 123', '한식', '된장찌개', '아늑함', '강남 된장찌개', '10:00-22:00', '20년 전통의 된장찌개 전문점', '02-123-4567'),
    (1, 4.3, 4.5, 4.4, 85, '2024-04-11 10:15:00', '2024-05-02 11:45:00', '서울시 마포구 홍대로 456', '양식', '파스타', '모던함', '홍대 파스타', '11:00-23:00', '신선한 재료로 만든 파스타', '02-234-5678'),
    (1, 4.7, 4.8, 4.9, 210, '2024-04-12 11:30:00', '2024-05-03 13:00:00', '서울시 용산구 이태원로 789', '일식', '초밥', '고급스러움', '용산 스시', '12:00-22:00', '신선한 해산물로 만든 스시', '02-345-6789'),
    (1, 4.1, 4.3, 4.2, 65, '2024-04-13 12:45:00', '2024-05-04 14:15:00', '서울시 서초구 강남대로 101', '중식', '짜장면', '복고풍', '서초 중화요리', '11:00-21:00', '손으로 만든 수제 면발', '02-456-7890'),
    (1, 4.6, 4.7, 4.8, 150, '2024-04-14 14:00:00', '2024-05-05 15:30:00', '서울시 동대문구 왕산로 202', '카페', '커피', '힙함', '동대문 카페', '09:00-23:00', '로스팅부터 핸드드립까지', '02-567-8901'),
    (1, 4.8, 4.9, 4.7, 180, '2024-04-15 15:15:00', '2024-05-06 16:45:00', '서울시 성북구 성북로 303', '분식', '떡볶이', '친근함', '성북 분식', '11:00-22:00', '비밀 레시피로 만든 매콤한 떡볶이', '02-678-9012'),
    (1, 4.2, 4.4, 4.3, 95, '2024-04-16 16:30:00', '2024-05-07 18:00:00', '서울시 송파구 올림픽로 404', '태국음식', '팟타이', '이국적임', '송파 태국', '12:00-23:00', '현지 셰프가 만드는 정통 태국 요리', '02-789-0123'),
    (1, 4.9, 4.8, 4.9, 220, '2024-04-17 17:45:00', '2024-05-08 19:15:00', '서울시 중구 을지로 505', '고기', '삼겹살', '활기참', '을지로 고기', '16:00-02:00', '숯불에 구운 최상급 삼겹살', '02-890-1234'),
    (1, 4.4, 4.6, 4.5, 110, '2024-04-18 19:00:00', '2024-05-09 20:30:00', '서울시 광진구 능동로 606', '베이커리', '크루아상', '따뜻함', '광진 베이커리', '07:00-21:00', '매일 아침 굽는 신선한 빵', '02-901-2345'),
    (1, 4.0, 4.2, 4.1, 75, '2024-04-19 20:15:00', '2024-05-10 21:45:00', '서울시 종로구 종로 707', '치킨', '양념치킨', '즐거움', '종로 치킨', '16:00-02:00', '특제 양념으로 버무린 바삭한 치킨', '02-012-3456');

-- 팀/모임(team) 데이터
INSERT INTO team (activated, max_people, now_people, created_at, meet_date, modified_at, team_date, image_url, team_details, team_title, user_id, status, restaurant_name)
VALUES
    (1, 4, 2, '2024-05-05 10:00:00', '2024-05-15 18:30:00', '2024-05-05 10:00:00', '2024-05-15 18:30:00', 'https://example.com/images/team1.jpg', '강남 된장찌개에서 같이 식사하실 분 구해요!', '된장찌개 모임', 'user1', 'RECRUITING', '강남 된장찌개'),
    (1, 3, 3, '2024-05-06 11:15:00', '2024-05-16 19:00:00', '2024-05-06 11:15:00', '2024-05-16 19:00:00', 'https://example.com/images/team2.jpg', '홍대 파스타 맛집 같이 가요~', '파스타 모임', 'user2', 'FULL', '홍대 파스타'),
    (1, 5, 1, '2024-05-07 12:30:00', '2024-05-17 20:00:00', '2024-05-07 12:30:00', '2024-05-17 20:00:00', 'https://example.com/images/team3.jpg', '스시 좋아하시는 분들 모여요!', '스시 모임', 'user3', 'RECRUITING', '용산 스시'),
    (1, 6, 6, '2024-05-08 13:45:00', '2024-05-10 18:00:00', '2024-05-08 13:45:00', '2024-05-10 18:00:00', 'https://example.com/images/team4.jpg', '짜장면 먹고 카페 가실 분!', '중식 모임', 'user4', 'COMPLETED', '서초 중화요리'),
    (1, 3, 0, '2024-05-09 15:00:00', '2024-05-20 17:30:00', '2024-05-09 15:00:00', '2024-05-20 17:30:00', 'https://example.com/images/team5.jpg', '카페에서 수다 떨어요', '카페 모임', 'user5', 'CANCELED', '동대문 카페'),
    (1, 5, 3, '2024-05-10 16:15:00', '2024-05-18 19:30:00', '2024-05-10 16:15:00', '2024-05-18 19:30:00', 'https://example.com/images/team6.jpg', '매콤한 떡볶이 드실 분!', '떡볶이 모임', 'user6', 'RECRUITING', '성북 분식'),
    (1, 4, 4, '2024-05-11 17:30:00', '2024-05-19 20:00:00', '2024-05-11 17:30:00', '2024-05-19 20:00:00', 'https://example.com/images/team7.jpg', '정통 태국 음식 체험하실 분~', '태국음식 모임', 'user7', 'FULL', '송파 태국'),
    (1, 8, 5, '2024-05-12 18:45:00', '2024-05-21 19:00:00', '2024-05-12 18:45:00', '2024-05-21 19:00:00', 'https://example.com/images/team8.jpg', '을지로 고깃집에서 삼겹살 파티!', '삼겹살 모임', 'user8', 'RECRUITING', '을지로 고기'),
    (1, 3, 1, '2024-05-13 20:00:00', '2024-05-22 10:30:00', '2024-05-13 20:00:00', '2024-05-22 10:30:00', 'https://example.com/images/team9.jpg', '아침에 갓 구운 빵 먹으러 가요', '베이커리 모임', 'user9', 'RECRUITING', '광진 베이커리'),
    (1, 6, 3, '2024-05-14 21:15:00', '2024-05-23 20:00:00', '2024-05-14 21:15:00', '2024-05-23 20:00:00', 'https://example.com/images/team10.jpg', '치맥 좋아하시는 분들 모여요!', '치킨 모임', 'user10', 'RECRUITING', '종로 치킨');

-- 팀 리뷰(team_review) 데이터
INSERT INTO team_review (activated, rating, created_at, modified_at, team_id, reviewer, reviewee)
VALUES
    (1, 4.5, '2024-05-16 20:30:00', '2024-05-16 20:30:00', 2, 'user2', 'user3'),
    (1, 5.0, '2024-05-16 21:00:00', '2024-05-16 21:00:00', 2, 'user3', 'user2'),
    (1, 4.7, '2024-05-16 21:30:00', '2024-05-16 21:30:00', 2, 'user1', 'user2'),
    (1, 4.0, '2024-05-10 22:00:00', '2024-05-10 22:00:00', 4, 'user4', 'user1'),
    (1, 4.2, '2024-05-10 22:30:00', '2024-05-10 22:30:00', 4, 'user1', 'user4'),
    (1, 4.8, '2024-05-10 23:00:00', '2024-05-10 23:00:00', 4, 'user2', 'user4'),
    (1, 3.5, '2024-05-10 23:30:00', '2024-05-10 23:30:00', 4, 'user3', 'user4'),
    (1, 5.0, '2024-05-19 21:00:00', '2024-05-19 21:00:00', 7, 'user7', 'user5'),
    (1, 4.8, '2024-05-19 21:30:00', '2024-05-19 21:30:00', 7, 'user5', 'user7'),
    (1, 4.9, '2024-05-19 22:00:00', '2024-05-19 22:00:00', 7, 'user8', 'user7'),
    (1, 4.7, '2024-05-19 22:30:00', '2024-05-19 22:30:00', 7, 'user10', 'user7');

-- 지도/맵(map) 데이터
INSERT INTO map (activated, created_at, modified_at, restaurant_id, user_id)
VALUES
    (1, '2024-05-02 10:30:00', '2024-05-02 10:30:00', 1, 'user1'),
    (1, '2024-05-03 11:45:00', '2024-05-03 11:45:00', 2, 'user2'),
    (1, '2024-05-04 13:00:00', '2024-05-04 13:00:00', 3, 'user3'),
    (1, '2024-05-05 14:15:00', '2024-05-05 14:15:00', 4, 'user4'),
    (1, '2024-05-06 15:30:00', '2024-05-06 15:30:00', 5, 'user5'),
    (1, '2024-05-07 16:45:00', '2024-05-07 16:45:00', 2, 'user1'),
    (1, '2024-05-08 18:00:00', '2024-05-08 18:00:00', 3, 'user2'),
    (1, '2024-05-09 19:15:00', '2024-05-09 19:15:00', 6, 'user6'),
    (1, '2024-05-10 20:30:00', '2024-05-10 20:30:00', 7, 'user7'),
    (1, '2024-05-11 10:45:00', '2024-05-11 10:45:00', 8, 'user8'),
    (1, '2024-05-12 12:00:00', '2024-05-12 12:00:00', 9, 'user9'),
    (1, '2024-05-13 13:15:00', '2024-05-13 13:15:00', 10, 'user10'),
    (1, '2024-05-14 14:30:00', '2024-05-14 14:30:00', 3, 'user5'),
    (1, '2024-05-15 15:45:00', '2024-05-15 15:45:00', 5, 'user7'),
    (1, '2024-05-16 17:00:00', '2024-05-16 17:00:00', 7, 'user9'),
    (1, '2024-05-17 18:15:00', '2024-05-17 18:15:00', 1, 'user10'),
    (1, '2024-05-18 19:30:00', '2024-05-18 19:30:00', 2, 'user8'),
    (1, '2024-05-19 20:45:00', '2024-05-19 20:45:00', 4, 'user6'),
    (1, '2024-05-20 22:00:00', '2024-05-20 22:00:00', 6, 'user4'),
    (1, '2024-05-21 23:15:00', '2024-05-21 23:15:00', 8, 'user2');

-- 참가자(participant) 데이터
INSERT INTO participant (activated, created_at, modified_at, team_id, user_id, participant_status, role)
VALUES
-- 팀 1의 참가자들
(1, '2024-05-05 10:30:00', '2024-05-05 10:30:00', 1, 'user1', 'APPROVED', 'LEADER'),
(1, '2024-05-05 11:00:00', '2024-05-05 16:00:00', 1, 'user3', 'APPROVED', 'MEMBER'),
(1, '2024-05-05 11:30:00', '2024-05-05 11:30:00', 1, 'user5', 'PENDING', 'MEMBER'),
(1, '2024-05-05 12:00:00', '2024-05-05 12:00:00', 1, 'user7', 'PENDING', 'MEMBER'),
-- 팀 2의 참가자들
(1, '2024-05-06 11:30:00', '2024-05-06 11:30:00', 2, 'user2', 'APPROVED', 'LEADER'),
(1, '2024-05-06 12:00:00', '2024-05-06 18:00:00', 2, 'user1', 'APPROVED', 'MEMBER'),
(1, '2024-05-06 12:30:00', '2024-05-06 19:00:00', 2, 'user3', 'APPROVED', 'MEMBER'),
-- 팀 3의 참가자들
(1, '2024-05-07 13:00:00', '2024-05-07 13:00:00', 3, 'user3', 'APPROVED', 'LEADER'),
(1, '2024-05-07 13:30:00', '2024-05-07 13:30:00', 3, 'user6', 'PENDING', 'MEMBER'),
(1, '2024-05-07 14:00:00', '2024-05-07 14:00:00', 3, 'user9', 'REJECTED', 'MEMBER'),
-- 팀 4의 참가자들
(1, '2024-05-08 14:00:00', '2024-05-08 14:00:00', 4, 'user4', 'APPROVED', 'LEADER'),
(1, '2024-05-08 14:30:00', '2024-05-08 20:00:00', 4, 'user1', 'APPROVED', 'MEMBER'),
(1, '2024-05-08 15:00:00', '2024-05-08 21:00:00', 4, 'user2', 'APPROVED', 'MEMBER'),
(1, '2024-05-08 15:30:00', '2024-05-08 22:00:00', 4, 'user3', 'APPROVED', 'MEMBER'),
(1, '2024-05-08 16:00:00', '2024-05-08 23:00:00', 4, 'user5', 'APPROVED', 'MEMBER'),
(1, '2024-05-08 16:30:00', '2024-05-08 23:30:00', 4, 'user8', 'APPROVED', 'MEMBER'),
-- 팀 5의 참가자들
(1, '2024-05-09 15:30:00', '2024-05-09 15:30:00', 5, 'user5', 'APPROVED', 'LEADER'),
-- 팀 6의 참가자들
(1, '2024-05-10 16:30:00', '2024-05-10 16:30:00', 6, 'user6', 'APPROVED', 'LEADER'),
(1, '2024-05-10 17:00:00', '2024-05-10 17:00:00', 6, 'user2', 'APPROVED', 'MEMBER'),
(1, '2024-05-10 17:30:00', '2024-05-10 17:30:00', 6, 'user9', 'APPROVED', 'MEMBER'),
(1, '2024-05-10 18:00:00', '2024-05-10 18:00:00', 6, 'user10', 'PENDING', 'MEMBER'),
-- 팀 7의 참가자들
(1, '2024-05-11 18:00:00', '2024-05-11 18:00:00', 7, 'user7', 'APPROVED', 'LEADER'),
(1, '2024-05-11 18:30:00', '2024-05-11 18:30:00', 7, 'user5', 'APPROVED', 'MEMBER'),
(1, '2024-05-11 19:00:00', '2024-05-11 19:00:00', 7, 'user8', 'APPROVED', 'MEMBER'),
(1, '2024-05-11 19:30:00', '2024-05-11 19:30:00', 7, 'user10', 'APPROVED', 'MEMBER'),
-- 팀 8의 참가자들
(1, '2024-05-12 19:00:00', '2024-05-12 19:00:00', 8, 'user8', 'APPROVED', 'LEADER'),
(1, '2024-05-12 19:30:00', '2024-05-12 19:30:00', 8, 'user1', 'APPROVED', 'MEMBER'),
(1, '2024-05-12 20:00:00', '2024-05-12 20:00:00', 8, 'user3', 'APPROVED', 'MEMBER'),
(1, '2024-05-12 20:30:00', '2024-05-12 20:30:00', 8, 'user5', 'APPROVED', 'MEMBER'),
(1, '2024-05-12 21:00:00', '2024-05-12 21:00:00', 8, 'user7', 'APPROVED', 'MEMBER'),
(1, '2024-05-12 21:30:00', '2024-05-12 21:30:00', 8, 'user9', 'PENDING', 'MEMBER'),
-- 팀 9의 참가자들
(1, '2024-05-13 20:30:00', '2024-05-13 20:30:00', 9, 'user9', 'APPROVED', 'LEADER'),
(1, '2024-05-13 21:00:00', '2024-05-13 21:00:00', 9, 'user2', 'PENDING', 'MEMBER'),
(1, '2024-05-13 21:30:00', '2024-05-13 21:30:00', 9, 'user4', 'PENDING', 'MEMBER'),
-- 팀 10의 참가자들
(1, '2024-05-14 21:30:00', '2024-05-14 21:30:00', 10, 'user10', 'APPROVED', 'LEADER'),
(1, '2024-05-14 22:00:00', '2024-05-14 22:00:00', 10, 'user4', 'APPROVED', 'MEMBER'),
(1, '2024-05-14 22:30:00', '2024-05-14 22:30:00', 10, 'user6', 'APPROVED', 'MEMBER'),
(1, '2024-05-14 23:00:00', '2024-05-14 23:00:00', 10, 'user8', 'PENDING', 'MEMBER');

-- 선호도(preference) 데이터
INSERT INTO preference (activated, big_store, clean, good_menu, good_music, good_picture, good_talk, good_view, is_terrace, long_stay, many_drink, created_at, modified_at, preference_id, user_id)
VALUES
    (1, 0, 1, 1, 0, 0, 1, 0, 0, 1, 0, '2024-05-01 10:30:00', '2024-05-01 10:30:00', 1, 'user1'),
    (1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 0, '2024-05-01 12:00:00', '2024-05-01 12:00:00', 2, 'user2'),
    (1, 0, 0, 1, 1, 1, 1, 0, 0, 0, 1, '2024-05-02 13:30:00', '2024-05-02 13:30:00', 3, 'user3'),
    (1, 1, 0, 1, 0, 1, 0, 1, 1, 1, 0, '2024-05-02 15:00:00', '2024-05-02 15:00:00', 4, 'user4'),
    (1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1, '2024-05-03 16:30:00', '2024-05-03 16:30:00', 5, 'user5'),
    (1, 1, 1, 1, 0, 0, 0, 1, 1, 0, 1, '2024-05-04 11:00:00', '2024-05-04 11:00:00', 6, 'user6'),
    (1, 0, 1, 1, 1, 0, 1, 0, 0, 1, 0, '2024-05-04 14:30:00', '2024-05-04 14:30:00', 7, 'user7'),
    (1, 1, 0, 0, 1, 1, 1, 0, 1, 0, 1, '2024-05-05 10:00:00', '2024-05-05 10:00:00', 8, 'user8'),
    (1, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, '2024-05-05 12:30:00', '2024-05-05 12:30:00', 9, 'user9'),
    (1, 1, 1, 0, 0, 0, 1, 1, 1, 0, 1, '2024-05-06 15:00:00', '2024-05-06 15:00:00', 10, 'user10');

-- 신고(report) 데이터
INSERT INTO report (activated, created_at, modified_at, reason, reported_id, user_id)
VALUES
    (1, '2024-05-10 10:00:00', '2024-05-10 10:00:00', '부적절한 행동', 'user5', 'user1'),
    (1, '2024-05-11 11:15:00', '2024-05-11 11:15:00', '불쾌한 메시지', 'user3', 'user2'),
    (1, '2024-05-12 12:30:00', '2024-05-12 12:30:00', '모임 규칙 위반', 'user7', 'user4'),
    (1, '2024-05-13 13:45:00', '2024-05-13 13:45:00', '약속 시간 안 지킴', 'user6', 'user8'),
    (1, '2024-05-14 15:00:00', '2024-05-14 15:00:00', '부적절한 프로필 사진', 'user9', 'user10');