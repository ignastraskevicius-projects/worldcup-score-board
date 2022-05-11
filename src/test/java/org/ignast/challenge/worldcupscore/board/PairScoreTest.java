package org.ignast.challenge.worldcupscore.board;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

import lombok.val;
import org.junit.jupiter.api.Test;

class PairScoreTest {

    @Test
    public void shouldNotCreateScoreForNullParticipants() {
        assertThatNullPointerException().isThrownBy(() -> new PairScore(null, 0, new Away("Mexico"), 1));
        assertThatNullPointerException().isThrownBy(() -> new PairScore(new Home("Canada"), 0, null, 1));
        new PairScore(new Home("Canada"), 0, new Away("Mexico"), 1);
    }

    @Test
    public void shouldReconstructGame() {
        val pairScore = new PairScore(new Home("Canada"), 0, new Away("Mexico"), 1);

        val game = pairScore.toGame();

        assertThat(game).isEqualTo(new Game(new Participants(new Home("Canada"), new Away("Mexico")), 0, 1));
    }

    @Test
    public void shouldExtractScoreFromGame() {
        val game = new Game(new Participants(new Home("Canada"), new Away("Mexico")), 0, 1);

        val pairScore = PairScore.fromGame(game);

        assertThat(pairScore).isEqualTo(new PairScore(new Home("Canada"), 0, new Away("Mexico"), 1));
    }
}
