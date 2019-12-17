
package com.compubase.tasaoq.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class ProductsModel extends RealmObject implements Parcelable{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("des")
    @Expose
    private String des;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("price_discount")
    @Expose
    private String priceDiscount;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("number_rate")
    @Expose
    private String numberRate;
    @SerializedName("number_star")
    @Expose
    private String numberStar;
    @SerializedName("rate")
    @Expose
    private String rate;
    @SerializedName("img_1")
    @Expose
    private String img1;
    @SerializedName("img_2")
    @Expose
    private String img2;
    @SerializedName("img_3")
    @Expose
    private String img3;
    @SerializedName("datee")
    @Expose
    private String datee;
    @SerializedName("isfav")
    @Expose
    private String isfav;

    protected ProductsModel(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        title = in.readString();
        des = in.readString();
        price = in.readString();
        priceDiscount = in.readString();
        category = in.readString();
        numberRate = in.readString();
        numberStar = in.readString();
        rate = in.readString();
        img1 = in.readString();
        img2 = in.readString();
        img3 = in.readString();
        datee = in.readString();
        isfav = in.readString();
    }

    public static final Creator<ProductsModel> CREATOR = new Creator<ProductsModel>() {
        @Override
        public ProductsModel createFromParcel(Parcel in) {
            return new ProductsModel(in);
        }

        @Override
        public ProductsModel[] newArray(int size) {
            return new ProductsModel[size];
        }
    };

    public ProductsModel() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPriceDiscount() {
        return priceDiscount;
    }

    public void setPriceDiscount(String priceDiscount) {
        this.priceDiscount = priceDiscount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getNumberRate() {
        return numberRate;
    }

    public void setNumberRate(String numberRate) {
        this.numberRate = numberRate;
    }

    public String getNumberStar() {
        return numberStar;
    }

    public void setNumberStar(String numberStar) {
        this.numberStar = numberStar;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getImg1() {
        return img1;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }

    public String getImg2() {
        return img2;
    }

    public void setImg2(String img2) {
        this.img2 = img2;
    }

    public String getImg3() {
        return img3;
    }

    public void setImg3(String img3) {
        this.img3 = img3;
    }

    public String getDatee() {
        return datee;
    }

    public void setDatee(String datee) {
        this.datee = datee;
    }

    public String getIsfav() {
        return isfav;
    }

    public void setIsfav(String isfav) {
        this.isfav = isfav;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(title);
        dest.writeString(des);
        dest.writeString(price);
        dest.writeString(priceDiscount);
        dest.writeString(category);
        dest.writeString(numberRate);
        dest.writeString(numberStar);
        dest.writeString(rate);
        dest.writeString(img1);
        dest.writeString(img2);
        dest.writeString(img3);
        dest.writeString(datee);
        dest.writeString(isfav);
    }
}
