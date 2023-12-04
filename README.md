# Weather API Service

Weather API Service is a Spring Boot application that provides up-to-date weather information. It retrieves data from the Open-Meteo API and offers a simplified, custom endpoint for client consumption.

## Features

- Get current weather data based on latitude, longitude and additional parameters.

## Installation

To run Weather API Service, you'll need to have Java installed on your machine.

### Prerequisites

- Java JDK 21

### Setup

1. Clone the repository:

```
git clone https://github.com/dreamcareer7/weather-api.git
cd weather-api-service
```

2. Compile and package the application with Maven:

```
mvn clean package
```

3. Run the application using the Spring Boot Maven plugin:

```
mvn spring-boot:run
```

## Usage

Once the application is running, you can make requests to the endpoint to retrieve weather information:

```
GET /weather?latitude={latitude}&longitude={longitude}&hourly={hourly}&timezone={timezone}
```

Replace {latitude}, {longitude}, {hourly}, and {timezone} with your desired parameters.

### Hourly Parameter Definition

The `&hourly=` parameter accepts the following values. Most weather variables are given as an instantaneous value for the indicated hour. Some variables like precipitation are calculated from the preceding hour as an average or sum.

| Variable | Valid time | Unit | Description |
|--------------------|--------------|-----------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `temperature_2m` | Instant | °C (°F) | Air temperature at 2 meters above ground |
| `relative_humidity_2m` | Instant | % | Relative humidity at 2 meters above ground |
| `dew_point_2m` | Instant | °C (°F) | Dew point temperature at 2 meters above ground |
| `apparent_temperature` | Instant | °C (°F) | Apparent temperature is the perceived feels-like temperature combining wind chill factor, relative humidity and solar radiation |
| `pressure_msl`<br>`surface_pressure` | Instant | hPa | Atmospheric air pressure reduced to mean sea level (msl) or pressure at surface. Typically pressure on mean sea level is used in meteorology. Surface pressure gets lower with increasing elevation. |
| `cloud_cover` | Instant | % | Total cloud cover as an area fraction |
| `cloud_cover_low` | Instant | % | Low level clouds and fog up to 3 km altitude |
| `cloud_cover_mid` | Instant | % | Mid level clouds from 3 to 8 km altitude |
| `cloud_cover_high` | Instant | % | High level clouds from 8 km altitude |
| `wind_speed_10m`<br>`wind_speed_80m`<br>`wind_speed_120m`<br>`wind_speed_180m` | Instant | km/h (mph, m/s, knots) | Wind speed at 10, 80, 120 or 180 meters above ground. Wind speed on 10 meters is the standard level. |
| `wind_direction_10m`<br>`wind_direction_80m`<br>`wind_direction_120m`<br>`wind_direction_180m` | Instant | ° | Wind direction at 10, 80, 120 or 180 meters above ground |
| `wind_gusts_10m` | Preceding hour max | km/h (mph, m/s, knots) | Gusts at 10 meters above ground as a maximum of the preceding hour |
| `shortwave_radiation` | Preceding hour mean | W/m² | Shortwave solar radiation as average of the preceding hour. This is equal to the total global horizontal irradiation |
| `direct_radiation`<br>`direct_normal_irradiance` | Preceding hour mean | W/m² | Direct solar radiation as average of the preceding hour on the horizontal plane and the normal plane (perpendicular to the sun) |
| `diffuse_radiation` | Preceding hour mean | W/m² | Diffuse solar radiation as average of the preceding hour |
| `vapour_pressure_deficit` | Instant | kPa | Vapour Pressure Deficit (VPD) in kilopascal (kPa). For high VPD (>1.6), water transpiration of plants increases. For low VPD (<0.4), transpiration decreases |
| `cape` | Instant | J/kg | Convective available potential energy. See Wikipedia. |
| `evapotranspiration` | Preceding hour sum | mm (inch) | Evapotranspiration from land surface and plants that weather models assume for this location. Available soil water is considered. 1 mm evapotranspiration per hour equals 1 liter of water per square meter. |
| `et0_fao_evapotranspiration` | Preceding hour sum | mm (inch) | ET₀ Reference Evapotranspiration of a well-watered grass field. Based on FAO-56 Penman-Monteith equations ET₀ is calculated from temperature, wind speed, humidity and solar radiation. Unlimited soil water is assumed. ET₀ is commonly used to estimate the required irrigation for plants. |
| `precipitation` | Preceding hour sum | mm (inch) | Total precipitation (rain, showers, snow) sum of the preceding hour |
| `snowfall` | Preceding hour sum | cm (inch) | Snowfall amount of the preceding hour in centimeters. For the water equivalent in millimeters, divide by 7. E.g., 7 cm snow = 10 mm precipitation water equivalent |
| `precipitation_probability` | Preceding hour probability | % | Probability of precipitation with more than 0.1 mm of the preceding hour. Probability is based on ensemble weather models with 0.25° (~27 km) resolution. 30 different simulations are computed to better represent future weather conditions. |
| `rain` | Preceding hour sum | mm (inch) | Rain from large-scale weather systems of the preceding hour in millimeters |
| `showers` | Preceding hour sum | mm (inch) | Showers from convective precipitation in millimeters from the preceding hour |
| `weather_code` | Instant | WMO code | Weather condition as a numeric code. Follow WMO weather interpretation codes. See table below for details. |
| `snow_depth` | Instant | meters | Snow depth on the ground |
| `freezing_level_height` | Instant | meters | Altitude above sea level of the 0°C level |
| `visibility` | Instant | meters | Viewing distance in meters. Influenced by low clouds, humidity and aerosols. Maximum visibility is approximately 24 km. |
| `soil_temperature_0cm`<br>`soil_temperature_6cm`<br>`soil_temperature_18cm`<br>`soil_temperature_54cm` | Instant | °C (°F) | Temperature in the soil at 0, 6, 18, and 54 cm depths. 0 cm is the surface temperature on land or water surface temperature on water. |
| `soil_moisture_0_to_1cm`<br>`soil_moisture_1_to_3cm`<br>`soil_moisture_3_to_9cm`<br>`soil_moisture_9_to_27cm`<br>`soil_moisture_27_to_81cm` | Instant | m³/m³ | Average soil water content as volumetric mixing ratio at 0-1, 1-3, 3-9, 9-27, and 27-81 cm depths. |
| `is_day` | Instant | Dimensionless | 1 if the current time step has daylight, 0 at night. |

### Timezone Parameter Definition
If timezone is set, all timestamps are returned as local-time and data is returned starting at 00:00 local-time. Any time zone name from the [time zone database](https://en.wikipedia.org/wiki/List_of_tz_database_time_zones) is supported. If auto is set as a time zone, the coordinates will be automatically resolved to the local time zone. For multiple coordinates, a comma separated list of timezones can be specified.