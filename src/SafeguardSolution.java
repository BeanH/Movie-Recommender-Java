import java.io.*;
import java.util.*;

/**
 * Created by helili on 2018/7/30.
 */
public class SafeguardSolution {
    public static void main(String[] args) throws IOException {

        BufferedReader reader = null;
        BufferedWriter output = null;
        try {
            for (int i = 1; i <= 10; i++) {
                reader = new BufferedReader(new FileReader("input/" + i + ".in"));
                output = new BufferedWriter(new FileWriter(new File("output/" + i + ".out")));
                String line = reader.readLine();
                List<Interval> intervals = new ArrayList<>();
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.trim().split(" ");
                    Interval interval = new Interval(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
                    intervals.add(interval);
                }
                Collections.sort(intervals);
                Long result = handle(intervals);
                System.out.println(result);
                output.write(result + "\n");
                output.flush();
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            reader.close();
            output.close();
        }


    }

    public static Long handle(List<Interval> intervals) {
        Long count = 0L;
        int start =intervals.get(0).start;
        int end = intervals.get(0).end;
        int n = intervals.size();
        int[] impacts = new int[n];
        for (int i = 0; i < n; i++) {
            impacts[i] = intervals.get(i).end - intervals.get(i).start;
        }
        for (int i = 1, j = 0; i < n; i++) {
            if (intervals.get(i).start < intervals.get(j).end) {
                if (intervals.get(j + 1).end < intervals.get(j).end) {
                    impacts[j + 1] = 0;
                    continue;
                }
                int overlapping = intervals.get(j).end - intervals.get(i).start;
                count += overlapping;
                impacts[i] = impacts[i] - overlapping;
                impacts[i - 1] = impacts[i - 1] - overlapping;
                j++;
            } else {
                while (intervals.get(i).start > intervals.get(j).end) {
                    j++;
                }
            }
        }
        int minimalImpact = impacts[0];
        count += impacts[0];
        for (int i = 1; i < n; ++i) {
            if (impacts[i] > 0) {
                count += impacts[i];
            }
            if (minimalImpact > impacts[i]) {
                minimalImpact = impacts[i];
            }
        }
        System.out.println("minimal impact is :" + minimalImpact);
        return count - minimalImpact;
    }


}
