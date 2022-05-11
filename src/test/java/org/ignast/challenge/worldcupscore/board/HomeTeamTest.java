package org.ignast.challenge.worldcupscore.board;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;

import org.junit.jupiter.api.Test;

class HomeTeamTest {

    @Test
    public void shouldNotBeCreatedWithNullName() {
        assertThatNullPointerException().isThrownBy(() -> new HomeTeam(null));
        new HomeTeam("Canada");
    }
}
