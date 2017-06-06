
package com.yoscholar.ping.retrofitPojo.tokenDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TokenDetails {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("token_id")
    @Expose
    private String tokenId;
    @SerializedName("family_magento_id")
    @Expose
    private String familyMagentoId;
    @SerializedName("otp")
    @Expose
    private Integer otp;
    @SerializedName("valid_up_to")
    @Expose
    private String validUpTo;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getFamilyMagentoId() {
        return familyMagentoId;
    }

    public void setFamilyMagentoId(String familyMagentoId) {
        this.familyMagentoId = familyMagentoId;
    }

    public Integer getOtp() {
        return otp;
    }

    public void setOtp(Integer otp) {
        this.otp = otp;
    }

    public String getValidUpTo() {
        return validUpTo;
    }

    public void setValidUpTo(String validUpTo) {
        this.validUpTo = validUpTo;
    }

}
