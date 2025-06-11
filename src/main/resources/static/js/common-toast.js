(function() {
  checkAndDisplayToast();

  document.addEventListener('DOMContentLoaded', checkAndDisplayToast);

  function checkAndDisplayToast() {
    // 1. URL 파라미터에서 메시지 확인
    const urlParams = new URLSearchParams(window.location.search);
    const toastMessage = urlParams.get('toast_message');
    const toastType = urlParams.get('toast_type') || 'success';

    if (toastMessage) {
      const message = decodeURIComponent(toastMessage);

      setTimeout(() => {
        showToast(message, toastType);
      }, 100);

      const currentUrl = window.location.pathname;
      window.history.replaceState({}, document.title, currentUrl);
      return;
    }

    // 2. 세션 스토리지에서 메시지 확인
    const sessionMessage = sessionStorage.getItem('toast_message');
    const sessionType = sessionStorage.getItem('toast_type') || 'success';

    if (sessionMessage) {
      setTimeout(() => {
        showToast(sessionMessage, sessionType);
      }, 100);

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
  if (typeof Toastify === 'function') {
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

    Toastify({
      text: message,
      duration: 3000,
      close: true,
      gravity: "top", // 상단 배치 유지
      position: "right",
      backgroundColor: backgroundColor,
      stopOnFocus: true,
      className: "custom-toast",
      offset: {
        y: 70
      }
    }).showToast();
  } else {
    console.warn('Toastify 라이브러리가 로드되지 않았습니다. toast 메시지를 표시할 수 없습니다.');
    createCustomToast(message, type);
  }
}

/**
 * Toastify 없이 기본 DOM 요소를 사용한 토스트 메시지 생성
 * @param {string} message - 표시할 메시지
 * @param {string} type - 메시지 타입 (success, error, warning, info)
 */
function createCustomToast(message, type = 'success') {
  let toastContainer = document.querySelector('.custom-toast-container');

  if (!toastContainer) {
    toastContainer = document.createElement('div');
    toastContainer.className = 'custom-toast-container';
    toastContainer.style.position = 'fixed';
    toastContainer.style.top = '70px';
    toastContainer.style.right = '20px';
    toastContainer.style.zIndex = '9999';
    document.body.appendChild(toastContainer);
  }

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

  const toast = document.createElement('div');
  toast.className = 'custom-toast';
  toast.style.backgroundColor = backgroundColor;
  toast.style.color = 'white';
  toast.style.padding = '12px 20px';
  toast.style.borderRadius = '4px';
  toast.style.boxShadow = '0 2px 10px rgba(0,0,0,0.2)';
  toast.style.marginBottom = '10px';
  toast.style.animation = 'fadeIn 0.3s, fadeOut 0.3s 2.7s';
  toast.style.minWidth = '250px';
  toast.style.maxWidth = '400px';
  toast.style.wordWrap = 'break-word';
  toast.textContent = message;

  const closeButton = document.createElement('span');
  closeButton.innerHTML = '&times;';
  closeButton.style.marginLeft = '10px';
  closeButton.style.float = 'right';
  closeButton.style.fontSize = '20px';
  closeButton.style.fontWeight = 'bold';
  closeButton.style.cursor = 'pointer';
  closeButton.onclick = function() {
    toast.remove();
  };

  toast.appendChild(closeButton);
  toastContainer.appendChild(toast);

  const style = document.createElement('style');
  style.textContent = `
    @keyframes fadeIn {
      from { opacity: 0; transform: translateY(-20px); }
      to { opacity: 1; transform: translateY(0); }
    }
    @keyframes fadeOut {
      from { opacity: 1; transform: translateY(0); }
      to { opacity: 0; transform: translateY(-20px); }
    }
  `;
  document.head.appendChild(style);

  setTimeout(() => {
    toast.remove();
  }, 3000);
}

/**
 * 페이지 이동 시 토스트 메시지를 함께 전달하는 함수 (URL 파라미터 사용)
 * @param {string} url - 이동할 URL
 * @param {string} message - 표시할 메시지
 * @param {string} type - 메시지 타입 (success, error, warning, info)
 */
function redirectWithToast(url, message, type = 'success') {
  const encodedMessage = encodeURIComponent(message);

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
  try {
    sessionStorage.setItem('toast_message', message);
    sessionStorage.setItem('toast_type', type);
  } catch (e) {
    console.error('Failed to save toast message to session storage:', e);
  }

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