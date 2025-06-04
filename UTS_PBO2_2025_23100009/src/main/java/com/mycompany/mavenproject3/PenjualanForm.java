package com.mycompany.mavenproject3;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class PenjualanForm extends JFrame {

    private JTable salesTable;
    private DefaultTableModel tableModel;

    public PenjualanForm(List<Penjualan> penjualanList) {
        setTitle("Kelola Penjualan");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

       
        String[] columnNames = {"ID", "Kode Produk", "Nama Produk", "Jumlah", "Total Harga"};

       
        tableModel = new DefaultTableModel(columnNames, 0);
        salesTable = new JTable(tableModel);

        
        for (Penjualan p : penjualanList) {
            Object[] rowData = {
                p.getId(),
                p.getKodeProduk(),
                p.getNamaProduk(),
                p.getJumlah(),
                p.getTotalHarga()
            };
            tableModel.addRow(rowData);
        }

        add(new JScrollPane(salesTable), BorderLayout.CENTER);
    }
}
