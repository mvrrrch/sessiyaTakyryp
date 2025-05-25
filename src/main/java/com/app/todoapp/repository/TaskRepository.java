package com.app.todoapp.repository;

import com.app.todoapp.model.Task;
import com.app.todoapp.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findAllByUser(User user, Pageable pageable);
    Optional<Task> findByIdAndUser(Long id, User user);
}
