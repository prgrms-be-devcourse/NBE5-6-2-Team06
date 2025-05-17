document.addEventListener('DOMContentLoaded', function () {
  document.getElementById(
      'profile-name').textContent = auth.getUserInfo().userId + "님";
  // 탭 처리
  const tabs = document.querySelectorAll('.tab');

  tabs.forEach(tab => {
    tab.addEventListener('click', function () {
      const tabContainer = this.closest('.tab-container');
      const tabId = this.getAttribute('data-tab');

      // 활성 탭 업데이트
      tabContainer.querySelectorAll('.tab').forEach(
          t => t.classList.remove('active'));
      this.classList.add('active');

      // 탭 콘텐츠 표시
      tabContainer.querySelectorAll('.tab-content').forEach(
          content => content.classList.remove('active'));
      tabContainer.querySelector(`#${tabId}`).classList.add('active');
    });
  });

  // 히스토리 탭 처리
  const historyTabs = document.querySelectorAll('.history-tab');

  historyTabs.forEach(tab => {
    tab.addEventListener('click', function () {
      const historyContainer = this.closest('.user-history');
      const tabId = this.getAttribute('data-tab');

      // 활성 탭 업데이트
      historyContainer.querySelectorAll('.history-tab').forEach(
          t => t.classList.remove('active'));
      this.classList.add('active');

      // 탭 콘텐츠 표시
      historyContainer.querySelectorAll('.history-content').forEach(
          content => content.classList.remove('active'));
      historyContainer.querySelector(`#${tabId}`).classList.add('active');
    });
  });

  // 모달 처리
  const modals = document.querySelectorAll('.modal');
  const closeButtons = document.querySelectorAll('.close-modal, .cancel-btn');

  // 모달 닫기
  closeButtons.forEach(button => {
    button.addEventListener('click', function () {
      modals.forEach(modal => {
        modal.scrollTop = 0;
        modal.style.display = 'none';
      });
    });
  });

  // 모달 외부 클릭 시 닫기
  window.addEventListener('click', function (event) {
    modals.forEach(modal => {
      if (event.target === modal) {
        modal.scrollTop = 0;
        modal.style.display = 'none';
      }
    });
  });
  // 알림 모달 처리
  const notificationBell = document.getElementById('notification-bell');
  const notificationModal = document.getElementById('notification-modal');
  const closeNotificationBtn = document.querySelector('.close-notification');

  // 알림 벨 클릭 시 모달 표시/숨김
  if (notificationBell && notificationModal) {
    notificationBell.addEventListener('click', function (event) {
      event.stopPropagation(); // 이벤트 버블링 방지

      // 모달 표시 상태 토글
      if (notificationModal.style.display === 'block') {
        notificationModal.style.display = 'none';
      } else {
        notificationModal.style.display = 'block';
      }
    });

    notificationModal.addEventListener('click', function(event) {
      event.stopPropagation();
    });
  }

  // 알림 모달 닫기 버튼
  if (closeNotificationBtn) {
    closeNotificationBtn.addEventListener('click', function () {
      notificationModal.style.display = 'none';
    });
  }

  // 알림 모달 외부 클릭 시 닫기
  document.addEventListener('click', function (event) {
    if (notificationModal && notificationModal.style.display === 'block') {
      if (!notificationModal.contains(event.target) && event.target
          !== notificationBell) {
        notificationModal.style.display = 'none';
      }
    }
  });

  // 알림 탭 처리
  const notificationTabs = document.querySelectorAll('.notification-tab');
  const notificationItems = document.querySelectorAll('.notification-item');

  notificationTabs.forEach(tab => {
    tab.addEventListener('click', function (event) {
      event.stopPropagation();

      // 활성 탭 업데이트
      notificationTabs.forEach(t => t.classList.remove('active'));
      this.classList.add('active');

      // 탭에 따라 알림 필터링
      const tabType = this.getAttribute('data-tab');

      notificationItems.forEach(item => {
        if (tabType === 'all') {
          item.style.display = 'flex';
        } else if (tabType === 'unread') {
          item.style.display = item.classList.contains('unread') ? 'flex'
              : 'none';
        } else if (tabType === 'system') {
          item.style.display = item.classList.contains('system') ? 'flex'
              : 'none';
        }
      });
    });
  });

  // 알림 읽음 표시 버튼
  const markReadButtons = document.querySelectorAll('.mark-read');

  markReadButtons.forEach(button => {
    button.addEventListener('click', function (event) {
      event.stopPropagation();
      const notificationItem = this.closest('.notification-item');

      // 읽음 표시 (unread 클래스 제거)
      notificationItem.classList.remove('unread');

      // 읽음 버튼을 삭제 버튼으로 변경
      const notificationActions = notificationItem.querySelector(
          '.notification-actions');
      notificationActions.innerHTML = `
                <button class="delete-notification" title="삭제"><i class="fas fa-trash"></i></button>
            `;

      // 새로운 삭제 버튼에 이벤트 리스너 추가
      setupDeleteButtons(
          notificationActions.querySelector('.delete-notification'));

      // 알림 카운트 업데이트 (실제 구현에서는 서버와 통신 필요)
      updateNotificationCount();
    });
  });

  // 모두 읽음 표시 버튼
  const markAllReadButton = document.querySelector('.mark-all-read');

  if (markAllReadButton) {
    markAllReadButton.addEventListener('click', function () {
      const unreadItems = document.querySelectorAll(
          '.notification-item.unread');

      unreadItems.forEach(item => {
        // 읽음 표시 (unread 클래스 제거)
        item.classList.remove('unread');

        // 읽음 버튼을 삭제 버튼으로 변경
        const notificationActions = item.querySelector('.notification-actions');
        notificationActions.innerHTML = `
                    <button class="delete-notification" title="삭제"><i class="fas fa-trash"></i></button>
                `;

        // 새로운 삭제 버튼에 이벤트 리스너 추가
        setupDeleteButtons(
            notificationActions.querySelector('.delete-notification'));
      });

      // 알림 카운트 업데이트 (실제 구현에서는 서버와 통신 필요)
      updateNotificationCount();
    });
  }

  // 알림 삭제 버튼
  const deleteButtons = document.querySelectorAll('.delete-notification');

  deleteButtons.forEach(button => {
    setupDeleteButtons(button);
  });

  // 삭제 버튼 이벤트 설정 함수
  function setupDeleteButtons(button) {
    button.addEventListener('click', function (event) {
      event.stopPropagation();
      const notificationItem = this.closest('.notification-item');

      // 알림 항목 삭제 (실제 구현에서는 서버와 통신 필요)
      notificationItem.remove();

      // 알림 카운트 업데이트 (필요한 경우)
      if (notificationItem.classList.contains('unread')) {
        updateNotificationCount();
      }
    });
  }

  // 알림 카운트 업데이트 함수
  function updateNotificationCount() {
    const unreadCount = document.querySelectorAll(
        '.notification-item.unread').length;
    const badgeElement = document.querySelector('#notification-bell .badge');

    if (badgeElement) {
      badgeElement.textContent = unreadCount;

      // 알림이 없으면 배지 숨기기 (선택적)
      if (unreadCount === 0) {
        badgeElement.style.display = 'none';
      } else {
        badgeElement.style.display = 'flex';
      }
    }
  }

  // 신고 알림 클릭 이벤트 (신고 관리 페이지로 이동)
  const reportNotification = document.getElementById('report-notification');

  if (reportNotification) {
    reportNotification.addEventListener('click', function () {
      // 실제 구현에서는 신고 관리 페이지로 이동
      window.location.href = '/admin/user-management';

      // 탭 전환 (실제 구현에서는 페이지 로드 후 처리 필요)
      // document.querySelector('.tab[data-tab="user-reports"]').click();
    });
  }
});