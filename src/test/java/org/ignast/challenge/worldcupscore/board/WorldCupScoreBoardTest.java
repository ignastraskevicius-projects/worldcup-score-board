package org.ignast.challenge.worldcupscore.board;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import lombok.val;
import org.junit.jupiter.api.Test;

class WorldCupScoreBoardTest {

    private final WorldCupScoreBoard worldCupScoreBoard = new WorldCupScoreBoard();

    @Test
    public void initiallySummaryShouldBeEmpty() {
        assertThat(worldCupScoreBoard.getSummary()).isEmpty();
    }

    @Test
    public void startedGameShouldBeRepresentedInSummary() {
        worldCupScoreBoard.startGame(new HomeTeam("Mexico"), new AwayTeam("Canada"));

        val summary = worldCupScoreBoard.getSummary();

        assertThat(summary).hasSize(1);
        assertThat(summary.get(0))
            .isEqualTo(new PairScore(new HomeTeam("Mexico"), 0, new AwayTeam("Canada"), 0));
    }

    @Test
    public void summaryShouldBeProvidedInDescendingTotalScoreOrder() {
        worldCupScoreBoard.startGame(new HomeTeam("Mexico"), new AwayTeam("Canada"));
        worldCupScoreBoard.updateScore(new PairScore(new HomeTeam("Mexico"), 2, new AwayTeam("Canada"), 1));
        worldCupScoreBoard.startGame(new HomeTeam("Germany"), new AwayTeam("Lithuania"));
        worldCupScoreBoard.updateScore(
            new PairScore(new HomeTeam("Germany"), 4, new AwayTeam("Lithuania"), 2)
        );
        worldCupScoreBoard.startGame(new HomeTeam("Spain"), new AwayTeam("Italy"));
        worldCupScoreBoard.updateScore(new PairScore(new HomeTeam("Spain"), 1, new AwayTeam("Italy"), 3));

        val summary = worldCupScoreBoard.getSummary();

        assertThat(summary).hasSize(3);
        assertThat(summary.get(0))
            .isEqualTo(new PairScore(new HomeTeam("Germany"), 4, new AwayTeam("Lithuania"), 2));
        assertThat(summary.get(1))
            .isEqualTo(new PairScore(new HomeTeam("Spain"), 1, new AwayTeam("Italy"), 3));
        assertThat(summary.get(2))
            .isEqualTo(new PairScore(new HomeTeam("Mexico"), 2, new AwayTeam("Canada"), 1));
    }

    @Test
    public void summaryShouldBeProvidedInMostRecentlyAddedOrderIfTotalScoreIsTheSame() {
        worldCupScoreBoard.startGame(new HomeTeam("Mexico"), new AwayTeam("Canada"));
        worldCupScoreBoard.updateScore(new PairScore(new HomeTeam("Mexico"), 2, new AwayTeam("Canada"), 1));
        worldCupScoreBoard.startGame(new HomeTeam("Spain"), new AwayTeam("Italy"));
        worldCupScoreBoard.updateScore(new PairScore(new HomeTeam("Spain"), 1, new AwayTeam("Italy"), 2));
        worldCupScoreBoard.startGame(new HomeTeam("Germany"), new AwayTeam("Lithuania"));
        worldCupScoreBoard.updateScore(
            new PairScore(new HomeTeam("Germany"), 3, new AwayTeam("Lithuania"), 0)
        );

        val summary = worldCupScoreBoard.getSummary();

        assertThat(summary).hasSize(3);
        assertThat(summary.get(0))
            .isEqualTo(new PairScore(new HomeTeam("Germany"), 3, new AwayTeam("Lithuania"), 0));
        assertThat(summary.get(1))
            .isEqualTo(new PairScore(new HomeTeam("Spain"), 1, new AwayTeam("Italy"), 2));
        assertThat(summary.get(2))
            .isEqualTo(new PairScore(new HomeTeam("Mexico"), 2, new AwayTeam("Canada"), 1));
    }

    @Test
    public void shouldNotStartDuplicateGame() {
        worldCupScoreBoard.startGame(new HomeTeam("Mexico"), new AwayTeam("Canada"));

        assertThatIllegalArgumentException()
            .isThrownBy(() -> worldCupScoreBoard.startGame(new HomeTeam("Mexico"), new AwayTeam("Canada")))
            .withMessage("Mexico-Canada game has already in progress and cannot be started");
    }

    @Test
    public void summaryShouldBeImmutable() {
        assertThatExceptionOfType(UnsupportedOperationException.class)
            .isThrownBy(() ->
                worldCupScoreBoard
                    .getSummary()
                    .add(new PairScore(new HomeTeam("Canada"), 0, new AwayTeam("Spain"), 1))
            );
    }

    @Test
    public void finishedGameShouldNoLongerBeMentionedInTheSummary() {
        worldCupScoreBoard.startGame(new HomeTeam("Mexico"), new AwayTeam("Canada"));
        worldCupScoreBoard.finishGame(new HomeTeam("Mexico"), new AwayTeam("Canada"));

        val summary = worldCupScoreBoard.getSummary();

        assertThat(summary).isEmpty();
    }

    @Test
    public void shouldContinueSummarisingGamesInProgressAfterSomeOfThemFinish() {
        worldCupScoreBoard.startGame(new HomeTeam("Mexico"), new AwayTeam("Canada"));
        worldCupScoreBoard.startGame(new HomeTeam("Spain"), new AwayTeam("Italy"));
        worldCupScoreBoard.finishGame(new HomeTeam("Mexico"), new AwayTeam("Canada"));

        val summary = worldCupScoreBoard.getSummary();

        assertThat(summary).hasSize(1);
        assertThat(summary.get(0))
            .isEqualTo(new PairScore(new HomeTeam("Spain"), 0, new AwayTeam("Italy"), 0));
    }

    @Test
    public void shouldNotAllowToFinishNotExistentGameWhenThereAreUnrelatedGamesInProgress() {
        worldCupScoreBoard.startGame(new HomeTeam("Spain"), new AwayTeam("Italy"));
        assertThatExceptionOfType(IllegalStateException.class)
            .isThrownBy(() -> worldCupScoreBoard.finishGame(new HomeTeam("Mexico"), new AwayTeam("Canada")))
            .withMessage("Mexico-Canada game is not in progress and cannot be finished");
    }

    @Test
    public void startedGameShouldBeAbleToGetItsScoreChanged() {
        worldCupScoreBoard.startGame(new HomeTeam("Mexico"), new AwayTeam("Canada"));
        worldCupScoreBoard.updateScore(new PairScore(new HomeTeam("Mexico"), 2, new AwayTeam("Canada"), 1));

        val summary = worldCupScoreBoard.getSummary();

        assertThat(summary).hasSize(1);
        assertThat(summary.get(0))
            .isEqualTo(new PairScore(new HomeTeam("Mexico"), 2, new AwayTeam("Canada"), 1));
    }

    @Test
    public void shouldNotUpdateScoreForNonexistentGame() {
        worldCupScoreBoard.startGame(new HomeTeam("Mexico"), new AwayTeam("Canada"));

        assertThatIllegalArgumentException()
            .isThrownBy(() ->
                worldCupScoreBoard.updateScore(
                    new PairScore(new HomeTeam("Italy"), 2, new AwayTeam("Spain"), 1)
                )
            )
            .withMessage("Italy-Spain game is not in progress and cannot have its score updated");
    }

    @Test
    public void shouldNotFinishedGameWhichIsNotInProgress() {
        assertThatExceptionOfType(IllegalStateException.class)
            .isThrownBy(() -> worldCupScoreBoard.finishGame(new HomeTeam("Mexico"), new AwayTeam("Canada")))
            .withMessage("Mexico-Canada game is not in progress and cannot be finished");
    }
}
