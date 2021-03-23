package app;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Aggregator {
    private static final String CSV_SEPARATOR = ",";
    private static final String CHARSET = "UTF-8";
    private Path csv_path;
    private String output_path;
    private List<UserHistory> stagedList;

    public Aggregator(String incoming_path) {
        stagedList = new ArrayList<>();
        csv_path = Paths.get(incoming_path);
        this.output_path = incoming_path.replace("inputs", "outputs");
        this.output_path = output_path.replace(csv_path.getFileName().toString(), "");
        this.output_path = output_path.substring(0, output_path.length() - 3);
    }

    private void addOrUpdate(UserHistory hist_loc) {
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

    private void addExistingData(String existing_path) {
        try (BufferedReader buffer = Files.newBufferedReader(Paths.get(existing_path), StandardCharsets.UTF_8)) {
            String line;
            while ((line = buffer.readLine()) != null) {
                String[] field_vals = line.split(CSV_SEPARATOR);
                UserHistory hist_loc = new UserHistory(field_vals[0], field_vals[1], field_vals[2]);
                addOrUpdate(hist_loc);
            }
            new File(existing_path).delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String findExistingFile() {
        File existing = new File(this.output_path);
        File[] sub_files = existing.listFiles();
        String output = "";
        for (File f : sub_files) {
            if (f.isDirectory()) {
                continue;
            } else if (f.getPath().contains(".csv")) {
                output = f.getPath();
                break;
            } else {
                f.delete();
            }
        }
        return output;
    }

    private void readcsv() {
        try (BufferedReader buffer = Files.newBufferedReader(csv_path, StandardCharsets.UTF_8)) {
            String line;
            while ((line = buffer.readLine()) != null) {
                String[] field_vals = line.split(CSV_SEPARATOR);
                UserHistory hist_loc = new UserHistory(field_vals[1], field_vals[2], field_vals[3]);
                addOrUpdate(hist_loc);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writecsv() {
        try {
            String output_path = this.output_path + this.output_path.hashCode() + ".csv";
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(output_path), CHARSET);
            BufferedWriter buffer = new BufferedWriter(writer);
            for (UserHistory hist : stagedList) {
                buffer.write(hist.toString());
                buffer.newLine();
            }
            buffer.flush();
            buffer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void execute() {
        File new_path = new File(output_path);
        if (!new_path.exists()) {
            new_path.mkdirs();
        } else {
            String existingPath = this.findExistingFile();
            if (!existingPath.equals(null) || !existingPath.equals("")) {
                this.addExistingData(existingPath);
            }
        }
        this.readcsv();
        this.writecsv();
    }
}
