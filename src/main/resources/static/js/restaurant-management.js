document.addEventListener('DOMContentLoaded', function() {
    // 식당 추가 버튼 클릭 이벤트
    const addRestaurantBtn = document.getElementById('add-restaurant-btn');
    if (addRestaurantBtn) {
        addRestaurantBtn.addEventListener('click', function() {
            // 모달 제목 설정
            document.getElementById('restaurant-modal-title').textContent = '새 식당 추가';
            
            // 폼 초기화
            document.getElementById('restaurantForm').reset();
            document.getElementById('restaurant-id').value = '';

            // 모달 표시
            document.getElementById('restaurantModal').style.display = 'block';
        });
    }
    
    // 카테고리 추가 버튼 클릭 이벤트
    const addCategoryBtn = document.getElementById('add-category-btn');
    if (addCategoryBtn) {
        addCategoryBtn.addEventListener('click', function() {
            // 모달 제목 설정
            document.getElementById('category-modal-title').textContent = '새 카테고리 추가';
            
            // 폼 초기화
            document.getElementById('categoryForm').reset();
            document.getElementById('category-id').value = '';
            
            // 모달 표시
            document.getElementById('categoryModal').style.display = 'block';
        });
    }
    // 최대 선택 가능 개수
    const MAX_MOOD_SELECTION = 3;

    // 체크박스 선택 제한
    const moodCheckboxes = document.querySelectorAll('input[name="restaurant-mood"]');
    moodCheckboxes.forEach(checkbox => {
        checkbox.addEventListener('change', () => {
            const checkedCount = document.querySelectorAll('input[name="restaurant-mood"]:checked').length;
            if (checkedCount > MAX_MOOD_SELECTION) {
                checkbox.checked = false;
                alert(`최대 ${MAX_MOOD_SELECTION}개까지만 선택할 수 있습니다.`);
            }
        });
    });

    // 식당 수정 버튼 클릭 이벤트
    const editButtons = document.querySelectorAll('.action-btn.edit');

    editButtons.forEach(button => {
        button.addEventListener('click', function () {
            const restaurantId = this.getAttribute('data-id');

            // 서버에서 식당 데이터 가져오기
            fetch(`/api/admin/restaurant/${restaurantId}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('서버 응답 실패');
                }
                return response.json();
            })
            .then(data => {
                console.log(data.data);
                const result = data.data;
                // 받아온 데이터로 모달 채우기
                document.getElementById('restaurant-modal-title').textContent = '식당 정보 수정';
                document.getElementById('restaurant-id').value = result.restaurantId;
                document.getElementById('restaurant-name').value = result.name;
                document.getElementById('restaurant-category').value = result.category;
                document.getElementById('restaurant-address').value = result.address;
                document.getElementById('restaurant-phone').value = result.tel;
                document.getElementById('restaurant-hours').value = result.openTime;
                document.getElementById('restaurant-main-menu').value = result.mainFood;
                document.getElementById('restaurant-description').value = result.summary;
                document.getElementById('restaurant-google-rating').value = result.googleRating;
                document.getElementById('restaurant-naver-rating').value = result.naverRating;
                document.getElementById('restaurant-kakao-rating').value = result.kakaoRating;


                // 분위기 체크박스 설정 (예: 쉼표로 구분된 문자열인 경우)
                const moodArray = result.mood.split(',').map(m => m.trim());
                moodCheckboxes.forEach(checkbox => {
                    checkbox.checked = moodArray.includes(checkbox.value);
                });

                // 모달 표시
                document.getElementById('restaurantModal').style.display = 'block';
            })
            .catch(error => {
                console.error('식당 정보를 불러오는 중 오류 발생:', error);
                alert('식당 정보를 불러오지 못했습니다.');
            });
        });
    });

    // 식당 수정 - 저장 버튼 클릭 이벤트
    // 새 식당 추가 - 저장 버튼 클릭 이벤트
    document.getElementById('save-button').addEventListener('click', function () {
        const restaurantId = document.getElementById('restaurant-id').value;

        // 선택된 mood 값들 최대 3개
        const moodValues = Array.from(document.querySelectorAll('input[name="restaurant-mood"]:checked'))
        .map(cb => cb.value);

        const payload = {
            name: document.getElementById('restaurant-name').value,
            category: document.getElementById('restaurant-category').value,
            address: document.getElementById('restaurant-address').value,
            tel: document.getElementById('restaurant-phone').value,
            openTime: document.getElementById('restaurant-hours').value,
            mainFood: document.getElementById('restaurant-main-menu').value,
            summary: document.getElementById('restaurant-description').value,
            mood: moodValues.join(','),
            googleRating: parseFloat(document.getElementById('restaurant-google-rating').value),
            naverRating: parseFloat(document.getElementById('restaurant-naver-rating').value),
            kakaoRating: parseFloat(document.getElementById('restaurant-kakao-rating').value)
        };

        const url = restaurantId
            ? `/api/admin/restaurant/${restaurantId}` // 수정 (PATCH)
            : `/api/admin/restaurant`;      // 생성 (POST)

        const method = restaurantId ? 'PATCH' : 'POST';

        fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(payload)
        })
        .then(async (response) => {
            if (response.ok) {
                alert("저장이 완료되었습니다!");
                // 모달 닫기 등 후처리
                document.getElementById('restaurantModal').style.display = 'none';
                location.reload(); // 새로고침으로 반영
            } else {
                const errorMessages = await response.json();
                document.querySelector('#error-name').textContent = errorMessages.name;
                document.querySelector('#error-category').textContent = errorMessages.category;
                document.querySelector('#error-address').textContent = errorMessages.address;
                document.querySelector('#error-phone').textContent = errorMessages.tel;
                document.querySelector('#error-hours').textContent = errorMessages.openTime;
                document.querySelector('#error-main-menu').textContent = errorMessages.mainFood;
                document.querySelector('#error-description').textContent = errorMessages.summary;
                document.querySelector('#error-mood').textContent = errorMessages.mood;
                document.querySelector('#error-google-rating').textContent = errorMessages.googleRating;
                document.querySelector('#error-naver-rating').textContent = errorMessages.naverRating;
                document.querySelector('#error-kakao-rating').textContent = errorMessages.kakaoRating;
                alert('필수 항목을 모두 입력해주세요.');
            }
        })
        .catch(err => {
            console.error(err);
            alert("저장 중 오류가 발생했습니다.");
        });
    });

    // 식당 삭제 버튼 클릭 이벤트
    document.querySelectorAll('.action-btn.delete').forEach(button => {
        button.addEventListener('click', function () {
            const restaurantId = this.getAttribute('data-id');

            if (confirm("정말 이 식당을 삭제하시겠습니까?")) {
                fetch(`/api/admin/restaurant/${restaurantId}`, {
                    method: 'DELETE',
                    headers: {
                        'Content-Type': 'application/json'
                    }
                })
                .then(response => {
                    if (response.ok) {
                        alert("식당이 삭제되었습니다.");
                        window.location.href = "/admin/restaurant";
                    } else {
                        alert("삭제 실패: 서버 오류");
                    }
                })
                .catch(error => {
                    console.error("에러 발생:", error);
                    alert("삭제 중 문제가 발생했습니다.");
                });
            }
        });
    });

    // 카테고리 저장 버튼 클릭 이벤트
    const saveCategoryBtn = document.querySelector('#categoryModal .save-btn');
    if (saveCategoryBtn) {
        saveCategoryBtn.addEventListener('click', function() {
            // 폼 유효성 검사
            const categoryName = document.getElementById('category-name').value;
            
            if (!categoryName) {
                alert('카테고리명을 입력해주세요.');
                return;
            }
            
            // 실제 구현에서는 서버로 데이터 전송
            // 여기서는 모달 닫기만 수행
            document.getElementById('categoryModal').style.display = 'none';
            
            // 성공 메시지 (실제 구현에서는 서버 응답 후 표시)
            alert('카테고리가 저장되었습니다.');
            
            // 페이지 새로고침 (실제 구현에서는 필요에 따라 수행)
            // window.location.reload();
        });
    }
});