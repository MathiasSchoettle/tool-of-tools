package de.mscho.toftws.pdf.service;

import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PDFImageService {

    private final PDFService pdfService;

    private final PDFCache pdfCache;

    public Optional<String[]> getImages(UUID id, Set<Integer> indices) {

        var path = pdfService.getPDFPath(id);

        return pdfCache.getPDFData(path).map((bytes) -> {
            try (var document = Loader.loadPDF(bytes)) {
                int pageCount = document.getNumberOfPages();

                var renderer = new PDFRenderer(document);
                var filtered = indices.stream().filter(index -> index > 0 && index < pageCount).toList();
                String[] images = new String[filtered.size()];

                for (int i = 0; i < filtered.size(); i++) {
                    var index = filtered.get(i);
                    var page = renderer.renderImage(index);
                    var imageBytes = getBytes(page);
                    images[i] = Base64.getEncoder().encodeToString(imageBytes);
                }

                return images;

            } catch (IOException exception) {
                return null;
            }
        });
    }

    private byte[] getBytes(BufferedImage image) throws IOException {
        var byteStream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", byteStream);
        return byteStream.toByteArray();
    }
}
