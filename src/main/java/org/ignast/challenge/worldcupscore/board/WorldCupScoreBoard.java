package org.ignast.challenge.worldcupscore.board;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
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

    public int getScoreForTeam(String teamName) {
        val homeScore = scanGamesForTeamScore(teamName, Participants::homeTeam);
        val awayScore = scanGamesForTeamScore(teamName, Participants::awayTeam);
        return homeScore.or(() -> awayScore).orElseThrow(() -> teamIsOfflineError(teamName));
    }

    private Optional<Integer> scanGamesForTeamScore(String teamName, Function<Participants, Team> teamChoice) {
        return gamesInProgress
            .stream()
            .filter(g -> byTeamName(teamName, teamChoice, g))
            .map(g -> toTeamScore(teamChoice, g))
            .findFirst();
    }

    private boolean byTeamName(String teamName, Function<Participants, Team> teamChoice, Game g) {
        return teamChoice.apply(g.getParticipants()).name().equals(teamName);
    }

    private int toTeamScore(Function<Participants, Team> teamChoice, Game g) {
        if (teamChoice.apply(g.getParticipants()) instanceof HomeTeam) {
            return g.getScorePair().home();
        } else {
            return g.getScorePair().away();
        }
    }

    private IllegalArgumentException teamIsOfflineError(String teamName) {
        return new IllegalArgumentException(String.format("Team %s is offline", teamName));
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
