package tests;
import app.UserHistory;
import app.PathFinder;
import org.junit.BeforeClass;
import org.junit.ComparisonFailure;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import static org.junit.Assert.*;

public class BaselineTest {
    private final String CSV_SEPARATOR = ",";
    static String abs_path;

    @BeforeClass
    public static void runBeforeTests() {
        abs_path = System.getProperty("user.dir");
    }

    @Test
    public void ConfirmHistoricalSnapshotTest() {
        PathFinder reader = new PathFinder(abs_path + "/inputs");
        Queue<String> queue = reader.getFileQueue();
        String snapshot_current = queue.poll();
        try (BufferedReader buffer = Files.newBufferedReader(
                Paths.get(abs_path + "/src/tests/snapshots/inputs_snapshot.txt"),
                StandardCharsets.UTF_8)
        ) {
            String line;
            while ((line = buffer.readLine()) != null) {
                try {
                    snapshot_current = snapshot_current.replace(abs_path, "");
                    assertEquals(line, snapshot_current);
                    snapshot_current = queue.poll();
                } catch(ComparisonFailure e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void UniqueIDCombinationTest() {
        List<UserHistory> stagedList = new ArrayList<>();
        List<UserHistory> actualList = new ArrayList<>();
        try (BufferedReader buffer = Files.newBufferedReader(
                Paths.get(abs_path + "/src/tests/snapshots/outputs_before_snapshot.csv"),
                StandardCharsets.UTF_8)
        ) {
            String line;
            while ((line = buffer.readLine()) != null) {
                String[] field_vals = line.split(",");
                UserHistory hist_loc = new UserHistory(field_vals[1], field_vals[2], field_vals[3]);
                boolean match_found = false;
                for (UserHistory hist : stagedList) {
                    if (hist.matchUserHistory(hist_loc)) {
                        hist.updateRequestCount(hist_loc);
                        match_found = true;
                        break;
                    }
                }
                if (!match_found) {
                    stagedList.add(hist_loc);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            assertEquals(7, stagedList.size());
        } catch(ComparisonFailure e) {
            System.out.println(e.getMessage());
        }

        Path csv_path = Paths.get(abs_path + "/src/tests/snapshots/outputs_after_snapshot.csv");

        try (BufferedReader buffer = Files.newBufferedReader(csv_path, StandardCharsets.UTF_8)) {
            int i = 0;
            String line;
            while ((line = buffer.readLine()) != null) {
                String[] field_vals = line.split(CSV_SEPARATOR);
                UserHistory hist_loc = new UserHistory(field_vals[0], field_vals[1], field_vals[2]);
                actualList.add(hist_loc);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String> acceptable_responses = new ArrayList<String>(7);

        for (UserHistory u : stagedList) {
            acceptable_responses.add(u.toString());
        }

        for (UserHistory u : actualList) {
            int idx = acceptable_responses.indexOf(u.toString());
            if (idx > -1) {
                acceptable_responses.remove(idx);
            }
        }

        assertEquals(0, acceptable_responses.size());
    }
}
