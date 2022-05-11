package org.ignast.challenge.worldcupscore.board;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

import lombok.val;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class GameTest {

    @Test
    public void gameShouldNotBeCreatedWithNulls() {
        assertThatNullPointerException().isThrownBy(() -> new Game(null, new ScorePair(0, 1), 0));
        assertThatNullPointerException()
            .isThrownBy(() -> new Game(new Participants(new Home("Canada"), new Away("Spain")), null, 0));
        new Game(new Participants(new Home("Canada"), new Away("Spain")), new ScorePair(1, 2), 0);
    }

    @Test
    public void gameShouldBeConsideredTheSameIfParticipantsAreTheSame() {
        EqualsVerifier.forClass(Game.class).withIgnoredFields("scorePair", "creationOrder").verify();

        val canadaSpain1 = new Participants(new Home("Canada"), new Away("Spain"));
        val canadaSpain2 = new Participants(new Home("Canada"), new Away("Spain"));
        val italyGermany = new Participants(new Home("Italy"), new Away("Germany"));

        val canadaSpainGame1 = new Game(canadaSpain1, new ScorePair(1, 2), 0);
        val canadaSpainGame2 = new Game(canadaSpain2, new ScorePair(2, 0), 1);
        val italyGermanyGame = new Game(italyGermany, new ScorePair(2, 0), 2);

        assertThat(canadaSpainGame1).isEqualTo(canadaSpainGame2);
        assertThat(canadaSpainGame1).isNotEqualTo(italyGermanyGame);
    }

    @Test
    public void gameShouldPreserveItsData() {
        val game = new Game(new Participants(new Home("Canada"), new Away("Spain")), new ScorePair(1, 2), 3);

        assertThat(game.getParticipants()).isEqualTo(new Participants(new Home("Canada"), new Away("Spain")));
        assertThat(game.getScorePair().home()).isEqualTo(1);
        assertThat(game.getScorePair().away()).isEqualTo(2);
        assertThat(game.getCreationOrder()).isEqualTo(3);
    }

    @Test
    public void gameShouldUpdateScore() {
        val game = new Game(new Participants(new Home("Canada"), new Away("Spain")), new ScorePair(1, 2), 3);

        val updatedGame = game.updateScore(new ScorePair(2, 4));

        assertThat(updatedGame.getParticipants())
            .isEqualTo(new Participants(new Home("Canada"), new Away("Spain")));
        assertThat(updatedGame.getScorePair().home()).isEqualTo(2);
        assertThat(updatedGame.getScorePair().away()).isEqualTo(4);
        assertThat(updatedGame.getCreationOrder()).isEqualTo(3);
    }

    @Test
    public void factoryShouldCreateNewGame() {
        assertThat(new Game.Factory().create(new Home("Canada"), new Away("Spain")))
            .isEqualTo(
                new Game(new Participants(new Home("Canada"), new Away("Spain")), new ScorePair(0, 0), 0)
            );
    }

    @Test
    public void factoryShouldGenerateCreationOrder() {
        val gameFactory = new Game.Factory();
        assertThat(gameFactory.create(new Home("Canada"), new Away("Spain")).getCreationOrder()).isEqualTo(0);
        assertThat(gameFactory.create(new Home("Canada"), new Away("Spain")).getCreationOrder()).isEqualTo(1);
        assertThat(gameFactory.create(new Home("Canada"), new Away("Spain")).getCreationOrder()).isEqualTo(2);
    }
}
