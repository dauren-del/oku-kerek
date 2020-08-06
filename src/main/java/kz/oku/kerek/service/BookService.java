package kz.oku.kerek.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

/**
 * This service provides methods to manipulate books
 *
 * @author Dauren Delmukhambetov
 * @since 0.1.0
 */
public interface BookService {

    void downloadPages(Long bookId) throws IOException;

    void generatePdf(Long bookId, Path pdfPath) throws FileNotFoundException;

    String getStatus(Long bookId);

    void setStatus(Long bookId, String status);

}
