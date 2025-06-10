document.addEventListener('DOMContentLoaded', function () {
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
              display: false,
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
  const genderDistributionCtx = document.getElementById(
      'genderDistributionChart');
  if (genderDistributionCtx) {
    fetch('/api/admin/user/statistics/gender-distribution')
    .then(response => {
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      return response.json();
    })
    .then(data => {
      const result = data.data;
      const manCount = result.MAN;
      const womanCount = result.WOMAN;

      new Chart(genderDistributionCtx, {
        type: 'doughnut',
        data: {
          labels: ['남성', '여성'],
          datasets: [{
            label: '사용자 수',
            data: [manCount, womanCount],
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
    })
    .catch(error => {
      console.error('데이터를 가져오는 데 실패했습니다:', error);
    });
  }

  // 일일 이용 회원 수 차트
  const dailyActiveUsersCtx = document.getElementById('dailyActiveUsersChart');
  if (dailyActiveUsersCtx) {
    fetch('/api/admin/user-activity/statistics/week')
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
      new Chart(dailyActiveUsersCtx, {
        type: 'line',
        data: {
          labels: labels,
          datasets: [{
            label: '이용 회원 수',
            data: chartData,
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
              text: '일일 이용 회원 수'
            }
          },
          scales: {
            y: {
              beginAtZero: true
            }
          }
        }
      });
    });
  }

  // 사용자 취향 선호도
  const preferenceCountsCtx = document.getElementById('preferenceCountsChart');
  if (preferenceCountsCtx) {
    fetch('/api/admin/user/statistics/preference-counts')
    .then(response => {
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      return response.json();
    })
    .then(data => {
      const result = data.data;
      console.log(result)
      new Chart(preferenceCountsCtx, {
        type: 'bar',
        data: {
          labels: ['넓은 매장', '깨끗함', '다양한 메뉴', '좋은 음악', '사진', '대화', '좋은 뷰', '테라스', '오래 머물기', '다양한 술'],
          datasets: [{
            label: '선택 횟수',
            data: [
              result.bigStoreCount,
              result.cleanCount,
              result.goodMenuCount,
              result.goodMusicCount,
              result.goodPictureCount,
              result.goodTalkCount,
              result.goodViewCount,
              result.isTerraceCount,
              result.longStayCount,
              result.manyDrinkCount
            ],
            backgroundColor: [
              'rgba(255, 99, 132, 0.8)',    // Strong Red
              'rgba(54, 162, 235, 0.8)',    // Strong Blue
              'rgba(255, 205, 86, 0.8)',    // Strong Yellow
              'rgba(75, 192, 192, 0.8)',    // Strong Teal
              'rgba(153, 102, 255, 0.8)',   // Strong Purple
              'rgba(255, 159, 64, 0.8)',    // Strong Orange
              'rgba(0, 123, 255, 0.8)',     // Bright Blue
              'rgba(220, 53, 69, 0.8)',     // Crimson
              'rgba(40, 167, 69, 0.8)',     // Forest Green
              'rgba(253, 126, 20, 0.8)'     // Burnt Orange
            ],
            borderColor: [
              'rgba(255, 99, 132, 1)',
              'rgba(54, 162, 235, 1)',
              'rgba(255, 205, 86, 1)',
              'rgba(75, 192, 192, 1)',
              'rgba(153, 102, 255, 1)',
              'rgba(255, 159, 64, 1)',
              'rgba(0, 123, 255, 1)',
              'rgba(220, 53, 69, 1)',
              'rgba(40, 167, 69, 1)',
              'rgba(253, 126, 20, 1)'
            ],
            borderWidth: 1
          }]
        },
        options: {
          responsive: true,
          scales: {
            y: {
              beginAtZero: true,
            }
          },
          plugins: {
            legend: {
              display: false // 범례 숨김
            },
            title: {
              display: true,
              text: '사용자 취향 선호도'
            }
          }
        }
      });
    });
  }

  // 모임 성공률 추이 차트
  const meetingSuccessRateCtx = document.getElementById(
      'meetingSuccessRateChart');
  if (meetingSuccessRateCtx) {
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
      new Chart(meetingSuccessRateCtx, {
        type: 'line',
        data: {
          labels: labels,
          datasets: [{
            label: '성공률 (%)',
            data: chartData,
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
    })
  }

  // 모임 참가 인원 분포 차트
  const participantDistributionCtx = document.getElementById('participantDistributionChart');
  if (participantDistributionCtx) {
    fetch('/api/admin/team/statistics/participant-distribution')
    .then(response => {
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      return response.json();
    })
    .then(data => {
      const result = data.data
      const labels = ["2-3명", "4-5명", "6-7명", "8-10명"];
      const values = labels.map(label => result[label]);

      console.log(labels)
      console.log(values)
      new Chart(participantDistributionCtx, {
        type: 'bar',
        data: {
          labels: labels,
          datasets: [{
            label: '모임 수',
            data: values,
            backgroundColor: [
              'rgba(54, 162, 235, 0.7)',
              'rgba(255, 99, 132, 0.7)',
              'rgba(255, 206, 86, 0.7)',
              'rgba(75, 192, 192, 0.7)'
            ],
            borderColor: [
              'rgba(54, 162, 235, 1)',
              'rgba(255, 99, 132, 1)',
              'rgba(255, 206, 86, 1)',
              'rgba(75, 192, 192, 1)'
            ],
            borderWidth: 1
          }]
        },
        options: {
          responsive: true,
          scales: {
            y: {
              beginAtZero: true,
              ticks: {
                stepSize: 1
              }
            }
          },
          plugins: {
            legend: {
              display: false
            },
            title: {
              display: true,
              text: '모임 참가 인원 분포'
            }
          }
        }
      });
    });
  }

  // 최근 7일 일일 모임 생성 수 차트
  const dailyNewTeamsCtx = document.getElementById('dailyNewTeamsChart');
  if (dailyNewTeamsCtx) {
    fetch('/api/admin/team/statistics/daily-new-teams')
    .then(response => {
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      return response.json();
    })
    .then(data => {
      const result = data.data;
      const labels = Object.keys(result);
      const values = Object.values(result);

      new Chart(dailyNewTeamsCtx, {
        type: 'bar',
        data: {
          labels: labels,
          datasets: [{
            label: '생성된 모임 수',
            data: values,
            backgroundColor: 'rgba(75, 192, 192, 0.7)',
            borderColor: 'rgba(75, 192, 192, 1)',
            borderWidth: 1
          }]
        },
        options: {
          responsive: true,
          scales: {
            y: {
              beginAtZero: true,
              ticks: {
                stepSize: 1
              }
            }
          },
          plugins: {
            legend: {
              display: false
            },
            title: {
              display: true,
              text: '최근 7일 일일 모임 생성 수'
            }
          }
        }
      });
    });
  }

  const categoryDistributionCtx = document.getElementById('categoryDistributionChart');
  if (categoryDistributionCtx) {
    fetch('/api/admin/restaurant/statistics/category-distribution')
    .then(response => {
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      return response.json();
    })
    .then(data => {
      const result = data.data;
      const labels = Object.keys(result);
      const values = Object.values(result);
      const backgroundColors = [
        'rgba(255, 99, 132, 0.7)',
        'rgba(54, 162, 235, 0.7)',
        'rgba(255, 206, 86, 0.7)',
        'rgba(75, 192, 192, 0.7)',
        'rgba(153, 102, 255, 0.7)',
        'rgba(255, 159, 64, 0.7)'
        // 필요에 따라 더 많은 색상 추가
      ];
      const borderColors = [
        'rgba(255, 99, 132, 1)',
        'rgba(54, 162, 235, 1)',
        'rgba(255, 206, 86, 1)',
        'rgba(75, 192, 192, 1)',
        'rgba(153, 102, 255, 1)',
        'rgba(255, 159, 64, 1)'
        // 필요에 따라 더 많은 색상 추가
      ];

      new Chart(categoryDistributionCtx, {
        type: 'bar', // 파이 차트 대신 막대 그래프로 변경
        data: {
          labels: labels,
          datasets: [{
            label: '식당 수',
            data: values,
            backgroundColor: backgroundColors,
            borderColor: borderColors,
            borderWidth: 1
          }]
        },
        options: {
          responsive: true,
          scales: {
            y: {
              beginAtZero: true,
              ticks: {
                stepSize: 1
              }
            }
          },
          plugins: {
            legend: {
              display: false // 막대 그래프에서는 범례를 숨기는 것이 일반적
            },
            title: {
              display: true,
              text: '카테고리별 식당 분포'
            }
          }
        }
      });
    });
  }

  // 식당 분위기 선호도 차트
  const moodPreferenceCtx = document.getElementById('moodPreferenceChart');
  if (moodPreferenceCtx) {
    fetch('/api/admin/restaurant/statistics/mood-preference')
    .then(response => {
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      return response.json();
    })
    .then(data => {
      const result = data.data;
      const labels = Object.keys(result);
      const values = Object.values(result);
      const backgroundColors = [
        'rgba(255, 99, 132, 0.7)',
        'rgba(54, 162, 235, 0.7)',
        'rgba(255, 206, 86, 0.7)',
        'rgba(75, 192, 192, 0.7)',
        'rgba(153, 102, 255, 0.7)',
        'rgba(255, 159, 64, 0.7)',
        'rgba(120, 120, 120, 0.7)',
        'rgba(200, 200, 50, 0.7)',
        'rgba(50, 200, 200, 0.7)',
        'rgba(200, 50, 200, 0.7)'
      ];
      const borderColors = [
        'rgba(255, 99, 132, 1)',
        'rgba(54, 162, 235, 1)',
        'rgba(255, 206, 86, 1)',
        'rgba(75, 192, 192, 1)',
        'rgba(153, 102, 255, 1)',
        'rgba(255, 159, 64, 1)',
        'rgba(120, 120, 120, 1)',
        'rgba(200, 200, 50, 1)',
        'rgba(50, 200, 200, 1)',
        'rgba(200, 50, 200, 1)'
      ];

      new Chart(moodPreferenceCtx, {
        type: 'bar',
        data: {
          labels: labels,
          datasets: [{
            label: '식당 수',
            data: values,
            backgroundColor: backgroundColors,
            borderColor: borderColors,
            borderWidth: 1
          }]
        },
        options: {
          responsive: true,
          scales: {
            y: {
              beginAtZero: true,
              ticks: {
                stepSize: 1
              }
            }
          },
          plugins: {
            legend: {
              display: false
            },
            title: {
              display: true,
              text: '식당 분위기 선호도'
            }
          }
        }
      });
    });
  }

  // AI 추천 누적 Top 5 식당 차트
  const topRecommendedCtx = document.getElementById('topRecommendedChart');
  if (topRecommendedCtx) {
    fetch('/api/admin/restaurant/statistics/top-recommended')
    .then(response => {
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      return response.json();
    })
    .then(data => {
      const result = data.data;
      const labels = result.map(item => item.name);
      const values = result.map(item => item.recommendedCount);

      new Chart(topRecommendedCtx, {
        type: 'bar',
        data: {
          labels: labels,
          datasets: [{
            label: '추천 수',
            data: values,
            backgroundColor: 'rgba(255, 206, 86, 0.7)',
            borderColor: 'rgba(255, 206, 86, 1)',
            borderWidth: 1
          }]
        },
        options: {
          responsive: true,
          indexAxis: 'y',
          scales: {
            x: {
              beginAtZero: true,
              ticks: {
                stepSize: 10
              }
            }
          },
          plugins: {
            legend: {
              display: false
            },
            title: {
              display: true,
              text: 'AI 추천 누적 Top 5 식당'
            }
          }
        }
      });
    });
  }

  // 기간 필터 변경 이벤트
  const periodFilter = document.getElementById('period-filter');
  if (periodFilter) {
    periodFilter.addEventListener('change', function () {
      // 실제 구현에서는 서버에서 데이터를 다시 가져와야 함
      // 여기서는 예시로 알림만 표시
      alert('선택한 기간: ' + this.value + '\n실제 구현에서는 이 기간에 맞는 데이터로 차트가 업데이트됩니다.');
    });
  }

  const successPeriodFilter = document.getElementById('success-period-filter');
  if (successPeriodFilter) {
    successPeriodFilter.addEventListener('change', function () {
      // 실제 구현에서는 서버에서 데이터를 다시 가져와야 함
      // 여기서는 예시로 알림만 표시
      alert('선택한 기간: ' + this.value + '\n실제 구현에서는 이 기간에 맞는 데이터로 차트가 업데이트됩니다.');
    });
  }
});