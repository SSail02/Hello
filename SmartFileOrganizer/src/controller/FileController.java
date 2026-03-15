package controller;

import model.FileItem;
import service.FileOrganizerService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Controller class that coordinates data between view and service/model layers.
 */
public class FileController {
    private final FileOrganizerService organizerService;

    public FileController() {
        this.organizerService = new FileOrganizerService();
    }

    public List<FileItem> scanDirectory(Path directory) throws IOException {
        if (directory == null || !Files.isDirectory(directory)) {
            throw new IllegalArgumentException("Please select a valid folder.");
        }

        List<FileItem> fileItems = new ArrayList<>();
        try (var files = Files.list(directory)) {
            files.filter(Files::isRegularFile).forEach(path -> {
                try {
                    BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
                    String formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                            .format(new Date(attrs.lastModifiedTime().toMillis()));

                    fileItems.add(new FileItem(
                            path.getFileName().toString(),
                            path.toAbsolutePath().toString(),
                            organizerService.detectType(path.getFileName().toString()),
                            attrs.size(),
                            formattedDate
                    ));
                } catch (IOException e) {
                    throw new RuntimeException("Error reading file attributes for " + path.getFileName(), e);
                }
            });
        } catch (RuntimeException ex) {
            if (ex.getCause() instanceof IOException ioException) {
                throw ioException;
            }
            throw ex;
        }

        return fileItems;
    }

    public void organize(Path directory) throws IOException {
        organizerService.organizeFiles(directory);
    }

    public boolean renameFile(Path filePath, String newName) throws IOException {
        if (filePath == null || newName == null || newName.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid file name.");
        }

        Path target = filePath.resolveSibling(newName.trim());
        if (Files.exists(target)) {
            return false;
        }

        Files.move(filePath, target);
        return true;
    }

    public boolean deleteFile(Path filePath) throws IOException {
        if (filePath == null || !Files.exists(filePath)) {
            return false;
        }
        return Files.deleteIfExists(filePath);
    }
}
