package com.ootd.be.util.file;

import com.google.common.base.Joiner;
import com.ootd.be.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringJoiner;

@Slf4j
public class FileManager {

    private static final String HOME_PATH = System.getProperty("user.home", "");
    private static final File ROOT_DIR = new File(HOME_PATH + "/DATA");

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");

    public static final FileManager I = new FileManager();

    private FileManager() {
        log.info("file root path >> {}", HOME_PATH);
    }

    public File path(String today, String...path) {
        StringJoiner joiner = new StringJoiner(File.separator);
        joiner.add(today);
        for (String p : path) {
            joiner.add(p);
        }

        File dir = new File(ROOT_DIR, joiner.toString());
        if (!dir.exists()) dir.mkdirs();

        return dir;
    }

    public File today(String...path) {
        return path(DATE_FORMAT.format(new Date()), path);
    }

    public String relativePath(File file) {
        return file.getAbsolutePath().replace(HOME_PATH, ROOT_DIR.getAbsolutePath());
    }

    public enum PathType {
        url("/"), file(File.separator);
        final String separator;

        PathType(String separator) {
            this.separator = separator;
        }
    }

    public static void main(String[] args) {
        File temp = FileManager.I.today("temp");
        log.info("{}", temp.getPath());
        log.info("{}", temp.getAbsolutePath());
        log.info("{}", temp.getAbsoluteFile());
    }

    public String findFileName(PathType pathType, String path) {
        if (path == null) {
            throw new ValidationException("파일이름 오류");
        }

        int folderIndex = path.lastIndexOf(pathType.separator);
        if (folderIndex == -1) {
            return null;
        }

        return path.substring(folderIndex + 1);
    }

    public String findFileExtension(PathType pathType, String path) {
        if (path == null) {
            throw new ValidationException("파일이름 오류");
        }

        int extIndex = path.lastIndexOf(".");
        if (extIndex == -1) {
            return null;
        }

        int folderIndex = path.lastIndexOf(pathType.separator);
        if (folderIndex > extIndex) {
            return null;
        }

        return path.substring(extIndex + 1);
    }

}
