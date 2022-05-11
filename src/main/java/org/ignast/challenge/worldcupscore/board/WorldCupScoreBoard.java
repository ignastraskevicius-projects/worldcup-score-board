package org.ignast.challenge.worldcupscore.board;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WorldCupScoreBoard {

    private PairScore pairScore;

    public void startGame(final Home homeTeam, final Away awayTeam) {
        if (pairScore == null) {
            pairScore = new PairScore(homeTeam, 0, awayTeam, 0);
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
        if (Objects.isNull(pairScore)) {
            throw new IllegalStateException(
                String.format(
                    "%s-%s game is not in progress and cannot be finished",
                    homeTeam.name(),
                    awayTeam.name()
                )
            );
        } else {
            pairScore = null;
        }
    }

    public List<PairScore> getSummary() {
        if (pairScore != null) {
            return List.of(pairScore);
        } else {
            return new ArrayList<>();
        }
    }

    public void updateScore(final PairScore pairScore) {
        if (
            Objects.equals(pairScore.homeTeam(), this.pairScore.homeTeam()) &&
            Objects.equals(pairScore.awayTeam(), this.pairScore.awayTeam())
        ) {
            this.pairScore = pairScore;
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
