package com.mycompany.mavenproject3;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class PenjualanWindow extends JFrame {
    private DefaultTableModel model;

    public PenjualanWindow(List<Penjualan> penjualanList, List<Product> products, JFrame parent) {
        setTitle("Kelola Penjualan");
        setSize(600, 400);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

       
        String[] columnNames = {"ID", "Kode", "Nama Produk", "Jumlah", "Total Harga", "Customer"};
        model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

       
        JPanel inputPanel = new JPanel(new GridLayout(2, 4, 5, 5));

        JTextField customerField = new JTextField();
        JComboBox<String> productBox = new JComboBox<>();
        for (Product p : products) {
            productBox.addItem(p.getName());
        }
        JTextField jumlahField = new JTextField();
        JButton submitBtn = new JButton("Buat Pesanan");

        inputPanel.add(new JLabel("Nama Customer:"));
        inputPanel.add(customerField);
        inputPanel.add(new JLabel("Pilih Produk:"));
        inputPanel.add(productBox);
        inputPanel.add(new JLabel("Jumlah Beli:"));
        inputPanel.add(jumlahField);
        inputPanel.add(new JLabel(""));
        inputPanel.add(submitBtn);

        add(inputPanel, BorderLayout.SOUTH);

        
        submitBtn.addActionListener(e -> {
            String customerName = customerField.getText().trim();
            String namaProduk = (String) productBox.getSelectedItem();
            String jumlahStr = jumlahField.getText().trim();

            if (customerName.isEmpty() || jumlahStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Isi semua field!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int jumlah;
            try {
                jumlah = Integer.parseInt(jumlahStr);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Jumlah harus angka!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Product selected = null;
            for (Product p : products) {
                if (p.getName().equals(namaProduk)) {
                    selected = p;
                    break;
                }
            }

            if (selected != null) {
                if (selected.getStock() >= jumlah) {
                    selected.setStock(selected.getStock() - jumlah);
                    int total = (int) (jumlah * selected.getPrice()); 
                    Penjualan newP = new Penjualan(
                            penjualanList.size() + 1,
                            selected.getCode(),
                            selected.getName(),
                            jumlah,
                            total,
                            customerName
                    );
                    penjualanList.add(newP);
                    model.addRow(new Object[]{
                            newP.getId(),
                            newP.getKodeProduk(),
                            newP.getNamaProduk(),
                            newP.getJumlah(),
                            newP.getTotalHarga(),
                            newP.getCustomerName()
                    });
                    JOptionPane.showMessageDialog(this, "✅ Pesanan berhasil dilakukan.");
                    jumlahField.setText("");
                    customerField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Stok tidak cukup!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        
        for (Penjualan p : penjualanList) {
            model.addRow(new Object[]{
                    p.getId(),
                    p.getKodeProduk(),
                    p.getNamaProduk(),
                    p.getJumlah(),
                    p.getTotalHarga(),
                    p.getCustomerName()
            });
        }
    }
}
