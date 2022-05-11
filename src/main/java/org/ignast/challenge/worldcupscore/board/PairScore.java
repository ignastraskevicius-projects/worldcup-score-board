package org.ignast.challenge.worldcupscore.board;

import lombok.NonNull;
import lombok.val;

public record PairScore(@NonNull Home homeTeam, int homeScore, @NonNull Away awayTeam, int awayScore) {
    static PairScore fromGame(final Game game) {
        val participants = game.getParticipants();
        return new PairScore(
            participants.homeTeam(),
            game.getHomeScore(),
            participants.awayTeam(),
            game.getAwayScore()
        );
    }

    Game toGame() {
        return new Game(new Participants(homeTeam, awayTeam), homeScore, awayScore, 0);
    }
}
