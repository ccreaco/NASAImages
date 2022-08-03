package com.example.finalproject;

public class NASAImage {
    private String imgDate;
    private String imgurl;
    private String hdurl;
    private String title;
    private String copyright;
    private String description;

    public NASAImage() {

    }

    public NASAImage(String imgDate, String imgurl, String hdurl, String title, String copyright, String description) {
        this.copyright = copyright;
        this.imgurl = imgurl;
        this.imgDate = imgDate;
        this.title = title;
        this.description = description;
        this.hdurl = hdurl;
    }


    @Override
    public String toString() {
        return "NASAImage{" +
                "Date:'" + imgDate + '\'' +
                ", Title:'" + title + '\'' +
                ", Copyright:'" + copyright + '\'' +
                ", Description:'" + description + '\'' +
                '}';
    }

    public String getImgDate() {

        return imgDate;
    }

    public void setImgDate(String imgDate) {

        this.imgDate = imgDate;
    }

    public String getImgurl() {

        return imgurl;
    }

    public void setImgurl(String imgurl) {

        this.imgurl = imgurl;
    }

    public String getHdurl() {

        return hdurl;
    }

    public void setHdurl(String hdurl) {

        this.hdurl = hdurl;
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {

        this.title = title;
    }

    public String getCopyright() {

        return copyright;
    }

    public void setCopyright(String copyright) {

        this.copyright = copyright;
    }

    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {

        this.description = description;
    }

}
