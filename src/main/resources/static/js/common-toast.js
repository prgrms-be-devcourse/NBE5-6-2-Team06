/**
 * 공통 토스트 메시지 처리 스크립트 (최적화 버전)
 */

// 즉시 실행 함수로 감싸서 초기화 로직 실행
(function() {
  // 페이지 로드 시 즉시 실행 (DOMContentLoaded 이벤트 대기 없이)
  checkAndDisplayToast();

  // DOMContentLoaded 이벤트 시에도 한 번 더 확인 (안전장치)
  document.addEventListener('DOMContentLoaded', checkAndDisplayToast);

  // 토스트 메시지가 있는지 확인하고 표시하는 함수
  function checkAndDisplayToast() {
    // 1. URL 파라미터에서 메시지 확인
    const urlParams = new URLSearchParams(window.location.search);
    const toastMessage = urlParams.get('toast_message');
    const toastType = urlParams.get('toast_type') || 'success';

    if (toastMessage) {
      // URL 디코딩
      const message = decodeURIComponent(toastMessage);

      // 토스트 표시 (약간의 지연으로 DOM이 완전히 로드된 후 실행)
      setTimeout(() => {
        showToast(message, toastType);
      }, 100);

      // 브라우저 히스토리에서 파라미터 제거 (URL 정리)
      const currentUrl = window.location.pathname;
      window.history.replaceState({}, document.title, currentUrl);
      return; // URL 파라미터로 메시지를 표시했으면 세션 확인 불필요
    }

    // 2. 세션 스토리지에서 메시지 확인
    const sessionMessage = sessionStorage.getItem('toast_message');
    const sessionType = sessionStorage.getItem('toast_type') || 'success';

    if (sessionMessage) {
      // 약간의 지연으로 DOM이 완전히 로드된 후 실행
      setTimeout(() => {
        showToast(sessionMessage, sessionType);
      }, 100);

      // 메시지 표시 후 세션 스토리지에서 제거
      sessionStorage.removeItem('toast_message');
      sessionStorage.removeItem('toast_type');
    }
  }
})();

/**
 * 토스트 메시지 표시 함수
 * @param {string} message - 표시할 메시지
 * @param {string} type - 메시지 타입 (success, error, warning, info)
 */
function showToast(message, type = 'success') {
  // Toastify가 로드되었는지 확인
  if (typeof Toastify !== 'function') {
    console.error('Toastify is not loaded');
    // 폴백: alert로 표시
    alert(message);
    return;
  }

  // 배경색 결정
  let backgroundColor;
  switch (type) {
    case 'success':
      backgroundColor = '#4CAF50';
      break;
    case 'error':
      backgroundColor = '#F44336';
      break;
    case 'warning':
      backgroundColor = '#FF9800';
      break;
    case 'info':
      backgroundColor = '#2196F3';
      break;
    default:
      backgroundColor = '#4CAF50';
  }

  // Toastify로 메시지 표시
  Toastify({
    text: message,
    duration: 3000,
    close: true,
    gravity: "top", // 상단 배치 유지
    position: "right",
    backgroundColor: backgroundColor,
    stopOnFocus: true,
    className: "custom-toast", // 커스텀 클래스 추가
    offset: { // 헤더 높이를 고려한 오프셋 추가
      y: 70 // 헤더 높이에 따라 조정 (px)
    }
  }).showToast();
}

/**
 * 페이지 이동 시 토스트 메시지를 함께 전달하는 함수 (URL 파라미터 사용)
 * @param {string} url - 이동할 URL
 * @param {string} message - 표시할 메시지
 * @param {string} type - 메시지 타입 (success, error, warning, info)
 */
function redirectWithToast(url, message, type = 'success') {
  // URL 인코딩
  const encodedMessage = encodeURIComponent(message);

  // 파라미터가 있는 URL인지 확인
  if (url.includes('?')) {
    window.location.href = `${url}&toast_message=${encodedMessage}&toast_type=${type}`;
  } else {
    window.location.href = `${url}?toast_message=${encodedMessage}&toast_type=${type}`;
  }
}

/**
 * 페이지 이동 시 토스트 메시지를 함께 전달하는 함수 (세션 스토리지 사용)
 * - 페이지 간 이동 시 가장 안정적인 방법
 * @param {string} url - 이동할 URL
 * @param {string} message - 표시할 메시지
 * @param {string} type - 메시지 타입 (success, error, warning, info)
 */
function redirectWithSessionToast(url, message, type = 'success') {
  // 세션 스토리지에 메시지 저장
  try {
    sessionStorage.setItem('toast_message', message);
    sessionStorage.setItem('toast_type', type);
  } catch (e) {
    console.error('Failed to save toast message to session storage:', e);
  }

  // 페이지 이동
  window.location.href = url;
}

/**
 * 즉시 토스트 메시지 표시 (현재 페이지)
 * @param {string} message - 표시할 메시지
 * @param {string} type - 메시지 타입 (success, error, warning, info)
 */
function showToastNow(message, type = 'success') {
  // 약간의 지연으로 DOM이 완전히 로드된 후 실행
  setTimeout(() => {
    showToast(message, type);
  }, 50);
}
