// Global variables
let currentRoleId = 'admin';
let roles = [];
let users = [];

// Initialize when document is ready
$(document).ready(function() {
    loadRoles();
    initializeSearchHandlers();
});

// Load all roles
function loadRoles() {
    $.ajax({
        url: '/api/admin/roles',
        method: 'GET',
        success: function(response) {
            roles = response;
            renderRoleList();
            showRolePermissions('admin'); // Show admin permissions by default
        },
        error: function(xhr) {
            showError('Không thể tải danh sách vai trò');
        }
    });
}

// Render role list
function renderRoleList() {
    const roleList = $('.list-group');
    roleList.empty();

    roles.forEach(role => {
        roleList.append(`
            <a href="#" class="list-group-item list-group-item-action ${role.id === currentRoleId ? 'active' : ''}" 
               onclick="showRolePermissions('${role.id}')">
                <div class="d-flex w-100 justify-content-between">
                    <h6 class="mb-1">${role.name}</h6>
                    <small>${role.userCount} người dùng</small>
                </div>
                <small>${role.description}</small>
            </a>
        `);
    });
}

// Show role permissions
function showRolePermissions(roleId) {
    currentRoleId = roleId;
    
    // Update UI active state
    $('.list-group-item').removeClass('active');
    $(`.list-group-item[onclick="showRolePermissions('${roleId}')"]`).addClass('active');
    
    // Update role name in header
    $('#currentRole').text(roles.find(r => r.id === roleId)?.name || '');

    // Load permissions
    $.ajax({
        url: `/api/admin/roles/${roleId}/permissions`,
        method: 'GET',
        success: function(permissions) {
            updatePermissionCheckboxes(permissions);
            loadRoleUsers(roleId);
        },
        error: function(xhr) {
            showError('Không thể tải thông tin quyền');
        }
    });
}

// Update permission checkboxes
function updatePermissionCheckboxes(permissions) {
    // Reset all checkboxes
    $('.form-check-input').prop('checked', false);
    
    // Update checkboxes based on permissions
    permissions.forEach(permission => {
        $(`input[data-permission="${permission}"]`).prop('checked', true);
    });
}

// Load users in role
function loadRoleUsers(roleId) {
    $.ajax({
        url: `/api/admin/roles/${roleId}/users`,
        method: 'GET',
        success: function(users) {
            renderRoleUsers(users);
        },
        error: function(xhr) {
            showError('Không thể tải danh sách người dùng');
        }
    });
}

// Render users in role
function renderRoleUsers(users) {
    const userList = $('.card:eq(1) .list-group');
    userList.empty();

    users.forEach(user => {
        userList.append(`
            <div class="list-group-item">
                <div class="d-flex w-100 justify-content-between align-items-center">
                    <div>
                        <h6 class="mb-1">${user.name}</h6>
                        <small>${user.email}</small>
                    </div>
                    <button class="btn btn-sm btn-danger" onclick="removeUserFromRole(${user.id})">
                        <i class="fas fa-times"></i>
                    </button>
                </div>
            </div>
        `);
    });
}

// Add new role
function addRole() {
    const form = document.getElementById('addRoleForm');
    if (!form.checkValidity()) {
        form.reportValidity();
        return;
    }

    const roleData = {
        name: $('#addRoleForm input[type="text"]').val(),
        description: $('#addRoleForm textarea').val(),
        copyFromRoleId: $('#addRoleForm select').val()
    };

    $.ajax({
        url: '/api/admin/roles',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(roleData),
        success: function(response) {
            showSuccess('Đã thêm vai trò mới');
            $('#addRoleModal').modal('hide');
            loadRoles();
        },
        error: function(xhr) {
            showError('Không thể thêm vai trò mới');
        }
    });
}

// Save permissions
function savePermissions() {
    const permissions = [];
    $('.form-check-input:checked').each(function() {
        permissions.push($(this).data('permission'));
    });

    $.ajax({
        url: `/api/admin/roles/${currentRoleId}/permissions`,
        method: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify(permissions),
        success: function(response) {
            showSuccess('Đã lưu thay đổi quyền');
        },
        error: function(xhr) {
            showError('Không thể lưu thay đổi quyền');
        }
    });
}

// Initialize search handlers
function initializeSearchHandlers() {
    // Role user search
    let userSearchTimeout;
    $('.card:eq(1) input[type="text"]').on('input', function() {
        clearTimeout(userSearchTimeout);
        const searchTerm = $(this).val();
        
        userSearchTimeout = setTimeout(() => {
            if (searchTerm.length >= 2) {
                searchRoleUsers(searchTerm);
            }
        }, 300);
    });

    // Add user modal search
    let modalSearchTimeout;
    $('#addUserToRoleModal input[type="text"]').on('input', function() {
        clearTimeout(modalSearchTimeout);
        const searchTerm = $(this).val();
        
        modalSearchTimeout = setTimeout(() => {
            if (searchTerm.length >= 2) {
                searchAvailableUsers(searchTerm);
            }
        }, 300);
    });
}

// Search users in role
function searchRoleUsers(term) {
    $.ajax({
        url: `/api/admin/roles/${currentRoleId}/users/search?term=${encodeURIComponent(term)}`,
        method: 'GET',
        success: function(users) {
            renderRoleUsers(users);
        }
    });
}

// Search available users for role
function searchAvailableUsers(term) {
    $.ajax({
        url: `/api/admin/users/search?term=${encodeURIComponent(term)}&excludeRole=${currentRoleId}`,
        method: 'GET',
        success: function(users) {
            renderAvailableUsers(users);
        }
    });
}

// Render available users in modal
function renderAvailableUsers(users) {
    const userList = $('#addUserToRoleModal .list-group');
    userList.empty();

    users.forEach(user => {
        userList.append(`
            <div class="list-group-item">
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" value="${user.id}">
                    <label class="form-check-label">
                        <div>
                            <h6 class="mb-1">${user.name}</h6>
                            <small>${user.email}</small>
                        </div>
                    </label>
                </div>
            </div>
        `);
    });
}

// Add users to role
function addUsersToRole() {
    const selectedUserIds = [];
    $('#addUserToRoleModal .form-check-input:checked').each(function() {
        selectedUserIds.push($(this).val());
    });

    if (selectedUserIds.length === 0) {
        showError('Vui lòng chọn ít nhất một người dùng');
        return;
    }

    $.ajax({
        url: `/api/admin/roles/${currentRoleId}/users`,
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(selectedUserIds),
        success: function(response) {
            showSuccess('Đã thêm người dùng vào vai trò');
            $('#addUserToRoleModal').modal('hide');
            loadRoleUsers(currentRoleId);
        },
        error: function(xhr) {
            showError('Không thể thêm người dùng vào vai trò');
        }
    });
}

// Remove user from role
function removeUserFromRole(userId) {
    Swal.fire({
        title: 'Xác nhận xóa?',
        text: 'Bạn có chắc chắn muốn xóa người dùng này khỏi vai trò?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Xóa',
        cancelButtonText: 'Hủy'
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                url: `/api/admin/roles/${currentRoleId}/users/${userId}`,
                method: 'DELETE',
                success: function(response) {
                    showSuccess('Đã xóa người dùng khỏi vai trò');
                    loadRoleUsers(currentRoleId);
                },
                error: function(xhr) {
                    showError('Không thể xóa người dùng khỏi vai trò');
                }
            });
        }
    });
}

// Utility functions for notifications
function showSuccess(message) {
    Swal.fire({
        icon: 'success',
        title: 'Thành công!',
        text: message,
        confirmButtonText: 'OK'
    });
}

function showError(message) {
    Swal.fire({
        icon: 'error',
        title: 'Lỗi!',
        text: message,
        confirmButtonText: 'OK'
    });
} 