package com.example.appbansach.modle;

public class mh {
    private String maHang;
    private String tenHang;
    private String moTa;
    private double gia;
    private int soLuong;

    public mh() {
    }

    public mh(String maHang, String tenHang, String moTa, double gia, int soLuong) {
        this.maHang = maHang;
        this.tenHang = tenHang;
        this.moTa = moTa;
        this.gia = gia;
        this.soLuong = soLuong;
    }

    public String getMaHang() {
        return maHang;
    }

    public void setMaHang(String maHang) {
        this.maHang = maHang;
    }

    public String getTenHang() {
        return tenHang;
    }

    public void setTenHang(String tenHang) {
        this.tenHang = tenHang;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public double getGia() {
        return gia;
    }

    public void setGia(double gia) {
        this.gia = gia;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

}
