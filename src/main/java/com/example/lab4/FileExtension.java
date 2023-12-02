package com.example.lab4;

import java.io.File;
import java.util.Objects;

public class FileExtension {

    private File file;
    private Level level;

    public FileExtension(File file, Level level) {
        this.file = file;
        this.level = level;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileExtension that = (FileExtension) o;
        return Objects.equals(file, that.file) && Objects.equals(level, that.level);
    }

    @Override
    public int hashCode() {
        return Objects.hash(file, level);
    }
}
