document.addEventListener('DOMContentLoaded', function() {
    const rankingModal = document.getElementById('rankingModal');
    const detailModal = document.getElementById('detailModal');
    const searchForm = document.querySelector('form[action="/admin/content-rankings"]');
    const titleSearch = document.getElementById('search-title');
    const activeFilter = document.getElementById('active-filter');
    const sortFilter = document.getElementById('sort-filter');

    const rankingModalTitle = document.getElementById('ranking-modal-title');
    const rankingForm = document.getElementById('rankingForm');
    const rankingId = document.getElementById('ranking-id');
    const rankingTitle = document.getElementById('ranking-title');
    const rankingSubtitle = document.getElementById('ranking-subtitle');
    const rankingStartDate = document.getElementById('ranking-start-date');
    const rankingEndDate = document.getElementById('ranking-end-date');
    const rankingIsActive = document.getElementById('ranking-is-active');
    const activeStatusText = document.getElementById('active-status-text');
    const rankingItemsContainer = document.getElementById('ranking-items-container');

    const addRankingBtn = document.getElementById('add-ranking-btn');
    const saveRankingBtn = document.getElementById('save-ranking-btn');
    const closeButtons = document.querySelectorAll('.close-modal, .cancel-btn');

    const editButtons = document.querySelectorAll('.action-btn.edit');
    const infoButtons = document.querySelectorAll('.action-btn.info');

    let isEditMode = false;

    init();

    function init() {
        addRankingBtn.addEventListener('click', openCreateModal);
        saveRankingBtn.addEventListener('click', saveRanking);
        rankingIsActive.addEventListener('change', updateActiveStatusText);

        closeButtons.forEach(button => {
            button.addEventListener('click', closeModals);
        });

        editButtons.forEach(button => {
            button.addEventListener('click', function() {
                const id = this.getAttribute('data-id');
                editRanking(id);
            });
        });

        infoButtons.forEach(button => {
            button.addEventListener('click', function() {
                const id = this.getAttribute('data-id');
                showRankingDetail(id);
            });
        });

        if (titleSearch) {
            titleSearch.addEventListener('keypress', function(e) {
                if (e.key === 'Enter') {
                    e.preventDefault();
                    searchForm.submit();
                }
            });
        }

        document.addEventListener('keydown', function(event) {
            if (event.key === 'Escape') {
                closeModals();
            }
        });

        window.addEventListener('click', function(event) {
            if (event.target === rankingModal || event.target === detailModal) {
                closeModals();
            }
        });

        highlightActiveFilters();
    }

    function openCreateModal() {
        isEditMode = false;
        rankingModalTitle.textContent = '새 랭킹 추가';
        rankingForm.reset();
        rankingId.value = '';
        rankingStartDate.value = new Date().toISOString().split('T')[0];
        rankingIsActive.checked = true;
        updateActiveStatusText();
        clearErrorMessages();

        rankingItemsContainer.innerHTML = '';
        addRankingItem();
        addRankingItem();

        rankingModal.style.display = 'block';
    }

    function editRanking(id) {
        isEditMode = true;
        rankingModalTitle.textContent = '랭킹 수정';
        clearErrorMessages();

        showLoading(true);

        fetch(`/api/admin/content-rankings/${id}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('랭킹 정보를 불러올 수 없습니다.');
                }
                return response.json();
            })
            .then(data => {
                rankingId.value = data.id;
                rankingTitle.value = data.title;
                rankingSubtitle.value = data.subtitle || '';
                rankingStartDate.value = data.startDate;
                rankingEndDate.value = data.endDate || '';
                rankingIsActive.checked = data.isActive;
                updateActiveStatusText();

                rankingItemsContainer.innerHTML = '';
                if (data.items && data.items.length > 0) {
                    data.items.forEach(item => {
                        addRankingItem(item);
                    });
                } else {
                    addRankingItem();
                }

                rankingModal.style.display = 'block';
            })
            .catch(error => {
                console.error('Error:', error);
                showErrorMessage('랭킹 정보를 불러오는데 실패했습니다.');
            })
            .finally(() => {
                showLoading(false);
            });
    }

    function showRankingDetail(id) {
        fetch(`/api/admin/content-rankings/${id}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('랭킹 정보를 불러올 수 없습니다.');
                }
                return response.json();
            })
            .then(data => {
                document.getElementById('detail-ranking-title').textContent = data.title;
                document.getElementById('detail-ranking-subtitle').textContent = data.subtitle || '';

                const startDate = formatDate(data.startDate);
                const endDate = data.endDate ? formatDate(data.endDate) : '무기한';
                document.getElementById('detail-ranking-period').textContent = `${startDate} ~ ${endDate}`;

                const statusSpan = document.getElementById('detail-ranking-status');
                statusSpan.textContent = data.isActive ? '활성' : '비활성';
                statusSpan.className = data.isActive ? 'status-badge active' : 'status-badge inactive';

                const tableBody = document.getElementById('detail-items-table-body');
                tableBody.innerHTML = '';

                if (data.items && data.items.length > 0) {
                    data.items.forEach(item => {
                        const row = document.createElement('tr');
                        row.innerHTML = `
                            <td class="text-center">${item.ranking}</td>
                            <td>${escapeHtml(item.itemName)}</td>
                            <td>${escapeHtml(item.description || '')}</td>
                            <td class="text-center">
                                <span class="status-badge ${item.isActive ? 'active' : 'inactive'}">
                                    ${item.isActive ? '활성' : '비활성'}
                                </span>
                            </td>
                        `;
                        tableBody.appendChild(row);
                    });
                } else {
                    const row = document.createElement('tr');
                    row.innerHTML = `<td colspan="4" class="text-center">등록된 항목이 없습니다.</td>`;
                    tableBody.appendChild(row);
                }

                detailModal.style.display = 'block';
            })
            .catch(error => {
                console.error('Error:', error);
                showErrorMessage('랭킹 정보를 불러오는데 실패했습니다.');
            });
    }

    function closeModals() {
        rankingModal.style.display = 'none';
        detailModal.style.display = 'none';
    }

    window.addRankingItem = function(itemData = null) {
        const itemCount = rankingItemsContainer.children.length + 1;

        const itemDiv = document.createElement('div');
        itemDiv.className = 'ranking-item-row';
        itemDiv.innerHTML = `
            <span class="ranking-number">${itemCount}</span>
            <div class="item-content">
                <div class="form-row">
                    <div class="form-group" style="width: 60%;">
                        <label>아이템명 <span class="required">*</span></label>
                        <input type="text" class="item-name" placeholder="예: 짜장면" 
                               value="${itemData ? escapeHtml(itemData.itemName) : ''}" required>
                    </div>
                    <div class="form-group" style="width: 40%;">
                        <label>상태</label>
                        <div class="switch-container">
                            <label class="toggle-switch">
                                <input type="checkbox" class="item-active" 
                                       ${itemData ? (itemData.isActive ? 'checked' : '') : 'checked'}>
                                <span class="toggle-slider"></span>
                            </label>
                            <span class="toggle-text ${itemData && !itemData.isActive ? 'inactive' : 'active'}">
                                ${itemData && !itemData.isActive ? '비활성화됨' : '활성화됨'}
                            </span>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <label>설명</label>
                    <textarea class="item-description" rows="2" placeholder="아이템에 대한 설명을 입력하세요">${itemData ? escapeHtml(itemData.description || '') : ''}</textarea>
                </div>
                <div class="item-controls">
                    <button type="button" class="remove-item-btn" onclick="removeRankingItem(this)">
                        <i class="fas fa-trash"></i> 삭제
                    </button>
                </div>
            </div>
        `;

        rankingItemsContainer.appendChild(itemDiv);
        updateRankingNumbers();

        const toggleInput = itemDiv.querySelector('.item-active');
        const toggleText = itemDiv.querySelector('.toggle-text');
        toggleInput.addEventListener('change', function() {
            if (this.checked) {
                toggleText.textContent = '활성화됨';
                toggleText.className = 'toggle-text active';
            } else {
                toggleText.textContent = '비활성화됨';
                toggleText.className = 'toggle-text inactive';
            }
        });
    };

    window.removeRankingItem = function(button) {
        const container = rankingItemsContainer;
        if (container.children.length <= 1) {
            showWarningMessage('최소 1개의 아이템이 필요합니다.');
            return;
        }

        button.closest('.ranking-item-row').remove();
        updateRankingNumbers();
    };

    function updateRankingNumbers() {
        const rows = rankingItemsContainer.querySelectorAll('.ranking-item-row');
        rows.forEach((row, index) => {
            row.querySelector('.ranking-number').textContent = index + 1;
        });
    }

    function collectRankingItems() {
        const items = [];
        const rows = rankingItemsContainer.querySelectorAll('.ranking-item-row');

        rows.forEach((row, index) => {
            const itemName = row.querySelector('.item-name').value.trim();
            const description = row.querySelector('.item-description').value.trim();
            const isActive = row.querySelector('.item-active').checked;

            if (itemName) {
                items.push({
                    ranking: index + 1,
                    itemName: itemName,
                    description: description || null,
                    isActive: isActive
                });
            }
        });

        return items;
    }

    function validateForm() {
        clearErrorMessages();
        let isValid = true;

        if (!rankingTitle.value.trim()) {
            showFieldError('error-title', '제목을 입력해주세요.');
            isValid = false;
        }

        if (!rankingStartDate.value) {
            showFieldError('error-start-date', '시작일을 선택해주세요.');
            isValid = false;
        }

        if (rankingEndDate.value && rankingStartDate.value) {
            if (new Date(rankingEndDate.value) < new Date(rankingStartDate.value)) {
                showFieldError('error-end-date', '종료일은 시작일보다 늦어야 합니다.');
                isValid = false;
            }
        }

        const items = collectRankingItems();
        if (items.length === 0) {
            showFieldError('error-items', '최소 1개의 아이템을 입력해주세요.');
            isValid = false;
        }

        return isValid;
    }

    function saveRanking() {
        if (!validateForm()) {
            return;
        }

        const items = collectRankingItems();
        const formData = {
            title: rankingTitle.value.trim(),
            subtitle: rankingSubtitle.value.trim() || null,
            startDate: rankingStartDate.value,
            endDate: rankingEndDate.value || null,
            isActive: rankingIsActive.checked,
            items: items
        };

        const url = isEditMode
            ? `/api/admin/content-rankings/${rankingId.value}`
            : '/api/admin/content-rankings';

        const method = isEditMode ? 'PUT' : 'POST';

        const originalText = saveRankingBtn.textContent;
        saveRankingBtn.disabled = true;
        saveRankingBtn.innerHTML = '<span class="spinner"></span> 저장 중...';

        fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(formData)
        })
            .then(response => {
                if (!response.ok) {
                    return response.text().then(text => {
                        throw new Error(text || '저장에 실패했습니다.');
                    });
                }
                return response.json();
            })
            .then(data => {
                showSuccessMessage(isEditMode ? '랭킹이 성공적으로 수정되었습니다.' : '랭킹이 성공적으로 생성되었습니다.');
                closeModals();

                setTimeout(() => {
                    location.reload();
                }, 1000);
            })
            .catch(error => {
                console.error('Error:', error);
                showErrorMessage('오류: ' + error.message);
            })
            .finally(() => {
                saveRankingBtn.disabled = false;
                saveRankingBtn.textContent = originalText;
            });
    }

    window.deleteRanking = function(id) {
        if (!confirm('정말로 이 랭킹을 삭제하시겠습니까?\n삭제된 랭킹과 관련 아이템들은 복구할 수 없습니다.')) {
            return;
        }

        fetch(`/api/admin/content-rankings/${id}`, {
            method: 'DELETE'
        })
            .then(response => {
                if (response.ok) {
                    showSuccessMessage('랭킹이 성공적으로 삭제되었습니다.');
                    setTimeout(() => {
                        location.reload();
                    }, 1000);
                } else {
                    throw new Error('삭제에 실패했습니다.');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                showErrorMessage('오류: ' + error.message);
            });
    };

    function updateActiveStatusText() {
        if (rankingIsActive.checked) {
            activeStatusText.textContent = '활성화됨';
            activeStatusText.className = 'toggle-text active';
        } else {
            activeStatusText.textContent = '비활성화됨';
            activeStatusText.className = 'toggle-text inactive';
        }
    }

    function clearErrorMessages() {
        const errorElements = document.querySelectorAll('.error-message');
        errorElements.forEach(element => {
            element.textContent = '';
        });

        const formGroups = document.querySelectorAll('.form-group');
        formGroups.forEach(group => {
            group.classList.remove('error');
        });
    }

    function showFieldError(fieldId, message) {
        const errorElement = document.getElementById(fieldId);
        if (errorElement) {
            errorElement.textContent = message;
            const formGroup = errorElement.closest('.form-group');
            if (formGroup) {
                formGroup.classList.add('error');
            }
        }
    }

    function showLoading(show) {
        const modalBody = rankingModal.querySelector('.modal-body');
        if (show) {
            modalBody.classList.add('loading');
        } else {
            modalBody.classList.remove('loading');
        }
    }

    function escapeHtml(text) {
        if (!text) return '';
        const div = document.createElement('div');
        div.textContent = text;
        return div.innerHTML;
    }

    function formatDate(dateString) {
        if (!dateString) return '';
        try {
            return new Date(dateString).toLocaleDateString('ko-KR');
        } catch (e) {
            return dateString;
        }
    }

    function showSuccessMessage(message) {
        alert(message);
    }

    function showErrorMessage(message) {
        alert(message);
    }

    function showWarningMessage(message) {
        alert(message);
    }

    function highlightActiveFilters() {
        const params = new URLSearchParams(window.location.search);

        if (activeFilter && params.get('active')) {
            activeFilter.classList.add('active-filter');
        }

        if (sortFilter && params.get('sort')) {
            sortFilter.classList.add('active-filter');
        }
    }

    window.removeFilter = function(filterName) {
        const url = new URL(window.location.href);
        url.searchParams.delete(filterName);
        window.location.href = url.toString();
    };
});