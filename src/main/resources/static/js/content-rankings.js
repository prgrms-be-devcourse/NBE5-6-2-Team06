document.addEventListener('DOMContentLoaded', function() {
    const rankingModal = document.getElementById('rankingModal');
    const itemsModal = document.getElementById('itemsModal');
    const itemModal = document.getElementById('itemModal');

    const searchForm = document.querySelector('form[action="/admin/content-rankings"]');
    const titleSearch = searchForm?.querySelector('input[name="title"]');
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

    const itemModalTitle = document.getElementById('item-modal-title');
    const itemForm = document.getElementById('itemForm');
    const itemId = document.getElementById('item-id');
    const itemRankingId = document.getElementById('item-ranking-id');
    const itemRanking = document.getElementById('item-ranking');
    const itemName = document.getElementById('item-name');
    const itemDescription = document.getElementById('item-description');
    const itemIsActive = document.getElementById('item-is-active');
    const itemActiveStatusText = document.getElementById('item-active-status-text');
    const descriptionCharCount = document.getElementById('description-char-count');

    const addRankingBtn = document.getElementById('add-ranking-btn');
    const saveRankingBtn = document.getElementById('save-ranking-btn');
    const addItemBtn = document.getElementById('add-item-btn');
    const saveItemBtn = document.getElementById('save-item-btn');

    const closeButtons = document.querySelectorAll('.close-modal, .cancel-btn');

    const editButtons = document.querySelectorAll('.action-btn.edit');
    const infoButtons = document.querySelectorAll('.action-btn.info');

    const statusBadges = document.querySelectorAll('.status-badge[data-id]');

    closeButtons.forEach(button => {
        button.addEventListener('click', function() {
            rankingModal.style.display = 'none';
            itemsModal.style.display = 'none';
            itemModal.style.display = 'none';
        });
    });

    function updateActiveStatusText() {
        if (rankingIsActive.checked) {
            activeStatusText.textContent = '활성화됨';
            activeStatusText.className = 'toggle-text active';
        } else {
            activeStatusText.textContent = '비활성화됨';
            activeStatusText.className = 'toggle-text inactive';
        }
    }

    rankingIsActive.addEventListener('change', updateActiveStatusText);

    function updateItemActiveStatusText() {
        if (itemIsActive.checked) {
            itemActiveStatusText.textContent = '활성화됨';
            itemActiveStatusText.className = 'toggle-text active';
        } else {
            itemActiveStatusText.textContent = '비활성화됨';
            itemActiveStatusText.className = 'toggle-text inactive';
        }
    }

    itemIsActive.addEventListener('change', updateItemActiveStatusText);

    itemDescription.addEventListener('input', function() {
        const maxLength = 1000;
        const currentLength = this.value.length;

        descriptionCharCount.textContent = `${currentLength}/${maxLength}`;

        if (currentLength > maxLength) {
            this.value = this.value.substring(0, maxLength);
            descriptionCharCount.textContent = `${maxLength}/${maxLength}`;
        }
    });

    addRankingBtn.addEventListener('click', function() {
        rankingModalTitle.textContent = '새 랭킹 추가';
        rankingForm.reset();
        rankingId.value = '';
        rankingStartDate.value = new Date().toISOString().split('T')[0];
        rankingIsActive.checked = false;
        updateActiveStatusText();
        rankingModal.style.display = 'block';
    });

    editButtons.forEach(button => {
        button.addEventListener('click', function() {
            const id = this.getAttribute('data-id');
            fetch(`/api/admin/content-rankings/${id}`)
                .then(response => response.json())
                .then(data => {
                    rankingModalTitle.textContent = '랭킹 수정';
                    rankingId.value = data.id;
                    rankingTitle.value = data.title;
                    rankingSubtitle.value = data.subtitle;
                    rankingStartDate.value = data.startDate;
                    rankingEndDate.value = data.endDate || '';
                    rankingIsActive.checked = data.isActive;
                    updateActiveStatusText();
                    rankingModal.style.display = 'block';
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('랭킹 정보를 가져오는 중 오류가 발생했습니다.');
                });
        });
    });

    saveRankingBtn.addEventListener('click', function() {
        if (!rankingTitle.value) {
            document.getElementById('error-title').textContent = '제목을 입력해주세요.';
            return;
        }

        if (!rankingSubtitle.value) {
            document.getElementById('error-subtitle').textContent = '부제목을 입력해주세요.';
            return;
        }

        if (!rankingStartDate.value) {
            document.getElementById('error-start-date').textContent = '시작일을 입력해주세요.';
            return;
        }

        if (rankingEndDate.value && new Date(rankingEndDate.value) < new Date(rankingStartDate.value)) {
            document.getElementById('error-end-date').textContent = '종료일은 시작일보다 이후여야 합니다.';
            return;
        }

        if (!rankingId.value && rankingIsActive.checked) {
            const activeStatusBadges = document.querySelectorAll('.status-badge.active[data-id]');
            if (activeStatusBadges.length > 0) {
                if (!confirm('이미 활성화된 랭킹이 있습니다. 새 랭킹을 활성화하면 기존 활성화된 랭킹은 비활성화됩니다. 계속하시겠습니까?')) {
                    return;
                }
            }
        }

        if (rankingId.value && rankingIsActive.checked) {
            const currentStatusBadge = document.querySelector(`.status-badge[data-id="${rankingId.value}"]`);
            const isCurrentlyActive = currentStatusBadge && currentStatusBadge.classList.contains('active');

            if (!isCurrentlyActive) {
                const activeStatusBadges = document.querySelectorAll('.status-badge.active[data-id]');
                if (activeStatusBadges.length > 0) {
                    if (!confirm('이미 활성화된 랭킹이 있습니다. 이 랭킹을 활성화하면 기존 활성화된 랭킹은 비활성화됩니다. 계속하시겠습니까?')) {
                        return;
                    }
                }
            }
        }

        const rankingData = {
            id: rankingId.value || null,
            title: rankingTitle.value,
            subtitle: rankingSubtitle.value,
            startDate: rankingStartDate.value,
            endDate: rankingEndDate.value || null,
            isActive: rankingIsActive.checked
        };

        const method = rankingId.value ? 'PUT' : 'POST';
        const url = rankingId.value ?
            `/api/admin/content-rankings/${rankingId.value}` :
            '/api/admin/content-rankings';

        fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(rankingData)
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('랭킹 저장 중 오류가 발생했습니다.');
                }
                return response.json();
            })
            .then(data => {
                if (rankingData.isActive) {
                    const activeStatusBadges = document.querySelectorAll('.status-badge.active[data-id]');
                    const promises = [];

                    activeStatusBadges.forEach(badge => {
                        const activeId = badge.getAttribute('data-id');
                        if (activeId && activeId !== (rankingId.value || data.id)) {
                            const promise = fetch(`/api/admin/content-rankings/${activeId}/toggle`, {
                                method: 'PATCH'
                            });
                            promises.push(promise);
                        }
                    });

                    if (promises.length > 0) {
                        Promise.all(promises)
                            .then(() => {
                                rankingModal.style.display = 'none';
                                alert(rankingId.value ? '랭킹이 수정되었습니다.' : '새 랭킹이 추가되었습니다.');
                                location.reload();
                            })
                            .catch(error => {
                                console.error('Error:', error);
                                alert('일부 랭킹의 상태 변경 중 오류가 발생했습니다. 페이지를 새로고침합니다.');
                                location.reload();
                            });
                    } else {
                        rankingModal.style.display = 'none';
                        alert(rankingId.value ? '랭킹이 수정되었습니다.' : '새 랭킹이 추가되었습니다.');
                        location.reload();
                    }
                } else {
                    rankingModal.style.display = 'none';
                    alert(rankingId.value ? '랭킹이 수정되었습니다.' : '새 랭킹이 추가되었습니다.');
                    location.reload();
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert(error.message);
            });
    });

    infoButtons.forEach(button => {
        button.addEventListener('click', function() {
            const id = this.getAttribute('data-id');
            loadRankingItems(id);
        });
    });

    function loadRankingItems(rankingId) {
        fetch(`/api/admin/content-rankings/${rankingId}`)
            .then(response => response.json())
            .then(data => {
                document.getElementById('items-ranking-title').textContent = data.title;
                document.getElementById('items-ranking-subtitle').textContent = data.subtitle;

                const startDate = new Date(data.startDate).toLocaleDateString();
                const endDate = data.endDate ? new Date(data.endDate).toLocaleDateString() : '무기한';
                document.getElementById('items-ranking-period').textContent = `${startDate} ~ ${endDate}`;

                const statusSpan = document.getElementById('items-ranking-status');
                statusSpan.textContent = data.isActive ? '활성' : '비활성';
                statusSpan.className = data.isActive ? 'status-badge active' : 'status-badge inactive';

                addItemBtn.setAttribute('data-ranking-id', rankingId);

                const itemsTableBody = document.getElementById('items-table-body');
                itemsTableBody.innerHTML = '';

                if (data.items && data.items.length > 0) {
                    data.items.forEach(item => {
                        const row = document.createElement('tr');

                        row.innerHTML = `
                                    <td class="text-center">${item.ranking}</td>
                                    <td>${item.itemName}</td>
                                    <td class="item-description">${item.description}</td>
                                    <td class="text-center">
                                        <span class="status-badge ${item.isActive ? 'active' : 'inactive'}">
                                            ${item.isActive ? '활성' : '비활성'}
                                        </span>
                                    </td>
                                    <td>
                                        <button class="action-btn edit-item" data-id="${item.id}" data-ranking-id="${rankingId}" title="항목 수정">
                                            <i class="fas fa-edit"></i>
                                        </button>
                                        <button class="action-btn toggle-item" 
                                                data-id="${item.id}" 
                                                data-ranking-id="${rankingId}"
                                                data-active="${item.isActive}" 
                                                title="${item.isActive ? '비활성화' : '활성화'}">
                                            <i class="fas ${item.isActive ? 'fa-toggle-on' : 'fa-toggle-off'}"></i>
                                        </button>
                                        <button class="action-btn delete-item" data-id="${item.id}" data-ranking-id="${rankingId}" title="항목 삭제">
                                            <i class="fas fa-trash"></i>
                                        </button>
                                    </td>
                                `;

                        itemsTableBody.appendChild(row);
                    });

                    document.querySelectorAll('.edit-item').forEach(button => {
                        button.addEventListener('click', function() {
                            const itemId = this.getAttribute('data-id');
                            const rankingId = this.getAttribute('data-ranking-id');
                            editItem(rankingId, itemId);
                        });
                    });

                    document.querySelectorAll('.toggle-item').forEach(button => {
                        button.addEventListener('click', function() {
                            const itemId = this.getAttribute('data-id');
                            const rankingId = this.getAttribute('data-ranking-id');
                            toggleItemStatus(rankingId, itemId);
                        });
                    });

                    document.querySelectorAll('.delete-item').forEach(button => {
                        button.addEventListener('click', function() {
                            const itemId = this.getAttribute('data-id');
                            const rankingId = this.getAttribute('data-ranking-id');
                            deleteItem(rankingId, itemId);
                        });
                    });
                } else {
                    const row = document.createElement('tr');
                    row.innerHTML = `<td colspan="5" class="text-center">등록된 항목이 없습니다.</td>`;
                    itemsTableBody.appendChild(row);
                }

                itemsModal.style.display = 'block';
            })
            .catch(error => {
                console.error('Error:', error);
                alert('랭킹 정보를 가져오는 중 오류가 발생했습니다.');
            });
    }

    addItemBtn.addEventListener('click', function() {
        const rankingId = this.getAttribute('data-ranking-id');
        itemModalTitle.textContent = '새 항목 추가';
        itemForm.reset();
        itemId.value = '';
        itemRankingId.value = rankingId;
        itemIsActive.checked = true;
        updateItemActiveStatusText();
        descriptionCharCount.textContent = '0/1000';
        itemsModal.style.display = 'none';
        itemModal.style.display = 'block';
    });

    function editItem(rankingId, itemId) {
        fetch(`/api/admin/content-rankings/${rankingId}/items/${itemId}`)
            .then(response => response.json())
            .then(data => {
                itemModalTitle.textContent = '항목 수정';
                document.getElementById('item-id').value = data.id;
                document.getElementById('item-ranking-id').value = rankingId;
                document.getElementById('item-ranking').value = data.ranking;
                document.getElementById('item-name').value = data.itemName;
                document.getElementById('item-description').value = data.description;
                document.getElementById('item-is-active').checked = data.isActive;
                updateItemActiveStatusText();
                descriptionCharCount.textContent = `${data.description.length}/1000`;

                itemsModal.style.display = 'none';
                itemModal.style.display = 'block';
            })
            .catch(error => {
                console.error('Error:', error);
                alert('항목 정보를 가져오는 중 오류가 발생했습니다.');
            });
    }

    saveItemBtn.addEventListener('click', function() {
        if (!itemRanking.value || itemRanking.value < 1) {
            document.getElementById('error-ranking').textContent = '유효한 순위를 입력해주세요.';
            return;
        }

        if (!itemName.value) {
            document.getElementById('error-item-name').textContent = '항목명을 입력해주세요.';
            return;
        }

        if (!itemDescription.value) {
            document.getElementById('error-description').textContent = '설명을 입력해주세요.';
            return;
        }

        const itemData = {
            id: itemId.value || null,
            contentRankingId: itemRankingId.value,
            ranking: parseInt(itemRanking.value),
            itemName: itemName.value,
            description: itemDescription.value,
            isActive: itemIsActive.checked
        };

        const rankingId = itemRankingId.value;
        const method = itemId.value ? 'PUT' : 'POST';
        const url = itemId.value ?
            `/api/admin/content-rankings/${rankingId}/items/${itemId.value}` :
            `/api/admin/content-rankings/${rankingId}/items`;

        fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(itemData)
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('항목 저장 중 오류가 발생했습니다.');
                }
                return response.json();
            })
            .then(data => {
                itemModal.style.display = 'none';

                loadRankingItems(rankingId);
            })
            .catch(error => {
                console.error('Error:', error);
                alert(error.message);
            });
    });

    window.removeFilter = function(filterName) {
        const url = new URL(window.location.href);
        url.searchParams.delete(filterName);
        window.location.href = url.toString();
    };

    window.deleteRanking = function(id) {
        if (confirm('정말 삭제하시겠습니까? 이 작업은 되돌릴 수 없으며, 해당 랭킹의 모든 항목도 함께 삭제됩니다.')) {
            fetch(`/api/admin/content-rankings/${id}`, {
                method: 'DELETE'
            })
                .then(response => {
                    if (response.ok) {
                        location.reload();
                    } else {
                        throw new Error('랭킹 삭제 중 오류가 발생했습니다.');
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert(error.message);
                });
        }
    };

    window.toggleRankingStatus = function(id) {
        const statusBadge = document.querySelector(`.status-badge[data-id="${id}"]`);
        const isCurrentlyActive = statusBadge && statusBadge.getAttribute('data-active') === 'true';

        if (isCurrentlyActive) {
            fetch(`/api/admin/content-rankings/${id}/toggle`, {
                method: 'PATCH'
            })
                .then(response => {
                    if (response.ok) {
                        location.reload();
                    } else {
                        throw new Error('랭킹 상태 변경 중 오류가 발생했습니다.');
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert(error.message);
                });
            return;
        }

        const promises = [];

        const activeStatusBadges = document.querySelectorAll('.status-badge.active[data-id]');
        activeStatusBadges.forEach(badge => {
            const activeId = badge.getAttribute('data-id');
            if (activeId && activeId !== id) {
                const promise = fetch(`/api/admin/content-rankings/${activeId}/toggle`, {
                    method: 'PATCH'
                });
                promises.push(promise);
            }
        });

        Promise.all(promises)
            .then(() => {
                return fetch(`/api/admin/content-rankings/${id}/toggle`, {
                    method: 'PATCH'
                });
            })
            .then(response => {
                if (response.ok) {
                    location.reload();
                } else {
                    throw new Error('랭킹 상태 변경 중 오류가 발생했습니다.');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('랭킹 상태 변경 중 오류가 발생했습니다. 페이지를 새로고침하고 다시 시도해주세요.');
                location.reload();
            });
    };

    function deleteItem(rankingId, itemId) {
        if (confirm('정말 삭제하시겠습니까? 이 작업은 되돌릴 수 없습니다.')) {
            fetch(`/api/admin/content-rankings/${rankingId}/items/${itemId}`, {
                method: 'DELETE'
            })
                .then(response => {
                    if (response.ok) {
                        loadRankingItems(rankingId);
                    } else {
                        throw new Error('항목 삭제 중 오류가 발생했습니다.');
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert(error.message);
                });
        }
    }

    function toggleItemStatus(rankingId, itemId) {
        fetch(`/api/admin/content-rankings/${rankingId}/items/${itemId}/toggle`, {
            method: 'PATCH'
        })
            .then(response => {
                if (response.ok) {
                    loadRankingItems(rankingId);
                } else {
                    throw new Error('항목 상태 변경 중 오류가 발생했습니다.');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert(error.message);
            });
    }

    if (titleSearch) {
        titleSearch.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                e.preventDefault();
                searchForm.submit();
            }
        });

        if (titleSearch.value) {
            const clearButton = document.createElement('button');
            clearButton.type = 'button';
            clearButton.className = 'clear-search';
            clearButton.innerHTML = '<i class="fas fa-times"></i>';
            clearButton.title = '검색어 지우기';
            clearButton.style.position = 'absolute';
            clearButton.style.right = '40px';
            clearButton.style.top = '50%';
            clearButton.style.transform = 'translateY(-50%)';
            clearButton.style.background = 'none';
            clearButton.style.border = 'none';
            clearButton.style.cursor = 'pointer';
            clearButton.style.color = '#888';

            const searchContainer = titleSearch.parentElement;
            if (searchContainer && getComputedStyle(searchContainer).position === 'static') {
                searchContainer.style.position = 'relative';
            }

            clearButton.addEventListener('click', function() {
                titleSearch.value = '';
                searchForm.submit();
            });

            titleSearch.parentElement.appendChild(clearButton);
        }
    }

    function getUrlParameter(name) {
        name = name.replace(/[\[]/, '\\[').replace(/[\]]/, '\\]');
        const regex = new RegExp('[\\?&]' + name + '=([^&#]*)');
        const results = regex.exec(location.search);
        return results === null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
    }

    function highlightActiveFilters() {
        if (activeFilter) {
            const activeValue = getUrlParameter('active');
            if (activeValue) {
                activeFilter.classList.add('active-filter');
            }
        }

        if (sortFilter) {
            const sortValue = getUrlParameter('sort');
            if (sortValue) {
                sortFilter.classList.add('active-filter');
            }
        }
    }

    highlightActiveFilters();

    document.addEventListener('keydown', function(event) {
        if (event.key === 'Escape') {
            rankingModal.style.display = 'none';
            itemsModal.style.display = 'none';
            itemModal.style.display = 'none';
        }
    });

    window.addEventListener('click', function(event) {
        if (event.target === rankingModal) {
            rankingModal.style.display = 'none';
        }
        if (event.target === itemsModal) {
            itemsModal.style.display = 'none';
        }
        if (event.target === itemModal) {
            itemModal.style.display = 'none';
        }
    });
});