// Friend search functionality
document.addEventListener('DOMContentLoaded', function() {
    const searchForm = document.getElementById('searchFriendForm');
    if (searchForm) {
        searchForm.addEventListener('submit', handleSearch);
    }
});

function handleSearch(e) {
    e.preventDefault();
    
    const phone = document.getElementById('searchPhone').value;
    const searchResults = document.getElementById('searchResults');
    
    console.log('Searching for:', phone); // Debug log
    
    // Hiển thị loading spinner
    searchResults.innerHTML = `
        <div class="text-center p-3">
            <div class="spinner-border text-primary" role="status">
                <span class="visually-hidden">Đang tìm kiếm...</span>
            </div>
        </div>
    `;

    // Gọi API tìm kiếm
    fetch(`/ketban/search-ajax?phone=${encodeURIComponent(phone)}`)
        .then(response => {
            console.log('Response status:', response.status); // Debug log
            return response.json();
        })
        .then(data => {
            console.log('Response data:', data); // Debug log
            
            if (!data.success) {
                throw new Error(data.error || 'Đã xảy ra lỗi không xác định');
            }
            
            if (data.user) {
                // Hiển thị kết quả tìm kiếm
                searchResults.innerHTML = `
                    <div class="search-result">
                        <div class="card border-0 shadow-sm result-card">
                            <div class="card-body p-3">
                                <div class="d-flex align-items-center">
                                    <div class="position-relative me-3">
                                        <img src="${data.user.avatar || '/img/avatars/NULL.jpg'}" 
                                             class="rounded-circle result-avatar" style="width: 40px; height: 40px;">
                                    </div>
                                    <div class="flex-grow-1">
                                        <h6 class="mb-1 result-name fw-bold">${data.user.ho} ${data.user.ten}</h6>
                                        <small class="text-muted result-phone">
                                            <i class="fas fa-phone-alt me-1"></i>
                                            <span>${data.user.sdt}</span>
                                        </small>
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
            console.error('Error:', error); // Debug log
            searchResults.innerHTML = `
                <div class="alert alert-danger" role="alert">
                    <i class="fas fa-exclamation-circle me-2"></i>
                    ${error.message || 'Đã xảy ra lỗi khi tìm kiếm. Vui lòng thử lại sau.'}
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
        }
    })
    .catch(error => {
        console.error('Error:', error);
        showToast('danger', 'Lỗi', 'Đã xảy ra lỗi khi gửi lời mời kết bạn. Vui lòng thử lại sau!');
    });
}

function showToast(type, title, message) {
    const toast = document.createElement('div');
    toast.className = 'toast position-fixed bottom-0 end-0 m-3';
    toast.setAttribute('role', 'alert');
    toast.setAttribute('aria-live', 'assertive');
    toast.setAttribute('aria-atomic', 'true');
    toast.innerHTML = `
        <div class="toast-header bg-${type} text-white">
            <i class="fas fa-${type === 'success' ? 'check' : 'exclamation'}-circle me-2"></i>
            <strong class="me-auto">${title}</strong>
            <button type="button" class="btn-close btn-close-white" data-bs-dismiss="toast" aria-label="Close"></button>
        </div>
        <div class="toast-body">
            ${message}
        </div>
    `;
    document.body.appendChild(toast);
    const bsToast = new bootstrap.Toast(toast);
    bsToast.show();
} 