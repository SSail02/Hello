package view;

import controller.FileController;
import model.FileItem;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Main Swing GUI frame for the Smart File Organizer application.
 */
public class MainFrame extends JFrame {
    private final FileController fileController;
    private final FileTablePanel fileTablePanel;

    private final JButton selectFolderButton;
    private final JButton organizeFilesButton;
    private final JButton renameFileButton;
    private final JButton deleteFileButton;
    private final JButton refreshButton;

    private Path currentDirectory;

    public MainFrame() {
        this.fileController = new FileController();

        setTitle("Smart File Organizer");
        setSize(1000, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Top section
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        selectFolderButton = new JButton("Select Folder");
        organizeFilesButton = new JButton("Organize Files");

        topPanel.add(selectFolderButton);
        topPanel.add(organizeFilesButton);

        // Center section
        fileTablePanel = new FileTablePanel();

        // Bottom section
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        renameFileButton = new JButton("Rename File");
        deleteFileButton = new JButton("Delete File");
        refreshButton = new JButton("Refresh");

        bottomPanel.add(renameFileButton);
        bottomPanel.add(deleteFileButton);
        bottomPanel.add(refreshButton);

        add(topPanel, BorderLayout.NORTH);
        add(fileTablePanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        wireEvents();
    }

    private void wireEvents() {
        selectFolderButton.addActionListener(e -> selectFolder());
        organizeFilesButton.addActionListener(e -> organizeFiles());
        renameFileButton.addActionListener(e -> renameSelectedFile());
        deleteFileButton.addActionListener(e -> deleteSelectedFile());
        refreshButton.addActionListener(e -> refreshFileList());
    }

    private void selectFolder() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int option = chooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            currentDirectory = chooser.getSelectedFile().toPath();
            refreshFileList();
        }
    }

    private void organizeFiles() {
        if (currentDirectory == null) {
            showInfo("Please select a folder first.");
            return;
        }

        try {
            fileController.organize(currentDirectory);
            showInfo("Files organized successfully.");
            refreshFileList();
        } catch (Exception ex) {
            showError("Error while organizing files: " + ex.getMessage());
        }
    }

    private void renameSelectedFile() {
        int selectedRow = fileTablePanel.getFileTable().getSelectedRow();
        if (selectedRow < 0) {
            showInfo("Select a file to rename.");
            return;
        }

        String oldPath = (String) fileTablePanel.getFileTable().getValueAt(selectedRow, 3);
        String oldName = (String) fileTablePanel.getFileTable().getValueAt(selectedRow, 0);

        String newName = JOptionPane.showInputDialog(this, "Enter new file name:", oldName);
        if (newName == null || newName.trim().isEmpty()) {
            return;
        }

        try {
            boolean renamed = fileController.renameFile(Paths.get(oldPath), newName);
            if (renamed) {
                showInfo("File renamed successfully.");
                refreshFileList();
            } else {
                showInfo("A file with that name already exists.");
            }
        } catch (Exception ex) {
            showError("Failed to rename file: " + ex.getMessage());
        }
    }

    private void deleteSelectedFile() {
        int selectedRow = fileTablePanel.getFileTable().getSelectedRow();
        if (selectedRow < 0) {
            showInfo("Select a file to delete.");
            return;
        }

        String filePath = (String) fileTablePanel.getFileTable().getValueAt(selectedRow, 3);
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this file?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            boolean deleted = fileController.deleteFile(Paths.get(filePath));
            if (deleted) {
                showInfo("File deleted successfully.");
                refreshFileList();
            } else {
                showInfo("File not found.");
            }
        } catch (Exception ex) {
            showError("Failed to delete file: " + ex.getMessage());
        }
    }

    private void refreshFileList() {
        if (currentDirectory == null) {
            return;
        }

        try {
            List<FileItem> items = fileController.scanDirectory(currentDirectory);
            fileTablePanel.updateTable(items);
        } catch (Exception ex) {
            showError("Unable to load files: " + ex.getMessage());
        }
    }

    private void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Smart File Organizer", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Smart File Organizer - Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void applyLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // Fallback to default look-and-feel when system style is unavailable.
        }
    }

    public static void launch() {
        applyLookAndFeel();
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
