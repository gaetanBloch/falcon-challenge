package io.gbloch.falcon.challenge.core.domain;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public final class JourneyLog {

    private String currentPlanet;
    private int travelDays;
    private int autonomyLeft;
    private List<String> route;
}
