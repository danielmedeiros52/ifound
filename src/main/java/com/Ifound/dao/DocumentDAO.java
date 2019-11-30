package com.Ifound.dao;

import com.Ifound.model.Document;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentDAO extends CrudRepository <Document, Object> {
    Document findByNumberOfRegistry(String numberOfRegistry);
}
