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
    $.get('/api/roles', function(roles) {
        const rolesList = $('#rolesList');
        rolesList.empty();
        
        roles.forEach(role => {
            rolesList.append(`
                <div class="list-group-item d-flex justify-content-between align-items-center" data-role-id="${role.maSo}">
                    <div>
                        <i class="fas fa-user-tag me-2"></i>
                        <span>${role.ten}</span>
                    </div>
                    <div class="btn-group">
                        <button class="btn btn-sm btn-danger delete-role">
                            <i class="fas fa-trash"></i>
                        </button>
                    </div>
                </div>
            `);
        });
    });
}

// Select a role
function selectRole(roleId) {
    currentRoleId = roleId;
    $('#addUserToRoleBtn').prop('disabled', false);
    loadRoleUsers(roleId);
    
    // Update active state
    $('.list-group-item').removeClass('active');
    $(`[data-role-id="${roleId}"]`).addClass('active');
}

// Load users in a role
function loadRoleUsers(roleId) {
    $.get(`/api/roles/${roleId}/users`, function(users) {
        const usersTable = $('#usersInRole');
        usersTable.empty();
        
        users.forEach(user => {
            usersTable.append(`
                <tr>
                    <td>${user.maSo}</td>
                    <td>${user.ho} ${user.ten}</td>
                    <td>${user.email}</td>
                    <td>
                        <button class="btn btn-sm btn-danger remove-from-role" data-user-id="${user.maSo}">
                            <i class="fas fa-user-minus"></i>
                        </button>
                    </td>
                </tr>
            `);
        });
    });
}

// Save new role
function saveRole() {
    const roleName = $('#roleName').val();
    if (!roleName) {
        showError('Vui lòng nhập tên vai trò');
        return;
    }
    
    $.post('/api/roles', { ten: roleName }, function() {
        $('#addRoleModal').modal('hide');
        $('#roleName').val('');
        loadRoles();
        showSuccess('Thêm vai trò thành công');
    });
}

// Delete role
function deleteRole(roleId, event) {
    event.stopPropagation();
    if (!confirm('Bạn có chắc chắn muốn xóa vai trò này?')) {
        return;
    }
    
    $.ajax({
        url: `/api/roles/${roleId}`,
        type: 'DELETE',
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
    $.get('/api/users', function(users) {
        const availableUsers = $('#availableUsers');
        availableUsers.empty();
        
        users.forEach(user => {
            availableUsers.append(`
                <tr>
                    <td>${user.maSo}</td>
                    <td>${user.ho} ${user.ten}</td>
                    <td>${user.email}</td>
                    <td>
                        <button class="btn btn-sm btn-success add-to-role" data-user-id="${user.maSo}">
                            <i class="fas fa-plus"></i>
                        </button>
                    </td>
                </tr>
            `);
        });
    });
}

// Save users to role
function saveUsersToRole() {
    if (!currentRoleId) return;
    
    const selectedUsers = [];
    $('#availableUsers tr').each(function() {
        if ($(this).find('input[type="checkbox"]:checked').length > 0) {
            selectedUsers.push($(this).find('td:eq(0)').text());
        }
    });
    
    if (selectedUsers.length === 0) {
        showError('Vui lòng chọn ít nhất một người dùng');
        return;
    }
    
    $.post(`/api/roles/${currentRoleId}/users`, { userIds: selectedUsers }, function() {
        $('#addUserToRoleModal').modal('hide');
        loadRoleUsers(currentRoleId);
        showSuccess('Thêm người dùng vào vai trò thành công');
    });
}

// Remove user from role
function removeUserFromRole(userId) {
    if (!currentRoleId) return;
    
    if (!confirm('Bạn có chắc chắn muốn xóa người dùng này khỏi vai trò?')) {
        return;
    }
    
    $.ajax({
        url: `/api/roles/${currentRoleId}/users/${userId}`,
        type: 'DELETE',
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
        url: `/api/roles/${currentRoleId}/users/search?term=${encodeURIComponent(term)}`,
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
        url: `/api/users/available/search?term=${encodeURIComponent(term)}`,
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