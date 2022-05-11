package org.ignast.challenge.worldcupscore.board;

import lombok.NonNull;

public record Participants(@NonNull HomeTeam homeTeam, @NonNull AwayTeam awayTeam) {}
