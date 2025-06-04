package com.mycompany.mavenproject3;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class CustomerForm extends JFrame {
    private DefaultTableModel tableModel;
    private JTable customerTable;

    private final String DATA_FILE = "customer_data.csv";

    public CustomerForm() {
        setTitle("Data Customer");
        setSize(600, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

      
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel nameLabel = new JLabel("Nama:");
        JTextField nameField = new JTextField();

        JLabel genderLabel = new JLabel("Kelamin:");
        String[] genders = {"Laki-laki", "Perempuan"};
        JComboBox<String> genderCombo = new JComboBox<>(genders);

        JLabel addressLabel = new JLabel("Alamat:");
        JTextField addressField = new JTextField();

        JLabel ageLabel = new JLabel("Umur:");
        JTextField ageField = new JTextField();

        JButton saveButton = new JButton("Simpan");

        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(genderLabel);
        formPanel.add(genderCombo);
        formPanel.add(addressLabel);
        formPanel.add(addressField);
        formPanel.add(ageLabel);
        formPanel.add(ageField);
        formPanel.add(new JLabel());
        formPanel.add(saveButton);

        add(formPanel, BorderLayout.NORTH);

       
        String[] columnNames = {"Nama", "Kelamin", "Alamat", "Umur"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        customerTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(customerTable);
        add(scrollPane, BorderLayout.CENTER);

       
        JPanel buttonPanel = new JPanel();

        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Hapus");

        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        add(buttonPanel, BorderLayout.SOUTH);

        
        loadDataFromFile();

        
        saveButton.addActionListener(e -> {
            String nama = nameField.getText().trim();
            String kelamin = (String) genderCombo.getSelectedItem();
            String alamat = addressField.getText().trim();
            String umurText = ageField.getText().trim();

            if (nama.isEmpty() || alamat.isEmpty() || umurText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Semua field harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int umur;
            try {
                umur = Integer.parseInt(umurText);
                if (umur < 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Umur harus berupa angka positif!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Object[] row = {nama, kelamin, alamat, umur};
            tableModel.addRow(row);

            nameField.setText("");
            genderCombo.setSelectedIndex(0);
            addressField.setText("");
            ageField.setText("");
        });

        
        deleteButton.addActionListener(e -> {
            int selectedRow = customerTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Pilih data customer yang ingin dihapus.", "Info", JOptionPane.PLAIN_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                tableModel.removeRow(selectedRow);
            }
        });

       
        editButton.addActionListener(e -> {
            int selectedRow = customerTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Pilih data customer yang ingin diedit.", "Info", JOptionPane.PLAIN_MESSAGE);
                return;
            }

            String currentName = (String) tableModel.getValueAt(selectedRow, 0);
            String currentGender = (String) tableModel.getValueAt(selectedRow, 1);
            String currentAddress = (String) tableModel.getValueAt(selectedRow, 2);
            int currentAge = (int) tableModel.getValueAt(selectedRow, 3);

            JTextField editName = new JTextField(currentName);
            JComboBox<String> editGender = new JComboBox<>(genders);
            editGender.setSelectedItem(currentGender);
            JTextField editAddress = new JTextField(currentAddress);
            JTextField editAge = new JTextField(String.valueOf(currentAge));

            JPanel editPanel = new JPanel(new GridLayout(4, 2, 10, 10));
            editPanel.add(new JLabel("Nama:"));
            editPanel.add(editName);
            editPanel.add(new JLabel("Kelamin:"));
            editPanel.add(editGender);
            editPanel.add(new JLabel("Alamat:"));
            editPanel.add(editAddress);
            editPanel.add(new JLabel("Umur:"));
            editPanel.add(editAge);

            int result = JOptionPane.showConfirmDialog(this, editPanel, "Edit Data Customer", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                String newName = editName.getText().trim();
                String newGender = (String) editGender.getSelectedItem();
                String newAddress = editAddress.getText().trim();
                String newAgeText = editAge.getText().trim();

                if (newName.isEmpty() || newAddress.isEmpty() || newAgeText.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Semua field harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int newAge;
                try {
                    newAge = Integer.parseInt(newAgeText);
                    if (newAge < 0) throw new NumberFormatException();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Umur harus berupa angka positif!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                tableModel.setValueAt(newName, selectedRow, 0);
                tableModel.setValueAt(newGender, selectedRow, 1);
                tableModel.setValueAt(newAddress, selectedRow, 2);
                tableModel.setValueAt(newAge, selectedRow, 3);
            }
        });

       
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                saveDataToFile();
                dispose();
            }
        });
    }

    private void loadDataFromFile() {
        File file = new File(DATA_FILE);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
               
                String[] parts = line.split(",", -1);
                if (parts.length == 4) {
                    String nama = parts[0];
                    String kelamin = parts[1];
                    String alamat = parts[2];
                    int umur = Integer.parseInt(parts[3]);
                    Object[] row = {nama, kelamin, alamat, umur};
                    tableModel.addRow(row);
                }
            }
        } catch (IOException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Gagal load data dari file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveDataToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DATA_FILE))) {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String nama = tableModel.getValueAt(i, 0).toString();
                String kelamin = tableModel.getValueAt(i, 1).toString();
                String alamat = tableModel.getValueAt(i, 2).toString();
                String umur = tableModel.getValueAt(i, 3).toString();

                String line = String.join(",", nama, kelamin, alamat, umur);
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan data ke file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
