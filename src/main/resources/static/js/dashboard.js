document.addEventListener('DOMContentLoaded', function() {
    // 사용자 활동 추이 차트
    const userActivityCtx = document.getElementById('userActivityChart');
    if (userActivityCtx) {
        fetch('/api/admin/user-activity/statistics/monthly')
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            const result = data.data;
            const labels = result.map(item => item.label);
            const chartData = result.map(item => item.value);

            new Chart(userActivityCtx, {
                type: 'line',
                data: {
                    labels: labels,
                    datasets: [{
                        label: '이용 회원 수',
                        data: chartData,
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
        })
        .catch(error => {
            console.error('데이터를 가져오는 중 오류가 발생했습니다:', error);
        });
    }
    
    // 모임 성공률 차트
    const meetingSuccessCtx = document.getElementById('meetingSuccessChart');
    if (meetingSuccessCtx) {
        fetch('/api/admin/team/statistics/success-rate/monthly')
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            const result = data.data;
            const labels = result.map(item => item.label);
            const chartData = result.map(item => item.value);

            new Chart(meetingSuccessCtx, {
                type: 'bar',
                data: {
                    labels: labels,
                    datasets: [{
                        label: '성공률 (%)',
                        data: chartData,
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
        })
        .catch(error => {
            console.error('모임 성공률 데이터를 가져오는 중 오류 발생:', error);
        });
    }
});