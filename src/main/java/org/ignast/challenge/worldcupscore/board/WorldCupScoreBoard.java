package org.ignast.challenge.worldcupscore.board;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.val;

public final class WorldCupScoreBoard {

    private static final Comparator<Game> BY_TOTAL_SCORE_DESCENDING_AND_LATEST_CREATED_FIRST = Comparator
        .<Game, Integer>comparing(g -> g.getScorePair().totalScore())
        .thenComparing(Game::getCreationOrder)
        .reversed();

    private final SortedArraySet<Game> gamesInProgress = new SortedArraySet<>(
        BY_TOTAL_SCORE_DESCENDING_AND_LATEST_CREATED_FIRST
    );

    private final Game.Factory games = new Game.Factory();

    public void startGame(final HomeTeam homeTeam, final AwayTeam awayTeam) {
        val gameToBeAdded = games.create(homeTeam, awayTeam);
        if (!gamesInProgress.add(gameToBeAdded)) {
            throw gameIsAlreadyStartedError(homeTeam, awayTeam);
        }
    }

    public void finishGame(final HomeTeam homeTeam, final AwayTeam awayTeam) {
        if (!gamesInProgress.remove(games.create(homeTeam, awayTeam))) {
            throw gameNotFoundError(homeTeam, awayTeam);
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
                this::updateGameInProgress,
                () -> {
                    throw gameNotFoundError(pairScore.homeTeam(), pairScore.awayTeam());
                }
            );
    }

    private void updateGameInProgress(Game updatedGame) {
        gamesInProgress.remove(updatedGame);
        gamesInProgress.add(updatedGame);
    }

    private IllegalArgumentException gameIsAlreadyStartedError(HomeTeam homeTeam, AwayTeam awayTeam) {
        return new IllegalArgumentException(
            String.format("%s-%s game has already started", homeTeam.name(), awayTeam.name())
        );
    }

    private IllegalArgumentException gameNotFoundError(HomeTeam homeTeam, AwayTeam awayTeam) {
        return new IllegalArgumentException(
            String.format("%s-%s game was not found", homeTeam.name(), awayTeam.name())
        );
    }
}
