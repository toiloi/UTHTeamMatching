// Tạo toast container nếu chưa tồn tại
function getOrCreateToastContainer() {
    let container = document.querySelector('.toast-container');
    if (!container) {
        container = document.createElement('div');
        container.className = 'toast-container';
        document.body.appendChild(container);
    }
    return container;
}

function showToast(type, title, message) {
    const container = getOrCreateToastContainer();
    
    const toast = document.createElement('div');
    toast.className = 'toast';
    toast.innerHTML = `
        <div class="toast-header bg-${type}">
            <i class="fas fa-${type === 'success' ? 'check' : 'exclamation'}-circle me-2"></i>
            <strong class="me-auto">${title}</strong>
            <button type="button" class="btn-close btn-close-white" onclick="this.closest('.toast').remove()"></button>
        </div>
        <div class="toast-body">
            ${message}
        </div>
    `;
    
    container.appendChild(toast);
    
    // Tự động xóa toast sau 3 giây
    setTimeout(() => {
        toast.classList.add('hiding');
        setTimeout(() => {
            toast.remove();
        }, 300);
    }, 3000);
}

// Friend search functionality
document.addEventListener('DOMContentLoaded', function() {
    const searchForm = document.getElementById('searchFriendForm');
    if (searchForm) {
        searchForm.addEventListener('submit', handleSearch);
    }
});

function handleSearch(e) {
    e.preventDefault();
    const searchPhone = document.getElementById('searchPhone');
    const phone = searchPhone.value.trim();
    const searchResults = document.getElementById('searchResults');
    
    if (!phone) {
        searchResults.innerHTML = `
            <div class="alert alert-warning" role="alert">
                <i class="fas fa-exclamation-triangle me-2"></i>
                Vui lòng nhập số điện thoại hoặc tên người dùng
            </div>
        `;
        return;
    }
    
    searchResults.innerHTML = `
        <div class="text-center">
            <div class="spinner-border text-primary" role="status">
                <span class="visually-hidden">Đang tìm kiếm...</span>
            </div>
        </div>
    `;
    
    fetch(`/ketban/search-ajax?phone=${encodeURIComponent(phone)}`)
        .then(response => response.json())
        .then(data => {
            console.log('Response data:', data); // Debug log
            if (data.success && data.user) {
                searchResults.innerHTML = `
                    <div class="card">
                        <div class="card-body">
                            <div class="d-flex align-items-center">
                                <img src="${data.user.avatar || '/img/avatars/NULL.jpg'}" 
                                     class="rounded-circle me-3" 
                                     style="width: 50px; height: 50px;">
                                <div class="flex-grow-1">
                                    <h6 class="mb-1">${data.user.ho} ${data.user.ten}</h6>
                                    <small class="text-muted">${data.user.sdt || 'Không có số điện thoại'}</small>
                                </div>
                                ${data.isFriend ? `
                                    <div class="ms-2">
                                        <span class="badge bg-secondary">Đã là bạn bè</span>
                                    </div>
                                ` : data.hasSentRequest ? `
                                    <div class="ms-2">
                                        <span class="badge bg-primary">Đã gửi lời mời</span>
                                    </div>
                                ` : `
                                    <div class="friend-action ms-2">
                                        <button onclick="sendFriendRequest('${data.user.maSo}')" 
                                                class="btn btn-primary btn-sm add-friend-btn px-3">
                                            Kết bạn
                                        </button>
                                    </div>
                                `}
                            </div>
                        </div>
                    </div>
                `;
            } else {
                // Hiển thị thông báo không tìm thấy
                searchResults.innerHTML = `
                    <div class="alert alert-info" role="alert">
                        <i class="fas fa-info-circle me-2"></i>
                        Không tìm thấy người dùng với thông tin tìm kiếm này
                    </div>
                `;
            }
        })
        .catch(error => {
            console.error('Error:', error);
            searchResults.innerHTML = `
                <div class="alert alert-danger" role="alert">
                    <i class="fas fa-exclamation-circle me-2"></i>
                    Đã xảy ra lỗi khi tìm kiếm. Vui lòng thử lại sau!
                </div>
            `;
        });
}

function sendFriendRequest(friendId) {
    fetch('/ketban/send-request', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: `friendId=${friendId}`
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            // Cập nhật UI sau khi gửi lời mời thành công
            const actionButton = document.querySelector(`button[onclick="sendFriendRequest('${friendId}')"]`).parentElement;
            actionButton.innerHTML = `
                <div class="ms-2">
                    <span class="badge bg-primary">Đã gửi lời mời</span>
                </div>
            `;
            showToast('success', 'Thành công', 'Đã gửi lời mời kết bạn thành công!');
        } else {
            showToast('danger', 'Lỗi', data.error || 'Đã xảy ra lỗi khi gửi lời mời kết bạn');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        showToast('danger', 'Lỗi', 'Đã xảy ra lỗi khi gửi lời mời kết bạn. Vui lòng thử lại sau!');
    });
} 