package com.Ifound.dao;

import com.Ifound.model.Document;
import com.Ifound.model.Search;
import org.springframework.data.repository.CrudRepository;

public interface SearchingDAO extends CrudRepository<Search, Object> {

    Integer countAllByDocuments(Document document);
}
