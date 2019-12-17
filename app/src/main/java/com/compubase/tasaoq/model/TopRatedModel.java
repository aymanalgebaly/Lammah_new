package com.compubase.tasaoq.model;

public class TopRatedModel {

    private int img_item,img_heart;
    private String txt_sale,txt_sale_offer,txt_offer,txt_rate,title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public TopRatedModel(int img_item, int img_heart, String txt_sale, String txt_sale_offer,
                         String txt_offer, String txt_rate, String title) {
        this.img_item = img_item;
        this.img_heart = img_heart;
        this.txt_sale = txt_sale;
        this.txt_sale_offer = txt_sale_offer;
        this.txt_offer = txt_offer;
        this.txt_rate = txt_rate;
        this.title = title;
    }

    public TopRatedModel() {
    }

    public int getImg_item() {
        return img_item;
    }

    public void setImg_item(int img_item) {
        this.img_item = img_item;
    }

    public int getImg_heart() {
        return img_heart;
    }

    public void setImg_heart(int img_heart) {
        this.img_heart = img_heart;
    }

    public String getTxt_sale() {
        return txt_sale;
    }

    public void setTxt_sale(String txt_sale) {
        this.txt_sale = txt_sale;
    }

    public String getTxt_sale_offer() {
        return txt_sale_offer;
    }

    public void setTxt_sale_offer(String txt_sale_offer) {
        this.txt_sale_offer = txt_sale_offer;
    }

    public String getTxt_offer() {
        return txt_offer;
    }

    public void setTxt_offer(String txt_offer) {
        this.txt_offer = txt_offer;
    }

    public String getTxt_rate() {
        return txt_rate;
    }

    public void setTxt_rate(String txt_rate) {
        this.txt_rate = txt_rate;
    }
}
