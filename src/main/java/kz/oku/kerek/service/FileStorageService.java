package kz.oku.kerek.service;

import java.net.URL;
import java.nio.file.Path;
import java.util.List;

public interface FileStorageService {

    void putFile(Path filePath, String fileName);

    void deleteByExtensions(String directory, String extention);

    byte[] getFile(String key);

    List<String> list(String directory);

    URL generateAccessUrl(String key);

    boolean exists(String key);

}
