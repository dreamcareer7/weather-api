package com.sinead.weatherapi.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sinead.weatherapi.dto.WeatherResponse;
import com.sinead.weatherapi.exception.ExternalServiceException;
import com.sinead.weatherapi.exception.WeatherNotFoundException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.stream.DoubleStream;

@Service
public class WeatherService {

    @Value("${open-meteo.api.url}")
    private String weatherApiUrl;
    @Value("${open-meteo.api.path}")
    private String weatherApiPath;
    @Value("${open-meteo.api.forecast_days}")
    private String weatherApiForecastDays;

    private WebClient webClient;
    private final WebClient.Builder webClientBuilder;

    public WeatherService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @PostConstruct
    public void init() {
        this.webClient = webClientBuilder.baseUrl(weatherApiUrl).build();
    }

    public Mono<WeatherResponse> getCurrentWeather(double latitude, double longitude, String hourly, String timezone) {
        return webClient.get().uri(uriBuilder -> uriBuilder
                        .path(weatherApiPath)
                        .queryParam("forecast_days", weatherApiForecastDays)
                        .queryParam("latitude", latitude)
                        .queryParam("longitude", longitude)
                        .queryParam("hourly", hourly)
                        .queryParam("timezone", timezone)
                        .build())
                .retrieve()
                .onStatus(HttpStatus.BAD_REQUEST::equals, response -> handleBadRequest(response, latitude, longitude))
                .onStatus(HttpStatus.INTERNAL_SERVER_ERROR::equals,
                            clientResponse -> Mono.error(new ExternalServiceException("External Service Exception while calling Open-Meteo API!")))
                .bodyToMono(JsonNode.class)
                .map(this::convertToWeatherResponse);
    }

    private Mono<? extends Throwable> handleBadRequest(ClientResponse clientResponse, double latitude, double longitude) {
        return clientResponse
                .bodyToMono(JsonNode.class)
                .flatMap(errorMessage -> Mono.error(new WeatherNotFoundException(
                        "Weather not found for location: latitude-" + latitude + ", longitude-" + longitude + "! (" + errorMessage.get("reason").asText() + ")")));
    }

    private WeatherResponse convertToWeatherResponse(JsonNode jsonNode) {
        // Parse the JSON response to extract needed data.
        double latitude = jsonNode.get("latitude").asDouble();
        double longitude = jsonNode.get("longitude").asDouble();
        String timezone = formatTimezone(jsonNode); // format: American/New_York (EST)
        String date = LocalDate.now().toString();
        JsonNode hourly_units = jsonNode.get("hourly_units");
        JsonNode hourly = jsonNode.get("hourly");
        JsonNode average_weather_info = calculateAverageWeatherInfo(hourly_units, hourly);

        return new WeatherResponse(latitude, longitude, timezone, date, average_weather_info, hourly_units, hourly);
    }

    private String formatTimezone(JsonNode jsonNode) {
        return jsonNode.get("timezone").asText() + " (" + jsonNode.get("timezone_abbreviation").asText() + ")";
    }

    private JsonNode calculateAverageWeatherInfo(JsonNode hourly_units, JsonNode hourly) {
        ObjectNode average_weather_info = JsonNodeFactory.instance.objectNode();

        // Iterate each weather variable (ex: Temperature, Relative Humidity, ...)
        hourly_units.fieldNames().forEachRemaining(weatherVariable -> {

            if (!weatherVariable.equals("time")) {
                JsonNode weatherValues = hourly.get(weatherVariable);
                double[] iWeatherValues = new double[weatherValues.size()];

                for (int i = 0; i < iWeatherValues.length; i++) {
                    iWeatherValues[i] = weatherValues.get(i).asDouble();
                }

                double weatherAverage = DoubleStream.of(iWeatherValues).average().orElse(0);
                average_weather_info.put(weatherVariable, (int) weatherAverage + hourly_units.get(weatherVariable).asText());
            }
        });

        return average_weather_info;
    }
}
