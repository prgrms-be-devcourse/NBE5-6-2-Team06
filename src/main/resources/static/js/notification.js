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
      .then(async (response) => {
        if (response.ok) {
          alert("공지사항이 성공적으로 발송되었습니다.");
          createNotificationModal.style.display = 'none';
          notificationForm.reset();
          window.location.reload(); // 페이지 새로고침
        } else {
          const result = (await response.json()).data
          document.querySelector('#error-content').textContent = result.content;
          alert('필수 항목을 확인해주세요.');
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

  // 공지사항 비활성화(삭제) 버튼 클릭 이벤트
  document.querySelectorAll('.action-btn.delete').forEach(button => {
    button.addEventListener('click', function () {
      const noticeId = this.getAttribute('data-id');

      if (!confirm('정말로 이 공지사항을 비활성화하시겠습니까?')) {
        return;
      }

      fetch(`/api/admin/notification/${noticeId}`, {
        method: 'DELETE',
      })
      .then(response => {
        if (response.ok) {
          alert("공지사항이 비활성화되었습니다.");
          window.location.reload(); // 페이지 새로고침
        } else {
          return response.text().then(text => { throw new Error(text) });
        }
      })
      .catch(error => {
        console.error('에러 발생:', error);
        alert('공지사항 비활성화 중 문제가 발생했습니다.');
      });
    });
  });

  // 공지사항 발송 버튼 클릭 이벤트
  document.querySelectorAll('.action-btn.send').forEach(button => {
    button.addEventListener('click', function () {
      const noticeId = this.getAttribute('data-id');

      fetch(`/api/admin/notification/${noticeId}`, {
        method: 'POST',
      })
      .then(response => {
        if (response.ok) {
          alert("공지사항이 성공적으로 발송되었습니다.");
          window.location.reload(); // 페이지 새로고침
        } else {
          return response.text().then(text => { throw new Error(text) });
        }
      })
      .catch(error => {
        console.error('에러 발생:', error);
        alert('공지사항 발송 중 문제가 발생했습니다.');
      });
    });
  });

});