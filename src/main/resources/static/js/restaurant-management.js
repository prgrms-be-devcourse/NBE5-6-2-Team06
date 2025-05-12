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
            
            // 메뉴 항목 초기화
            const menuContainer = document.getElementById('menu-items-container');
            menuContainer.innerHTML = `
                <div class="menu-item">
                    <input type="text" class="menu-name" placeholder="메뉴명">
                    <input type="text" class="menu-price" placeholder="가격">
                    <button type="button" class="remove-menu-btn"><i class="fas fa-times"></i></button>
                </div>
            `;
            
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
    
    // 식당 수정 버튼 클릭 이벤트
    const editButtons = document.querySelectorAll('.action-btn.edit');
    editButtons.forEach(button => {
        button.addEventListener('click', function() {
            const restaurantId = this.getAttribute('data-id');
            
            // 실제 구현에서는 서버에서 데이터를 가져와야 함
            // 여기서는 예시 데이터로 모달 채우기
            document.getElementById('restaurant-modal-title').textContent = '식당 정보 수정';
            document.getElementById('restaurant-id').value = restaurantId;
            
            // 예시 데이터 (실제로는 서버에서 가져온 데이터로 채워야 함)
            document.getElementById('restaurant-name').value = '맛있는 치킨';
            document.getElementById('restaurant-category').value = '한식';
            document.getElementById('restaurant-address').value = '서울 강남구 테헤란로 123';
            document.getElementById('restaurant-phone').value = '02-123-4567';
            document.getElementById('restaurant-hours').value = '평일 11:00-22:00, 주말 11:00-23:00';
            document.getElementById('restaurant-main-menu').value = '후라이드 치킨';
            document.getElementById('restaurant-price-range').value = '보통';
            document.getElementById('restaurant-description').value = '맛있는 치킨을 제공하는 식당입니다.';
            
            // 분위기 체크박스 설정 (예시)
            document.getElementById('mood-clean').checked = true;
            document.getElementById('mood-spacious').checked = true;
            
            // 메뉴 항목 설정 (예시)
            const menuContainer = document.getElementById('menu-items-container');
            menuContainer.innerHTML = `
                <div class="menu-item">
                    <input type="text" class="menu-name" placeholder="메뉴명" value="후라이드 치킨">
                    <input type="text" class="menu-price" placeholder="가격" value="18,000원">
                    <button type="button" class="remove-menu-btn"><i class="fas fa-times"></i></button>
                </div>
                <div class="menu-item">
                    <input type="text" class="menu-name" placeholder="메뉴명" value="양념 치킨">
                    <input type="text" class="menu-price" placeholder="가격" value="19,000원">
                    <button type="button" class="remove-menu-btn"><i class="fas fa-times"></i></button>
                </div>
            `;
            
            // 모달 표시
            document.getElementById('restaurantModal').style.display = 'block';
        });
    });
    
    // 메뉴 추가 버튼 클릭 이벤트
    const addMenuBtn = document.querySelector('.add-menu-btn');
    if (addMenuBtn) {
        addMenuBtn.addEventListener('click', function() {
            const menuContainer = document.getElementById('menu-items-container');
            const newMenuItem = document.createElement('div');
            newMenuItem.className = 'menu-item';
            newMenuItem.innerHTML = `
                <input type="text" class="menu-name" placeholder="메뉴명">
                <input type="text" class="menu-price" placeholder="가격">
                <button type="button" class="remove-menu-btn"><i class="fas fa-times"></i></button>
            `;
            menuContainer.appendChild(newMenuItem);
            
            // 새로 추가된 메뉴 항목의 삭제 버튼에 이벤트 리스너 추가
            setupRemoveMenuButtons();
        });
    }
    
    // 메뉴 삭제 버튼 이벤트 설정 함수
    function setupRemoveMenuButtons() {
        const removeButtons = document.querySelectorAll('.remove-menu-btn');
        removeButtons.forEach(button => {
            button.addEventListener('click', function() {
                const menuItem = this.closest('.menu-item');
                menuItem.remove();
            });
        });
    }
    
    // 초기 메뉴 삭제 버튼 이벤트 설정
    setupRemoveMenuButtons();
    
    // 식당 저장 버튼 클릭 이벤트
    const saveRestaurantBtn = document.querySelector('#restaurantModal .save-btn');
    if (saveRestaurantBtn) {
        saveRestaurantBtn.addEventListener('click', function() {
            // 폼 유효성 검사
            const restaurantName = document.getElementById('restaurant-name').value;
            const restaurantCategory = document.getElementById('restaurant-category').value;
            const restaurantAddress = document.getElementById('restaurant-address').value;
            
            if (!restaurantName || !restaurantCategory || !restaurantAddress) {
                alert('필수 항목을 모두 입력해주세요.');
                return;
            }
            
            // 실제 구현에서는 서버로 데이터 전송
            // 여기서는 모달 닫기만 수행
            document.getElementById('restaurantModal').style.display = 'none';
            
            // 성공 메시지 (실제 구현에서는 서버 응답 후 표시)
            alert('식당 정보가 저장되었습니다.');
            
            // 페이지 새로고침 (실제 구현에서는 필요에 따라 수행)
            // window.location.reload();
        });
    }
    
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