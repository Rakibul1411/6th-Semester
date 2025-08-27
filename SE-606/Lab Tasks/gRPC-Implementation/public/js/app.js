// API Configuration
const API_BASE_URL = 'http://localhost:4000/api';

// Global variables
let currentPage = 1;
let currentSearch = '';
let currentRole = '';
let currentLimit = 10;
let deleteUserId = null;

// Modal instances
let userModal;
let deleteModal;

// Initialize application
document.addEventListener('DOMContentLoaded', function() {
    // Initialize Bootstrap modals
    userModal = new bootstrap.Modal(document.getElementById('userModal'));
    deleteModal = new bootstrap.Modal(document.getElementById('deleteModal'));
    
    // Load dashboard on startup
    showDashboard();
    loadDashboardData();
});

// Navigation functions
function showDashboard() {
    document.getElementById('dashboardSection').style.display = 'block';
    document.getElementById('userListSection').style.display = 'none';
    loadDashboardData();
}

function showUserList() {
    document.getElementById('dashboardSection').style.display = 'none';
    document.getElementById('userListSection').style.display = 'block';
    loadUsers();
}

// Dashboard functions
async function loadDashboardData() {
    try {
        // Load users for statistics
        const usersResponse = await fetch(`${API_BASE_URL}/users?limit=1000`);
        const usersData = await usersResponse.json();
        
        if (usersData.success) {
            const users = usersData.users || [];
            const totalUsers = usersData.total || 0;
            const adminUsers = users.filter(user => user.role === 'admin').length;
            
            document.getElementById('totalUsers').textContent = totalUsers;
            document.getElementById('adminUsers').textContent = adminUsers;
        }

        // Load queue status
        try {
            const queueResponse = await fetch('http://localhost:3000/queue/status');
            const queueData = await queueResponse.json();
            
            if (queueData.queue_status) {
                const pending = queueData.queue_status.pending || 0;
                const total = queueData.queue_status.total || 0;
                
                // Show pending jobs or indicate operational status
                if (total > 0) {
                    document.getElementById('queueStatus').textContent = `${pending} pending`;
                } else {
                    document.getElementById('queueStatus').textContent = 'Active';
                }
            } else {
                document.getElementById('queueStatus').textContent = 'Active';
            }
        } catch (error) {
            console.warn('Could not load queue status:', error);
            document.getElementById('queueStatus').textContent = 'Active';
        }

        // Check system health
        try {
            const healthResponse = await fetch(`${API_BASE_URL}/health`);
            const healthData = await healthResponse.json();
            
            document.getElementById('systemStatus').textContent = 
                healthData.status === 'OK' ? 'OK' : 'Error';
        } catch (error) {
            document.getElementById('systemStatus').textContent = 'Error';
        }

    } catch (error) {
        console.error('Error loading dashboard data:', error);
        showAlert('Error loading dashboard data', 'danger');
    }
}

async function refreshDashboard() {
    showAlert('Refreshing dashboard...', 'info');
    await loadDashboardData();
    showAlert('Dashboard refreshed successfully', 'success');
}

// User management functions
async function loadUsers(page = 1) {
    try {
        showLoading(true);
        
        currentPage = page;
        currentSearch = document.getElementById('searchInput')?.value || '';
        currentRole = document.getElementById('roleFilter')?.value || '';
        currentLimit = parseInt(document.getElementById('limitSelect')?.value || 10);

        const params = new URLSearchParams({
            page: currentPage,
            limit: currentLimit,
            search: currentSearch
        });

        const response = await fetch(`${API_BASE_URL}/users?${params}`);
        const data = await response.json();

        if (data.success) {
            displayUsers(data.users || []);
            displayPagination(data.total || 0, currentPage, currentLimit);
        } else {
            showAlert(data.message || 'Failed to load users', 'danger');
        }
    } catch (error) {
        console.error('Error loading users:', error);
        showAlert('Error loading users', 'danger');
    } finally {
        showLoading(false);
    }
}

function displayUsers(users) {
    const tbody = document.getElementById('usersTableBody');
    
    if (users.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="6" class="text-center text-muted">
                    <i class="fas fa-inbox me-2"></i>
                    No users found
                </td>
            </tr>
        `;
        return;
    }

    tbody.innerHTML = users.map(user => `
        <tr>
            <td>
                <div class="d-flex align-items-center">
                    <div class="avatar bg-primary text-white rounded-circle d-flex align-items-center justify-content-center me-2" 
                         style="width: 32px; height: 32px; font-size: 14px;">
                        ${user.name.charAt(0).toUpperCase()}
                    </div>
                    ${escapeHtml(user.name)}
                </div>
            </td>
            <td>${escapeHtml(user.email)}</td>
            <td>${escapeHtml(user.phone)}</td>
            <td>
                <span class="badge role-badge ${getRoleBadgeClass(user.role)}">
                    ${escapeHtml(user.role)}
                </span>
            </td>
            <td>${formatDate(user.created_at)}</td>
            <td>
                <div class="btn-group btn-group-sm btn-group-actions" role="group">
                    <button class="btn btn-outline-primary" onclick="viewUser('${user.id}')" title="View User">
                        <i class="fas fa-eye"></i>
                    </button>
                    <button class="btn btn-outline-secondary" onclick="editUser('${user.id}')" title="Edit User">
                        <i class="fas fa-edit"></i>
                    </button>
                    <button class="btn btn-outline-danger" onclick="showDeleteModal('${user.id}', '${escapeHtml(user.name)}')" title="Delete User">
                        <i class="fas fa-trash"></i>
                    </button>
                </div>
            </td>
        </tr>
    `).join('');
}

function displayPagination(total, page, limit) {
    const totalPages = Math.ceil(total / limit);
    const pagination = document.getElementById('pagination');
    
    if (totalPages <= 1) {
        pagination.innerHTML = '';
        return;
    }

    let paginationHTML = '';
    
    // Previous button
    paginationHTML += `
        <li class="page-item ${page === 1 ? 'disabled' : ''}">
            <a class="page-link" href="#" onclick="loadUsers(${page - 1}); return false;">
                <i class="fas fa-chevron-left"></i>
            </a>
        </li>
    `;

    // Page numbers
    const startPage = Math.max(1, page - 2);
    const endPage = Math.min(totalPages, page + 2);

    if (startPage > 1) {
        paginationHTML += `<li class="page-item"><a class="page-link" href="#" onclick="loadUsers(1); return false;">1</a></li>`;
        if (startPage > 2) {
            paginationHTML += `<li class="page-item disabled"><span class="page-link">...</span></li>`;
        }
    }

    for (let i = startPage; i <= endPage; i++) {
        paginationHTML += `
            <li class="page-item ${i === page ? 'active' : ''}">
                <a class="page-link" href="#" onclick="loadUsers(${i}); return false;">${i}</a>
            </li>
        `;
    }

    if (endPage < totalPages) {
        if (endPage < totalPages - 1) {
            paginationHTML += `<li class="page-item disabled"><span class="page-link">...</span></li>`;
        }
        paginationHTML += `<li class="page-item"><a class="page-link" href="#" onclick="loadUsers(${totalPages}); return false;">${totalPages}</a></li>`;
    }

    // Next button
    paginationHTML += `
        <li class="page-item ${page === totalPages ? 'disabled' : ''}">
            <a class="page-link" href="#" onclick="loadUsers(${page + 1}); return false;">
                <i class="fas fa-chevron-right"></i>
            </a>
        </li>
    `;

    pagination.innerHTML = paginationHTML;
}

// Search functionality
function handleSearch(event) {
    if (event.key === 'Enter' || event.type === 'input') {
        clearTimeout(window.searchTimeout);
        window.searchTimeout = setTimeout(() => {
            loadUsers(1);
        }, 500);
    }
}

// User CRUD operations
function showCreateUserModal() {
    document.getElementById('userModalTitle').innerHTML = '<i class="fas fa-user-plus me-2"></i>Add New User';
    document.getElementById('userForm').reset();
    document.getElementById('userId').value = '';
    document.getElementById('passwordField').style.display = 'block';
    document.getElementById('userPassword').required = true;
    userModal.show();
}

async function editUser(userId) {
    try {
        const response = await fetch(`${API_BASE_URL}/users/${userId}`);
        const data = await response.json();

        if (data.success && data.user) {
            const user = data.user;
            
            document.getElementById('userModalTitle').innerHTML = '<i class="fas fa-user-edit me-2"></i>Edit User';
            document.getElementById('userId').value = user.id;
            document.getElementById('userName').value = user.name;
            document.getElementById('userEmail').value = user.email;
            document.getElementById('userPhone').value = user.phone;
            document.getElementById('userRole').value = user.role;
            
            // Hide password field for editing
            document.getElementById('passwordField').style.display = 'none';
            document.getElementById('userPassword').required = false;
            
            userModal.show();
        } else {
            showAlert(data.message || 'Failed to load user', 'danger');
        }
    } catch (error) {
        console.error('Error loading user:', error);
        showAlert('Error loading user', 'danger');
    }
}

async function viewUser(userId) {
    try {
        const response = await fetch(`${API_BASE_URL}/users/${userId}`);
        const data = await response.json();

        if (data.success && data.user) {
            const user = data.user;
            
            // For now, just show an alert with user info
            // You could create a separate modal for viewing
            showAlert(
                `User Details:\nName: ${user.name}\nEmail: ${user.email}\nPhone: ${user.phone}\nRole: ${user.role}\nCreated: ${formatDate(user.created_at)}`,
                'info'
            );
        } else {
            showAlert(data.message || 'Failed to load user', 'danger');
        }
    } catch (error) {
        console.error('Error loading user:', error);
        showAlert('Error loading user', 'danger');
    }
}

async function saveUser() {
    try {
        const userId = document.getElementById('userId').value;
        const name = document.getElementById('userName').value.trim();
        const email = document.getElementById('userEmail').value.trim();
        const password = document.getElementById('userPassword').value;
        const phone = document.getElementById('userPhone').value.trim();
        const role = document.getElementById('userRole').value;

        // Validation
        if (!name || !email || !phone) {
            showAlert('Please fill in all required fields', 'danger');
            return;
        }

        if (!userId && !password) {
            showAlert('Password is required for new users', 'danger');
            return;
        }

        const userData = { name, email, phone, role };
        if (password) {
            userData.password = password;
        }

        let response;
        if (userId) {
            // Update existing user
            response = await fetch(`${API_BASE_URL}/users/${userId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(userData)
            });
        } else {
            // Create new user
            response = await fetch(`${API_BASE_URL}/users`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(userData)
            });
        }

        const data = await response.json();

        if (data.success) {
            showAlert(
                userId ? 'User updated successfully' : 'User created successfully',
                'success'
            );
            userModal.hide();
            
            // Refresh the appropriate view
            if (document.getElementById('userListSection').style.display !== 'none') {
                loadUsers(currentPage);
            } else {
                loadDashboardData();
            }
        } else {
            showAlert(data.message || 'Failed to save user', 'danger');
        }
    } catch (error) {
        console.error('Error saving user:', error);
        showAlert('Error saving user', 'danger');
    }
}

function showDeleteModal(userId, userName) {
    deleteUserId = userId;
    document.getElementById('deleteUserInfo').textContent = `User: ${userName}`;
    deleteModal.show();
}

async function confirmDelete() {
    if (!deleteUserId) return;

    try {
        const response = await fetch(`${API_BASE_URL}/users/${deleteUserId}`, {
            method: 'DELETE'
        });

        const data = await response.json();

        if (data.success) {
            showAlert('User deleted successfully', 'success');
            deleteModal.hide();
            
            // Refresh the appropriate view
            if (document.getElementById('userListSection').style.display !== 'none') {
                loadUsers(currentPage);
            } else {
                loadDashboardData();
            }
        } else {
            showAlert(data.message || 'Failed to delete user', 'danger');
        }
    } catch (error) {
        console.error('Error deleting user:', error);
        showAlert('Error deleting user', 'danger');
    } finally {
        deleteUserId = null;
    }
}

// Utility functions
function showAlert(message, type = 'info') {
    const alertContainer = document.getElementById('alertContainer');
    const alertId = 'alert-' + Date.now();
    
    const alertHTML = `
        <div id="${alertId}" class="alert alert-${type} alert-dismissible fade show" role="alert">
            <i class="fas fa-${getAlertIcon(type)} me-2"></i>
            ${escapeHtml(message)}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    `;
    
    alertContainer.insertAdjacentHTML('beforeend', alertHTML);
    
    // Auto-remove alert after 5 seconds
    setTimeout(() => {
        const alertElement = document.getElementById(alertId);
        if (alertElement) {
            const alert = new bootstrap.Alert(alertElement);
            alert.close();
        }
    }, 5000);
}

function getAlertIcon(type) {
    switch (type) {
        case 'success': return 'check-circle';
        case 'danger': return 'exclamation-circle';
        case 'warning': return 'exclamation-triangle';
        case 'info': return 'info-circle';
        default: return 'info-circle';
    }
}

function showLoading(show) {
    const loadingRow = document.querySelector('#usersTableBody .loading');
    if (loadingRow) {
        loadingRow.style.display = show ? 'table-row' : 'none';
    }
}

function getRoleBadgeClass(role) {
    switch (role) {
        case 'admin': return 'bg-danger';
        case 'moderator': return 'bg-warning';
        case 'user': return 'bg-primary';
        default: return 'bg-secondary';
    }
}

function formatDate(dateString) {
    try {
        const date = new Date(dateString);
        return date.toLocaleDateString() + ' ' + date.toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'});
    } catch (error) {
        return 'Invalid Date';
    }
}

function escapeHtml(text) {
    const map = {
        '&': '&amp;',
        '<': '&lt;',
        '>': '&gt;',
        '"': '&quot;',
        "'": '&#039;'
    };
    return text.replace(/[&<>"']/g, m => map[m]);
}
