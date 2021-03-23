package app;

import java.io.File;
import java.util.LinkedList;
import java.util.Queue;

public class PathFinder {
    Queue<String> file_paths = new LinkedList<String>();

    public PathFinder(String inputs_path) {
        try {
            this.walk_dir(inputs_path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void walk_dir(String path) {
        File curr_path = new File(path);
        File[] sub_files = curr_path.listFiles();
        for (File f : sub_files) {
            if (f.isDirectory()) {
                walk_dir(f.getAbsolutePath());
            } else if (f.getPath().contains(".csv")) {
                file_paths.add(f.getPath());
            }
        }
    }

    public Queue<String> getFileQueue() {
        return file_paths;
    }
}
