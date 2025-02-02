package de.mscho.toftws.pdf.service;

import java.util.List;

public record PDFCreationBody(
   List<PDFCreationEntry> entries
) {}
