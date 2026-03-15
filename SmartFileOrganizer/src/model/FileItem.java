package model;

/**
 * Model class representing a file displayed in the application.
 * Demonstrates encapsulation with private fields + getters/setters.
 */
public class FileItem {
    private String fileName;
    private String filePath;
    private String fileType;
    private long fileSize;
    private String lastModified;

    public FileItem() {
    }

    public FileItem(String fileName, String filePath, String fileType, long fileSize, String lastModified) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.lastModified = lastModified;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }
}
