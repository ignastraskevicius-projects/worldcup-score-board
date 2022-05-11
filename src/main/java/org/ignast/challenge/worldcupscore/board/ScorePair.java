package org.ignast.challenge.worldcupscore.board;

public record ScorePair(int home, int away) {
    public int totalScore() {
        return home + away;
    }
}
