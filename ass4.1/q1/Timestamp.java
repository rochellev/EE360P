package q1;

import java.io.Serializable;
import java.util.Date;

public class Timestamp implements Comparable, Serializable {
    private Date timestamp;
    private int id;

    Timestamp(int id) {
        this.id = id;
        this.timestamp = new Date();
    }

    Timestamp(int id, long timestamp) {
        this.id = id;
        this.timestamp = new Date(timestamp);
    }

    public int getId() {
        return this.id;
    }
    @Override
    public int compareTo(Object o) {
        return this.timestamp.compareTo(((Timestamp) o).timestamp);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Timestamp timestamp = (Timestamp) o;

        return id == timestamp.id;

    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "[ " + id + ", " + timestamp + " ]";
    }
}