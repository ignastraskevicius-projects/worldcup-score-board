package org.ignast.challenge.worldcupscore.board;

import lombok.NonNull;

public record Participants(@NonNull Home homeTeam, @NonNull Away awayTeam) {}
