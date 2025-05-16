-- 콘텐츠 랭킹 데이터
INSERT INTO content_rankings (title, subtitle, start_date, end_date, is_active, created_at, updated_at)
VALUES
('🍽️ 소개팅 때 먹으면 안 되는 음식 TOP 5 🍽️', '맛남이 알려드리는 소개팅 성공률을 높이는 음식 선택 팁!', '2023-08-15', NULL, true, NOW(), NOW()),
('💰 맛남 유저들이 선정한 가성비 최고 맛집 TOP 5 💰', '가벼운 지갑으로도 즐길 수 있는 착한 가격의 맛집!', '2023-08-15', '2023-08-31', false, NOW(), NOW());

-- 소개팅 음식 랭킹 항목
INSERT INTO ranking_items (content_ranking_id, ranking, item_name, description, is_active, created_at, updated_at)
VALUES
(1, 1, '짜장면', '입가에 소스가 묻기 쉽고, 면을 먹는 모습이 부담스러울 수 있어요. 하얀 옷에 튈 경우 대참사가 일어날 수 있습니다!', true, NOW(), NOW()),
(1, 2, '마늘이 많은 음식', '강한 마늘 향은 대화 중 불편함을 줄 수 있어요. 소개팅 후 2차로 이어질 가능성을 크게 낮춥니다.', true, NOW(), NOW()),
(1, 3, '랍스터, 대형 갑각류', '껍질을 까는 과정이 어색하고 손이 끈적거려 불쾌감을 줄 수 있어요. 식사에 집중하다 대화가 끊길 수도 있습니다.', true, NOW(), NOW()),
(1, 4, '매운 떡볶이', '매운 음식은 땀이 나고 코가 흐를 수 있어 민망한 상황이 연출될 수 있어요. 화장이 지워질 위험도 있습니다!', true, NOW(), NOW()),
(1, 5, '샐러드와 같은 건강식', '너무 건강에 신경쓰는 모습을 보이면 부담스러울 수 있어요. 또한 양이 적어 식사 후 배고파 할 수도 있습니다.', true, NOW(), NOW());

-- 가성비 맛집 랭킹 항목 (비활성화 상태로 미리 추가)
INSERT INTO ranking_items (content_ranking_id, ranking, item_name, description, is_active, created_at, updated_at)
VALUES
(2, 1, '백종원의 3대천왕', '합리적인 가격에 푸짐한 양과 맛을 자랑하는 대표 프랜차이즈!', true, NOW(), NOW()),
(2, 2, '동네 중국집', '언제나 믿고 먹을 수 있는 가성비 최고의 중화요리!', true, NOW(), NOW()),
(2, 3, '학교 앞 분식점', '추억의 맛과 함께 부담 없는 가격으로 즐길 수 있는 한식의 정석!', true, NOW(), NOW()),
(2, 4, '시장 국밥집', '든든한 한 끼를 저렴하게 즐길 수 있는 서민의 맛집!', true, NOW(), NOW()),
(2, 5, '동네 빵집', '대형 프랜차이즈보다 더 맛있고 저렴한 동네 빵집의 매력!', true, NOW(), NOW());