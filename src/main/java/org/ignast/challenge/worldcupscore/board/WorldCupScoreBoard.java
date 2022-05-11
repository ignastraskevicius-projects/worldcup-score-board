package org.ignast.challenge.worldcupscore.board;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.val;

public final class WorldCupScoreBoard {

    private static final Comparator<Game> BY_TOTAL_SCORE_DESCENDING_AND_LATEST_CREATED_FIRST = Comparator
        .<Game, Integer>comparing(g -> g.getScorePair().home() + g.getScorePair().away())
        .thenComparing(Game::getCreationOrder)
        .reversed();

    private final SortedArraySet<Game> gamesInProgress = new SortedArraySet<>(
        BY_TOTAL_SCORE_DESCENDING_AND_LATEST_CREATED_FIRST
    );

    private final Game.Factory games = new Game.Factory();

    public void startGame(final HomeTeam homeTeam, final AwayTeam awayTeam) {
        val gameToBeAdded = games.create(homeTeam, awayTeam);
        if (!gamesInProgress.add(gameToBeAdded)) {
            throw new IllegalArgumentException(
                String.format(
                    "%s-%s game has already in progress and cannot be started",
                    homeTeam.name(),
                    awayTeam.name()
                )
            );
        }
    }

    public void finishGame(final HomeTeam homeTeam, final AwayTeam awayTeam) {
        if (!gamesInProgress.remove(games.create(homeTeam, awayTeam))) {
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
        return gamesInProgress.stream().map(PairScore::fromGame).collect(Collectors.toUnmodifiableList());
    }

    public void updateScore(final PairScore pairScore) {
        val gameToUpdate = pairScore.toGame();
        val newScore = gameToUpdate.getScorePair();

        gamesInProgress
            .stream()
            .filter(g -> g.equals(gameToUpdate))
            .map(g -> g.updateScore(newScore))
            .findFirst()
            .ifPresentOrElse(
                updatedGame -> {
                    gamesInProgress.remove(updatedGame);
                    gamesInProgress.add(updatedGame);
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
}
