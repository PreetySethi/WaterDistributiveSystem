package com.example.wds.Model;

public class ProductModel {

        public String productName;
        public String price;
        public String quantity;
        public int imagePath;


    public ProductModel(String productName, String price, String quantity, int imagePath) {
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.imagePath = imagePath;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPrice() {

        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public int getImagePath() {
        return imagePath;
    }

    public void setImagePath(int imagePath) {
        this.imagePath = imagePath;
    }
}
