package com.codingwithimran.adminpanelecommerce.Modals;

public class AllProductModal {
    String  ProductId, product_img, description, name, rating, product_video;
    int price, stockProduct;

    public AllProductModal(String product_img, String description, String name, int price) {
        this.product_img = product_img;
        this.description = description;
        this.name = name;
        this.price = price;
    }
    public AllProductModal(String product_img, String description, String name, int stockProduct, int price) {
        this.product_img = product_img;
        this.description = description;
        this.name = name;
        this.stockProduct = stockProduct;
        this.price = price;
    }
    public AllProductModal(String description, String name, int stockProduct, int price) {
        this.description = description;
        this.name = name;
        this.stockProduct = stockProduct;
        this.price = price;
    }
    public AllProductModal(String description, String name, int price) {
        this.description = description;
        this.name = name;
        this.price = price;
    }
    public AllProductModal(String product_img, String description, String name, String rating, int price) {
        this.product_img = product_img;
        this.description = description;
        this.name = name;
        this.rating = rating;
        this.price = price;
    }

    public AllProductModal() {
    }

    public int getStockProduct() {
        return stockProduct;
    }

    public void setStockProduct(int stockProduct) {
        this.stockProduct = stockProduct;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getProduct_img() {
        return product_img;
    }

    public void setProduct_img(String product_img) {
        this.product_img = product_img;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getProduct_video() {
        return product_video;
    }

    public void setProduct_video(String product_video) {
        this.product_video = product_video;
    }
}
