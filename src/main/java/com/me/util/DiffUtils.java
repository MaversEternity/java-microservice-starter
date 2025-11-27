package com.me.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import org.jspecify.annotations.Nullable;

import lombok.experimental.UtilityClass;

@UtilityClass
public class DiffUtils {

    /**
     * Diff calculator between previous and new state.
     * As a result {@link DiffContainer} is created with three methods {@link DiffContainer#findToCreate()}, {@link DiffContainer#findToUpdate()}, {@link DiffContainer#findToDelete()}.
     *
     * @param oldRecords previous state
     * @param newRecords new state
     * @param oldId mapper to get unique id for old record
     * @param newId mapper to get unique id for new record
     * @return diff container
     * @param <O> Old type
     * @param <N> New Type
     */
    public static <O, N> DiffContainer<O, N> calcDiff(Collection<O> oldRecords,
                                                      Collection<N> newRecords,
                                                      Function<O, String> oldId, Function<N, String> newId) {

        validateUniqueness(oldRecords, oldId);
        validateUniqueness(newRecords, newId);

        List<Diff<O, N>> result = new ArrayList<>();

        List<N> newRecordsToProcess = new LinkedList<>(newRecords);

        for (O oldRecord : oldRecords) {
            boolean newRecordFound = false;
            Iterator<N> iterator = newRecordsToProcess.iterator();

            while (iterator.hasNext()) {
                N newRecord = iterator.next();

                String oldRecordIdentifier = oldId.apply(oldRecord);
                String newRecordIdentifier = newId.apply(newRecord);
                if (oldRecordIdentifier.equals(newRecordIdentifier)) {
                    result.add(new UpdateDiff<>(oldRecord, newRecord));
                    iterator.remove();
                    newRecordFound = true;
                    break;
                }
            }

            if (!newRecordFound) {
                result.add(new DeleteDiff<>(oldRecord));
            }
        }

        newRecordsToProcess.forEach(it -> result.add(new CreateDiff<>(it)));

        return new DiffContainer<>(result);
    }

    private <T> void validateUniqueness(Collection<T> records, Function<T, String> identifierField) {
        if (records.stream().map(identifierField).distinct().count() != records.size()) {
            List<String> codes = records.stream().map(identifierField).sorted().toList();
            throw new IllegalArgumentException("Not unique codes: [" + codes + "]");
        }
    }

    /**
     * Diff container is the result of the calculation
     *
     * @param diffList result diff records
     * @param <O> Old type
     * @param <N> New type
     */
    public record DiffContainer<O, N>(List<Diff<O, N>> diffList) {

        /**
         * @return Find records to create
         */
        public List<N> findToCreate() {
            return diffList.stream()
                .filter(CreateDiff.class::isInstance)
                .map(Diff::newValue)
                .toList();
        }

        /**
         * @return Find records to delete
         */
        public List<O> findToDelete() {
            return diffList.stream()
                .filter(DeleteDiff.class::isInstance)
                .map(Diff::oldValue)
                .toList();
        }

        /**
         * @return Find records to update
         */
        public List<UpdateDiff<O, N>> findToUpdate() {
            return diffList.stream()
                .filter(diff -> diff instanceof UpdateDiff<O, N>)
                .map(diff -> (UpdateDiff<O, N>) diff)
                .toList();
        }
    }

    public sealed interface Diff<O, N> permits CreateDiff, DeleteDiff, UpdateDiff {
        O oldValue();
        N newValue();
    }

    public record UpdateDiff<O, N>(O oldValue, N newValue) implements Diff<O, N> {

    }

    public record CreateDiff<O, N>(N newValue) implements Diff<O, N> {

        @Nullable
        @Override
        public O oldValue() {
            return null;
        }
    }

    public record DeleteDiff<O, N>(O oldValue) implements Diff<O, N> {

        @Nullable
        @Override
        public N newValue() {
            return null;
        }
    }

}
