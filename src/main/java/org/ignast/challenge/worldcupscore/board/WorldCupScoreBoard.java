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

    public void startGame(final Home homeTeam, final Away awayTeam) {
        val gameToBeAdded = new Game(new Participants(homeTeam, awayTeam), 0, 0);
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
        int index1 = -(index) - 1;
        return index1;
    }

    public void finishGame(final Home homeTeam, final Away awayTeam) {
        if (gamesInProgress.isEmpty()) {
            throw new IllegalStateException(
                String.format(
                    "%s-%s game is not in progress and cannot be finished",
                    homeTeam.name(),
                    awayTeam.name()
                )
            );
        } else {
            gamesInProgress = new ArrayList<>();
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
