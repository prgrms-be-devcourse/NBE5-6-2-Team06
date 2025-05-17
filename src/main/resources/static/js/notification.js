document.addEventListener('DOMContentLoaded', () => {
  // 새 공지사항 작성 버튼 클릭 이벤트
  const createNotificationBtn = document.getElementById('create-notification-btn');
  const createNotificationModal = document.getElementById('create-notification-modal');

  if (createNotificationBtn && createNotificationModal) {
    createNotificationBtn.addEventListener('click', function() {
      createNotificationModal.style.display = 'block';
    });
  }

  // 공지사항 발송 버튼 클릭 이벤트
  const sendNotificationBtn = document.getElementById('send-notification-btn');
  const notificationForm = document.getElementById('notification-form');
  const closeModalBtn = createNotificationModal.querySelector('.close-modal');
  const cancelBtn = createNotificationModal.querySelector('.cancel-btn');

  if (sendNotificationBtn) {
    sendNotificationBtn.addEventListener('click', function() {
      const content = document.getElementById('notification-content').value.trim();
      const target = document.getElementById('notification-target').value;

      if (!content) {
        alert('메시지 내용을 입력해주세요.');
        return;
      }

      const notificationData = {
        content: content,
        target: target
      };

      // 서버로 데이터 전송
      window.auth.fetchWithAuth('/api/admin/notification/broadcast', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(notificationData)
      })
      .then(response => {
        if (response.ok) {
          alert('공지사항이 성공적으로 발송되었습니다.');
          createNotificationModal.style.display = 'none';
          notificationForm.reset();
          // 선택적: 공지사항 목록을 다시 불러오거나 UI 업데이트
        } else {
          console.error('공지사항 발송 실패:', response.status, response.statusText);
          alert('공지사항 발송에 실패했습니다.');
        }
      })
      .catch(error => {
        console.error('공지사항 발송 요청 오류:', error);
        alert('공지사항 발송 중 오류가 발생했습니다.');
      });
    });
  }

// 모달 닫기 이벤트
  if (closeModalBtn) {
    closeModalBtn.addEventListener('click', () => {
      createNotificationModal.style.display = 'none';
      notificationForm.reset();
    });
  }

  if (cancelBtn) {
    cancelBtn.addEventListener('click', () => {
      createNotificationModal.style.display = 'none';
      notificationForm.reset();
    });
  }

});