package com.flame.bgfcreditapp.Model;

public class PdfHelper {

    public String name;
    public String url;

    public PdfHelper() {
    }

    public PdfHelper(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
