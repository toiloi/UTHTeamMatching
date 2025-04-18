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

function acceptFriendRequest(button, notificationId) {
    fetch('/ketban/accept-friend', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: `notificationId=${notificationId}`
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            // Xóa item lời mời kết bạn khỏi danh sách
            const requestItem = button.closest('.request-item');
            requestItem.remove();
            
            // Kiểm tra nếu không còn lời mời nào thì hiển thị thông báo trống
            const requestList = document.querySelector('.request-list');
            if (requestList && requestList.children.length === 0) {
                const emptyMessage = document.createElement('div');
                emptyMessage.className = 'text-center py-3 empty-requests';
                emptyMessage.innerHTML = '<p class="text-muted">Không có lời mời kết bạn nào</p>';
                requestList.parentNode.replaceChild(emptyMessage, requestList);
            }
            
            // Hiển thị thông báo thành công
            showToast('success', 'Thành công', 'Đã chấp nhận lời mời kết bạn thành công!');
            
            // Reload danh sách bạn bè
            location.reload();
        } else {
            showToast('danger', 'Lỗi', data.error || 'Đã xảy ra lỗi khi chấp nhận lời mời kết bạn');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        showToast('danger', 'Lỗi', 'Đã xảy ra lỗi khi chấp nhận lời mời kết bạn. Vui lòng thử lại sau!');
    });
} 