package dev.villanidev.sportstracker.dashboard.live;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class LiveMatchesClient {

    private final RestTemplate restTemplate = new RestTemplate();

    private final String operationsBaseUrl;

    public LiveMatchesClient(@Value("${operations.base-url:http://localhost:8080}") String operationsBaseUrl) {
        this.operationsBaseUrl = operationsBaseUrl;
    }

    public List<LiveMatchSummary> getLiveMatches() {
        String url = operationsBaseUrl + "/api/matches/live";
        LiveMatchSummary[] response = restTemplate.getForObject(url, LiveMatchSummary[].class);
        if (response == null) {
            return Collections.emptyList();
        }
        return Arrays.asList(response);
    }
}
