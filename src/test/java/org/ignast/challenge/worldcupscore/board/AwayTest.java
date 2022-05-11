package org.ignast.challenge.worldcupscore.board;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;

import org.junit.jupiter.api.Test;

class AwayTest {

    @Test
    public void shouldNotBeCreatedWithNullName() {
        assertThatNullPointerException().isThrownBy(() -> new Away(null));
        new Away("Canada");
    }
}
