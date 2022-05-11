package org.ignast.challenge.worldcupscore.board;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;

import org.junit.jupiter.api.Test;

class ParticipantsTest {

    @Test
    public void shouldNotBeCreatedWithNulls() {
        assertThatNullPointerException().isThrownBy(() -> new Participants(null, new AwayTeam("Canada")));
        assertThatNullPointerException().isThrownBy(() -> new Participants(new HomeTeam("Canada"), null));
        new Participants(new HomeTeam("Canada"), new AwayTeam("Mexico"));
    }
}
