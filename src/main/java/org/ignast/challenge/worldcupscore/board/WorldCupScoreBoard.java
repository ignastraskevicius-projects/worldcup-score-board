package org.ignast.challenge.worldcupscore.board;

import java.util.ArrayList;
import java.util.List;

public class WorldCupScoreBoard {

    private PairScore pairScore;

    public void startGame(Home homeTeam, Away awayTeam) {
        pairScore = new PairScore(homeTeam, 0, awayTeam, 0);
    }

    public void finishGame(Home homeTeam, Away awayTeam) {
        pairScore = null;
    }

    public List<PairScore> getSummary() {
        if (pairScore != null) {
            return List.of(pairScore);
        } else {
            return new ArrayList<>();
        }
    }
}
