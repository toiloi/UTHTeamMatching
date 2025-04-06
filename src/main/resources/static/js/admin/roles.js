// Global variables
let currentRoleId = null;
let roles = [];
let users = [];

// Initialize when document is ready
$(document).ready(function() {
    loadRoles();
    initializeSearchHandlers();
    
    // Event listeners
    $('#saveRoleBtn').click(saveRole);
    $('#addUserToRoleBtn').click(showAddUserModal);
    $('#saveUsersToRoleBtn').click(saveUsersToRole);
    $('#searchUser').on('input', function() {
        searchRoleUsers($(this).val());
    });
    $('#searchAvailableUser').on('input', function() {
        searchAvailableUsers($(this).val());
    });
});

// Load all roles
function loadRoles() {
    $.ajax({
        url: '/api/admin/roles',
        method: 'GET',
        success: function(roles) {
            const rolesList = $('#rolesList');
            rolesList.empty();
            
            roles.forEach(role => {
                rolesList.append(`
                    <a href="#" class="list-group-item list-group-item-action" onclick="selectRole(${role.maSo})">
                        <div class="d-flex w-100 justify-content-between align-items-center">
                            <h6 class="mb-1">${role.ten}</h6>
                            <button class="btn btn-sm btn-danger" onclick="deleteRole(${role.maSo}, event)">
                                <i class="fas fa-trash"></i>
                            </button>
                        </div>
                    </a>
                `);
            });
        },
        error: function(xhr) {
            showError('Không thể tải danh sách vai trò');
        }
    });
}

// Select a role
function selectRole(roleId) {
    currentRoleId = roleId;
    $('#addUserToRoleBtn').prop('disabled', false);
    loadRoleUsers(roleId);
    
    // Update active state
    $('.list-group-item').removeClass('active');
    $(`[onclick="selectRole(${roleId})"]`).addClass('active');
}

// Load users in a role
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
    const userList = $('#usersInRole');
    userList.empty();
    
    users.forEach(user => {
        userList.append(`
            <div class="list-group-item">
                <div class="d-flex w-100 justify-content-between align-items-center">
                    <div>
                        <h6 class="mb-1">${user.ho} ${user.ten}</h6>
                        <small>${user.email}</small>
                    </div>
                    <button class="btn btn-sm btn-danger" onclick="removeUserFromRole(${user.maSo})">
                        <i class="fas fa-times"></i>
                    </button>
                </div>
            </div>
        `);
    });
}

// Save new role
function saveRole() {
    const roleName = $('#roleName').val();
    if (!roleName) {
        showError('Vui lòng nhập tên vai trò');
        return;
    }
    
    $.ajax({
        url: '/api/admin/roles',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({ ten: roleName }),
        success: function() {
            $('#addRoleModal').modal('hide');
            $('#roleName').val('');
            loadRoles();
            showSuccess('Thêm vai trò thành công');
        },
        error: function(xhr) {
            showError('Không thể thêm vai trò');
        }
    });
}

// Delete role
function deleteRole(roleId, event) {
    event.stopPropagation();
    if (!confirm('Bạn có chắc chắn muốn xóa vai trò này?')) {
        return;
    }
    
    $.ajax({
        url: `/api/admin/roles/${roleId}`,
        method: 'DELETE',
        success: function() {
            loadRoles();
            if (currentRoleId === roleId) {
                currentRoleId = null;
                $('#addUserToRoleBtn').prop('disabled', true);
                $('#usersInRole').empty();
            }
            showSuccess('Xóa vai trò thành công');
        },
        error: function(xhr) {
            showError('Không thể xóa vai trò');
        }
    });
}

// Show add user modal
function showAddUserModal() {
    if (!currentRoleId) return;
    
    loadAvailableUsers();
    $('#addUserToRoleModal').modal('show');
}

// Load available users
function loadAvailableUsers() {
    $.ajax({
        url: '/api/admin/users/available',
        method: 'GET',
        success: function(users) {
            renderAvailableUsers(users);
        },
        error: function(xhr) {
            showError('Không thể tải danh sách người dùng');
        }
    });
}

// Render available users
function renderAvailableUsers(users) {
    const userList = $('#availableUsers');
    userList.empty();
    
    users.forEach(user => {
        userList.append(`
            <div class="list-group-item">
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" value="${user.maSo}">
                    <label class="form-check-label">
                        <div>
                            <h6 class="mb-1">${user.ho} ${user.ten}</h6>
                            <small>${user.email}</small>
                        </div>
                    </label>
                </div>
            </div>
        `);
    });
}

// Save users to role
function saveUsersToRole() {
    if (!currentRoleId) return;
    
    const selectedUsers = [];
    $('#availableUsers input[type="checkbox"]:checked').each(function() {
        selectedUsers.push($(this).val());
    });
    
    if (selectedUsers.length === 0) {
        showError('Vui lòng chọn ít nhất một người dùng');
        return;
    }
    
    $.ajax({
        url: `/api/admin/roles/${currentRoleId}/users`,
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(selectedUsers),
        success: function() {
            $('#addUserToRoleModal').modal('hide');
            loadRoleUsers(currentRoleId);
            showSuccess('Thêm người dùng vào vai trò thành công');
        },
        error: function(xhr) {
            showError('Không thể thêm người dùng vào vai trò');
        }
    });
}

// Remove user from role
function removeUserFromRole(userId) {
    if (!currentRoleId) return;
    
    if (!confirm('Bạn có chắc chắn muốn xóa người dùng này khỏi vai trò?')) {
        return;
    }
    
    $.ajax({
        url: `/api/admin/roles/${currentRoleId}/users/${userId}`,
        method: 'DELETE',
        success: function() {
            loadRoleUsers(currentRoleId);
            showSuccess('Xóa người dùng khỏi vai trò thành công');
        },
        error: function(xhr) {
            showError('Không thể xóa người dùng khỏi vai trò');
        }
    });
}

// Search users in role
function searchRoleUsers(term) {
    if (!currentRoleId) return;
    
    $.ajax({
        url: `/api/admin/roles/${currentRoleId}/users/search?term=${encodeURIComponent(term)}`,
        method: 'GET',
        success: function(users) {
            renderRoleUsers(users);
        },
        error: function(xhr) {
            showError('Không thể tìm kiếm người dùng');
        }
    });
}

// Search available users
function searchAvailableUsers(term) {
    $.ajax({
        url: `/api/admin/users/available/search?term=${encodeURIComponent(term)}`,
        method: 'GET',
        success: function(users) {
            renderAvailableUsers(users);
        },
        error: function(xhr) {
            showError('Không thể tìm kiếm người dùng');
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