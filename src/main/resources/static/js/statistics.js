document.addEventListener('DOMContentLoaded', function() {
    // 연령대별 사용자 분포 차트
    const ageDistributionCtx = document.getElementById('ageDistributionChart');
    if (ageDistributionCtx) {
        fetch('/api/admin/user/statistics/age-distribution')
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            const result = data.data
            // 예: [{"ageGroup": "10대", "count": 0}, {"ageGroup": "20대", "count": 6}, ...]
            const labels = result.map(item => item.ageGroup); // ['10대', '20대', ...]
            const counts = result.map(item => item.count);    // [0, 6, ...]

            new Chart(ageDistributionCtx, {
                type: 'bar',
                data: {
                    labels: labels,
                    datasets: [{
                        label: '사용자 수',
                        data: counts,
                        backgroundColor: [
                            'rgba(255, 99, 132, 0.7)',
                            'rgba(54, 162, 235, 0.7)',
                            'rgba(255, 206, 86, 0.7)',
                            'rgba(75, 192, 192, 0.7)',
                            'rgba(153, 102, 255, 0.7)',
                            'rgba(255, 159, 64, 0.7)'
                        ],
                        borderColor: [
                            'rgba(255, 99, 132, 1)',
                            'rgba(54, 162, 235, 1)',
                            'rgba(255, 206, 86, 1)',
                            'rgba(75, 192, 192, 1)',
                            'rgba(153, 102, 255, 1)',
                            'rgba(255, 159, 64, 1)'
                        ],
                        borderWidth: 1
                    }]
                },
                options: {
                    responsive: true,
                    plugins: {
                        legend: {
                            position: 'top',
                        },
                        title: {
                            display: true,
                            text: '연령대별 사용자 분포'
                        }
                    },
                    scales: {
                        y: {
                            beginAtZero: true,
                            ticks: {
                                precision: 0
                            }
                        }
                    }
                }
            });
        })
        .catch(error => {
            console.error('연령대별 사용자 분포 데이터 가져오기 실패:', error);
        });
    }
    
    // 성별 사용자 분포 차트
    const genderDistributionCtx = document.getElementById('genderDistributionChart');
    if (genderDistributionCtx) {
        new Chart(genderDistributionCtx, {
            type: 'doughnut',
            data: {
                labels: ['남성', '여성'],
                datasets: [{
                    label: '사용자 수',
                    data: [685, 560],
                    backgroundColor: [
                        'rgba(54, 162, 235, 0.7)',
                        'rgba(255, 99, 132, 0.7)'
                    ],
                    borderColor: [
                        'rgba(54, 162, 235, 1)',
                        'rgba(255, 99, 132, 1)'
                    ],
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: {
                        position: 'top',
                    },
                    title: {
                        display: true,
                        text: '성별 사용자 분포'
                    }
                }
            }
        });
    }
    
    // 일일 활성 사용자 차트
    const dailyActiveUsersCtx = document.getElementById('dailyActiveUsersChart');
    if (dailyActiveUsersCtx) {
        new Chart(dailyActiveUsersCtx, {
            type: 'line',
            data: {
                labels: ['월', '화', '수', '목', '금', '토', '일'],
                datasets: [{
                    label: '활성 사용자',
                    data: [85, 92, 88, 95, 120, 135, 110],
                    borderColor: 'rgba(75, 192, 192, 1)',
                    backgroundColor: 'rgba(75, 192, 192, 0.2)',
                    tension: 0.3,
                    fill: true
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: {
                        position: 'top',
                    },
                    title: {
                        display: true,
                        text: '일일 활성 사용자'
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
            type: 'line',
            data: {
                labels: ['1월', '2월', '3월', '4월', '5월', '6월'],
                datasets: [{
                    label: '신규 가입자',
                    data: [65, 59, 80, 81, 56, 55],
                    borderColor: 'rgba(153, 102, 255, 1)',
                    backgroundColor: 'rgba(153, 102, 255, 0.2)',
                    tension: 0.3,
                    fill: true
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: {
                        position: 'top',
                    },
                    title: {
                        display: true,
                        text: '신규 가입자 추이'
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
    const meetingSuccessRateCtx = document.getElementById('meetingSuccessRateChart');
    if (meetingSuccessRateCtx) {
        new Chart(meetingSuccessRateCtx, {
            type: 'line',
            data: {
                labels: ['1월', '2월', '3월', '4월', '5월', '6월'],
                datasets: [{
                    label: '성공률 (%)',
                    data: [75, 78, 80, 82, 85, 88],
                    borderColor: 'rgba(54, 162, 235, 1)',
                    backgroundColor: 'rgba(54, 162, 235, 0.2)',
                    tension: 0.3,
                    fill: true
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: {
                        position: 'top',
                    },
                    title: {
                        display: true,
                        text: '모임 성공률 추이'
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
    
    // 카테고리별 성공률 차트
    const categorySuccessRateCtx = document.getElementById('categorySuccessRateChart');
    if (categorySuccessRateCtx) {
        new Chart(categorySuccessRateCtx, {
            type: 'bar',
            data: {
                labels: ['한식', '중식', '일식', '양식', '분식', '디저트'],
                datasets: [{
                    label: '성공률 (%)',
                    data: [90, 85, 88, 82, 78, 75],
                    backgroundColor: [
                        'rgba(255, 99, 132, 0.7)',
                        'rgba(54, 162, 235, 0.7)',
                        'rgba(255, 206, 86, 0.7)',
                        'rgba(75, 192, 192, 0.7)',
                        'rgba(153, 102, 255, 0.7)',
                        'rgba(255, 159, 64, 0.7)'
                    ],
                    borderColor: [
                        'rgba(255, 99, 132, 1)',
                        'rgba(54, 162, 235, 1)',
                        'rgba(255, 206, 86, 1)',
                        'rgba(75, 192, 192, 1)',
                        'rgba(153, 102, 255, 1)',
                        'rgba(255, 159, 64, 1)'
                    ],
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: {
                        position: 'top',
                    },
                    title: {
                        display: true,
                        text: '카테고리별 성공률'
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
    
    // 인원수별 성공률 차트
    const memberCountSuccessRateCtx = document.getElementById('memberCountSuccessRateChart');
    if (memberCountSuccessRateCtx) {
        new Chart(memberCountSuccessRateCtx, {
            type: 'bar',
            data: {
                labels: ['2-4명', '5-8명', '9-12명', '13명 이상'],
                datasets: [{
                    label: '성공률 (%)',
                    data: [92, 85, 75, 65],
                    backgroundColor: 'rgba(75, 192, 192, 0.7)',
                    borderColor: 'rgba(75, 192, 192, 1)',
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: {
                        position: 'top',
                    },
                    title: {
                        display: true,
                        text: '인원수별 성공률'
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
    
    // 기간 필터 변경 이벤트
    const periodFilter = document.getElementById('period-filter');
    if (periodFilter) {
        periodFilter.addEventListener('change', function() {
            // 실제 구현에서는 서버에서 데이터를 다시 가져와야 함
            // 여기서는 예시로 알림만 표시
            alert('선택한 기간: ' + this.value + '\n실제 구현에서는 이 기간에 맞는 데이터로 차트가 업데이트됩니다.');
        });
    }
    
    const successPeriodFilter = document.getElementById('success-period-filter');
    if (successPeriodFilter) {
        successPeriodFilter.addEventListener('change', function() {
            // 실제 구현에서는 서버에서 데이터를 다시 가져와야 함
            // 여기서는 예시로 알림만 표시
            alert('선택한 기간: ' + this.value + '\n실제 구현에서는 이 기간에 맞는 데이터로 차트가 업데이트됩니다.');
        });
    }
});