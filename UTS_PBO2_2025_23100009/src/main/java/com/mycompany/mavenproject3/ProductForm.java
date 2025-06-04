package com.mycompany.mavenproject3;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.BorderFactory;
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

public class ProductForm extends JFrame {
    private JTable drinkTable;
    private DefaultTableModel tableModel;
    private JTextField codeField, nameField, priceField, stockField;
    private JComboBox<String> categoryField;
    private JButton addButton, editButton, deleteButton;

    private List<Product> products;
    private Mavenproject3 parent;

    public ProductForm(List<Product> products, Mavenproject3 parent) {
        this.products = products;
        this.parent = parent;

        setTitle("WK. Cuan | Stok Barang");
        setSize(800, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Form Panel
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("Form Produk"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Kode Barang
        gbc.gridx = 0; gbc.gridy = 0;
        leftPanel.add(new JLabel("Kode Barang:"), gbc);
        gbc.gridx = 1;
        codeField = new JTextField();
        leftPanel.add(codeField, gbc);

        // Nama Barang
        gbc.gridx = 0; gbc.gridy++;
        leftPanel.add(new JLabel("Nama Barang:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField();
        leftPanel.add(nameField, gbc);

        // Kategori
        gbc.gridx = 0; gbc.gridy++;
        leftPanel.add(new JLabel("Kategori:"), gbc);
        gbc.gridx = 1;
        categoryField = new JComboBox<>(new String[]{"Coffee", "Dairy", "Juice", "Soda", "Tea"});
        leftPanel.add(categoryField, gbc);

        // Harga Jual
        gbc.gridx = 0; gbc.gridy++;
        leftPanel.add(new JLabel("Harga Jual:"), gbc);
        gbc.gridx = 1;
        priceField = new JTextField();
        leftPanel.add(priceField, gbc);

        // Stok Tersedia
        gbc.gridx = 0; gbc.gridy++;
        leftPanel.add(new JLabel("Stok Tersedia:"), gbc);
        gbc.gridx = 1;
        stockField = new JTextField();
        leftPanel.add(stockField, gbc);

        // Tombol
        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Tambah");
        editButton = new JButton("Edit");
        deleteButton = new JButton("Hapus");
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        leftPanel.add(buttonPanel, gbc);

        add(leftPanel, BorderLayout.WEST);

        // Tabel Produk
       tableModel = new DefaultTableModel(new String[]{"Kode", "Nama", "Kategori", "Harga", "Stok"}, 0) {
     @Override
        public boolean isCellEditable(int row, int column) {
        return false; // semua sel tidak bisa diedit langsung
    }
};
        drinkTable = new JTable(tableModel);
        loadProductData();
        JScrollPane scrollPane = new JScrollPane(drinkTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Daftar Produk"));
        add(scrollPane, BorderLayout.CENTER);

        // Event listeners
        addButton.addActionListener(e -> tambahProduk());
        editButton.addActionListener(e -> editProduk());
        deleteButton.addActionListener(e -> hapusProduk());
        drinkTable.getSelectionModel().addListSelectionListener(e -> isiFormDariTabel());
    }

    private void loadProductData() {
        tableModel.setRowCount(0);
        for (Product p : products) {
            tableModel.addRow(new Object[]{
                p.getCode(), p.getName(), p.getCategory(), p.getPrice(), p.getStock()
            });
        }
    }

    private void tambahProduk() {
        String code = codeField.getText().trim();
        if (code.isEmpty() || nameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Kode dan nama produk tidak boleh kosong.");
            return;
        }

        for (Product existing : products) {
            if (existing.getCode().equalsIgnoreCase(code)) {
                JOptionPane.showMessageDialog(this, "Kode produk sudah ada.");
                return;
            }
        }

        try {
            Product p = new Product(
                products.size() + 1,
                code,
                nameField.getText().trim(),
                (String) categoryField.getSelectedItem(),
                Double.parseDouble(priceField.getText()),
                Integer.parseInt(stockField.getText())
            );
            products.add(p);
            loadProductData();
            clearForm();
            parent.refreshBanner();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Harga dan stok harus berupa angka.");
        }
    }

    private void editProduk() {
        int selectedRow = drinkTable.getSelectedRow();
        if (selectedRow >= 0) {
            Product p = products.get(selectedRow);
            String newCode = codeField.getText().trim();
            if (newCode.isEmpty() || nameField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Kode dan nama produk tidak boleh kosong.");
                return;
            }

            for (int i = 0; i < products.size(); i++) {
                if (i != selectedRow && products.get(i).getCode().equalsIgnoreCase(newCode)) {
                    JOptionPane.showMessageDialog(this, "Kode produk sudah digunakan di entri lain.");
                    return;
                }
            }

            p.setCode(newCode);
            p.setName(nameField.getText().trim());
            p.setCategory((String) categoryField.getSelectedItem());
            try {
                p.setPrice(Double.parseDouble(priceField.getText()));
                p.setStock(Integer.parseInt(stockField.getText()));
                loadProductData();
                clearForm();
                parent.refreshBanner();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Harga dan stok harus berupa angka.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Pilih produk yang ingin diedit.");
        }
    }

    private void hapusProduk() {
        int selectedRow = drinkTable.getSelectedRow();
        if (selectedRow >= 0) {
            int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus produk ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                products.remove(selectedRow);
                loadProductData();
                clearForm();
                parent.refreshBanner();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Pilih produk yang ingin dihapus.");
        }
    }

    private void isiFormDariTabel() {
        int row = drinkTable.getSelectedRow();
        if (row >= 0) {
            Product p = products.get(row);
            codeField.setText(p.getCode());
            nameField.setText(p.getName());
            categoryField.setSelectedItem(p.getCategory());
            priceField.setText(String.valueOf(p.getPrice()));
            stockField.setText(String.valueOf(p.getStock()));
        }
    }

    private void clearForm() {
        codeField.setText("");
        nameField.setText("");
        categoryField.setSelectedIndex(0);
        priceField.setText("");
        stockField.setText("");
        drinkTable.clearSelection();
    }
}
