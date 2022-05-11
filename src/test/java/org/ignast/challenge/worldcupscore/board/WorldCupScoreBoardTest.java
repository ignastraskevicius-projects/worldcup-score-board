package org.ignast.challenge.worldcupscore.board;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class WorldCupScoreBoardTest {

    private final WorldCupScoreBoard worldCupScoreBoard = new WorldCupScoreBoard();

    @Test
    public void shouldStartAndFinishGame() {
        worldCupScoreBoard.startGame(new Home("Mexico"), new Away("Canada"));
        worldCupScoreBoard.finishGame(new Home("Mexico"), new Away("Canada"));
    }
}