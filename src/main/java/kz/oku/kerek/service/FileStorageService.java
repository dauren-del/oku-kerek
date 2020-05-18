package kz.oku.kerek.service;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;

public interface FileStorageService {

    void putFile(Path filePath, String fileName) throws IOException;

    byte[] getFile(String key);

    List<String> list(String directory);

    URL generateAccessUrl(String key);

    boolean exists(String key);

}
