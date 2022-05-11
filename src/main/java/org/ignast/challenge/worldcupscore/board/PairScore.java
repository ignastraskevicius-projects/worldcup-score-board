package org.ignast.challenge.worldcupscore.board;

import lombok.NonNull;
import lombok.val;

public record PairScore(
    @NonNull HomeTeam homeTeam,
    int homeScore,
    @NonNull AwayTeam awayTeam,
    int awayScore
) {
    static PairScore fromGame(final Game game) {
        val participants = game.getParticipants();
        return new PairScore(
            participants.homeTeam(),
            game.getScorePair().home(),
            participants.awayTeam(),
            game.getScorePair().away()
        );
    }

    Game toGame() {
        return new Game(new Participants(homeTeam, awayTeam), new ScorePair(homeScore, awayScore), 0);
    }
}
