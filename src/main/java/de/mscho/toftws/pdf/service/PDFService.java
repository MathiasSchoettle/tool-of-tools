package de.mscho.toftws.pdf.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PDFService {

    private static final String TEMP_DIR = "toft";

    private final Logger logger;

    public Optional<UUID> storePDF(byte[] data) {
        var uuid = UUID.nameUUIDFromBytes(data);
        var path = getPDFPath(uuid);

        try {
            Files.createDirectories(path.getParent());
            Files.write(path, data, StandardOpenOption.CREATE);
            logger.info("Created PDF at {}", path);
        } catch (IOException exception) {
            return Optional.empty();
        }

        return Optional.of(uuid);
    }

    public Path getPDFPath(UUID id) {
        var systemTempDir = System.getProperty("java.io.tmpdir");
        return Paths.get(systemTempDir, TEMP_DIR, id + ".pdf");
    }
}
