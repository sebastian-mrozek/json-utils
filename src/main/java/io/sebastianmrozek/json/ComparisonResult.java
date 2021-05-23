package io.sebastianmrozek.json;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ComparisonResult {
    private final boolean applicable;
    private final List<String> errors;

    public static final ComparisonResult NO_ERRORS = new ComparisonResult(true, Collections.emptyList());
    public static final ComparisonResult NOT_APPLICABLE = new ComparisonResult(false, Collections.emptyList());

    public static ComparisonResult error(String error) {
        return new ComparisonResult(true, Collections.singletonList(error));
    }

    public static ComparisonResult errors(List<String> errors) {
        return new ComparisonResult(true, errors);
    }

    private ComparisonResult(boolean applicable, List<String> errors) {
        this.applicable = applicable;
        this.errors = new ArrayList<>(errors);
    }

    public boolean isApplicable() {
        return applicable;
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public List<String> getErrors() {
        return errors;
    }
}
