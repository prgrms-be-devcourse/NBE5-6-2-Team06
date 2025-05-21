document.addEventListener('DOMContentLoaded', function () {
  if (auth.isLoggedIn()){
    document.getElementById(
        'profile-name').textContent = auth.getUserInfo().nickname + "님";
  }

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
});