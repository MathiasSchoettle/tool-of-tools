package de.mscho.toftws.pdf.service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.utils.PdfMerger;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PDFService {

    private final PDFCache pdfCache;

    private final Logger logger;

    private static final String TEMP_DIR = "toft";

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

    public Optional<byte[]> createPDF(UUID id, List<PDFCreationEntry> entries) {
        var path = getPDFPath(id);
        var pdfData = pdfCache.getPDFData(path);

        if (pdfData.isEmpty()) {
            return Optional.empty();
        }

        var data = pdfData.get();
        var outputStream = new ByteArrayOutputStream();

        try (
                var reader = new PdfReader(new ByteArrayInputStream(data));
                var writer = new PdfWriter(outputStream);
                var source = new PdfDocument(reader);
                var destination = new PdfDocument(writer)
        ) {
            var merger = new PdfMerger(destination);

            for (var entry : entries) {
                int index = entry.pageIndex() + 1;
                merger.merge(source, index, index);
            }
        } catch (IOException e) {
            return Optional.empty();
        }

        return Optional.of(outputStream.toByteArray());
    }

    public Path getPDFPath(UUID id) {
        var systemTempDir = System.getProperty("java.io.tmpdir");
        return Paths.get(systemTempDir, TEMP_DIR, id + ".pdf");
    }
}
