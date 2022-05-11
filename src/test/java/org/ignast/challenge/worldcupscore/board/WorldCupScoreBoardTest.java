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
        worldCupScoreBoard.startGame(new Home("Mexico"), new Away("Canada"));

        val summary = worldCupScoreBoard.getSummary();

        assertThat(summary).hasSize(1);
        assertThat(summary.get(0)).isEqualTo(new PairScore(new Home("Mexico"), 0, new Away("Canada"), 0));
    }

    @Test
    public void summaryShouldBeProvidedInDescendingTotalScoreOrder() {
        worldCupScoreBoard.startGame(new Home("Mexico"), new Away("Canada"));
        worldCupScoreBoard.updateScore(new PairScore(new Home("Mexico"), 2, new Away("Canada"), 1));
        worldCupScoreBoard.startGame(new Home("Spain"), new Away("Italy"));
        worldCupScoreBoard.updateScore(new PairScore(new Home("Spain"), 1, new Away("Italy"), 3));
        worldCupScoreBoard.startGame(new Home("Germany"), new Away("Lithuania"));
        worldCupScoreBoard.updateScore(new PairScore(new Home("Germany"), 4, new Away("Lithuania"), 2));

        val summary = worldCupScoreBoard.getSummary();

        assertThat(summary).hasSize(3);
        assertThat(summary.get(0)).isEqualTo(new PairScore(new Home("Germany"), 4, new Away("Lithuania"), 2));
        assertThat(summary.get(1)).isEqualTo(new PairScore(new Home("Spain"), 1, new Away("Italy"), 3));
        assertThat(summary.get(2)).isEqualTo(new PairScore(new Home("Mexico"), 2, new Away("Canada"), 1));
    }

    @Test
    public void shouldNotStartDuplicateGame() {
        worldCupScoreBoard.startGame(new Home("Mexico"), new Away("Canada"));

        assertThatIllegalArgumentException()
            .isThrownBy(() -> worldCupScoreBoard.startGame(new Home("Mexico"), new Away("Canada")))
            .withMessage("Mexico-Canada game has already in progress and cannot be started");
    }

    @Test
    public void finishedGameShouldNoLongerBeMentionedInTheSummary() {
        worldCupScoreBoard.startGame(new Home("Mexico"), new Away("Canada"));
        worldCupScoreBoard.finishGame(new Home("Mexico"), new Away("Canada"));

        val summary = worldCupScoreBoard.getSummary();

        assertThat(summary).isEmpty();
    }

    @Test
    public void shouldContinueSummarisingGamesInProgressAfterSomeOfThemFinish() {
        worldCupScoreBoard.startGame(new Home("Mexico"), new Away("Canada"));
        worldCupScoreBoard.startGame(new Home("Spain"), new Away("Italy"));
        worldCupScoreBoard.finishGame(new Home("Mexico"), new Away("Canada"));

        val summary = worldCupScoreBoard.getSummary();

        assertThat(summary).hasSize(1);
        assertThat(summary.get(0)).isEqualTo(new PairScore(new Home("Spain"), 0, new Away("Italy"), 0));
    }

    @Test
    public void startedGameShouldBeAbleToGetItsScoreChanged() {
        worldCupScoreBoard.startGame(new Home("Mexico"), new Away("Canada"));
        worldCupScoreBoard.updateScore(new PairScore(new Home("Mexico"), 2, new Away("Canada"), 1));

        val summary = worldCupScoreBoard.getSummary();

        assertThat(summary).hasSize(1);
        assertThat(summary.get(0)).isEqualTo(new PairScore(new Home("Mexico"), 2, new Away("Canada"), 1));
    }

    @Test
    public void shouldNotUpdateScoreForNonexistentGame() {
        worldCupScoreBoard.startGame(new Home("Mexico"), new Away("Canada"));

        assertThatIllegalArgumentException()
            .isThrownBy(() ->
                worldCupScoreBoard.updateScore(new PairScore(new Home("Italy"), 2, new Away("Spain"), 1))
            )
            .withMessage("Italy-Spain game is not in progress and cannot have its score updated");
    }

    @Test
    public void shouldNotFinishedGameWhichIsNotInProgress() {
        assertThatExceptionOfType(IllegalStateException.class)
            .isThrownBy(() -> worldCupScoreBoard.finishGame(new Home("Mexico"), new Away("Canada")))
            .withMessage("Mexico-Canada game is not in progress and cannot be finished");
    }
}
