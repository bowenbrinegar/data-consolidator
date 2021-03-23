package app;

import java.util.Arrays;
import java.util.stream.Collectors;

public class UserHistory {
    int user_id;
    int platform_id;
    int n_requests;

    public UserHistory(String user_id, String platform_id, String n_requests) {
        this.user_id = Integer.parseInt(user_id);
        this.platform_id = Integer.parseInt(platform_id);
        this.n_requests = Integer.parseInt(n_requests);
    }
    public boolean matchUserHistory(UserHistory p) {
        return user_id == p.getUserID() && platform_id == p.getPlatformID();
    }

    public int getUserID() {
        return user_id;
    }

    public int getPlatformID() {
        return platform_id;
    }

    public int getRequestCount() {
        return n_requests;
    }

    public void updateRequestCount(UserHistory p) {
        this.n_requests += p.getRequestCount();
    }

    public String toString() {
        int[] vals = new int[] {user_id, platform_id, n_requests};
        return Arrays.stream(vals).mapToObj(String::valueOf).collect(Collectors.joining(","));
    }
}
