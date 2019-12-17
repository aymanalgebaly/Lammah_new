
package com.compubase.tasaoq.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LammahOrdersModel implements Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("id_user")
    @Expose
    private Integer idUser;
    @SerializedName("id_product")
    @Expose
    private Integer idProduct;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("totle_price")
    @Expose
    private String totlePrice;
    @SerializedName("datee")
    @Expose
    private String datee;

    protected LammahOrdersModel(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        if (in.readByte() == 0) {
            idUser = null;
        } else {
            idUser = in.readInt();
        }
        if (in.readByte() == 0) {
            idProduct = null;
        } else {
            idProduct = in.readInt();
        }
        address = in.readString();
        totlePrice = in.readString();
        datee = in.readString();
    }

    public static final Creator<LammahOrdersModel> CREATOR = new Creator<LammahOrdersModel>() {
        @Override
        public LammahOrdersModel createFromParcel(Parcel in) {
            return new LammahOrdersModel(in);
        }

        @Override
        public LammahOrdersModel[] newArray(int size) {
            return new LammahOrdersModel[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public Integer getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(Integer idProduct) {
        this.idProduct = idProduct;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotlePrice() {
        return totlePrice;
    }

    public void setTotlePrice(String totlePrice) {
        this.totlePrice = totlePrice;
    }

    public String getDatee() {
        return datee;
    }

    public void setDatee(String datee) {
        this.datee = datee;
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
        if (idUser == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(idUser);
        }
        if (idProduct == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(idProduct);
        }
        dest.writeString(address);
        dest.writeString(totlePrice);
        dest.writeString(datee);
    }
}
