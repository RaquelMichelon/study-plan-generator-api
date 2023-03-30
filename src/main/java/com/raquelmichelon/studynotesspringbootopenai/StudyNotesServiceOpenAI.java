package com.raquelmichelon.studynotesspringbootopenai;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import reactor.core.publisher.Mono;

@Service
public class StudyNotesServiceOpenAI {

    private WebClient webClient;

    public StudyNotesServiceOpenAI(WebClient.Builder webClientBuilder, @Value("${openai.api.key}") String apiKey) {
        this.webClient = webClientBuilder
                .baseUrl("https://api.openai.com/v1/completions")
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Authorization", String.format("Bearer %s", apiKey))
                .build();
    }

    public Mono<OpenAIResponse> createStudyNotes(String subject) {
        OpenAIRequest request = createStudyRequest(subject);

        return webClient.post()
                .bodyValue(request)
                .retrieve()
                .bodyToMono(OpenAIResponse.class);
    }

    private OpenAIRequest createStudyRequest(String subject) {

        // prompt question
        String prompt = String.format("Please enterWhat are 5 key points I should know when studying %s?", subject);

        return new OpenAIRequest("text-davinci-003", prompt, 0.3, 2000, 1.0, 0.0, 0.0);
    }

}

// para serializar em snakecase and desserializar em camelcase
@JsonNaming(SnakeCaseStrategy.class)
record OpenAIRequest(
        String model, String prompt,
        Double temperature, Integer maxTokens, Double topP, Double frequencyPenalty, Double presencePenalty) {
}

record OpenAIResponse(List<Choice> choices) {
}

record Choice(String text) {
}
