package com.app.todoapp.service;

import com.app.todoapp.model.Task;
import com.app.todoapp.model.User;
import com.app.todoapp.repository.TaskRepository;
import com.app.todoapp.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class TaskService {
    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;

    public Page<Task> getAllTasks(int page, int size) {
        return taskRepository.findAll(PageRequest.of(page, size));
    }

    public Optional<Task> getTaskById(Long id) {
        if (id == null) {
            logger.error("Task ID is null when fetching task");
            throw new IllegalArgumentException("Task ID cannot be null");
        }
        return taskRepository.findById(id);
    }

    @Transactional
    public Task createTask(Task task,User user) {
        if (task.getTitle() == null || task.getTitle().trim().isEmpty()) {
            logger.error("Task title cannot be null or empty");
            throw new IllegalArgumentException("Task title cannot be null or empty");
        }
        try {
            task.setCreatedAt(LocalDateTime.now());
            task.setCompleted(false);
            task.setUser(user);
            taskRepository.save(task);// Устанавливаем по умолчанию

            logger.info("Task created: {} at {}", task.getTitle(), task.getCreatedAt());
            return task;
        } catch (Exception e) {
            logger.error("Error creating task: {}", e.getMessage());
            throw new RuntimeException("Failed to create task", e);
        }
    }


    @Transactional
    public Task updateTask(Long id, Task task) {
        if (id == null) {
            logger.error("Task ID cannot be null");
            throw new IllegalArgumentException("Task ID cannot be null");
        }
        if (task.getTitle() == null || task.getTitle().trim().isEmpty()) {
            logger.error("Task title cannot be null or empty");
            throw new IllegalArgumentException("Task title cannot be null or empty");
        }
        Optional<Task> existingTask = taskRepository.findById(id);
        if (existingTask.isPresent()) {
            Task updatedTask = existingTask.get();
            updatedTask.setTitle(task.getTitle());
            updatedTask.setDescription(task.getDescription());
            updatedTask.setCompleted(task.isCompleted());
            try {
                taskRepository.save(updatedTask);
                logger.info("Task updated: {} at {}", id);
                return updatedTask;
            } catch (Exception e) {
                logger.error("Error updating task: {}", e.getMessage());
                throw new RuntimeException("Failed to update task", e);
            }
        }
        return null;
    }

    @Transactional
    public void deleteTask(Long id) {
        if (id == null) {
            logger.error("Task ID cannot be null");
            throw new IllegalArgumentException("Task ID cannot be null");
        }
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Task not found: {}", id);
                    return new IllegalArgumentException("Task not found");
                });
        try {
            taskRepository.deleteById(id);
            logger.info("Task deleted: {} (originally created at {})", id, task.getCreatedAt());
        } catch (Exception e) {
            logger.error("Error deleting task: {}", e.getMessage());
            throw new RuntimeException("Failed to delete task", e);
        }
    }
    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
    }
}