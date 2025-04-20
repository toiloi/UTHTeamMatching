// Global variables
let users = [];
let roles = [];

// Initialize when document is ready
$(document).ready(function() {
    loadUsers();
    loadRoles();
    
    // Event listeners
    $('#saveUserBtn').click(saveUser);
    $('#searchUser').on('input', function() {
        searchUsers($(this).val());
    });
});

// Load all users
function loadUsers() {
    $.ajax({
        url: '/api/admin/users',
        method: 'GET',
        success: function(users) {
            renderUsers(users);
        },
        error: function(xhr) {
            showError('Không thể tải danh sách người dùng');
        }
    });
}

// Load all roles
function loadRoles() {
    $.ajax({
        url: '/api/admin/roles',
        method: 'GET',
        success: function(roles) {
            window.roles = roles;
        },
        error: function(xhr) {
            showError('Không thể tải danh sách vai trò');
        }
    });
}

// Render users
function renderUsers(users) {
    const usersList = $('#usersList');
    usersList.empty();
    
    users.forEach(user => {
        const roleNames = user.userRoles ? 
            Array.from(user.userRoles).map(ur => ur.role.ten).join(', ') : '';
        
        usersList.append(`
            <tr>
                <td>${user.maSo}</td>
                <td>${user.ho || ''} ${user.ten || ''}</td>
                <td>${user.sdt || ''}</td>
                <td>${user.email || ''}</td>
                <td>${user.chuyenNganh || ''}</td>
                <td>
                    <span class="badge ${user.enabled ? 'bg-success' : 'bg-danger'}">
                        <i class="fas fa-${user.enabled ? 'check' : 'times'}-circle me-1"></i>
                        ${user.enabled ? 'Hoạt động' : 'Khóa'}
                    </span>
                </td>
                <td>
                    ${user.userRoles ? Array.from(user.userRoles).map(ur => 
                        `<div class="badge bg-info me-1">
                            <i class="fas fa-user-tag me-1"></i>
                            ${ur.role.ten}
                        </div>`
                    ).join('') : ''}
                </td>
                <td>
                    <div class="btn-group">
                        <button class="btn btn-sm btn-primary" onclick="editUser(${user.maSo})">
                            <i class="fas fa-edit"></i>
                        </button>
                        <button class="btn btn-sm btn-danger" onclick="deleteUser(${user.maSo})">
                            <i class="fas fa-trash"></i>
                        </button>
                    </div>
                </td>
            </tr>
        `);
    });
}

// Search users
function searchUsers(term) {
    if (!term) {
        loadUsers();
        return;
    }
    
    $.ajax({
        url: `/api/admin/users/search?term=${encodeURIComponent(term)}`,
        method: 'GET',
        success: function(users) {
            renderUsers(users);
        },
        error: function(xhr) {
            showError('Không thể tìm kiếm người dùng');
        }
    });
}

// Edit user
function editUser(userId) {
    $.ajax({
        url: `/api/admin/users/${userId}`,
        method: 'GET',
        success: function(user) {
            $('#editUserId').val(user.maSo);
            $('#editHo').val(user.ho);
            $('#editTen').val(user.ten);
            $('#editEmail').val(user.email);
            $('#editSdt').val(user.sdt);
            $('#editChuyenNganh').val(user.chuyenNganh);
            $('#editGioiTinh').val(user.gioiTinh);
            $('#editEnabled').val(user.enabled);
            
            // Render roles
            const editRoles = $('#editRoles');
            editRoles.empty();
            
            roles.forEach(role => {
                const isChecked = user.userRoles && 
                    Array.from(user.userRoles).some(ur => ur.role.maSo === role.maSo);
                
                editRoles.append(`
                    <div class="form-check">
                        <input class="form-check-input" type="checkbox" value="${role.maSo}" 
                            ${isChecked ? 'checked' : ''}>
                        <label class="form-check-label">${role.ten}</label>
                    </div>
                `);
            });
            
            $('#editUserModal').modal('show');
        },
        error: function(xhr) {
            showError('Không thể tải thông tin người dùng');
        }
    });
}

// Save user
function saveUser() {
    const userId = $('#editUserId').val();
    const userData = {
        ho: $('#editHo').val(),
        ten: $('#editTen').val(),
        email: $('#editEmail').val(),
        sdt: $('#editSdt').val(),
        chuyenNganh: $('#editChuyenNganh').val(),
        gioiTinh: $('#editGioiTinh').val(),
        enabled: $('#editEnabled').val() === 'true'
    };
    
    // Get selected roles
    const selectedRoles = [];
    $('#editRoles input[type="checkbox"]:checked').each(function() {
        selectedRoles.push($(this).val());
    });
    
    $.ajax({
        url: `/api/admin/users/${userId}`,
        method: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify({
            ...userData,
            roles: selectedRoles
        }),
        success: function() {
            $('#editUserModal').modal('hide');
            loadUsers();
            showSuccess('Cập nhật người dùng thành công');
        },
        error: function(xhr) {
            showError('Không thể cập nhật người dùng');
        }
    });
}

// Delete user
function deleteUser(userId) {
    if (!confirm('Bạn có chắc chắn muốn xóa người dùng này?')) {
        return;
    }
    
    $.ajax({
        url: `/api/admin/users/${userId}`,
        method: 'DELETE',
        success: function() {
            loadUsers();
            showSuccess('Xóa người dùng thành công');
        },
        error: function(xhr) {
            showError('Không thể xóa người dùng');
        }
    });
}

// Show error message
function showError(message) {
    alert(message); // You can replace this with a better UI notification
}

// Show success message
function showSuccess(message) {
    alert(message); // You can replace this with a better UI notification
} 