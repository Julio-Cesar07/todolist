package com.jfilho.todolist.task;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jfilho.todolist.utils.Utils;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("tasks")
public class TaskController {
    
    @Autowired
    private ITaskRepository taskRepository;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody TaskModel taskModel, HttpServletRequest request) {
        UUID userId = (UUID) request.getAttribute("userId");
        taskModel.setUserId(userId);

        var currentDate = LocalDateTime.now();
        
        if(currentDate.isAfter(taskModel.getStartAt())) {
            return ResponseEntity.status(409).body("A data de início deve ser maior que o horário atual.");
        }

        if(currentDate.isAfter(taskModel.getEndAt()) || taskModel.getStartAt().isAfter(taskModel.getEndAt())){
            return ResponseEntity.status(409).body("A data de fim deve ser maior que o horário atual e que a data de início.");
        }

        var task = this.taskRepository.save(taskModel);
        return ResponseEntity.status(201).body(task);
    }

    @GetMapping
    public ResponseEntity<?> list(HttpServletRequest request){
        var userId = (UUID) request.getAttribute("userId");

        var taskList = this.taskRepository.findByUserId(userId);

        return ResponseEntity.status(200).body(taskList);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody TaskModel taskModel, HttpServletRequest request, @PathVariable UUID id){

        var task = this.taskRepository.findById(id).orElse(null);

        if(task == null) return ResponseEntity.status(409).body("Task não encontrada.");

        var userId = request.getAttribute("userId");

        if(!task.getUserId().equals(userId)) ResponseEntity.status(401).body("Usuário não autorizado.");

        Utils.copyNonNullProperties(taskModel, task);

        this.taskRepository.save(task);

        return ResponseEntity.status(200).body(task);
    }
}
