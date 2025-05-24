const apiUrl = '/api/tasks';

async function fetchTasks() {
    try {
        const response = await fetch(`${apiUrl}?page=0&size=10`);
        if (!response.ok) throw new Error('Failed to fetch tasks');
        const data = await response.json();
        const tasks = data.content;
        const taskList = document.getElementById('taskList');
        taskList.innerHTML = '';
        tasks.forEach(task => {
            const li = document.createElement('li');
            li.className = 'flex justify-between items-center p-2 bg-white rounded shadow';
            li.innerHTML = `
                <div>
                    <input type="checkbox" ${task.completed ? 'checked' : ''} onchange="updateTask(${task.id}, this.checked)">
                    <span class="${task.completed ? 'line-through' : ''}">${task.title}</span>
                    <p class="text-gray-600">${task.description || ''}</p>
                </div>
                <div>
                    <button onclick="showEditModal(${task.id}, '${task.title}', '${task.description || ''}')" class="bg-yellow-500 text-white p-1 rounded mr-2">Edit</button>
                    <button onclick="deleteTask(${task.id})" class="bg-red-500 text-white p-1 rounded">Delete</button>
                </div>
            `;
            taskList.appendChild(li);
        });
    } catch (error) {
        showError(error.message);
    }
}

async function addTask() {
    const title = document.getElementById('taskTitle').value;
    const description = document.getElementById('taskDescription').value;
    if (!title) {
        showError('Title is required');
        return;
    }
    try {
        const response = await fetch(apiUrl, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ title, description, completed: false })
        });
        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.title || 'Failed to add task');
        }
        document.getElementById('taskTitle').value = '';
        document.getElementById('taskDescription').value = '';
        fetchTasks();
    } catch (error) {
        showError(error.message);
    }
}

async function updateTask(id, completed) {
    try {
        const response = await fetch(`${apiUrl}/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ completed })
        });
        if (!response.ok) throw new Error('Failed to update task');
        fetchTasks();
    } catch (error) {
        showError(error.message);
    }
}

async function updateTaskDetails(id) {
    const title = document.getElementById('editTaskTitle').value;
    const description = document.getElementById('editTaskDescription').value;
    if (!title) {
        showError('Title is required');
        return;
    }
    try {
        const response = await fetch(`${apiUrl}/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ title, description, completed: false })
        });
        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.title || 'Failed to update task');
        }
        hideEditModal();
        fetchTasks();
    } catch (error) {
        showError(error.message);
    }
}

async function deleteTask(id) {
    try {
        const response = await fetch(`${apiUrl}/${id}`, { method: 'DELETE' });
        if (!response.ok) throw new Error('Failed to delete task');
        fetchTasks();
    } catch (error) {
        showError(error.message);
    }
}

function showError(message) {
    const errorDiv = document.createElement('div');
    errorDiv.className = 'text-red-500 p-2 mb-2 bg-red-100 rounded';
    errorDiv.textContent = message;
    document.querySelector('.container').insertBefore(errorDiv, document.getElementById('taskList'));
    setTimeout(() => errorDiv.remove(), 3000);
}

function showEditModal(id, title, description) {
    const modal = document.createElement('div');
    modal.id = 'editModal';
    modal.className = 'fixed inset-0 bg-gray-600 bg-opacity-50 flex items-center justify-center';
    modal.innerHTML = `
        <div class="bg-white p-4 rounded shadow-lg">
            <h2 class="text-xl mb-4">Edit Task</h2>
            <input type="text" id="editTaskTitle" value="${title}" class="border p-2 mb-2 w-full">
            <input type="text" id="editTaskDescription" value="${description}" class="border p-2 mb-2 w-full">
            <div class="flex justify-end">
                <button onclick="updateTaskDetails(${id})" class="bg-blue-500 text-white p-2 rounded mr-2">Save</button>
                <button onclick="hideEditModal()" class="bg-gray-500 text-white p-2 rounded">Cancel</button>
            </div>
        </div>
    `;
    document.body.appendChild(modal);
}

function hideEditModal() {
    const modal = document.getElementById('editModal');
    if (modal) modal.remove();
}

document.addEventListener('DOMContentLoaded', fetchTasks);