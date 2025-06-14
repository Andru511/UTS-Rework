package com.mycompany.mavenproject3;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Mavenproject3 extends JFrame implements Runnable {
    private String text;
    private int x;
    private int width;
    private BannerPanel bannerPanel;
    private JButton addProductButton;
    private JButton addPenjualanButton;
    private JButton customerButton; // ✅ Tambahan
    private List<Product> products;
    private List<Penjualan> penjualanList;
    

    public Mavenproject3(List<Product> products) {
        this.products = products;
        this.penjualanList = new ArrayList<>();
        this.text = generateProductText(products);

        setTitle("WK. STI Chill");
        setSize(600, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        bannerPanel = new BannerPanel();
        add(bannerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        addProductButton = new JButton("Kelola Produk");
        addPenjualanButton = new JButton("Kelola Penjualan");
        customerButton = new JButton("Customer"); // ✅ Tambahan

        bottomPanel.add(addProductButton);
        bottomPanel.add(addPenjualanButton);
        bottomPanel.add(customerButton); // ✅ Tambahan

        add(bottomPanel, BorderLayout.SOUTH);

        // Event handler tombol
        addProductButton.addActionListener(e -> {
            new ProductForm(products, this).setVisible(true);
        });

        addPenjualanButton.addActionListener(e -> {
            new PenjualanWindow(penjualanList, products, this).setVisible(true);
        });

        customerButton.addActionListener(e -> {
            new CustomerForm().setVisible(true); // ✅ Pastikan class CustomerForm ada
        });

        setVisible(true);

        Thread thread = new Thread(this);
        thread.start();
    }

    public void refreshBanner() {
        this.text = generateProductText(products);
    }

    private String generateProductText(List<Product> products) {
        StringBuilder sb = new StringBuilder("Menu yang tersedia: ");
        for (int i = 0; i < products.size(); i++) {
            sb.append(products.get(i).getName());
            if (i < products.size() - 1) sb.append(" | ");
        }
        return sb.toString();
    }

    class BannerPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 18));
            g.drawString(text, x, getHeight() / 2);
        }
    }

    @Override
    public void run() {
        width = getWidth();
        while (true) {
            x += 5;
            if (x > width) {
                x = -getFontMetrics(new Font("Arial", Font.BOLD, 18)).stringWidth(text);
            }
            bannerPanel.repaint();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public static void main(String[] args) {
        List<Product> products = new ArrayList<>();
        products.add(new Product(1, "P001", "Americano", "Coffee", 18000, 10));
        products.add(new Product(2, "P002", "Pandan Latte", "Coffee", 15000, 8));

        new Mavenproject3(products);
    }
}
