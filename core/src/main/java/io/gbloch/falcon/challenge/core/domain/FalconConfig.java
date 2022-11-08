package io.gbloch.falcon.challenge.core.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FalconConfig(
    int autonomy,
    String departure,
    String arrival,
    @JsonProperty("routes_db") String RoutesDbPath) {

}
