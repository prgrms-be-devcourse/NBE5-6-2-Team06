document.addEventListener('DOMContentLoaded', function() {
    updateHeader();
    updateLoginLogoutButton();
});

function updateHeader() {
    const headerButtons = document.querySelector('.header-buttons');

    const headerRight = document.querySelector('.header-right');

    if (headerButtons) {
        if (auth.isLoggedIn()) {
            const userInfo = auth.getUserInfo();

            headerButtons.innerHTML = `
                <span style="margin-right: 1rem;">${userInfo.userId}님</span>
                <span style="font-size: 1.2rem; cursor: pointer;">🔔</span>
                <span style="font-size: 1.2rem; cursor: pointer;">👤</span>
                <button class="btn btn-primary" onclick="location.href='/team/create'">맛남 모임 만들기</button>
                <button class="btn btn-outline" onclick="auth.logout()">로그아웃</button>
            `;
        }
    }

    if (headerRight) {
        if (auth.isLoggedIn()) {
            const userInfo = auth.getUserInfo();

            headerRight.innerHTML = `
                <span style="margin-right: 1rem;">${userInfo.userId}님</span>
                <span title="알림">🔔</span>
                <span title="프로필">👤</span>
                <button class="btn-primary" onclick="location.href='/meeting/create'">모임 만들기</button>
                <button class="btn-outline" onclick="auth.logout()">로그아웃</button>
            `;
        }
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