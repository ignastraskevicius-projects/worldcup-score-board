package org.ignast.challenge.worldcupscore.board;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.val;

public class WorldCupScoreBoard {

    private static final Comparator<Game> BY_TOTAL_SCORE_DESCENDING = Comparator
        .<Game, Integer>comparing(g -> g.getHomeScore() + g.getAwayScore())
        .reversed();
    private ArrayList<Game> gamesInProgress = new ArrayList<>();

    private final Game.Factory games = new Game.Factory();

    public void startGame(final Home homeTeam, final Away awayTeam) {
        val gameToBeAdded = games.create(homeTeam, awayTeam);
        if (!gamesInProgress.contains(gameToBeAdded)) {
            gamesInProgress.add(getIndexPreservingOrder(gameToBeAdded), gameToBeAdded);
        } else {
            throw new IllegalArgumentException(
                String.format(
                    "%s-%s game has already in progress and cannot be started",
                    homeTeam.name(),
                    awayTeam.name()
                )
            );
        }
    }

    private int getIndexPreservingOrder(Game gameToBeAdded) {
        val index = Collections.binarySearch(gamesInProgress, gameToBeAdded, BY_TOTAL_SCORE_DESCENDING);
        return index >= 0 ? index : -(index) - 1;
    }

    public void finishGame(final Home homeTeam, final Away awayTeam) {
        if (!gamesInProgress.contains(games.create(homeTeam, awayTeam))) {
            throw new IllegalStateException(
                String.format(
                    "%s-%s game is not in progress and cannot be finished",
                    homeTeam.name(),
                    awayTeam.name()
                )
            );
        } else {
            gamesInProgress.remove(games.create(homeTeam, awayTeam));
        }
    }

    public List<PairScore> getSummary() {
        return gamesInProgress.stream().map(PairScore::fromGame).collect(Collectors.toList());
    }

    public void updateScore(final PairScore pairScore) {
        val gameToUpdate = pairScore.toGame();

        if (gamesInProgress.contains(gameToUpdate)) {
            gamesInProgress.remove(gameToUpdate);
            gamesInProgress.add(getIndexPreservingOrder(gameToUpdate), gameToUpdate);
        } else {
            throw new IllegalArgumentException(
                String.format(
                    "%s-%s game is not in progress and cannot have its score updated",
                    pairScore.homeTeam().name(),
                    pairScore.awayTeam().name()
                )
            );
        }
    }
}
