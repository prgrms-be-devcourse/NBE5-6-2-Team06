document.addEventListener('DOMContentLoaded', function() {
    updateHeader();
    // updateLoginLogoutButton();

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

function updateHeader() {
    const headerLogin = document.getElementById('header-login');
    const headerAnonymous = document.getElementById('header-anonymous');

    if (headerAnonymous && headerLogin && auth.isLoggedIn()) {
        const userInfo = auth.getUserInfo();
        headerAnonymous.style.display = 'none';
        headerLogin.style.display = 'flex';
        document.getElementById('profile-name').textContent = userInfo.nickname + '님';
    } else if (headerAnonymous && headerLogin && !auth.isLoggedIn()){
        headerAnonymous.style.display = 'flex';
        headerLogin.style.display = 'none';
    }

}

function updateLoginLogoutButton() {
    const loginButton = document.querySelector('a[href="/templates/user/signin"]') ||
        document.querySelector('a[href*="signin"]');

    if (loginButton && auth.isLoggedIn()) {
        loginButton.textContent = '마이페이지';
        loginButton.href = '/user/mypage';
        loginButton.onclick = null;
    }

    const navMenu = document.querySelector('nav');
    if (navMenu && !loginButton) {
        const lastNavItem = navMenu.lastElementChild;

        const authLink = document.createElement('a');

        if (auth.isLoggedIn()) {
            authLink.textContent = '마이페이지';
            authLink.href = '/user/mypage';
        } else {
            authLink.textContent = '로그인';
            authLink.href = '/user/signin';
        }

        if (lastNavItem) {
            navMenu.insertAdjacentHTML('beforeend', ' | ');
            navMenu.appendChild(authLink);
        } else {
            navMenu.appendChild(authLink);
        }
    }
}