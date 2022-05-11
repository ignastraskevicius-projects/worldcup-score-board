package org.ignast.challenge.worldcupscore.board;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;

import org.junit.jupiter.api.Test;

class HomeTest {

    @Test
    public void shouldNotBeCreatedWithNullName() {
        assertThatNullPointerException().isThrownBy(() -> new Home(null));
        new Home("Canada");
    }
}
