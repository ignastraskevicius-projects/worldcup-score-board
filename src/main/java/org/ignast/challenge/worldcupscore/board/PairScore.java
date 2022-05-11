package org.ignast.challenge.worldcupscore.board;

import lombok.NonNull;

public record PairScore(@NonNull Home homeTeam, int homeScore, @NonNull Away awayTeam, int awayScore) {
    Game toGame() {
        return new Game(new Participants(homeTeam, awayTeam), homeScore, awayScore);
    }
}
