document.addEventListener('DOMContentLoaded', function() {
    // 사용자 활동 추이 차트
    const userActivityCtx = document.getElementById('userActivityChart');
    if (userActivityCtx) {
        fetch('/api/admin/dashboard/user/activity/monthly')
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            const result = data.data;
            const labels = result.map(item => item.activityMonth);
            const chartData = result.map(item => item.uniqueUserCount);

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
});