
package com.compubase.tasaoq.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrdersModel {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("id_owner")
    @Expose
    private Integer idOwner;
    @SerializedName("owner")
    @Expose
    private String owner;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("price_rent_per_day")
    @Expose
    private String priceRentPerDay;
    @SerializedName("available_date_from")
    @Expose
    private String availableDateFrom;
    @SerializedName("available_date_to")
    @Expose
    private String availableDateTo;
    @SerializedName("number_km")
    @Expose
    private String numberKm;
    @SerializedName("price_km")
    @Expose
    private String priceKm;
    @SerializedName("price_trip")
    @Expose
    private String priceTrip;
    @SerializedName("rate")
    @Expose
    private String rate;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("area")
    @Expose
    private String area;
    @SerializedName("st_name")
    @Expose
    private String stName;
    @SerializedName("number_hone")
    @Expose
    private String numberHone;
    @SerializedName("lon")
    @Expose
    private String lon;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("number_of_trip")
    @Expose
    private String numberOfTrip;
    @SerializedName("model")
    @Expose
    private String model;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("Datee")
    @Expose
    private String datee;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdOwner() {
        return idOwner;
    }

    public void setIdOwner(Integer idOwner) {
        this.idOwner = idOwner;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPriceRentPerDay() {
        return priceRentPerDay;
    }

    public void setPriceRentPerDay(String priceRentPerDay) {
        this.priceRentPerDay = priceRentPerDay;
    }

    public String getAvailableDateFrom() {
        return availableDateFrom;
    }

    public void setAvailableDateFrom(String availableDateFrom) {
        this.availableDateFrom = availableDateFrom;
    }

    public String getAvailableDateTo() {
        return availableDateTo;
    }

    public void setAvailableDateTo(String availableDateTo) {
        this.availableDateTo = availableDateTo;
    }

    public String getNumberKm() {
        return numberKm;
    }

    public void setNumberKm(String numberKm) {
        this.numberKm = numberKm;
    }

    public String getPriceKm() {
        return priceKm;
    }

    public void setPriceKm(String priceKm) {
        this.priceKm = priceKm;
    }

    public String getPriceTrip() {
        return priceTrip;
    }

    public void setPriceTrip(String priceTrip) {
        this.priceTrip = priceTrip;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getStName() {
        return stName;
    }

    public void setStName(String stName) {
        this.stName = stName;
    }

    public String getNumberHone() {
        return numberHone;
    }

    public void setNumberHone(String numberHone) {
        this.numberHone = numberHone;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getNumberOfTrip() {
        return numberOfTrip;
    }

    public void setNumberOfTrip(String numberOfTrip) {
        this.numberOfTrip = numberOfTrip;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDatee() {
        return datee;
    }

    public void setDatee(String datee) {
        this.datee = datee;
    }

}
