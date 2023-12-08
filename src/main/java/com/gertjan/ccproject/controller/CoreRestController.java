package com.gertjan.ccproject.controller;

import com.gertjan.ccproject.domain.DataObject;
import com.gertjan.ccproject.service.CoreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
public class CoreRestController {

    private final CoreService service;

    public CoreRestController(CoreService service) {
        this.service = service;
    }

    @GetMapping(path = "/data/{id}")
    public ResponseEntity<DataObject> getById(@PathVariable UUID id) {
        return new ResponseEntity<>(service.getObjectById(id), HttpStatus.OK);
    }

    @GetMapping(path = "/data")
    public List<DataObject> getAll() {
        return service.getAll();
    }

    @PostMapping(path = "/data/add")
    public ResponseEntity<DataObject> add(@RequestBody DataObject dataObject) {
        return new ResponseEntity<>(service.persist(dataObject), HttpStatus.CREATED);
    }

    @PutMapping(path = "/data/{id}")
    public ResponseEntity<DataObject> update(@PathVariable UUID id, @RequestBody DataObject dataObject) {
        return new ResponseEntity<>(service.update(id, dataObject), HttpStatus.OK);
    }

    @DeleteMapping(path = "/data/{id}")
    public ResponseEntity<Objects> delete(@PathVariable UUID id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
