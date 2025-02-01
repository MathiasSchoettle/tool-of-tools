package de.mscho.toftws.pdf.controller;

import de.mscho.toftws.pdf.service.PDFImageService;
import de.mscho.toftws.pdf.service.PDFService;
import de.mscho.toftws.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("pdf")
@RequiredArgsConstructor
public class PDFController {

    private final PDFService pdfService;

    private final PDFImageService imageService;

    @PostMapping("upload")
    public ApiResponse uploadPDF(InputStream dataStream) throws IOException {
        byte[] bytes = dataStream.readAllBytes();

        return pdfService.storePDF(bytes)
                .map(ApiResponse::success)
                .orElseGet(() -> ApiResponse.error("Error during file upload"));
    }

    @GetMapping
    public ApiResponse getImages(@RequestParam UUID id, @RequestParam Set<Integer> indices) {
        return imageService.getImages(id, indices)
                .map(ApiResponse::success)
                .orElseGet(() -> ApiResponse.error("Error during image fetching"));
    }
}
