package com.yoscholar.ping.pojo;

/**
 * Created by agrim on 22/5/17.
 */

public class RefreshScreen {

    private boolean refresh;

    public RefreshScreen(boolean refresh) {
        this.refresh = refresh;
    }

    public boolean isRefresh() {
        return refresh;
    }

    public void setRefresh(boolean refresh) {
        this.refresh = refresh;
    }
}
