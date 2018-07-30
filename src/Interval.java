/**
 * Created by helili on 2018/7/30.
 */
public class Interval implements Comparable<Interval> {

    int start;
    int end;

    public Interval() {

    }

    public Interval(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    @Override
    public int compareTo(Interval o) {
        if (start < o.start) return -1;
        else if (start > o.start) return 1;
        return 0;
    }
}
