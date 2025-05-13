document.addEventListener('DOMContentLoaded', function() {
    const statusSelect = document.getElementById("edit-user-status");
    const suspendOptions = document.getElementById("suspend-options");
    const durationSelect = document.getElementById("edit-suspend-duration");
    const reasonTextarea = document.getElementById("edit-suspend-reason");

    function toggleSuspendOptions() {
        const isSuspended = statusSelect.value === "SUSPENDED";
        suspendOptions.style.display = isSuspended ? "block" : "none";
        durationSelect.disabled = !isSuspended;
        reasonTextarea.disabled = !isSuspended;
    }

    // 초기 상태 설정
    toggleSuspendOptions();

    // 상태 변경 시 이벤트
    statusSelect.addEventListener("change", toggleSuspendOptions);

    // 사용자 상태 수정 버튼 클릭 이벤트
    const editButtons = document.querySelectorAll('.action-btn.edit');
    editButtons.forEach(button => {
        button.addEventListener('click', function() {
            const userId = this.getAttribute('data-id');
            const nickname = this.getAttribute('data-nickname');
            const email = this.getAttribute('data-email');
            const status = this.getAttribute('data-status');
            const suspendDuration = this.getAttribute('data-suspendDuration');
            const dueReason = this.getAttribute('data-dueReason');

            document.getElementById('edit-user-id').value = userId;
            document.getElementById('edit-user-name').value = userId;
            document.getElementById('edit-user-nickname').value = nickname;
            document.getElementById('edit-user-email').value = email;
            document.getElementById('edit-user-status').value = status;
            document.getElementById('edit-suspend-duration').value = suspendDuration || "7";
            document.getElementById('edit-suspend-reason').value = dueReason;

            if (status === 'SUSPENDED') {
                suspendOptions.style.display = "block";
                durationSelect.disabled = false;
                reasonTextarea.disabled = false;
            } else {
                suspendOptions.style.display = "none";
                durationSelect.disabled = true;
                reasonTextarea.disabled = true;
            }

            // 모달 표시
            document.getElementById('userEditModal').style.display = 'block';
        });
    });

    // 사용자 상태 저장 버튼 클릭 이벤트
    const saveUserBtn = document.querySelector('#userEditModal .save-btn');
    if (saveUserBtn) {
        saveUserBtn.addEventListener('click', async function () {
            const userId = document.getElementById('edit-user-id').value;
            const status = document.getElementById('edit-user-status').value;
            const suspendDuration = document.getElementById('edit-suspend-duration').value;
            const suspendReason = document.getElementById('edit-suspend-reason').value;

            // 상태가 SUSPENDED일 때는 정지 기간/사유 필수 입력 검증
            if (status === 'SUSPENDED') {
                if (!suspendDuration || !suspendReason.trim()) {
                    alert('정지 기간과 사유를 모두 입력해야 합니다.');
                    return;
                }
            }

            // PATCH 요청을 위한 payload 구성
            const payload = {
                status: status
            };

            if (status === 'SUSPENDED') {
                payload.suspendDuration = parseInt(suspendDuration); // 숫자로 전송
                payload.dueReason = suspendReason.trim();
            }

            try {
                const response = await fetch(`/api/admin/user/${userId}`, {
                    method: 'PATCH',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(payload)
                });

                if (!response.ok) {
                    const error = await response.text();
                    alert('저장 실패: ' + error);
                    return;
                }

                alert('사용자 정보가 저장되었습니다.');
                document.getElementById('userEditModal').style.display = 'none';
                window.location.reload(); // 새로고침으로 반영

            } catch (error) {
                console.error('저장 오류:', error);
                alert('오류가 발생했습니다.');
            }
        });
    }

    // 사용자 비활성화(삭제) 버튼 클릭 이벤트
    document.querySelectorAll('.action-btn.delete').forEach(button => {
        button.addEventListener('click', function () {
            const userId = this.getAttribute('data-id');

            if (!confirm('정말로 이 사용자를 비활성화하시겠습니까?')) {
                return;
            }

            fetch(`/api/admin/user/${userId}`, {
                method: 'DELETE',
            })
            .then(response => {
                if (response.ok) {
                    response.text().then(text => { alert(text)});
                    window.location.reload(); // 페이지 새로고침
                } else {
                    return response.text().then(text => { throw new Error(text) });
                }
            })
            .catch(error => {
                console.error('에러 발생:', error);
                alert('사용자 비활성화 중 문제가 발생했습니다.');
            });
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