package de.tomasgng.utils.features;

public record WeatherFeature(
        boolean isEnabled,
        int changeChance,
        boolean rainToSnowInWinter,
        boolean isClearWeatherEnabled,
        boolean isStormWeatherEnabled,
        boolean isThunderWeatherEnabled
) {

}
