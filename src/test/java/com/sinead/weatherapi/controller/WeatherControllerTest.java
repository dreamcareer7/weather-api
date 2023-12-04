package com.sinead.weatherapi.controller;

import com.sinead.weatherapi.dto.WeatherResponse;
import com.sinead.weatherapi.exception.ExternalServiceException;
import com.sinead.weatherapi.exception.WeatherNotFoundException;
import com.sinead.weatherapi.service.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WeatherControllerTest {
    @Mock
    private WeatherService mockWeatherService;

    private WeatherController weatherController;


    @BeforeEach
    public void setup() {
        weatherController = new WeatherController(mockWeatherService);
    }

    @Test
    public void getWeather_Successful() {
        WeatherResponse expectedResponse = new WeatherResponse();
        when(mockWeatherService.getCurrentWeather(anyDouble(), anyDouble(), anyString(), anyString()))
                .thenReturn(Mono.just(expectedResponse));

        Mono<ResponseEntity<WeatherResponse>> actualResponse = weatherController.getWeather(52.52, 13.41, "temperature_2m", "America/New_York");

        StepVerifier.create(actualResponse)
                .expectNextMatches(responseEntity -> responseEntity.getStatusCode() == HttpStatus.OK)
                .verifyComplete();
    }

    @Test
    public void getWeather_NotFound() {
        when(mockWeatherService.getCurrentWeather(anyDouble(), anyDouble(), anyString(), anyString()))
                .thenReturn(Mono.empty());

        Mono<ResponseEntity<WeatherResponse>> actualResponse = weatherController.getWeather(52.52, 13.41, "temperature_2m", "America/New_York");

        StepVerifier.create(actualResponse)
                .expectNextMatches(responseEntity -> responseEntity.getStatusCode() == HttpStatus.NOT_FOUND)
                .verifyComplete();
    }

    @Test
    public void handleWeatherNotFoundException() {
        WeatherNotFoundException exception = new WeatherNotFoundException("Weather data not found");

        ResponseEntity<String> actualResponse = weatherController.handleWeatherNotFoundException(exception);

        assert actualResponse.getStatusCode() == HttpStatus.BAD_REQUEST;
        assert "Weather data not found".equals(actualResponse.getBody());
    }

    @Test
    public void handleExternalServiceException() {
        ExternalServiceException exception = new ExternalServiceException("External service failed");

        ResponseEntity<String> actualResponse = weatherController.handleExternalServiceException(exception);

        assert actualResponse.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR;
        assert "External service failed".equals(actualResponse.getBody());
    }
}
