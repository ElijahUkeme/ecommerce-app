package model;

public class DeliveredOrder {

    private String name;
    private String phone;
    private String address;
    private String city;
    private String totalAmount;
    private String orderedDate;
    private String orderedTime;
    private String deliveredDate;
    private String deliveredTime;
    private String receiversName;

    public DeliveredOrder() {
    }

    public DeliveredOrder(String name, String phone, String address, String city, String totalAmount, String orderedDate, String orderedTime, String deliveredDate, String deliveredTime, String receiversName) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.city = city;
        this.totalAmount = totalAmount;
        this.orderedDate = orderedDate;
        this.orderedTime = orderedTime;
        this.deliveredDate = deliveredDate;
        this.deliveredTime = deliveredTime;
        this.receiversName = receiversName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getOrderedDate() {
        return orderedDate;
    }

    public void setOrderedDate(String orderedDate) {
        this.orderedDate = orderedDate;
    }

    public String getOrderedTime() {
        return orderedTime;
    }

    public void setOrderedTime(String orderedTime) {
        this.orderedTime = orderedTime;
    }

    public String getDeliveredDate() {
        return deliveredDate;
    }

    public void setDeliveredDate(String deliveredDate) {
        this.deliveredDate = deliveredDate;
    }

    public String getDeliveredTime() {
        return deliveredTime;
    }

    public void setDeliveredTime(String deliveredTime) {
        this.deliveredTime = deliveredTime;
    }

    public String getReceiversName() {
        return receiversName;
    }

    public void setReceiversName(String receiversName) {
        this.receiversName = receiversName;
    }
}
