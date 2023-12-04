package com.sinead.weatherapi.controller;

import com.sinead.weatherapi.dto.WeatherResponse;
import com.sinead.weatherapi.exception.ExternalServiceException;
import com.sinead.weatherapi.exception.WeatherNotFoundException;
import com.sinead.weatherapi.service.WeatherService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService){
        this.weatherService = weatherService;
    }

    @GetMapping("/weather")
    public Mono<ResponseEntity<WeatherResponse>> getWeather(@RequestParam double latitude, @RequestParam double longitude, @RequestParam String hourly, @RequestParam String timezone){
        return weatherService.getCurrentWeather(latitude, longitude, hourly, timezone)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @ExceptionHandler(WeatherNotFoundException.class)
    public ResponseEntity<String> handleWeatherNotFoundException(WeatherNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(ExternalServiceException.class)
    public ResponseEntity<String> handleExternalServiceException(ExternalServiceException exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
    }
}
