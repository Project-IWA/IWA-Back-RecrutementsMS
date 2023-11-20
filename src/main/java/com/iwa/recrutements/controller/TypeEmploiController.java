package com.iwa.recrutements.controller;

import com.iwa.recrutements.model.TypeEmploi;
import com.iwa.recrutements.service.TypeEmploiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/typeEmplois")
public class TypeEmploiController {

    private TypeEmploiService typeEmploiService;

    @Autowired
    public TypeEmploiController(TypeEmploiService typeEmploiService) {
        this.typeEmploiService = typeEmploiService;
    }

    // All users can get all typeEmplois
    @GetMapping
    public ResponseEntity<List<TypeEmploi>> getAllTypeEmplois() {
        List<TypeEmploi> typeEmplois = typeEmploiService.getAllTypeEmplois();
        return new ResponseEntity<>(typeEmplois, HttpStatus.OK);
    }
}
