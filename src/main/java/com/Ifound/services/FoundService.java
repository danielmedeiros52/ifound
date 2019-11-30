package com.Ifound.services;

import com.Ifound.dto.DocumentDTO;
import com.Ifound.model.Document;
import com.Ifound.model.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class FoundService {

    private final DocumentService documentService;
    private final SearchingService searchingService;

    public FoundService(DocumentService documentService, SearchingService searchingService) {
        this.documentService = documentService;
        this.searchingService = searchingService;
    }

    public void foundDocument(List<DocumentDTO> listDto) {
        List<Document> documentList = new ArrayList<>();
        for (DocumentDTO dto : listDto) {
            Document document = Document.builder()
                    .numberOfRegistry(dto.getDocumentNumber())
                    .foundDate(LocalDateTime.now())
                    .ownerName(dto.getOwnerName())
                    .build();
            documentList.add(document);
        }
        save(documentList);
    }

    private void save(List<Document> documentList) {
        documentList.forEach(document -> save(document));
    }

    private void save(Document document) {
        processDocuments(documentService.save(lookingFor(document)));
    }

    private Document lookingFor(Document document) {
        Document doc = documentService.findDocument(document);
        return doc != null ? doc : document;
    }

    private User matchDocumentWithOwn(Document document) {
        return documentService.findOwner(document);
    }

    private void processDocuments(Document document) {
        User user = matchDocumentWithOwn(document);
        if (user != null) {
            sendMail(user);
        } else {
            searchingService.searchingDocument(document);
        }

    }

    private void sendMail(User user) {
        System.out.println("MAIL SENT");
        System.out.println("Name: " + user.getName());
        System.out.println("Mail: " + user.getEmail());
    }


}
