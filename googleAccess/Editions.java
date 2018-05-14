package com.appdirect.googleAccess;


public class Editions {

    private String editionName ;

    private String usdPrice;
    private String audPrice;
    private String gbpPrice;
    private String eurPrice;
    private String skuType;

    private String offerId,appId ;

    public String getEditionName() {
        return editionName;
    }

    public void setEditionName(String editionName) {
        this.editionName = editionName;
    }



    public String getusdPrice() {
        return usdPrice;
    }

    public void setusdPrice(String usdPrice) {
        this.usdPrice = usdPrice;
    }

    public String getaudPrice() {
        return audPrice;
    }

    public void setaudPrice(String audPrice) {
        this.audPrice = audPrice;
    }

    public String getgbpPrice() {
        return gbpPrice;
    }

    public void setgbpPrice(String gbpPrice) {
        this.gbpPrice = gbpPrice;
    }

    public String geteurPrice() {
        return eurPrice;
    }

    public void seteurPrice(String eurPrice) {
        this.eurPrice = eurPrice;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getappId() {
        return appId;
    }

    public void setappId(String appId) {
        this.appId = appId;
    }

    public String getSkuType() {
        return skuType;
    }
    public void setSkuType(String skuType) {
        this.skuType = skuType;
    }


}
