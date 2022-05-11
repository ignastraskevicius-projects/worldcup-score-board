package org.ignast.challenge.worldcupscore.board;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

import lombok.val;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class GameTest {

    @Test
    public void gameShouldNotBeCreatedWithNullParticipants() {
        assertThatNullPointerException().isThrownBy(() -> new Game(null, 1, 2));
        new Game(new Participants(new Home("Canada"), new Away("Spain")), 1, 2);
    }

    @Test
    public void gameShouldBeConsideredTheSameIfParticipantsAreTheSame() {
        EqualsVerifier.forClass(Game.class).withIgnoredFields("homeScore", "awayScore").verify();

        val canadaSpain1 = new Participants(new Home("Canada"), new Away("Spain"));
        val canadaSpain2 = new Participants(new Home("Canada"), new Away("Spain"));
        val italyGermany = new Participants(new Home("Italy"), new Away("Germany"));

        val canadaSpainGame1 = new Game(canadaSpain1, 1, 2);
        val canadaSpainGame2 = new Game(canadaSpain2, 2, 0);
        val italyGermanyGame = new Game(italyGermany, 2, 0);

        assertThat(canadaSpainGame1).isEqualTo(canadaSpainGame2);
        assertThat(canadaSpainGame1).isNotEqualTo(italyGermanyGame);
    }

    @Test
    public void gameShouldPreserveItsData() {
        val game = new Game(new Participants(new Home("Canada"), new Away("Spain")), 1, 2);

        assertThat(game.getParticipants()).isEqualTo(new Participants(new Home("Canada"), new Away("Spain")));
        assertThat(game.getHomeScore()).isEqualTo(1);
        assertThat(game.getAwayScore()).isEqualTo(2);
    }

    @Test
    public void shouldCreateNewGame() {
        assertThat(Game.create(new Home("Canada"), new Away("Spain")))
            .isEqualTo(new Game(new Participants(new Home("Canada"), new Away("Spain")), 0, 0));
    }
}
