package com.Ifound.services;

import com.Ifound.dao.SearchingDAO;
import com.Ifound.model.Document;
import com.Ifound.model.Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SearchingService {

    private final SearchingDAO dao;

    @Autowired
    public SearchingService(SearchingDAO dao) {
        this.dao = dao;
    }

    public void searchingDocument(Document document) {
        List<Document> documentList = new ArrayList<>();
        documentList.add(document);
        int i = dao.countAllByDocuments(document);

        dao.save(new Search(LocalDateTime.now(), documentList));


    }
}
