package model;

public class AdminOrders {

    private String name;
    private String city;
    private String homeAddress;
    private String time;
    private String date;
    private String status;
    private String totalAmount;
    private String phone;

    public AdminOrders(String name, String city, String homeAddress, String time, String date, String status, String totalAmount, String phone) {
        this.name = name;
        this.city = city;
        this.homeAddress = homeAddress;
        this.time = time;
        this.date = date;
        this.status = status;
        this.totalAmount = totalAmount;
        this.phone = phone;
    }

    public AdminOrders() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
