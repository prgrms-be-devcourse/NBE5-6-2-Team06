document.addEventListener('DOMContentLoaded', function () {
  // 모임 상세 보기 버튼 클릭 이벤트
  const viewButtons = document.querySelectorAll('.action-btn.view');
  viewButtons.forEach(button => {
    button.addEventListener('click', function () {
      const teamId = this.getAttribute('data-id');
      document.getElementById('view-team-id').value = teamId;

      fetch(`/api/admin/team/${teamId}`)
      .then(response => {
        if (!response.ok) {
          throw new Error('팀 정보를 불러올 수 없습니다.');
        }
        return response.json();
      }).then(data => {
        const result = data.data;
        console.log(result);
        // 모달 내용 채우기
        document.getElementById('team-title').textContent = result.teamTitle;
        document.getElementById(
            'team-organizer').textContent = result.organizerUserId;
        document.getElementById('team-members').textContent = result.nowPeople
            + '/' + result.maxPeople;
        document.getElementById('team-date').textContent = result.teamDate;
        document.getElementById('team-time').textContent = result.teamTime;
        document.getElementById(
            'team-location').textContent = result.restaurantName;
        document.getElementById(
            'team-status').textContent = result.statusKoreanName;
        document.getElementById('team-status').className = 'status '
            + result.status;
        document.getElementById(
            'team-description-text').textContent = result.teamDetails;
        document.getElementById('team-image').src = result.imageUrl;
        document.getElementById('team-address').textContent = result.address;
        document.getElementById('team-category').textContent = result.category;

      });

      fetch(`/api/admin/team/participant/${teamId}`)
      .then(response => {
        if (!response.ok) {
          throw new Error('팀 정보를 불러올 수 없습니다.');
        }
        return response.json();
      }).then(data => {
        const result = data.data;
        console.log(result);
        const organizerList = document.getElementById("organizer-list");
        const memberList = document.getElementById("member-list");
        const pendingList = document.getElementById("pending-list");
        const rejectedList = document.getElementById("rejected-list");

        organizerList.innerHTML = "";
        memberList.innerHTML = "";
        pendingList.innerHTML = "";
        rejectedList.innerHTML = "";

        result.forEach((participant) => {
          console.log(participant);
          const row = document.createElement("tr");
          row.innerHTML = `
            <td>${participant.userId}</td>
            <td>${participant.nickname}</td>
            <td>${participant.email}</td>
            <td>${participant.createdDate}</td>
          `;

          switch (participant.participantStatus) {
            case "APPROVED":
              if (participant.role === 'LEADER') {
                organizerList.appendChild(row);
              } else {
                memberList.appendChild(row);
              }
              break;
            case "PENDING":
              pendingList.appendChild(row);
              break;
            case "REJECTED":
              rejectedList.appendChild(row);
              break;
          }
        });
      });

      // 모달 표시
      document.getElementById('teamViewModal').style.display = 'block';
    });
  });

  const detailBtn = document.querySelector('#teamViewModal .detail-btn');
  if (detailBtn) {
    detailBtn.addEventListener('click', function () {
      const teamId = document.getElementById('view-team-id').value;

      if (teamId) {
        window.open(`/team/detail/${teamId}`, '_blank');
      } else {
        alert('팀 ID가 없습니다.');
      }
    });
  }

  // 모임 상태 변경 버튼 클릭 이벤트
  const editStatusBtn = document.querySelectorAll('.action-btn.edit');
  editStatusBtn.forEach(button => {
    button.addEventListener('click', function () {
      const teamId = this.getAttribute('data-id');
      const teamStatus = this.getAttribute('data-status');
      const title = this.getAttribute('data-title');

      document.getElementById('team-id').value = teamId;
      document.getElementById('team-prev-status').value = teamStatus;
      document.getElementById('team-name-display').value = title;

      document.getElementById('status-change-reason').value = '';

      // 현재 상태에 맞게 선택 옵션 설정
      const statusSelect = document.getElementById('team-status-select');
      switch (teamStatus) {
        case 'RECRUITING':
          statusSelect.value = 'RECRUITING';
          break;
        case 'FULL':
          statusSelect.value = 'FULL';
          break;
        case 'COMPLETED':
          statusSelect.value = 'COMPLETED';
          break;
        case 'CANCELED':
          statusSelect.value = 'CANCELED';
          break;
        default:
          statusSelect.value = ''; // 또는 기본값
      }

      // 상태 변경 모달 표시
      document.getElementById('teamStatusModal').style.display = 'block';
    });
  })

  // 상태 저장 버튼 클릭 이벤트
  const saveStatusBtn = document.getElementById('save-status-btn');
  if (saveStatusBtn) {
    saveStatusBtn.addEventListener('click', function () {
      const teamId = document.getElementById('team-id').value;
      const prevTeamStatus = document.getElementById('team-prev-status').value;

      // 선택한 상태 가져오기
      const statusSelect = document.getElementById('team-status-select');
      const selectedStatus = statusSelect.value;
      const changeReason = document.getElementById('status-change-reason').value;
      const statusText = statusSelect.options[statusSelect.selectedIndex].text;

      if (prevTeamStatus === selectedStatus) {
        alert("동일한 상태입니다.");
        return;
      }

      fetch(`/api/admin/team/${teamId}`, {
        method: 'PATCH',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          status: selectedStatus,
          reason: changeReason
        })
      }).then(response => {
        if (response.ok) {
          alert("모임 상태가 변경되었습니다.")
          document.getElementById('teamStatusModal').style.display = 'none';
          window.location.reload(); // 페이지 새로고침
        } else {
          return response.text().then(text => { throw new Error(text) });
        }
      })
      .catch(error => {
        console.error('에러 발생:', error);
        alert('모임 상태 변경 중 문제가 발생했습니다.');
      });
    });
  }

  // 모임 삭제 버튼 클릭 이벤트
  const deleteButtons = document.querySelectorAll('.action-btn.delete');
  deleteButtons.forEach(button => {
    button.addEventListener('click', function () {
      const teamId = this.getAttribute('data-id');

      if (!confirm('정말로 이 모임을 비활성화하시겠습니까?')) {
        return;
      }

      fetch(`/api/admin/team/${teamId}`, {
        method: 'DELETE',
      })
      .then(response => {
        if (response.ok) {
          alert("모임이 비활성화되었습니다.")
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
});