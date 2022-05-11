package org.ignast.challenge.worldcupscore.board;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.val;

public class WorldCupScoreBoard {

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

    public void finishGame(final Home homeTeam, final Away awayTeam) {
        if (gamesInProgress.contains(games.create(homeTeam, awayTeam))) {
            gamesInProgress.remove(games.create(homeTeam, awayTeam));
        } else {
            throw new IllegalStateException(
                String.format(
                    "%s-%s game is not in progress and cannot be finished",
                    homeTeam.name(),
                    awayTeam.name()
                )
            );
        }
    }

    public List<PairScore> getSummary() {
        return gamesInProgress.stream().map(PairScore::fromGame).collect(Collectors.toList());
    }

    public void updateScore(final PairScore pairScore) {
        val gameWithNewScore = pairScore.toGame();

        gamesInProgress
            .stream()
            .filter(g -> g.equals(gameWithNewScore))
            .map(g -> g.updateScore(gameWithNewScore.getScorePair()))
            .findFirst()
            .ifPresentOrElse(
                updatedGame -> {
                    gamesInProgress.remove(updatedGame);
                    gamesInProgress.add(getIndexPreservingOrder(updatedGame), updatedGame);
                },
                () -> {
                    throw new IllegalArgumentException(
                        String.format(
                            "%s-%s game is not in progress and cannot have its score updated",
                            pairScore.homeTeam().name(),
                            pairScore.awayTeam().name()
                        )
                    );
                }
            );
    }

    private int getIndexPreservingOrder(Game gameToBeAdded) {
        val byTotalScoreDescendingAndCreationOrder = Comparator
            .<Game, Integer>comparing(g -> g.getScorePair().home() + g.getScorePair().away())
            .reversed()
            .thenComparing(Game::getCreationOrder);

        val index = Collections.binarySearch(
            gamesInProgress,
            gameToBeAdded,
            byTotalScoreDescendingAndCreationOrder
        );
        int fromBinarySearchNotFoundToIndex = -(index) - 1;
        return index >= 0 ? index : fromBinarySearchNotFoundToIndex;
    }
}
