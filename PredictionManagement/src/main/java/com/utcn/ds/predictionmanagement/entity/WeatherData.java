package com.utcn.ds.predictionmanagement.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "weather_data")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeatherData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @Column(
            name = "date",
            nullable = false
    )
    private String date;

    @Column(
            name = "temperature",
            nullable = false
    )
    private Double temperature;

    @Column(
            name = "clearSky",
            nullable = false
    )
    private Double clearSky;

    @Column(
            name = "coveredSky",
            nullable = false
    )
    private Double coveredSky;

    @Column(
            name = "cloudySky",
            nullable = false
    )
    private Double cloudySky;

    @Column(
            name = "cumulonimbusSky",
            nullable = false
    )
    private Double cumulonimbusSky;

    @Column(
            name = "hazeAtmosphere",
            nullable = false
    )
    private Double hazeAtmosphere;

    @Column(
            name = "fogAtmosphere",
            nullable = false
    )
    private Double fogAtmosphere;

    @Column(
            name = "freezingFogAtmosphere",
            nullable = false
    )
    private Double freezingFogAtmosphere;

    @Column(
            name = "weakSnowfall",
            nullable = false
    )
    private Double weakSnowfall;

    @Column(
            name = "showerRain",
            nullable = false
    )
    private Double showerRain;

    @Column(
            name = "weakRain",
            nullable = false
    )
    private Double weakRain;

    @Column(
            name = "rain",
            nullable = false
    )
    private Double rain;

    @Column(
            name = "waterAndGranulatedSnow",
            nullable = false
    )
    private Double waterAndGranulatedSnow;

    @Column(
            name = "waterAndSnow",
            nullable = false
    )
    private Double waterAndSnow;

    @Column(
            name = "storm",
            nullable = false
    )
    private Double storm;

    @Column(
            name = "flashAndLightning",
            nullable = false
    )
    private Double flashAndLightning;

    @Column(
            name = "stormWithRain",
            nullable = false
    )
    private Double stormWithRain;

    @Column(
            name = "drizzle",
            nullable = false
    )
    private Double drizzle;

    @Column(
            name = "snowShower",
            nullable = false
    )
    private Double snowShower;

    @Column(
            name = "realValue",
            nullable = false
    )
    private Double realValue;


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"date\": ").append("\"" + date + "\"");
        sb.append(",\"temperature\": ").append(temperature);
        sb.append(",\"clearSky\": ").append(clearSky);
        sb.append(",\"coveredSky\": ").append(coveredSky);
        sb.append(",\"cloudySky\": ").append(cloudySky);
        sb.append(",\"cumulonimbusSky\": ").append(cumulonimbusSky);
        sb.append(",\"hazeAtmosphere\": ").append(hazeAtmosphere);
        sb.append(",\"fogAtmosphere\": ").append(fogAtmosphere);
        sb.append(",\"freezingFogAtmosphere\": ").append(freezingFogAtmosphere);
        sb.append(",\"weakSnowfall\": ").append(weakSnowfall);
        sb.append(",\"showerRain\": ").append(showerRain);
        sb.append(",\"weakRain\": ").append(weakRain);
        sb.append(",\"rain\": ").append(rain);
        sb.append(",\"waterAndGranulatedSnow\": ").append(waterAndGranulatedSnow);
        sb.append(",\"waterAndSnow\": ").append(waterAndSnow);
        sb.append(",\"storm\": ").append(storm);
        sb.append(",\"flashAndLightning\": ").append(flashAndLightning);
        sb.append(",\"stormWithRain\": ").append(stormWithRain);
        sb.append(",\"drizzle\": ").append(drizzle);
        sb.append(",\"snowShower\": ").append(snowShower);
        sb.append(",\"realValue\": ").append(realValue);
        sb.append("}");
        return sb.toString();
    }
}
