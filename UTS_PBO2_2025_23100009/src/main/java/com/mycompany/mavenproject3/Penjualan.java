package com.mycompany.mavenproject3;

public class Penjualan {
    private int id;
    private String kodeProduk;
    private String namaProduk;
    private int jumlah;
    private int totalHarga;
    private String customerName;

    public Penjualan(int id, String kodeProduk, String namaProduk, int jumlah, int totalHarga, String customerName) {
        this.id = id;
        this.kodeProduk = kodeProduk;
        this.namaProduk = namaProduk;
        this.jumlah = jumlah;
        this.totalHarga = totalHarga;
        this.customerName = customerName;
    }

    public int getId() { return id; }
    public String getKodeProduk() { return kodeProduk; }
    public String getNamaProduk() { return namaProduk; }
    public int getJumlah() { return jumlah; }
    public int getTotalHarga() { return totalHarga; }
    public String getCustomerName() { return customerName; }
}
