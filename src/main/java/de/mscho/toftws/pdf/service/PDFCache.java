package de.mscho.toftws.pdf.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Service
public class PDFCache {
    @Cacheable("pdf-data")
    public Optional<byte[]> getPDFData(Path path) {
        try {
            return Optional.of(Files.readAllBytes(path));
        } catch (IOException exception) {
            return Optional.empty();
        }
    }
}
