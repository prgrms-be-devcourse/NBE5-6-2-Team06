document.addEventListener('DOMContentLoaded', function() {
    // 사용자 편집 버튼 클릭 이벤트
    const editButtons = document.querySelectorAll('.action-btn.edit');
    editButtons.forEach(button => {
        button.addEventListener('click', function() {
            const userId = this.getAttribute('data-id');
            
            // 실제 구현에서는 서버에서 데이터를 가져와야 함
            // 여기서는 예시 데이터로 모달 채우기
            document.getElementById('edit-user-id').value = userId;
            document.getElementById('edit-user-name').value = '김철수';
            document.getElementById('edit-user-email').value = 'kim@example.com';
            document.getElementById('edit-user-status').value = 'active';
            
            // 모달 표시
            document.getElementById('userEditModal').style.display = 'block';
        });
    });
    
    // 신고 상세 보기 버튼 클릭 이벤트
    const viewReportButtons = document.querySelectorAll('.action-btn.view');
    viewReportButtons.forEach(button => {
        button.addEventListener('click', function() {
            const reportId = this.getAttribute('data-id');
            
            // 실제 구현에서는 서버에서 데이터를 가져와야 함
            // 여기서는 예시 데이터로 모달 채우기
            document.getElementById('report-id').textContent = reportId;
            document.getElementById('report-reporter').textContent = '김철수 (kim@example.com)';
            document.getElementById('report-target').textContent = '이영희 (lee@example.com)';
            document.getElementById('report-content').textContent = '부적절한 언어 사용';
            document.getElementById('report-date').textContent = '2023-06-15 14:30';
            document.getElementById('report-status').textContent = '대기중';
            document.getElementById('report-status').className = 'detail-value status pending';
            
            // 모달 표시
            document.getElementById('reportViewModal').style.display = 'block';
        });
    });
    
    // 사용자 정지 버튼 클릭 이벤트 (신고 상세 모달에서)
    const showSuspendOptionsBtn = document.getElementById('show-suspend-options');
    if (showSuspendOptionsBtn) {
        showSuspendOptionsBtn.addEventListener('click', function() {
            // 신고 상세 모달에서 정보 가져오기
            const reportId = document.getElementById('report-id').textContent;
            const targetInfo = document.getElementById('report-target').textContent;
            
            // 정지 모달에 정보 설정
            document.getElementById('suspend-report-id').value = reportId;
            document.getElementById('suspend-user-name').value = targetInfo;
            
            // 신고 상세 모달 닫기
            document.getElementById('reportViewModal').style.display = 'none';
            
            // 정지 모달 표시
            document.getElementById('suspendUserModal').style.display = 'block';
        });
    }
    
    // 정지 적용 버튼 클릭 이벤트
    const confirmSuspendBtn = document.getElementById('confirm-suspend-btn');
    if (confirmSuspendBtn) {
        confirmSuspendBtn.addEventListener('click', function() {
            // 폼 유효성 검사
            const suspendReason = document.getElementById('suspend-reason').value;
            
            if (!suspendReason) {
                alert('정지 사유를 입력해주세요.');
                return;
            }
            
            // 정지 기간 가져오기
            const suspendDuration = document.getElementById('suspend-duration').value;
            const notifyUser = document.getElementById('suspend-notify').checked;
            
            // 실제 구현에서는 서버로 데이터 전송
            // 여기서는 모달 닫기만 수행
            document.getElementById('suspendUserModal').style.display = 'none';
            
            // 성공 메시지 (실제 구현에서는 서버 응답 후 표시)
            let message = '사용자가 ';
            if (suspendDuration === 'permanent') {
                message += '영구 정지되었습니다.';
            } else {
                message += suspendDuration + '일 동안 정지되었습니다.';
            }
            
            if (notifyUser) {
                message += ' 사용자에게 알림이 전송되었습니다.';
            }
            
            alert(message);
            
            // 페이지 새로고침 (실제 구현에서는 필요에 따라 수행)
            // window.location.reload();
        });
    }
    
    // 사용자 정보 저장 버튼 클릭 이벤트
    const saveUserBtn = document.querySelector('#userEditModal .save-btn');
    if (saveUserBtn) {
        saveUserBtn.addEventListener('click', function() {
            // 폼 유효성 검사
            const userName = document.getElementById('edit-user-name').value;
            const userEmail = document.getElementById('edit-user-email').value;
            
            if (!userName || !userEmail) {
                alert('이름과 이메일을 모두 입력해주세요.');
                return;
            }
            
            // 실제 구현에서는 서버로 데이터 전송
            // 여기서는 모달 닫기만 수행
            document.getElementById('userEditModal').style.display = 'none';
            
            // 성공 메시지 (실제 구현에서는 서버 응답 후 표시)
            alert('사용자 정보가 저장되었습니다.');
            
            // 페이지 새로고침 (실제 구현에서는 필요에 따라 수행)
            // window.location.reload();
        });
    }
    
    // 신고 해결 처리 버튼 클릭 이벤트
    const resolveBtn = document.querySelector('.resolve-btn');
    if (resolveBtn) {
        resolveBtn.addEventListener('click', function() {
            // 실제 구현에서는 서버로 데이터 전송
            // 여기서는 모달 닫기만 수행
            document.getElementById('reportViewModal').style.display = 'none';
            
            // 성공 메시지 (실제 구현에서는 서버 응답 후 표시)
            alert('신고가 해결 처리되었습니다.');
            
            // 페이지 새로고침 (실제 구현에서는 필요에 따라 수행)
            // window.location.reload();
        });
    }
    
    // 신고 거부 버튼 클릭 이벤트
    const rejectBtn = document.querySelector('.reject-btn');
    if (rejectBtn) {
        rejectBtn.addEventListener('click', function() {
            // 실제 구현에서는 서버로 데이터 전송
            // 여기서는 모달 닫기만 수행
            document.getElementById('reportViewModal').style.display = 'none';
            
            // 성공 메시지 (실제 구현에서는 서버 응답 후 표시)
            alert('신고가 거부 처리되었습니다.');
            
            // 페이지 새로고침 (실제 구현에서는 필요에 따라 수행)
            // window.location.reload();
        });
    }
});