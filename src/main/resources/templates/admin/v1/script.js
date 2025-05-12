document.addEventListener('DOMContentLoaded', function() {
    // 네비게이션 처리
    const navItems = document.querySelectorAll('.nav-item');
    const pages = document.querySelectorAll('.page-content');

    navItems.forEach(item => {
        item.addEventListener('click', function() {
            const pageId = this.getAttribute('data-page');

            // 활성 네비게이션 아이템 업데이트
            navItems.forEach(nav => nav.classList.remove('active'));
            this.classList.add('active');

            // 페이지 표시
            pages.forEach(page => page.classList.remove('active'));
            document.getElementById(pageId).classList.add('active');
        });
    });

    // 탭 처리
    const tabs = document.querySelectorAll('.tab');

    tabs.forEach(tab => {
        tab.addEventListener('click', function() {
            const tabContainer = this.closest('.tab-container');
            const tabId = this.getAttribute('data-tab');

            // 활성 탭 업데이트
            tabContainer.querySelectorAll('.tab').forEach(t => t.classList.remove('active'));
            this.classList.add('active');

            // 탭 콘텐츠 표시
            tabContainer.querySelectorAll('.tab-content').forEach(content => content.classList.remove('active'));
            tabContainer.querySelector(`#${tabId}`).classList.add('active');
        });
    });

    // 히스토리 탭 처리
    const historyTabs = document.querySelectorAll('.history-tab');

    historyTabs.forEach(tab => {
        tab.addEventListener('click', function() {
            const historyContainer = this.closest('.user-history');
            const tabId = this.getAttribute('data-tab');

            // 활성 탭 업데이트
            historyContainer.querySelectorAll('.history-tab').forEach(t => t.classList.remove('active'));
            this.classList.add('active');

            // 탭 콘텐츠 표시
            historyContainer.querySelectorAll('.history-content').forEach(content => content.classList.remove('active'));
            historyContainer.querySelector(`#${tabId}`).classList.add('active');
        });
    });
    
    // 모달 처리
    const modals = document.querySelectorAll('.modal');
    const closeButtons = document.querySelectorAll('.close-modal, .cancel-btn');
    
    // 사용자 편집 모달 열기
    const editButtons = document.querySelectorAll('.action-btn.edit');
    editButtons.forEach(button => {
        button.addEventListener('click', function() {
            document.getElementById('userEditModal').style.display = 'block';
        });
    });
    
    // 사용자 정지 모달 열기
    const suspendButtons = document.querySelectorAll('.action-btn.suspend, .suspend-btn');
    suspendButtons.forEach(button => {
        button.addEventListener('click', function() {
            document.getElementById('suspendUserModal').style.display = 'block';
        });
    });
    
    // 신고 상세 모달 열기
    const viewReportButtons = document.querySelectorAll('.action-btn.view');
    viewReportButtons.forEach(button => {
        button.addEventListener('click', function() {
            document.getElementById('reportViewModal').style.display = 'block';
        });
    });
    
    // 모달 닫기
    closeButtons.forEach(button => {
        button.addEventListener('click', function() {
            modals.forEach(modal => {
                modal.style.display = 'none';
            });
        });
    });
    
    // 모달 외부 클릭 시 닫기
    window.addEventListener('click', function(event) {
        modals.forEach(modal => {
            if (event.target === modal) {
                modal.style.display = 'none';
            }
        });
    });
    
    // 슬라이더 값 표시
    const sliders = document.querySelectorAll('.slider');
    sliders.forEach(slider => {
        const valueDisplay = slider.nextElementSibling;
        slider.addEventListener('input', function() {
            valueDisplay.textContent = `${this.value}%`;
        });
    });
    
    // 차트 렌더링
    renderCharts();
});

// 차트 렌더링 함수
function renderCharts() {
    // 사용자 활동 추이 차트
    const userActivityCtx = document.getElementById('userActivityChart');
    if (userActivityCtx) {
        new Chart(userActivityCtx, {
            type: 'line',
            data: {
                labels: ['1월', '2월', '3월', '4월', '5월', '6월'],
                datasets: [{
                    label: '활성 사용자',
                    data: [650, 700, 750, 800, 850, 900],
                    borderColor: '#4a6cf7',
                    backgroundColor: 'rgba(74, 108, 247, 0.1)',
                    tension: 0.3,
                    fill: true
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: {
                        position: 'top',
                    }
                },
                scales: {
                    y: {
                        beginAtZero: true
                    }
                }
            }
        });
    }

    // 모임 성공률 차트
    const meetingSuccessCtx = document.getElementById('meetingSuccessChart');
    if (meetingSuccessCtx) {
        new Chart(meetingSuccessCtx, {
            type: 'bar',
            data: {
                labels: ['1월', '2월', '3월', '4월', '5월', '6월'],
                datasets: [{
                    label: '성공률 (%)',
                    data: [75, 78, 80, 82, 85, 88],
                    backgroundColor: '#4a6cf7'
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: {
                        position: 'top',
                    }
                },
                scales: {
                    y: {
                        beginAtZero: true,
                        max: 100
                    }
                }
            }
        });
    }

    // 연령대별 사용자 분포 차트
    const ageDistributionCtx = document.getElementById('ageDistributionChart');
    if (ageDistributionCtx) {
        new Chart(ageDistributionCtx, {
            type: 'pie',
            data: {
                labels: ['10대', '20대', '30대', '40대', '50대 이상'],
                datasets: [{
                    data: [5, 45, 30, 15, 5],
                    backgroundColor: [
                        '#4a6cf7',
                        '#6c757d',
                        '#28a745',
                        '#ffc107',
                        '#dc3545'
                    ]
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: {
                        position: 'right',
                    }
                }
            }
        });
    }

    // 성별 사용자 분포 차트
    const genderDistributionCtx = document.getElementById(
        'genderDistributionChart');
    if (genderDistributionCtx) {
        new Chart(genderDistributionCtx, {
            type: 'doughnut',
            data: {
                labels: ['남성', '여성', '기타'],
                datasets: [{
                    data: [55, 42, 3],
                    backgroundColor: [
                        '#4a6cf7',
                        '#dc3545',
                        '#6c757d'
                    ]
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: {
                        position: 'right',
                    }
                }
            }
        });
    }

    // 일일 활성 사용자 차트
    const dailyActiveUsersCtx = document.getElementById(
        'dailyActiveUsersChart');
    if (dailyActiveUsersCtx) {
        new Chart(dailyActiveUsersCtx, {
            type: 'line',
            data: {
                labels: ['월', '화', '수', '목', '금', '토', '일'],
                datasets: [{
                    label: '활성 사용자',
                    data: [120, 150, 180, 200, 250, 300, 220],
                    borderColor: '#28a745',
                    backgroundColor: 'rgba(40, 167, 69, 0.1)',
                    tension: 0.3,
                    fill: true
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: {
                        position: 'top',
                    }
                },
                scales: {
                    y: {
                        beginAtZero: true
                    }
                }
            }
        });
    }

    // 신규 가입자 추이 차트
    const newUsersCtx = document.getElementById('newUsersChart');
    if (newUsersCtx) {
        new Chart(newUsersCtx, {
            type: 'bar',
            data: {
                labels: ['월', '화', '수', '목', '금', '토', '일'],
                datasets: [{
                    label: '신규 가입자',
                    data: [15, 12, 18, 20, 25, 30, 22],
                    backgroundColor: '#17a2b8'
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: {
                        position: 'top',
                    }
                },
                scales: {
                    y: {
                        beginAtZero: true
                    }
                }
            }
        });
    }

    // 모임 성공률 추이 차트
    const meetingSuccessRateCtx = document.getElementById(
        'meetingSuccessRateChart');
    if (meetingSuccessRateCtx) {
        new Chart(meetingSuccessRateCtx, {
            type: 'line',
            data: {
                labels: ['1월', '2월', '3월', '4월', '5월', '6월'],
                datasets: [{
                    label: '성공률 (%)',
                    data: [75, 78, 80, 82, 85, 88],
                    borderColor: '#28a745',
                    backgroundColor: 'rgba(40, 167, 69, 0.1)',
                    tension: 0.3,
                    fill: true
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: {
                        position: 'top',
                    }
                },
                scales: {
                    y: {
                        beginAtZero: true,
                        max: 100
                    }
                }
            }
        });
    }

    // // 카테고리별 성공률 차트
    // const categorySuccessRateCtx = document.getElementById('categorySuccessRateChart');
    // if (categorySuccessRateCtx) {
    //     new Chart
}