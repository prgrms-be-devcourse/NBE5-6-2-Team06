const auth = {
    isLoggedIn() {
        const token = localStorage.getItem('jwtToken');
        return !!token;
    },

    getUserInfo() {
        if (!this.isLoggedIn()) return null;

        return {
            userId: localStorage.getItem('userId'),
            role: localStorage.getItem('userRole'),
            token: localStorage.getItem('jwtToken')
        };
    },

    getToken() {
        return localStorage.getItem('jwtToken');
    },

    logout() {
        localStorage.removeItem('jwtToken');
        localStorage.removeItem('userId');
        localStorage.removeItem('userRole');
        window.location.href = '/user/signin';
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
            headers
        });

        // SecurityConfig에서 권한처리 필요
        if (response.status === 401) {
            this.logout();
            return null;
        }

        return response;
    }
};

window.auth = auth;