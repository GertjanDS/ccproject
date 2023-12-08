package com.gertjan.ccproject.service;

import com.gertjan.ccproject.domain.DataObject;
import com.gertjan.ccproject.exception.NotFoundException;
import com.gertjan.ccproject.repo.DataObjectRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CoreService {

    private final DataObjectRepository repository;

    public CoreService(DataObjectRepository repository) {
        this.repository = repository;
    }


    public DataObject getObjectById(UUID id) {
        Optional<DataObject> objectOptional = repository.findById(id);

        if (objectOptional.isPresent()) {
            return objectOptional.get();
        }
        throw new NotFoundException(String.format("No object with id %s found", id));
    }

    public List<DataObject> getAll() {
        List<DataObject> objects = new ArrayList<>();
        repository.findAll().forEach(objects::add);

        return objects;
    }

    public DataObject persist(DataObject obj) {
        return repository.save(obj);
    }

    public DataObject update(UUID id, DataObject newObject) {
        Optional<DataObject> dataObject = repository.findById(id);

        if (dataObject.isEmpty()) {
            // This is technically a BAD way to do this in a real life application, as is technically allows ID enumeration
            // but for that reason we use UUID's, so it is fine...
            throw new NotFoundException(String.format("No object with id %s found", id));
        }
        DataObject object = dataObject.get();

        object.setName(newObject.getName());
        object.setDescription(newObject.getDescription());

        return repository.save(object);
    }

    public void delete(UUID id) {
        repository.deleteById(id);

    }
}
