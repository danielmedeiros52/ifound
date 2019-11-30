package com.Ifound.services;

import com.Ifound.dao.DocumentDAO;
import com.Ifound.dao.UserDAO;
import com.Ifound.model.Document;
import com.Ifound.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocumentService {

    private final DocumentDAO dao;
    private final UserDAO userDAO;

    @Autowired
    public DocumentService(DocumentDAO dao, UserDAO userDAO) {
        this.dao = dao;
        this.userDAO = userDAO;
    }

    public Document save(Document document) {
        return dao.save(document);
    }

    public User findOwner(Document document) {
        return userDAO.findByOwnerDocumentNumber(document.getNumberOfRegistry());
    }

    public Document findDocument(Document document) {
        return dao.findByNumberOfRegistry(document.getNumberOfRegistry());
    }
}
