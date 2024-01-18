package com.example.applt.model;

import java.util.List;

public class Bill {
    private String billId, userId, total;
    private List<Cart> listCart;

    public Bill(String userId, String total, List<Cart> listCart) {
        this.userId = userId;
        this.total = total;
        this.listCart = listCart;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public Bill() {
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Cart> getListCart() {
        return listCart;
    }

    public void setListCart(List<Cart> listCart) {
        this.listCart = listCart;
    }
}
