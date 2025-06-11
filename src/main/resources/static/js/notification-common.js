class NotificationHandler {
    constructor() {
        this.isServerRestarting = false;
        this.eventSource = null;
        this.unreadCount = 0;
        this.notificationBell = document.getElementById('notification-bell');
        this.badge = this.notificationBell ? this.notificationBell.querySelector('.badge') : null;
        this.notificationModal = document.getElementById('notification-modal');
        this.notificationList = this.notificationModal ? this.notificationModal.querySelector('.notification-list') : null;
        this.tabs = document.querySelectorAll('.notification-tab');
        this.currentTab = 'all';
        this.notifications = [];
        this.markAllReadButton = this.notificationModal ? this.notificationModal.querySelector('.mark-all-read') : null;

        this.initialize();
    }

    async initialize() {
        await this.fetchUnreadCount();
        this.connectSSE();
        await this.fetchNotifications(this.currentTab);
        this.setupTabEventListeners();
        this.setupModalToggle();
        this.setupMarkAllReadButton();
    }

    setupModalToggle() {
        if (this.notificationBell && this.notificationModal) {
            this.notificationBell.addEventListener('click', (event) => {
                event.stopPropagation();
                this.notificationModal.style.display = this.notificationModal.style.display === 'block' ? 'none' : 'block';
            });

            this.notificationModal.addEventListener('click', (event) => {
                event.stopPropagation();
            });

            document.addEventListener('click', (event) => {
                if (this.notificationModal && this.notificationModal.style.display === 'block' && !this.notificationModal.contains(event.target) && event.target !== this.notificationBell) {
                    this.notificationModal.style.display = 'none';
                }
            });

            const closeButton = this.notificationModal.querySelector('.close-notification');
            if (closeButton) {
                closeButton.addEventListener('click', () => {
                    this.notificationModal.style.display = 'none';
                });
            }
        }
    }

    setupTabEventListeners() {
        this.tabs.forEach(tab => {
            tab.addEventListener('click', async () => {
                this.tabs.forEach(t => t.classList.remove('active'));
                tab.classList.add('active');
                this.currentTab = tab.getAttribute('data-tab');
                await this.fetchNotifications(this.currentTab);
            });
        });
    }

    setupMarkAllReadButton() {
        if (this.markAllReadButton) {
            this.markAllReadButton.addEventListener('click', async () => {
                const unreadNotifications = this.notifications.filter(n => !n.isRead);
                if (unreadNotifications.length > 0) {
                    const unreadIds = unreadNotifications.map(n => n.id);
                    try {
                        const response = await window.auth.fetchWithAuth(`/api/notification/mark-read`, {
                            method: 'POST',
                            headers: {
                                'Content-Type': 'application/json'
                            },
                            body: JSON.stringify({ notificationIds: unreadIds })
                        });
                        if (response && response.ok) {
                            this.notifications = this.notifications.map(n => ({ ...n, isRead: true }));
                            this.unreadCount = 0;
                            this.updateBadge();
                            this.renderNotifications();
                        } else {
                            console.error('모두 읽음 처리 실패:', response ? await response.text() : 'Network error');
                        }
                    } catch (error) {
                        console.error('모두 읽음 처리 요청 실패:', error);
                    }
                }
            });
        }
    }

    async fetchUnreadCount() {
        try {
            const response = await window.auth.fetchWithAuth('/api/notification/unread-count');
            if (response && response.ok) {
                this.unreadCount = (await response.json()).data;
                this.updateBadge();
            }
        } catch (error) {
            console.error('읽지 않은 알림 개수 가져오기 실패:', error);
        }
    }

    async fetchNotifications(tab) {
        let url = '/api/notification';
        if (tab === 'unread') {
            url = '/api/notification/unread';
        } else if (tab === 'system') {
            url = '/api/notification/system';
        }

        try {
            const response = await window.auth.fetchWithAuth(url);
            if (response && response.ok) {
                const data = (await response.json()).data;
                // console.log(data);
                this.notifications = data;
                this.renderNotifications();
            }
        } catch (error) {
            console.error('알림 목록 가져오기 실패:', error);
        }
    }

    renderNotifications() {
        if (this.notificationList) {
            // console.log('this.notifications:', this.notifications);
            this.notificationList.innerHTML = '';
            const unreadNotifications = this.notifications.filter(n => !n.isRead);
            const readNotifications = this.notifications.filter(n => n.isRead);

            // 읽지 않은 알림 렌더링
            unreadNotifications.forEach(notification => {
                const notificationItem = this.createNotificationItemElement(notification);
                this.notificationList.appendChild(notificationItem);
                this.setupNotificationItemEventListeners(notificationItem);
            });

            // 읽은 알림 렌더링
            readNotifications.forEach(notification => {
                const notificationItem = this.createNotificationItemElement(notification);
                notificationItem.classList.add('read');
                this.notificationList.appendChild(notificationItem);
                // 읽은 알림에는 삭제 버튼만 표시 (읽음 버튼 제거)
                const deleteButton = notificationItem.querySelector('.delete-notification');
                if (deleteButton) {
                    this.setupDeleteButton(deleteButton);
                }
            });
        }
    }

    // 알림 아이템 엘리먼트 생성 함수
    createNotificationItemElement(notification) {
        const wrapper = document.createElement(notification.link ? 'a' : 'div');

        wrapper.classList.add('notification-item', notification.type.toLowerCase());
        if (notification.isRead) {
            wrapper.classList.add('read');
        } else {
            wrapper.classList.add('unread');
        }

        if (notification.link) {
            wrapper.href = notification.link;
        }

        wrapper.innerHTML = `
        <div class="notification-icon"> 
            ${this.getNotificationIcon(notification.type)}
        </div>
        <div class="notification-content">
            <p class="notification-text" title="${notification.message}">${notification.message}</p>
            <span class="notification-time">${this.formatTimeAgo(notification.createdAt)}</span>
        </div>
        <div class="notification-actions">
            ${notification.isRead
            ? `<button class="delete-notification" data-id="${notification.id}" title="삭제"><i class="fas fa-trash"></i></button>`
            : `<button class="mark-read" data-id="${notification.id}" title="읽음 표시"><i class="fas fa-check"></i></button>`
        }
        </div>
    `;

        // 액션 버튼에 이벤트 버블링 방지 처리
        const deleteBtn = wrapper.querySelector('.delete-notification');
        const markReadBtn = wrapper.querySelector('.mark-read');

        if (deleteBtn) {
            deleteBtn.addEventListener('click', (e) => {
                e.stopPropagation();
                e.preventDefault();
            });
        }

        if (markReadBtn) {
            markReadBtn.addEventListener('click', (e) => {
                e.stopPropagation();
                e.preventDefault();
            });
        }

        if (notification.link && !notification.isRead) {
            wrapper.addEventListener('click', async (e) => {
                try {
                    await this.markNotificationAsRead(notification.id);
                } catch (error) {
                    console.error('알림 클릭 시 읽음 처리 실패:', error);
                }
            });
        }


        return wrapper;
    }

    // 알림 아이템 이벤트 리스너 설정 함수
    setupNotificationItemEventListeners(notificationItem) {
        const markReadButton = notificationItem.querySelector('.mark-read');
        if (markReadButton) {
            this.setupMarkReadButton(markReadButton);
        }
        const deleteButton = notificationItem.querySelector('.delete-notification');
        if (deleteButton) {
            this.setupDeleteButton(deleteButton);
        }
    }

    formatTimeAgo(dateTimeString) {
        const now = new Date();
        const past = new Date(dateTimeString);
        const diffInSeconds = Math.floor((now - past) / 1000);

        if (diffInSeconds < 60) return `${diffInSeconds}초 전`;
        const diffInMinutes = Math.floor(diffInSeconds / 60);
        if (diffInMinutes < 60) return `${diffInMinutes}분 전`;
        const diffInHours = Math.floor(diffInMinutes / 60);
        if (diffInHours < 24) return `${diffInHours}시간 전`;
        return `${Math.floor(diffInHours / 24)}일 전`;
    }

    connectSSE() {
        this.eventSource = new EventSource('/api/sse/subscribe', {
            withCredentials: true,
            headers: window.auth.getAuthHeaders()
        });

        this.eventSource.onopen = () => {
            console.log('SSE 연결 성공');
        };

        this.eventSource.onerror = (error) => {
            console.error('SSE 연결 오류:', error);
            if (!this.isServerRestarting){
                setTimeout(() => this.connectSSE(), 3000);
            }
        };

        this.eventSource.addEventListener('unreadCount', (event) => {
            this.unreadCount = parseInt(event.data);
            this.updateBadge();
        });

        this.eventSource.addEventListener('newMessage', (event) => {
            const newNotification = JSON.parse(event.data);
            this.notifications.unshift(newNotification);
            this.renderNotifications();
            this.unreadCount++;
            this.updateBadge();
            showToastNow(newNotification.message, 'info');
        });

        this.eventSource.addEventListener('shutdown', (event) => {
            console.log("서버가 종료 중입니다:", event.data);
            this.isServerRestarting = true;
            this.eventSource.close();  // SSE 연결 종료
            this.eventSource = null;
        });

        // 페이지 언로드(새로고침/탭 닫기) 직전에 SSE 연결 닫기
        window.addEventListener('beforeunload', () => {
            if (this.eventSource) {
                console.log("beforeunload : SSE 연결 종료")
                this.eventSource.close();
                this.eventSource = null;
            }
        });
    }

    getNotificationIcon(type) {
        switch (type) {
            case 'USER_STATUS': return '<i class="fas fa-user-check"></i>';
            case 'TEAM_STATUS': return '<i class="fas fa-users"></i>';
            case 'NOTICE': return '<i class="fas fa-bullhorn"></i>';
            case 'PARTICIPANT_STATUS': return '<i class="fas fa-user-plus"></i>';
            case 'REVIEW_REQUEST': return '<i class="fas fa-star"></i>';
            case 'REVIEW_RECEIVED': return '<i class="fas fa-reply"></i>';
            case 'REPORT': return '<i class="fas fa-flag"></i>';
            default: return '<i class="fas fa-bell"></i>';
        }
    }

    updateBadge() {
        if (this.badge) {
            this.badge.textContent = this.unreadCount > 99 ? '99+' : this.unreadCount;
            this.badge.style.display = this.unreadCount > 0 ? 'flex' : 'none';
        }
    }

    setupMarkReadButton(button) {
        button.addEventListener('click', async (event) => {
            event.stopPropagation();
            const notificationItem = button.closest('.notification-item');
            const notificationId = button.dataset.id;

            try {
                const response = await window.auth.fetchWithAuth(`/api/notification/mark-read`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({ notificationIds: [parseInt(notificationId)] })
                });
                if (response && response.ok) {
                    this.notifications = this.notifications.map(n =>
                        n.id.toString() === notificationId ? { ...n, isRead: true } : n
                    );
                    this.renderNotifications();
                    this.unreadCount--;
                    this.updateBadge();
                } else {
                    console.error('읽음 처리 실패:', response ? await response.text() : 'Network error');
                }
            } catch (error) {
                console.error('읽음 처리 요청 실패:', error);
            }
        });
    }

    setupDeleteButton(button) {
        button.addEventListener('click', async (event) => {
            event.stopPropagation();
            const notificationItem = button.closest('.notification-item');
            const notificationId = button.dataset.id;

            try {
                const response = await window.auth.fetchWithAuth(`/api/notification/${notificationId}`, {
                    method: 'DELETE'
                });
                if (response && response.ok) {
                    this.notifications = this.notifications.filter(n => n.id.toString() !== notificationId);
                    this.renderNotifications();
                    if (!notificationItem.classList.contains('unread')) {
                        this.fetchUnreadCount(); // 삭제 후 읽지 않은 개수 다시 가져오기
                    }
                } else {
                    console.error('알림 삭제 실패:', response ? await response.text() : 'Network error');
                }
            } catch (error) {
                console.error('알림 삭제 요청 실패:', error);
            }
        });
    }

    async markNotificationAsRead(notificationId) {
        const response = await window.auth.fetchWithAuth(`/api/notification/mark-read`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ notificationIds: [parseInt(notificationId)] })
        });

        if (response && response.ok) {
            this.notifications = this.notifications.map(n =>
                n.id === notificationId ? { ...n, isRead: true } : n
            );
            this.unreadCount--;
            this.updateBadge();
            this.renderNotifications();
        } else {
            throw new Error(await response.text());
        }
    }
}

document.addEventListener('DOMContentLoaded', () => {
    if (auth.isLoggedIn()) {
        new NotificationHandler();
    }
});