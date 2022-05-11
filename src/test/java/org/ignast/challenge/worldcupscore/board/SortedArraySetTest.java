package org.ignast.challenge.worldcupscore.board;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class SortedArraySetTest {

    private static final Comparator<String> BY_SECOND_LETTER = (a, b) ->
        Comparator.<String, Character>comparing(s -> s.charAt(1)).compare(a, b);

    private final SortedArraySet<String> set = new SortedArraySet<>(BY_SECOND_LETTER);

    @Test
    public void shouldInsertElement() {
        assertThat(set.add("aa")).isTrue();
        assertThat(set.stream().collect(Collectors.joining())).isEqualTo("aa");
    }

    @Test
    public void shouldNotInsertDuplicates() {
        set.add("aa");
        assertThat(set.add("aa")).isFalse();
        assertThat(set.stream().collect(Collectors.joining())).isEqualTo("aa");
    }

    @Test
    public void shouldNotRemoveNonexistentElement() {
        assertThat(set.remove("aa")).isFalse();
        assertThat(set.stream().collect(Collectors.joining())).isEqualTo("");
    }

    @Test
    public void shouldRemoveElement() {
        set.add("aa");
        assertThat(set.remove("aa")).isTrue();
        assertThat(set.stream().collect(Collectors.joining())).isEqualTo("");
    }

    @Test
    public void shouldAcceptEqualElementsAccordingToComparator() {
        assertThat(set.add("ba")).isTrue();
        assertThat(set.add("ca")).isTrue();
        assertThat(set.stream().collect(Collectors.toSet())).isEqualTo(Set.of("ba", "ca"));
    }

    @Test
    public void shouldAcceptEqualElementsAccordingToComparatorEvenThoughItGoesAgainstNaturalOrdering() {
        assertThat(set.add("cb")).isTrue();
        assertThat(set.add("bc")).isTrue();
        assertThat(set.stream().collect(Collectors.joining(","))).isEqualTo("cb,bc");
    }

    @Test
    public void shouldAcceptEqualElementsAccordingToComparatorEvenThoughItGoesAgainstNaturalOrderingAndInsertionOrder() {
        assertThat(set.add("bc")).isTrue();
        assertThat(set.add("cb")).isTrue();
        assertThat(set.stream().collect(Collectors.joining(","))).isEqualTo("cb,bc");
    }
}
