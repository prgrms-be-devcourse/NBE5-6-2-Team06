(function(global) {
    const auth = {
        isLoggedIn() {
            return this.getCookie('jwtToken') !== null;
        },

    getUserInfo() {
        if (!this.isLoggedIn()) return null;

        return {
            nickname: this.getNickname('userNickname'),
            token: this.getCookie('jwtToken')
        };
    },

    getToken() {
        return this.getCookie('jwtToken');
    },

    setCookie(name, value, days = 1) {
        const date = new Date();
        date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
        const expires = "; expires=" + date.toUTCString();
        document.cookie = name + "=" + (value || "") + expires + "; path=/";
    },

    getCookie(name) {
        const nameEQ = name + "=";
        const ca = document.cookie.split(';');
        for (let i = 0; i < ca.length; i++) {
            let c = ca[i];
            while (c.charAt(0) === ' ') c = c.substring(1, c.length);
            if (c.indexOf(nameEQ) === 0) return c.substring(nameEQ.length, c.length);
        }
        return null;
    },

    getNickname(name) {
        const encodedNickname = this.getCookie('userNickname');
        return encodedNickname ? decodeURIComponent(encodedNickname) : null;
    },

    eraseCookie(name) {
        document.cookie = name + '=; Max-Age=-99999999; path=/';
    },

    logout(redirectUrl = '/') {
        this.eraseCookie('jwtToken');
        this.eraseCookie('userId');
        this.eraseCookie('userRole');
        window.location.href = redirectUrl;
    },

    getAuthHeaders() {
        const token = this.getToken();
        return token ? { 'Authorization': `Bearer ${token}` } : {};
    },

    async fetchWithAuth(url, options = {}) {
        const headers = {
            ...options.headers,
            ...this.getAuthHeaders()
        };

        const response = await fetch(url, {
            ...options,
            headers,
            credentials: 'same-origin'
        });

        if (response.status === 401) {
            this.logout();
            return null;
        }

        return response;
    }
};

    if (!global.auth) {
        global.auth = auth;
    }
})(window);
