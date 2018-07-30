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
                String line = reader.readLine(); //the first line is not used
                List<Interval> intervals = new ArrayList<>();
                while ((line = reader.readLine()) != null) { //read each line into Interval list
                    String[] parts = line.trim().split(" ");
                    Interval interval = new Interval(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
                    intervals.add(interval);
                }
                Collections.sort(intervals);  //sort the intervals by start time
                int result = handle(intervals); //handle the sorted list of intervals and return final result
                System.out.println(i + ".out: " + result);
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
        int count = intervals.get(0).end - intervals.get(0).start; //the amount of time units being covered by all safeguards
        int end = intervals.get(0).end; //points to the end time unit of intervals before

        boolean findMinimal = false;  //has found the safeguard that has minimal impact
        int minimalImpact = 0;   //minimal impact time units

        int n = intervals.size();  //the number of time intervals, also the number of safeguards
        int[] impacts = new int[n]; //impacts store the impact time units of each safeguard if he is fired
        for (int i = 0; i < n; i++) {  //init impacts of their working time units : end - start
            impacts[i] = intervals.get(i).end - intervals.get(i).start;
        }
        for (int i = 1; i < n; i++) {
            if (intervals.get(i).start < end) {//overlapping
                if (intervals.get(i).end < end) { //interval i is inside interval i-1
                    impacts[i] = 0;
                    findMinimal = true;
                    minimalImpact = 0;
                    continue;
                }
                count += (intervals.get(i).end - end);  //1 4 and 3 7
                //at first, count==3,end==4,intervals.get(i).end ==7
                //now, count += (7-4)
                //count = 6
                int overlapping = end - intervals.get(i).start;  //intervals.get(i).start ==3,end==4, overlapping==1
                end = intervals.get(i).end;   //end points to 7
                if (!findMinimal) {   //if not fund minimal impact
                    impacts[i] = impacts[i] - overlapping; //each safeguard's impact equals to his working hours - overlapping hours
                    if (impacts[i] <= 0) { //say 0 is the minimal impact
                        findMinimal = true;
                        minimalImpact = 0;
                    }
                    impacts[i - 1] = impacts[i - 1] - overlapping;
                    if (impacts[i - 1] <= 0) {
                        findMinimal = true;
                        minimalImpact = 0;
                    }
                }
            } else {//not overlapping
                count += (intervals.get(i).end - intervals.get(i).start);
                end = intervals.get(i).end;
            }
        }


        if (!findMinimal) {
            minimalImpact = impacts[0];
            for (int i = 1; i < n; ++i) {
                if (minimalImpact > impacts[i]) {
                    minimalImpact = impacts[i];
                }
            }
        }
        return count - minimalImpact;
    }


}
