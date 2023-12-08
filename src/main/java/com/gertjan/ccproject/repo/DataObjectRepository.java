package com.gertjan.ccproject.repo;

import com.gertjan.ccproject.domain.DataObject;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface DataObjectRepository extends CrudRepository<DataObject, UUID> {


}
