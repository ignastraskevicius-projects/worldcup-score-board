package org.ignast.challenge.worldcupscore.board;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;

import org.junit.jupiter.api.Test;

class AwayTeamTest {

    @Test
    public void shouldNotBeCreatedWithNullName() {
        assertThatNullPointerException().isThrownBy(() -> new AwayTeam(null));
        new AwayTeam("Canada");
    }
}
