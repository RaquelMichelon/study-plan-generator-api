package com.raquelmichelon.studynotesspringbootopenai;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
public class StudyNotesController {

    @Autowired
    private StudyNotesServiceOpenAI service;

    // return only the text
    @PostMapping("/study-plans")
    public Mono<String> createStudyPlan(@RequestBody String subject) {
        return service.createStudyNotes(subject)
                .map(response -> response.choices().get(0).text());
    }

}
