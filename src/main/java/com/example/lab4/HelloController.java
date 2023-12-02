package com.example.lab4;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HelloController {
    public TreeView<String> treeView;
    public Button addDict;
    public Button createDict;
    public ChoiceBox<String> changeDictBox;
    public ChoiceBox<String> rightBox;
    public Button change;
    public ChoiceBox<String> fromDictBox;
    public ChoiceBox<String> toDictBox;
    public Button copy;
    public Button deleteDict;
    public ChoiceBox<String> deleteDictBox;

    Map<String, FileExtension> fileMap = new HashMap<>();

    @FXML
    public void initialize() {
        ObservableList<String> levels = FXCollections.observableList(Level.getAll());
        rightBox.setItems(levels);
        File rootDict = new File("C:\\Users\\1\\IdeaProjects\\MBS\\lab4\\src\\main\\resources\\root");
        TreeItem<String> root = new TreeItem<>(rootDict.getName());
        for (File file : rootDict.listFiles()) {
            fileMap.put(file.getName(), new FileExtension(file, Level.NON_SECRET));
        }

        treeView.setRoot(root);

        resetView();
        treeView.setShowRoot(false);

        ObservableList<String> folders = FXCollections.observableList(
                new ArrayList<>(fileMap.keySet())
        );

        changeDictBox.setItems(folders);
        deleteDictBox.setItems(folders);
        fromDictBox.setItems(folders);
        toDictBox.setItems(folders);

        createDict.setOnAction(actionEvent -> {
            File newFile = getNewFile(rootDict, "new_folder");
            if (newFile.mkdir()) {
                fileMap.put(newFile.getName(), new FileExtension(newFile, Level.NON_SECRET));
                resetView();
                folders.add(newFile.getName());
            }
        });

        addDict.setOnAction(actionEvent -> {
            DirectoryChooser chooser = new DirectoryChooser();
            File addFile = chooser.showDialog(new Stage());

            if (fileMap.containsKey(addFile.getName())) {
                FileExtension existFile = fileMap.get(addFile.getName());
                if (existFile.getFile().getAbsolutePath().equals(addFile.getAbsolutePath()))
                    showAlert("Эта папка уже добавлена");
                else {
                    String newName = getNewExistingDictName(addFile);
                    fileMap.put(newName, new FileExtension(addFile, Level.NON_SECRET));
                    folders.add(newName);
                }
            } else {
                fileMap.put(addFile.getName(), new FileExtension(addFile, Level.NON_SECRET));
                folders.add(addFile.getName());
            }
            resetView();
        });

        deleteDict.setOnAction(actionEvent -> {
            String deleteFile = deleteDictBox.getValue();
            if (deleteFile != null) {
                FileExtension file = fileMap.get(deleteFile);
                folders.remove(file.getFile().getName());
                fileMap.remove(file.getFile().getName());
                resetView();
            } else showAlert("Необходимо выбрать папку для удаления");
        });

        change.setOnAction(actionEvent -> {
            String changeFile = changeDictBox.getValue();
            String level = rightBox.getValue();
            if (changeFile != null && level != null) {
                FileExtension file = fileMap.get(changeFile);
                file.setLevel(Level.valueOf(level));
                resetView();
            } else showAlert("Необходимо выбрать папку и/или уровень");
        });

        copy.setOnAction(actionEvent -> {
            String fromFile = fromDictBox.getValue();
            String toFile = toDictBox.getValue();
            if (fromFile != null && toFile != null) {
                FileExtension from = fileMap.get(fromFile);
                FileExtension to = fileMap.get(toFile);

                if (from.getLevel().checkLevel(to.getLevel())) {
                    showAlert("Уровень копируемой папки выше уровня папки, в которую копируют");
                } else {
                    File fileFrom = from.getFile();
                    File[] list = fileFrom.listFiles();
                    if (list != null) {
                        for (File file : list) {
                            try {
                                Files.copy(file.toPath(), getNewFile(to.getFile(), file.getName()).toPath());
                            } catch (IOException e) {
                                showAlert("Произошла ошибка при копировании файлов");
                            }
                        }
                    } else showAlert("В копируемой папке нет файлов");
                }
            } else showAlert("Необходимо выбрать папки");
        });
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private File getNewFile(File root, String name) {
        File newFile = new File(root, name);
        int i = 1;
        while (newFile.exists()) {
            String newName = name + "("+ i++ + ")";
            newFile = new File(root, newName);
        }

        return newFile;
    }

    private String getNewExistingDictName(File file) {
        int i = 1;
        String newName = file.getName();
        while (fileMap.containsKey(newName)) {
            newName = file.getName() + "("+ i++ + ")";
        }

        return newName;
    }

    private String getFileView(String fileName, String level) {
        return fileName + " (" + level + ")";
    }

    private void resetView() {
        treeView.getRoot().getChildren().clear();
        fileMap.forEach((key, value) -> treeView.getRoot().getChildren().add(new TreeItem<>(getFileView(key, value.getLevel().name()))));
    }
}