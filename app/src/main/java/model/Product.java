package model;

public class Product {
    private String pid;
    private String date;
    private String time;
    private String description;
    private String product_name;
    private String price;
    private String category;
    private String image;
    private String productStatus;

    public Product() {
    }

    public Product(String pid, String date, String time, String description, String product_name, String price, String category, String image, String productStatus) {
        this.pid = pid;
        this.date = date;
        this.time = time;
        this.description = description;
        this.product_name = product_name;
        this.price = price;
        this.category = category;
        this.image = image;
        this.productStatus = productStatus;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(String productStatus) {
        this.productStatus = productStatus;
    }
}
