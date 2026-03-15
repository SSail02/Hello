package service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Service class responsible for organizing files by extension.
 */
public class FileOrganizerService {
    private final Map<String, String> extensionCategoryMap;

    public FileOrganizerService() {
        extensionCategoryMap = new HashMap<>();
        loadSupportedCategories();
    }

    private void loadSupportedCategories() {
        // Images
        extensionCategoryMap.put("png", "Images");
        extensionCategoryMap.put("jpg", "Images");
        extensionCategoryMap.put("jpeg", "Images");
        extensionCategoryMap.put("gif", "Images");

        // Documents
        extensionCategoryMap.put("pdf", "Documents");
        extensionCategoryMap.put("doc", "Documents");
        extensionCategoryMap.put("docx", "Documents");
        extensionCategoryMap.put("txt", "Documents");

        // Music
        extensionCategoryMap.put("mp3", "Music");
        extensionCategoryMap.put("wav", "Music");

        // Videos
        extensionCategoryMap.put("mp4", "Videos");
        extensionCategoryMap.put("mkv", "Videos");

        // Archives
        extensionCategoryMap.put("zip", "Archives");
        extensionCategoryMap.put("rar", "Archives");
    }

    /**
     * Organizes files inside the provided directory by moving them into subfolders by type.
     *
     * @param directory root directory selected by the user
     * @throws IOException if file operations fail
     */
    public void organizeFiles(Path directory) throws IOException {
        if (directory == null || !Files.isDirectory(directory)) {
            throw new IllegalArgumentException("Invalid directory path.");
        }

        try (var stream = Files.list(directory)) {
            stream.filter(Files::isRegularFile).forEach(file -> {
                try {
                    String extension = getFileExtension(file.getFileName().toString());
                    String category = extensionCategoryMap.getOrDefault(extension, "Others");

                    Path categoryFolder = directory.resolve(category);
                    if (!Files.exists(categoryFolder)) {
                        Files.createDirectories(categoryFolder);
                    }

                    Path destination = categoryFolder.resolve(file.getFileName());
                    destination = resolveNameConflict(destination);

                    Files.move(file, destination, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException ex) {
                    // Convert checked exception so stream processing can continue.
                    throw new RuntimeException("Failed to process file: " + file.getFileName(), ex);
                }
            });
        } catch (RuntimeException ex) {
            if (ex.getCause() instanceof IOException ioException) {
                throw ioException;
            }
            throw ex;
        }
    }

    public String detectType(String fileName) {
        String ext = getFileExtension(fileName);
        return extensionCategoryMap.getOrDefault(ext, "Others");
    }

    private Path resolveNameConflict(Path destination) throws IOException {
        if (!Files.exists(destination)) {
            return destination;
        }

        String name = destination.getFileName().toString();
        String baseName = name;
        String extension = "";
        int dotIndex = name.lastIndexOf('.');
        if (dotIndex > 0) {
            baseName = name.substring(0, dotIndex);
            extension = name.substring(dotIndex);
        }

        int counter = 1;
        Path parent = destination.getParent();
        Path candidate;
        do {
            candidate = parent.resolve(baseName + "_" + counter + extension);
            counter++;
        } while (Files.exists(candidate));

        return candidate;
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex == -1 || dotIndex == fileName.length() - 1) {
            return "";
        }
        return fileName.substring(dotIndex + 1).toLowerCase(Locale.ROOT);
    }
}
