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
                // reader = new BufferedReader(new FileReader("1.1.in"));
                output = new BufferedWriter(new FileWriter(new File("output/" + i + ".out")));
                //output = new BufferedWriter(new FileWriter(new File("1.out")));
                String line = reader.readLine();
                List<Interval> intervals = new ArrayList<>();
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.trim().split(" ");
                    Interval interval = new Interval(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
                    intervals.add(interval);
                }
                Collections.sort(intervals);
                int result = handle(intervals);
                System.out.println(i+".out: "+result);
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

    public static int handle(List<Interval> intervals) {
        int count = intervals.get(0).end - intervals.get(0).start;
        int start = intervals.get(0).start;
        int end = intervals.get(0).end;

        boolean hasMinimal = false;
        int minimalImpact = 0;

        int n = intervals.size();
        int[] impacts = new int[n];
        for (int i = 0; i < n; i++) {
            impacts[i] = intervals.get(i).end - intervals.get(i).start;
        }
        for (int i = 1; i < n; i++) {
            if (intervals.get(i).start < end) {//overlapping
                if (intervals.get(i).end < end) { //interval i is inside interval i-1
                    impacts[i] = 0;
                    hasMinimal = true;
                    minimalImpact = 0;
                    continue;
                }
                count += (intervals.get(i).end - end);
                int overlapping = end - intervals.get(i).start;
                end = intervals.get(i).end;
                if (!hasMinimal) {
                    impacts[i] = impacts[i] - overlapping;
                    if (impacts[i] <= 0) {
                        hasMinimal = true;
                        minimalImpact = 0;
                    }
                    impacts[i - 1] = impacts[i - 1] - overlapping;
                    if (impacts[i - 1] <= 0) {
                        hasMinimal = true;
                        minimalImpact = 0;
                    }
                }
            } else {//not overlapping
                count += (intervals.get(i).end - intervals.get(i).start);
                start = intervals.get(i).start;
                end = intervals.get(i).end;
            }
        }


        if (!hasMinimal) {
            minimalImpact = impacts[0];
            for (int i = 1; i < n; ++i) {
                if (minimalImpact > impacts[i]) {
                    minimalImpact = impacts[i];
                }
            }
        }

        //System.out.println("minimal impact is :" + minimalImpact);
        return count - minimalImpact;
    }


}
