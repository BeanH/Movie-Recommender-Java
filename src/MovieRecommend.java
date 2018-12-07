import com.sun.deploy.util.StringUtils;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MSSMAssignment {

    public static String RATINGS_INPUT_FILE_PATH = "RatingsInput.csv";
    public static String NEW_USER_FILE_PATH = "NewUsers.csv";
    public static String FINAL_OUTPUT_FILE_PATH = "FinalOutput.csv";   //final output file path
    public static String HEADER = "UserID";   //keyword of RatingsInput header
    public static int MOVIENAME_INDEX = 4;   //column index of movieName
    public static int MOVIEID_INDEX = 3;      //column index of movieId
    public static int AGE_INDEX = 2;           //column index of age
    public static int RARING_INDEX = 5;         //column index of rating

    public static String[] RATING_ARRAY = new String[]{"5", "4", "3", "2", "1"};  //rating from best to worst

    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(RATINGS_INPUT_FILE_PATH));
            String line;
            Map<String, Map<String, List<String>>> ageMap = new HashMap<>();
            while ((line = reader.readLine()) != null) {
                if (line.contains(HEADER)) {  //handle header line, just ignore it
                    continue;
                }
                String splitter = ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)";//use regex splitter to handle MovieName with "" and ,
                String item[] = line.split(splitter);
                if (item.length != 6) { //if the line cannot split into 6 parts, just ignore it and continue
                    System.out.println("row: " + line + " is in a bad pattern, ignore it");
                    continue;
                }
                //handle each line, then put it into map
                //1.split  2.extract movieId and remove from MovieName column
                //3.capitalizing movieName, remove quotes from movie name
                //4.put age, rating and purified movie name into map
                String movieName = item[MOVIENAME_INDEX];
                String pattern = "\\d+?,";  //match "10,"  in "10,fighter"
                Matcher matcher = Pattern.compile(pattern).matcher(movieName);
                while (matcher.find()) {
                    String movieIdWithComma = matcher.group(0);
                    String movieId = movieIdWithComma.substring(0, movieIdWithComma.length() - 1);// extract movieId
                    item[MOVIEID_INDEX] = movieId;              //set movieId into MovieID column
                    movieName = movieName.replace(matcher.group(), ""); //remove movieId and comma from MovieName
                    item[MOVIENAME_INDEX] = toTitleCase(movieName); //capitalizing movieName
                }
                String movieNameWithQuotes = item[MOVIENAME_INDEX];
                String pureMovieName = movieNameWithQuotes.substring(1, movieNameWithQuotes.length() - 1);
                putDataIntoMap(ageMap, item[AGE_INDEX], item[RARING_INDEX], pureMovieName);
            }
            reader.close();

            //read from the NewUsers.csv file, find data from the map, then generate final output
            BufferedReader br = new BufferedReader(new FileReader(NEW_USER_FILE_PATH));
            BufferedWriter output = new BufferedWriter(new FileWriter(new File(FINAL_OUTPUT_FILE_PATH)));
            line = br.readLine();
            output.write(line + "\n");
            while ((line = br.readLine()) != null) {
                String[] items = line.split(",");
                String age = items[1];
                String num = items[2];
                items[3] = getMovieNamesFromMap(ageMap, age, num);
                output.write(concatArrayToStr(items) + "\n");
            }
            br.close();
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Capitalizing first letter of every word in the movie names
     *
     * @param givenString like "spitfire grill"
     */
    public static String toTitleCase(String givenString) {
        String[] arr = givenString.split(" ");
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < arr.length; i++) {
            if (i == 0) {
                sb.append("\"" + Character.toUpperCase(arr[i].charAt(1))).append(arr[i].substring(2)).append(" ");
            } else {
                sb.append(Character.toUpperCase(arr[i].charAt(0))).append(arr[i].substring(1)).append(" ");
            }
        }
        return sb.toString().trim();
    }


    /**
     * put data into the map
     * looks like  {"20":{"2":["Isle Of Man Tt 2004 Review","What The Hell Do We Know!?"],"3":["Screamers","My Bloody Valentine"]}}
     * @param map
     * @param age
     * @param rate
     * @param movieName
     */
    public static void putDataIntoMap(Map<String, Map<String, List<String>>> map, String age, String rate, String movieName) {
        if (map.containsKey(age)) {
            Map<String, List<String>> subMap = map.get(age);
            if (subMap.containsKey(rate)) {
                List<String> movieList = subMap.get(rate);
                if (!movieList.contains(movieName)) {
                    movieList.add(movieName);
                }
            } else {
                List<String> movieList = new ArrayList<>();
                movieList.add(movieName);
                subMap.put(rate, movieList);
            }
        } else {
            List<String> movieList = new ArrayList<>();
            movieList.add(movieName);
            Map<String, List<String>> subMap = new HashMap<>();
            subMap.put(rate, movieList);
            map.put(age, subMap);
        }
    }


    /**
     * find answer from the map by age, rating, and num of movies to find
     * 1.get the sub map by age, if not exist, find the nearest age both younger and older side
     * 2.find movies from the rating 5 to 1 until match the num to find
     * 3.join the movie names to one string, and add quotes to the result
     */
    public static String getMovieNamesFromMap(Map<String, Map<String, List<String>>> map, String ageStr, String numStr) {
        Map<String, List<String>> ageMap = map.get(ageStr);  //get the sub map by age
        if (ageMap == null) {                                //if not exist,
            int age = Integer.parseInt(ageStr);              //find the nearest age both younger and older side
            int older = age;
            int younger = age;
            while (ageMap == null) {
                older = older + 1;
                younger = younger - 1;
                ageMap = map.get(new Integer(older).toString());
                if (ageMap == null) {
                    ageMap = map.get(new Integer(younger).toString());
                }
            }
        }
        List<String> selectedMovies = new ArrayList<>();
        int count = 0;     //the num of movies have been found
        int num = Integer.parseInt(numStr);
        for (String rate : RATING_ARRAY) {   //find movies from best rating to worst rating
            int diff = num - count;          //the difference of the number to find and has found
            if (diff <= 0) break;
            List<String> movies = ageMap.get(rate);
            if (movies != null) {
                if (movies.size() < diff) {
                    selectedMovies.addAll(movies);
                    count += movies.size();
                } else {
                    for (int i = 0; i < movies.size(); i++) {
                        selectedMovies.add(movies.get(i));
                        count++;
                        if (num - count <= 0) break;
                    }
                }
            }
        }
        String ret = "\"" + StringUtils.join(selectedMovies, ",") + "\"";
        return ret;
    }


    /**
     * concat split items into a string
     */
    public static String concatArrayToStr(String[] items) {
        return StringUtils.join(Arrays.asList(items), ",");
    }
}
