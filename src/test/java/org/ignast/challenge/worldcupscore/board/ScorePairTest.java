package org.ignast.challenge.worldcupscore.board;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ScorePairTest {

    @Test
    public void shouldCalculateTotalScore() {
        assertThat(new ScorePair(3, 4).totalScore()).isEqualTo(7);
    }
}
