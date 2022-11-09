package io.gbloch.falcon.challenge.core.application.service;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import io.gbloch.falcon.challenge.core.application.FalconCoreException;
import io.gbloch.falcon.challenge.core.application.input.port.ComputeOddsUseCase;
import io.gbloch.falcon.challenge.core.application.service.db.GalaxyDbException;
import io.gbloch.falcon.challenge.core.application.service.db.GalaxyDbFileReader;
import io.gbloch.falcon.challenge.core.application.service.parser.FalconFileException;
import io.gbloch.falcon.challenge.core.application.service.parser.FalconFileParser;
import io.gbloch.falcon.challenge.core.domain.Empire;
import io.gbloch.falcon.challenge.core.domain.JourneyLog;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.stream.Stream;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@ApplicationScoped
final class OddsService implements ComputeOddsUseCase {

    final FalconFileParser falconFileParser;
    final GalaxyDbFileReader galaxyDbFileReader;

    final Multimap<String, Integer> bountyHunterPresence = HashMultimap.create();

    @Inject
    @Setter
    Validator validator;

    Empire empire;
    JourneyContext journeyContext;

    @Override
    public int whatAreTheOdds(String configFilePath, Empire empire) throws FalconCoreException {
        empire.validate(validator);
        try {
            log.info("Computing odds for empire {}...", empire);
            initJourney(configFilePath, empire);
        } catch (FalconFileException | GalaxyDbException e) {
            throw new FalconCoreException(e);
        } catch (Exception e) {
            log.error("Unexpected error", e);
            throw new FalconCoreException("Unexpected exception", e);
        }

        List<JourneyLog> journeyLogs = getSuccessfulPaths();
        if (journeyLogs.isEmpty()) {
            return 0;
        }
        int hunters = Integer.MAX_VALUE;
        for (JourneyLog journeyLog : journeyLogs) {
            int numberOfHunters = numberOfHuntersOnRoute(journeyLog.getRoute());
            if (numberOfHunters < hunters) {
                hunters = numberOfHunters;
            }
        }

        log.info("Computing odds for empire {}... OK", empire);
        return calculateOdds(hunters);
    }

    private void initJourney(String configFilePath, Empire empire) {
        this.empire = empire;
        this.empire.bountyHunters().forEach((bountyHunter) ->
            this.bountyHunterPresence.put(bountyHunter.planet(), bountyHunter.day()));
        this.journeyContext = JourneyContext.getInstance(falconFileParser, galaxyDbFileReader,
            configFilePath);
    }

    private List<JourneyLog> getSuccessfulPaths() {
        final Queue<JourneyLog> journey = new LinkedList<>();
        final List<JourneyLog> successfulJourneys = new ArrayList<>();

        journey.add(new JourneyLog(
            journeyContext.getFalconConfig().departure(),
            0,
            journeyContext.getFalconConfig().autonomy(),
            new ArrayList<>())
        );

        while (!journey.isEmpty()) {
            final JourneyLog journeyLog = journey.poll();

            for (String nextPlanet : journeyContext.getGalaxy()
                .successors(journeyLog.getCurrentPlanet())) {
                final Optional<Integer> daysToNextPlanetOpt = journeyContext.getGalaxy().edgeValue(
                    journeyLog.getCurrentPlanet(),
                    nextPlanet
                );
                int daysToNextPlanet;
                if (daysToNextPlanetOpt.isPresent()) {
                    daysToNextPlanet = daysToNextPlanetOpt.get();
                } else {
                    throw new IllegalStateException(
                        "No edge between " + journeyLog.getCurrentPlanet() + " and " + nextPlanet);
                }

                if (daysToNextPlanet <= journeyLog.getAutonomyLeft()
                    && daysToNextPlanet + journeyLog.getTravelDays() <= empire.countdown()) {
                    JourneyLog nextJourney = nextJourney(
                        journey,
                        journeyLog,
                        nextPlanet,
                        daysToNextPlanet
                    );
                    // Reaching the destination planet
                    addSuccessfulJourney(successfulJourneys, nextPlanet, nextJourney);
                }
            }

            refuel(journey, journeyLog);
        }
        log.info("Found {} successful journeys", successfulJourneys.size());
        return successfulJourneys;
    }

    private JourneyLog nextJourney(
        Queue<JourneyLog> journey,
        JourneyLog journeyLog,
        String nextPlanet,
        int daysToNextPlanet
    ) {
        log.trace("Advancing at {}, empire countdown={}", nextPlanet, empire.countdown());
        List<String> route = Stream.concat(
            journeyLog.getRoute().stream(),
            Stream.of(journeyLog.getCurrentPlanet())
        ).toList();
        JourneyLog nextJourney = new JourneyLog(
            nextPlanet,
            journeyLog.getTravelDays() + daysToNextPlanet,
            journeyLog.getAutonomyLeft() - daysToNextPlanet,
            route);
        journey.add(nextJourney);
        return nextJourney;
    }

    private void addSuccessfulJourney(
        List<JourneyLog> successfulJourneys,
        String nextPlanet,
        JourneyLog nextJourney
    ) {
        if (nextPlanet.equals(journeyContext.getFalconConfig().arrival())) {
            log.debug("Found successful journey: {}", nextJourney);
            successfulJourneys.add(nextJourney);
        }
    }

    private void refuel(Queue<JourneyLog> journey, JourneyLog journeyLog) {
        if (journeyLog.getTravelDays() + 1 <= empire.countdown()) {
            log.trace("Refueling at {}, empire countdown={}",
                journeyLog.getCurrentPlanet(),
                empire.countdown()
            );
            List<String> route = Stream.concat(
                journeyLog.getRoute().stream(),
                Stream.of(journeyLog.getCurrentPlanet())
            ).toList();
            journey.add(new JourneyLog(
                journeyLog.getCurrentPlanet(),
                journeyLog.getTravelDays() + 1,
                journeyContext.getFalconConfig().autonomy(),
                route
            ));
        }
    }

    private int numberOfHuntersOnRoute(List<String> route) {
        int numberOfHunters = 0;
        int days = 0;
        for (int i = 0; i < route.size() - 1; i++) {
            String planet = route.get(i);
            String destination = route.get(i + 1);
            days += planet.equals(destination) ?
                1 :
                journeyContext.getGalaxy().edgeValueOrDefault(planet, destination, 0);
            if (days == 0) {
                throw new IllegalStateException("Days distance between planets cannot be 0");
            }
            if (bountyHunterPresence.containsEntry(destination, days)) {
                numberOfHunters++;
            }
        }
        return numberOfHunters;
    }

    private int calculateOdds(int bountyHuntersEncountered) {
        double probability = 0;
        for (int i = 0; i < bountyHuntersEncountered; i++) {
            probability += Math.pow(9, i) / Math.pow(10, (i + 1));
        }
        return (int) ((1 - probability) * 100);
    }
}
