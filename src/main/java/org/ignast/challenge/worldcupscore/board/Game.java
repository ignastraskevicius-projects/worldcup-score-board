package org.ignast.challenge.worldcupscore.board;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter(AccessLevel.PROTECTED)
@RequiredArgsConstructor
final class Game {

    @NonNull
    @EqualsAndHashCode.Include
    private final Participants participants;

    private final int homeScore;
    private final int awayScore;

    private final int creationOrder;

    static class Factory {

        private int creationOrder = 0;

        public Game create(final Home homeTeam, final Away awayTeam) {
            return new Game(new Participants(homeTeam, awayTeam), 0, 0, creationOrder++);
        }
    }
}
