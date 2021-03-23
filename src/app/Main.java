package app;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Queue;

public class Main {

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException, ParseException {
        DateHelper dateHelper = new DateHelper();
        String dateInput = "2020/12/12";

        try {
            dateInput = args[0];
        } catch (IndexOutOfBoundsException e) {
            System.out.println("date parameter not provided");
            System.exit(1);
        }

        boolean validDate = dateHelper.isValidDate(dateInput);
        if (!validDate) {
            System.out.println("date format not valid");
            System.exit(1);
        }

        String abs_path = System.getProperty("user.dir");
        PathFinder reader = new PathFinder(abs_path + "/inputs");
        Queue<String> queue = reader.getFileQueue();

        String curr;
        while ((curr = queue.poll()) != null) {
            String[] pathSplit = curr.replace(abs_path, "").split("/");
            pathSplit = Arrays.copyOfRange(pathSplit, 2, 5);
            String pathDate = String.join("/", pathSplit);

            boolean shouldAggregate = dateHelper.gte(dateInput, pathDate);
            if (shouldAggregate) {
                Aggregator aggregator = new Aggregator(curr);
                aggregator.execute();
            }
        }
    }
}