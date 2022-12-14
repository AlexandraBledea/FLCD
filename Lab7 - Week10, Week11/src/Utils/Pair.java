package Utils;

import java.util.Objects;

public class Pair<First,Second> {
    private final First first;
    private final Second second;

    public First getFirst() {
        return this.first;
    }

    public Second getSecond() {
        return this.second;
    }

    public Pair(First first, Second second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public String toString() {
        return "(" + this.first.toString() + ", " + this.second.toString() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return first.equals(pair.first) && second.equals(pair.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

}
