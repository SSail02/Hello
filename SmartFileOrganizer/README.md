# Smart File Organizer

A Java Swing desktop application that automatically organizes files in a selected directory by file type using a clean MVC architecture.

## Features

- Select any folder from your system.
- Scan and display all files in a table.
- Organize files into subfolders (`Images`, `Documents`, `Music`, `Videos`, `Archives`, `Others`).
- Rename selected files.
- Delete selected files.
- Refresh file list after operations.

## Tech Stack

- Core Java
- Java Swing
- Java File API (`java.nio.file`, `java.io`)
- MVC (Model-View-Controller)

## Project Structure

```text
SmartFileOrganizer/
│
├── src/
│   ├── model/
│   │   └── FileItem.java
│   ├── controller/
│   │   └── FileController.java
│   ├── service/
│   │   └── FileOrganizerService.java
│   ├── view/
│   │   ├── MainFrame.java
│   │   └── FileTablePanel.java
│   └── Main.java
└── README.md
```

## How It Works

1. Click **Select Folder** to choose a target directory.
2. The file table displays all regular files in that directory.
3. Click **Organize Files** to move files into type-based subfolders.
4. Use **Rename File** and **Delete File** for file management.
5. Click **Refresh** to reload the table.

## Build and Run

From the `SmartFileOrganizer` folder:

```bash
mkdir -p out
javac -d out src/Main.java src/model/*.java src/controller/*.java src/service/*.java src/view/*.java
java -cp out Main
```

## Concepts Demonstrated

- Object-Oriented Programming (classes, encapsulation, separation of concerns)
- Swing GUI programming
- Event handling with action listeners
- Exception handling for robust file operations
- File handling using Java NIO APIs
