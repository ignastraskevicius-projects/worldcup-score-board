package org.ignast.challenge.worldcupscore.board;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.val;

public class WorldCupScoreBoard {

    private Game game;

    public void startGame(final Home homeTeam, final Away awayTeam) {
        if (game == null) {
            game = new Game(new Participants(homeTeam, awayTeam), 0, 0);
        } else {
            throw new IllegalArgumentException(
                String.format(
                    "%s-%s game has already in progress and cannot be started",
                    homeTeam.name(),
                    awayTeam.name()
                )
            );
        }
    }

    public void finishGame(final Home homeTeam, final Away awayTeam) {
        if (Objects.isNull(game)) {
            throw new IllegalStateException(
                String.format(
                    "%s-%s game is not in progress and cannot be finished",
                    homeTeam.name(),
                    awayTeam.name()
                )
            );
        } else {
            game = null;
        }
    }

    public List<PairScore> getSummary() {
        if (game != null) {
            return List.of(PairScore.fromGame(game));
        } else {
            return new ArrayList<>();
        }
    }

    public void updateScore(final PairScore pairScore) {
        val gameToUpdate = pairScore.toGame();
        if (game.equals(gameToUpdate)) {
            this.game = gameToUpdate;
        } else {
            throw new IllegalArgumentException(
                String.format(
                    "%s-%s game is not in progress and cannot have its score updated",
                    pairScore.homeTeam().name(),
                    pairScore.awayTeam().name()
                )
            );
        }
    }
}
