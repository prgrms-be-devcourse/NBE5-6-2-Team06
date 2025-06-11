document.addEventListener('DOMContentLoaded', function() {
    const statusSelect = document.getElementById("edit-user-status");
    const suspendOptions = document.getElementById("suspend-options");
    const durationSelect = document.getElementById("edit-suspend-duration");
    const reasonTextarea = document.getElementById("edit-suspend-reason");

    function toggleSuspendOptions() {
        const isSuspended = statusSelect.value === "SUSPENDED";
        const isBanned = statusSelect.value === "BANNED";
        const isActive = statusSelect.value === "ACTIVE";
        suspendOptions.style.display = isSuspended||isBanned ? "block" : "none";
        durationSelect.disabled = !isSuspended;
        reasonTextarea.disabled = isActive;
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
            } else if (status === 'BANNED') {
                suspendOptions.style.display = "block";
                durationSelect.disabled = true;
                reasonTextarea.disabled = false;
            }
            else {
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

            // PATCH 요청을 위한 payload 구성
            const payload = {
                status: status,
                suspendDuration: 0,
                dueReason: "정상"
            };

            if (status === 'SUSPENDED') {
                payload.suspendDuration = parseInt(suspendDuration); // 숫자로 전송
                payload.dueReason = suspendReason.trim();
            }

            if (status === 'BANNED') {
                payload.dueReason = suspendReason.trim();
            }

            try {
                const response = await fetch(`/api/admin/user/list/${userId}`, {
                    method: 'PATCH',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(payload)
                });

                if (!response.ok) {
                    const errorMessage = (await response.json()).data
                    document.querySelector('#error-suspend-reason').textContent = errorMessage.dueReason;
                    alert('필수 항목을 확인해주세요.');
                    return;
                }

                alert("사용자 상태가 수정되었습니다.");
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

            fetch(`/api/admin/user/list/${userId}`, {
                method: 'DELETE',
            })
            .then(response => {
                if (response.ok) {
                    alert("사용자가 비활성화되었습니다.")
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
            const userId = this.getAttribute('data-user-id');
            const reportedId = this.getAttribute('data-reported-id');
            const nickname = this.getAttribute('data-reported-nickname');
            const email = this.getAttribute('data-reported-email');
            const status = this.getAttribute('data-reported-status');
            const suspendDuration = this.getAttribute('data-reported-suspend-duration');
            const dueReason = this.getAttribute('data-reported-due-reason');
            const reason = this.getAttribute('data-reason');
            const createdAt = this.getAttribute('data-date');
            const activated = this.getAttribute('data-activated');
            const reportType = this.getAttribute('data-type');
            const chatId = this.getAttribute('data-chat-id');
            const teamId = this.getAttribute('data-post-id');

            document.getElementById('report-id').textContent = reportId;
            document.getElementById('report-reporter').textContent = userId;
            document.getElementById('report-target').textContent = reportedId;
            document.getElementById('report-detail-nickname').value = nickname;
            document.getElementById('report-detail-email').value = email;
            document.getElementById('report-detail-status').value = status;
            document.getElementById('report-detail-suspend-duration').value = suspendDuration;
            document.getElementById('report-detail-due-reason').value = dueReason;
            document.getElementById('report-type').textContent = reportType==='POST' ? '모임 게시글' : '채팅 메세지';
            document.getElementById('report-content').textContent = reason;
            document.getElementById('report-date').textContent = createdAt;
            document.getElementById('report-status').textContent = activated ? '대기 중' : '처리 완료';
            document.getElementById('report-status').className = activated ? 'detail-value status pending' : 'detail-value status completed';
            document.querySelector('.history-tab.active').textContent = reportType==='POST' ? '게시글 내역' : '채팅 내역';

            if (activated==='false') {
                document.getElementById('detail-modal-footer').style.display = 'none';
            } else {
                document.getElementById('detail-modal-footer').style.display = 'flex';
            }

            const reportTitle = document.querySelector('.report-title');
            const contentTime = document.querySelector('.content-time');
            const reportContent = document.querySelector('.report-content');
            const detailBtn = document.querySelector('.detail-btn');

            let apiUrl;
            let detailPageUrl;

            if (reportType === 'CHAT') {
                apiUrl = `/api/admin/user/report/chat/${chatId}`;
                detailPageUrl = `/team/page/${teamId}`;
            } else if (reportType === 'POST') {
                apiUrl = `/api/admin/user/report/team/${teamId}`;
                detailPageUrl = `/team/detail/${teamId}`;
            } else {
                console.error('알 수 없는 reportType:', reportType);
                alert('알 수 없는 활동 내역 타입입니다.');
                return; // reportType 이 유효하지 않으면 여기서 종료
            }

            fetch(apiUrl, {
                method: 'GET',
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                return response.json();
            })
            .then(data => {
                const result = data.data;

                if (reportType === 'CHAT') {
                    reportTitle.textContent = result.senderNickname + '(' + result.senderId + ')';
                    contentTime.textContent = result.sendDate;
                    reportContent.textContent = result.message;
                } else if (reportType === 'POST') {
                    reportTitle.textContent = result.teamTitle;
                    contentTime.textContent = result.createdAt;
                    reportContent.textContent = result.teamDetails;
                }

                // 기존 이벤트 리스너 제거 (혹시 있다면)
                detailBtn.removeEventListener('click', openDetailPage);

                // 새로운 URL을 사용하는 이벤트 리스너 등록
                detailBtn.addEventListener('click', openDetailPage);

                // detailPageUrl을 함수 스코프에서 사용할 수 있도록 전역 변수 또는 데이터 속성으로 관리
                detailBtn.dataset.detailUrl = detailPageUrl;

                document.getElementById('reportViewModal').style.display = 'block';
            })
            .catch(error => {
                console.error('에러 발생:', error);
                alert('활동 내역 조회 중 문제가 발생했습니다.');
            });

        });
    });

    function openDetailPage() {
        const detailUrl = this.dataset.detailUrl;
        window.open(detailUrl, '_blank');
    }

    // 사용자 정지 버튼 클릭 이벤트 (신고 상세 모달에서)
    const showSuspendOptionsBtn = document.getElementById('show-suspend-options');
    if (showSuspendOptionsBtn) {
        showSuspendOptionsBtn.addEventListener('click', function() {
            // 신고 상세 모달에서 정보 가져오기
            const reportId = document.getElementById('report-id').textContent;
            const reportedId = document.getElementById('report-target').textContent;
            const nickname = document.getElementById('report-detail-nickname').value;
            const email = document.getElementById('report-detail-email').value;
            const status = document.getElementById('report-detail-status').value;
            const suspendDuration = document.getElementById('report-detail-suspend-duration').value;
            const dueReason = document.getElementById('report-detail-due-reason').value;

            document.getElementById('edit-user-id').value = reportedId;
            document.getElementById('edit-user-name').value = reportedId;
            document.getElementById('edit-user-nickname').value = nickname;
            document.getElementById('edit-user-email').value = email;
            document.getElementById('edit-user-status').value = status;
            document.getElementById('edit-suspend-duration').value = suspendDuration || "7";
            document.getElementById('edit-suspend-reason').value = dueReason;

            if (status === 'SUSPENDED') {
                suspendOptions.style.display = "block";
                durationSelect.disabled = false;
                reasonTextarea.disabled = false;
            } else if (status === 'BANNED') {
                suspendOptions.style.display = "block";
                durationSelect.disabled = true;
                reasonTextarea.disabled = false;
            }
            else {
                suspendOptions.style.display = "none";
                durationSelect.disabled = true;
                reasonTextarea.disabled = true;
            }

            // 신고 상세 모달 닫기
            document.getElementById('reportViewModal').style.display = 'none';

            // 정지 모달 표시
            document.getElementById('userEditModal').style.display = 'block';
        });
    }

    // 신고 해결 처리 버튼 클릭 이벤트
    document.querySelectorAll('.action-btn.resolve').forEach(button => {
        button.addEventListener('click', function () {
            const reportId = this.getAttribute('data-id');

            if (!confirm('정말로 이 신고를 처리 완료하시겠습니까?')) {
                return;
            }

            fetch(`/api/admin/user/report/${reportId}`, {
                method: 'PATCH',
            })
            .then(response => {
                if (response.ok) {
                    alert("신고 처리가 완료되었습니다.")
                    window.location.reload(); // 페이지 새로고침
                } else {
                    return response.text().then(text => { throw new Error(text) });
                }
            })
            .catch(error => {
                console.error('에러 발생:', error);
                alert('신고 처리 완료 중 문제가 발생했습니다.');
            });
        });
    });

    // 신고 상세 모달 내 신고 해결 처리 버튼 클릭 이벤트
    const resolveBtn = document.querySelector('.resolve-btn');
    if (resolveBtn) {
        resolveBtn.addEventListener('click', function() {
            const reportId = document.getElementById('report-id').textContent;

            if (!confirm('정말로 이 신고를 처리 완료하시겠습니까?')) {
                return;
            }

            fetch(`/api/admin/user/report/${reportId}`, {
                method: 'PATCH',
            })
            .then(response => {
                if (response.ok) {
                    alert("신고 처리가 완료되었습니다.")
                    document.getElementById('reportViewModal').style.display = 'none';
                    window.location.reload(); // 페이지 새로고침
                } else {
                    return response.text().then(text => { throw new Error(text) });
                }
            })
            .catch(error => {
                console.error('에러 발생:', error);
                alert('신고 처리 완료 중 문제가 발생했습니다.');
            });
        });
    }
});