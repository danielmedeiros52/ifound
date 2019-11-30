package com.Ifound.controller;

import com.Ifound.dto.DocumentDTO;
import com.Ifound.services.FoundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/found")
public class FoundController {

    private final FoundService foundService;

    @Autowired
    public FoundController(FoundService foundService) {
        this.foundService = foundService;
    }

    @PostMapping
    public ResponseEntity foundDocument(@RequestBody List<DocumentDTO> documentDTO) {
        foundService.foundDocument(documentDTO);
        return ResponseEntity.ok("Thank you!");
    }

}
