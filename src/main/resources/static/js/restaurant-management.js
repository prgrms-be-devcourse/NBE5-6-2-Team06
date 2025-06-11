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


                // 분위기 체크박스 설정
                moodCheckboxes.forEach(checkbox => {
                    const fieldName = checkbox.value; // 예: "goodTalk", "clean" 등
                    if (result.hasOwnProperty(fieldName)) {
                        checkbox.checked = result[fieldName] === true;
                    } else {
                        checkbox.checked = false;
                    }
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

        const payload = {
            name: document.getElementById('restaurant-name').value,
            category: document.getElementById('restaurant-category').value,
            address: document.getElementById('restaurant-address').value,
            tel: document.getElementById('restaurant-phone').value,
            openTime: document.getElementById('restaurant-hours').value,
            mainFood: document.getElementById('restaurant-main-menu').value,
            summary: document.getElementById('restaurant-description').value,
            googleRating: parseFloat(document.getElementById('restaurant-google-rating').value),
        };

        // 분위기 체크박스를 기반으로 각 mood 항목을 boolean으로 추가
        moodCheckboxes.forEach(cb => {
            payload[cb.value] = cb.checked;
        });

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
            const result = (await response.json()).data;
            if (response.ok) {
                alert("식당이 저장되었습니다.")
                // 모달 닫기 등 후처리
                document.getElementById('restaurantModal').style.display = 'none';
                location.reload(); // 새로고침으로 반영
            } else {
                document.querySelector('#error-name').textContent = result.name;
                document.querySelector('#error-category').textContent = result.category;
                document.querySelector('#error-address').textContent = result.address;
                document.querySelector('#error-phone').textContent = result.tel;
                document.querySelector('#error-hours').textContent = result.openTime;
                document.querySelector('#error-main-menu').textContent = result.mainFood;
                document.querySelector('#error-description').textContent = result.summary;
                document.querySelector('#error-mood').textContent = result.moodSelectionCountValid;
                document.querySelector('#error-google-rating').textContent = result.googleRating;
                alert('필수 항목을 확인해주세요.');
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
                        alert("식당이 삭제되었습니다.")
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
});