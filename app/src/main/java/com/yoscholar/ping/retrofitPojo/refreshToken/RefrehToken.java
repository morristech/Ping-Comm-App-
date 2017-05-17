
package com.yoscholar.ping.retrofitPojo.refreshToken;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RefrehToken {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("token")
    @Expose
    private String token;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
