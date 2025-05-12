document.addEventListener('DOMContentLoaded', function() {
    // 새 공지 작성 버튼 클릭 이벤트
    const addNotificationBtn = document.getElementById('add-notification-btn');
    if (addNotificationBtn) {
        addNotificationBtn.addEventListener('click', function() {
            // 모달 제목 설정
            document.getElementById('notification-modal-title').textContent = '새 공지 작성';
            
            // 폼 초기화
            document.getElementById('notificationForm').reset();
            document.getElementById('notification-id').value = '';
            document.getElementById('notification-status').value = 'active';
            
            // 모달 표시
            document.getElementById('notificationModal').style.display = 'block';
        });
    }
    
    // 공지 수정 버튼 클릭 이벤트
    const editButtons = document.querySelectorAll('.action-btn.edit');
    editButtons.forEach(button => {
        button.addEventListener('click', function() {
            const notificationId = this.getAttribute('data-id');
            
            // 실제 구현에서는 서버에서 데이터를 가져와야 함
            // 여기서는 예시 데이터로 모달 채우기
            document.getElementById('notification-modal-title').textContent = '공지 수정';
            document.getElementById('notification-id').value = notificationId;
            document.getElementById('notification-title').value = '시스템 점검 안내';
            document.getElementById('notification-content').value = '2023년 7월 1일 오전 2시부터 4시까지 시스템 점검이 있을 예정입니다.';
            document.getElementById('notification-status').value = 'active';
            
            // 모달 표시
            document.getElementById('notificationModal').style.display = 'block';
        });
    });
    
    // 공지 상세 보기 버튼 클릭 이벤트
    const viewButtons = document.querySelectorAll('.action-btn.view');
    viewButtons.forEach(button => {
        button.addEventListener('click', function() {
            const notificationId = this.getAttribute('data-id');
            
            // 실제 구현에서는 서버에서 데이터를 가져와야 함
            // 여기서는 예시 데이터로 모달 채우기
            document.getElementById('notification-modal-title').textContent = '공지 상세 보기';
            document.getElementById('notification-id').value = notificationId;
            document.getElementById('notification-title').value = '시스템 점검 안내';
            document.getElementById('notification-content').value = '2023년 7월 1일 오전 2시부터 4시까지 시스템 점검이 있을 예정입니다.';
            document.getElementById('notification-status').value = 'active';
            
            // 읽기 전용으로 설정
            document.getElementById('notification-title').readOnly = true;
            document.getElementById('notification-content').readOnly = true;
            document.getElementById('notification-status').disabled = true;
            
            // 저장 버튼 숨기기
            document.querySelector('#notificationModal .save-btn').style.display = 'none';
            
            // 모달 표시
            document.getElementById('notificationModal').style.display = 'block';
        });
    });
    
    // 공지 삭제 버튼 클릭 이벤트
    const deleteButtons = document.querySelectorAll('.action-btn.delete');
    deleteButtons.forEach(button => {
        button.addEventListener('click', function() {
            const notificationId = this.getAttribute('data-id');
            
            if (confirm('정말로 이 공지를 삭제하시겠습니까?')) {
                // 실제 구현에서는 서버로 삭제 요청을 보내야 함
                // 여기서는 예시로 알림만 표시
                alert('공지가 삭제되었습니다.');
                
                // 페이지 새로고침 (실제 구현에서는 필요에 따라 수행)
                // window.location.reload();
            }
        });
    });
    
    // 공지 저장 버튼 클릭 이벤트
    const saveBtn = document.querySelector('#notificationModal .save-btn');
    if (saveBtn) {
        saveBtn.addEventListener('click', function() {
            // 폼 유효성 검사
            const title = document.getElementById('notification-title').value;
            const content = document.getElementById('notification-content').value;
            
            if (!title || !content) {
                alert('제목과 내용을 모두 입력해주세요.');
                return;
            }
            
            // 실제 구현에서는 서버로 데이터 전송
            // 여기서는 모달 닫기만 수행
            document.getElementById('notificationModal').style.display = 'none';
            
            // 성공 메시지 (실제 구현에서는 서버 응답 후 표시)
            alert('공지가 저장되었습니다.');
            
            // 페이지 새로고침 (실제 구현에서는 필요에 따라 수행)
            // window.location.reload();
        });
    }
    
    // 모달이 닫힐 때 읽기 전용 해제 및 저장 버튼 표시
    const closeButtons = document.querySelectorAll('#notificationModal .close-modal, #notificationModal .cancel-btn');
    closeButtons.forEach(button => {
        button.addEventListener('click', function() {
            document.getElementById('notification-title').readOnly = false;
            document.getElementById('notification-content').readOnly = false;
            document.getElementById('notification-status').disabled = false;
            document.querySelector('#notificationModal .save-btn').style.display = 'inline-block';
        });
    });
});