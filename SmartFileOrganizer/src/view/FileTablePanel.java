package view;

import model.FileItem;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.util.List;

/**
 * Reusable panel responsible for displaying file items in a JTable.
 */
public class FileTablePanel extends JPanel {
    private final JTable fileTable;
    private final DefaultTableModel tableModel;

    public FileTablePanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Files"));

        tableModel = new DefaultTableModel(new Object[]{"File Name", "Type", "Size (Bytes)", "Path", "Last Modified"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        fileTable = new JTable(tableModel);
        fileTable.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(fileTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void updateTable(List<FileItem> fileItems) {
        tableModel.setRowCount(0);
        for (FileItem item : fileItems) {
            tableModel.addRow(new Object[]{
                    item.getFileName(),
                    item.getFileType(),
                    item.getFileSize(),
                    item.getFilePath(),
                    item.getLastModified()
            });
        }
    }

    public JTable getFileTable() {
        return fileTable;
    }
}
