import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Main extends JFrame {
    private JTextField txtN, txtTime, txtResult;
    private JTable table;
    private DefaultTableModel model;
    private JButton btnAdd, btnDelete, btnClear;
    private final String filename = "data.txt";

    public Main() {
        setTitle("Производительность схемы с сохранением");
        setSize(720, 500);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
        loadDataFromFile(); // Загружаем данные при запуске

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                saveDataToFile(); // Сохраняем данные при закрытии
                dispose();
                System.exit(0);
            }
        });

        setVisible(true);
    }

    private void initComponents() {
        Color bgColor = new Color(45, 45, 45);
        Color fgColor = Color.WHITE;
        getContentPane().setBackground(bgColor);

        JLabel lblN = new JLabel("Число N:");
        JLabel lblTime = new JLabel("Время (сек):");
        JLabel lblResult = new JLabel("Получившееся число:");

        lblN.setForeground(fgColor);
        lblTime.setForeground(fgColor);
        lblResult.setForeground(fgColor);

        txtN = new JTextField(10);
        txtTime = new JTextField(10);
        txtResult = new JTextField(10);

        JPanel inputPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        inputPanel.setBackground(bgColor);
        inputPanel.add(lblN);
        inputPanel.add(lblTime);
        inputPanel.add(lblResult);
        inputPanel.add(new JLabel());
        inputPanel.add(txtN);
        inputPanel.add(txtTime);
        inputPanel.add(txtResult);
        inputPanel.add(new JLabel());

        btnAdd = new JButton("Добавить");
        btnDelete = new JButton("Удалить");
        btnClear = new JButton("Очистить");

        btnAdd.setBackground(new Color(70, 130, 180));
        btnAdd.setForeground(Color.WHITE);
        btnDelete.setBackground(new Color(178, 34, 34));
        btnDelete.setForeground(Color.WHITE);
        btnClear.setBackground(new Color(105, 105, 105));
        btnClear.setForeground(Color.WHITE);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(bgColor);
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);

        String[] columns = {"Число N", "Время (сек)", "Получившееся число"};
        model = new DefaultTableModel(null, columns) {
            public boolean isCellEditable(int row, int column) {
                return true;
            }
        };

        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setBackground(new Color(55, 55, 55));
        table.setForeground(fgColor);

        // --- Обработчики ---
        btnAdd.addActionListener(e -> {
            String NStr = txtN.getText().trim();
            String timeStr = txtTime.getText().trim();
            String resultStr = txtResult.getText().trim();

            if (NStr.isEmpty() || timeStr.isEmpty() || resultStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Заполните все поля");
                return;
            }
            model.addRow(new Object[]{NStr, timeStr, resultStr});
            saveDataToFile(); // сохраняем после добавления
        });

        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                model.removeRow(row);
                saveDataToFile();
            } else {
                JOptionPane.showMessageDialog(this, "Выберите строку для удаления");
            }
        });

        btnClear.addActionListener(e -> {
            txtN.setText("");
            txtTime.setText("");
            txtResult.setText("");
        });

        setLayout(new BorderLayout(10, 10));
        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);
    }

    // --- Загрузка данных из файла ---
    private void loadDataFromFile() {
        File file = new File(filename);
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\t", -1); // Позволяет пустые поля
                if (parts.length == 3) {
                    model.addRow(parts);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // --- Сохранение данных в файл ---
    private void saveDataToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (int i = 0; i < model.getRowCount(); i++) {
                StringBuilder sb = new StringBuilder();
                for (int j = 0; j < 3; j++) {
                    sb.append(String.valueOf(model.getValueAt(i, j)));
                    if (j < 2) sb.append("\t");
                }
                bw.write(sb.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}