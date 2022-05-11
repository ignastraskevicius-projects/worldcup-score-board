package org.ignast.challenge.worldcupscore.board;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Stream;
import lombok.val;

class SortedArraySet<T> {

    private final Comparator<T> comparator;

    private final ArrayList<T> array = new ArrayList<>();

    SortedArraySet(final Comparator<T> comparator) {
        this.comparator = comparator;
    }

    boolean add(T e) {
        val isNewElement = !array.contains(e);
        if (isNewElement) {
            array.add(getIndexForInsertionPreservingOrder(e), e);
        }
        return isNewElement;
    }

    boolean remove(Object e) {
        return array.remove(e);
    }

    Stream<T> stream() {
        return array.stream();
    }

    private int getIndexForInsertionPreservingOrder(T e) {
        val index = Collections.binarySearch(array, e, comparator);
        int fromBinarySearchNotFoundToIndex = -(index) - 1;
        return index >= 0 ? index : fromBinarySearchNotFoundToIndex;
    }
}
