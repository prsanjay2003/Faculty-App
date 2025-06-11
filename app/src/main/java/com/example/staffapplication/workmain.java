package com.example.staffapplication;

public class workmain {
    String edTitle;
    String edYear;
    String edMonth;
    String pdfUrl;

    public String getEdRadio() {
        return edRadio;
    }

    public void setEdRadio(String edRadio) {
        this.edRadio = edRadio;
    }

    String edRadio;

    public workmain() {
    }

    String edPlace;

    public workmain(String edTitle, String edYear, String edMonth, String edPlace, String pdfUrl,String edRadio) {
        this.edTitle = edTitle;
        this.edYear = edYear;
        this.edMonth = edMonth;
        this.edPlace = edPlace;
        this.pdfUrl=pdfUrl;
        this.edRadio=edRadio;
    }

    public String getEdTitle() {
        return edTitle;
    }
    public String getPdfUrl(){
        return pdfUrl;
    }
    public void setPdfUrl(String pdfUrl){
        this.pdfUrl  = pdfUrl;
    }

    public void setEdTitle(String edTitle) {
        this.edTitle = edTitle;
    }

    public String getEdYear() {
        return edYear;
    }

    public void setEdYear(String edYear) {
        this.edYear = edYear;
    }

    public String getEdMonth() {
        return edMonth;
    }

    public void setEdMonth(String edMonth) {
        this.edMonth = edMonth;
    }

    public String getEdPlace() {
        return edPlace;
    }

    public void setEdPlace(String edPlace) {
        this.edPlace = edPlace;
    }
}
