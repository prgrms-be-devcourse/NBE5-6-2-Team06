document.addEventListener('DOMContentLoaded', function() {
    // 모임 상세 보기 버튼 클릭 이벤트
    const viewButtons = document.querySelectorAll('.action-btn.view');
    viewButtons.forEach(button => {
        button.addEventListener('click', function() {
            const teamId = this.getAttribute('data-id');
            
            // 실제 구현에서는 서버에서 데이터를 가져와야 함
            // 여기서는 예시 데이터로 모달 채우기
            document.getElementById('team-title').textContent = '강남 맛집 탐방';
            document.getElementById('team-organizer').textContent = '주최자: 김철수';
            document.getElementById('team-members').textContent = '인원: 6/8';
            document.getElementById('team-date').textContent = '날짜: 2023-06-20';
            document.getElementById('team-time').textContent = '시간: 18:30';
            document.getElementById('team-location').textContent = '장소: 서울 강남구';
            document.getElementById('team-restaurant').textContent = '식당: 맛있는 치킨';
            document.getElementById('team-category').textContent = '카테고리: 한식';
            document.getElementById('team-status').textContent = '상태: 모집 중';
            document.getElementById('team-status').className = 'status recruiting';
            document.getElementById('team-description-text').textContent = '강남 맛집을 함께 탐방하는 모임입니다. 맛있는 음식을 좋아하는 분들과 함께 즐거운 시간을 보내고 싶습니다. 많은 참여 부탁드립니다.';
            
            // 참가자 목록 예시 데이터 (실제로는 서버에서 가져온 데이터로 채워야 함)
            const membersList = document.getElementById('team-members-list');
            membersList.innerHTML = `
                <tr>
                    <td>1</td>
                    <td>김철수</td>
                    <td>kim@example.com</td>
                    <td>2023-06-15</td>
                </tr>
                <tr>
                    <td>2</td>
                    <td>이영희</td>
                    <td>lee@example.com</td>
                    <td>2023-06-16</td>
                </tr>
                <tr>
                    <td>3</td>
                    <td>박지민</td>
                    <td>park@example.com</td>
                    <td>2023-06-17</td>
                </tr>
                <tr>
                    <td>4</td>
                    <td>최민수</td>
                    <td>choi@example.com</td>
                    <td>2023-06-17</td>
                </tr>
                <tr>
                    <td>5</td>
                    <td>정수진</td>
                    <td>jung@example.com</td>
                    <td>2023-06-18</td>
                </tr>
                <tr>
                    <td>6</td>
                    <td>강동원</td>
                    <td>kang@example.com</td>
                    <td>2023-06-18</td>
                </tr>
            `;
            
            // 모달 표시
            document.getElementById('teamViewModal').style.display = 'block';
        });
    });
    
    // 모임 상태 변경 버튼 클릭 이벤트
    const editStatusBtn = document.getElementById('edit-team-status-btn');
    if (editStatusBtn) {
        editStatusBtn.addEventListener('click', function() {
            // 모임 상세 모달에서 정보 가져오기
            const teamTitle = document.getElementById('team-title').textContent;
            const teamStatus = document.getElementById('team-status').textContent.replace('상태: ', '');
            
            // 상태 변경 모달에 정보 설정
            document.getElementById('team-name-display').value = teamTitle;
            
            // 현재 상태에 맞게 선택 옵션 설정
            const statusSelect = document.getElementById('team-status-select');
            if (teamStatus === '모집 중') {
                statusSelect.value = 'recruiting';
            } else if (teamStatus === '모집 마감') {
                statusSelect.value = 'closed';
            } else if (teamStatus === '완료') {
                statusSelect.value = 'completed';
            } else if (teamStatus === '취소') {
                statusSelect.value = 'cancelled';
            }
            
            // 모임 상세 모달 닫기
            document.getElementById('teamViewModal').style.display = 'none';
            
            // 상태 변경 모달 표시
            document.getElementById('teamStatusModal').style.display = 'block';
        });
    }
    
    // 상태 저장 버튼 클릭 이벤트
    const saveStatusBtn = document.getElementById('save-status-btn');
    if (saveStatusBtn) {
        saveStatusBtn.addEventListener('click', function() {
            // 선택한 상태 가져오기
            const statusSelect = document.getElementById('team-status-select');
            const selectedStatus = statusSelect.value;
            const statusText = statusSelect.options[statusSelect.selectedIndex].text;
            
            // 알림 여부 확인
            const notifyMembers = document.getElementById('notify-members').checked;
            
            // 실제 구현에서는 서버로 데이터 전송
            // 여기서는 모달 닫기만 수행
            document.getElementById('teamStatusModal').style.display = 'none';
            
            // 성공 메시지 (실제 구현에서는 서버 응답 후 표시)
            let message = '모임 상태가 "' + statusText + '"(으)로 변경되었습니다.';
            if (notifyMembers) {
                message += ' 참가자들에게 알림이 전송되었습니다.';
            }
            
            alert(message);
            
            // 페이지 새로고침 (실제 구현에서는 필요에 따라 수행)
            // window.location.reload();
        });
    }
    
    // 모임 수정 버튼 클릭 이벤트 (상태 변경 모달로 연결)
    const editButtons = document.querySelectorAll('.action-btn.edit');
    editButtons.forEach(button => {
        button.addEventListener('click', function() {
            const teamId = this.getAttribute('data-id');
            
            // 실제 구현에서는 서버에서 데이터를 가져와야 함
            // 여기서는 예시 데이터로 모달 채우기
            document.getElementById('team-name-display').value = '강남 맛집 탐방';
            document.getElementById('team-status-select').value = 'recruiting';
            
            // 상태 변경 모달 표시
            document.getElementById('teamStatusModal').style.display = 'block';
        });
    });
    
    // 모임 삭제 버튼 클릭 이벤트
    const deleteButtons = document.querySelectorAll('.action-btn.delete');
    deleteButtons.forEach(button => {
        button.addEventListener('click', function() {
            const teamId = this.getAttribute('data-id');
            
            if (confirm('정말로 이 모임을 삭제하시겠습니까?')) {
                // 실제 구현에서는 서버로 삭제 요청을 보내야 함
                // 여기서는 예시로 알림만 표시
                alert('모임이 삭제되었습니다.');
                
                // 페이지 새로고침 (실제 구현에서는 필요에 따라 수행)
                // window.location.reload();
            }
        });
    });
});