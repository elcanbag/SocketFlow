package com.example.cubesat.service;

import com.example.cubesat.model.FieldRecord;
import com.example.cubesat.repository.FieldRecordRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.opencsv.CSVWriter;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExportService {

    private final FieldRecordRepository fieldRecordRepository;
    private final ObjectMapper objectMapper;


    public void export(HttpServletResponse response,
                       String format,
                       Long deviceId,
                       LocalDateTime start,
                       LocalDateTime end) throws IOException {


        List<FieldRecord> records = fieldRecordRepository.findByFieldDeviceId(deviceId);


        if (start != null && end != null) {
            records = records.stream()
                    .filter(r -> !r.getTimestamp().isBefore(start) && !r.getTimestamp().isAfter(end))
                    .collect(Collectors.toList());
        }


        switch (format.toLowerCase()) {
            case "pdf"  -> exportToPDF(response, records);
            case "xlsx" -> exportToExcel(response, records);
            case "csv"  -> exportToCSV(response, records);
            case "json" -> exportToJSON(response, records);
            default     -> throw new IllegalArgumentException("Unsupported format: " + format);
        }
    }

    private void exportToPDF(HttpServletResponse response, List<FieldRecord> records) throws IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=data.pdf");

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        document.add(new Paragraph("Field Records Export"));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(4);
        table.addCell("ID");
        table.addCell("Field ID");
        table.addCell("Timestamp");
        table.addCell("Value");

        for (FieldRecord r : records) {
            table.addCell(r.getId().toString());
            table.addCell(r.getField().getId().toString());
            table.addCell(r.getTimestamp().toString());
            table.addCell(Double.toString(r.getValue()));
        }

        document.add(table);
        document.close();
    }

    private void exportToExcel(HttpServletResponse response, List<FieldRecord> records) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=data.xlsx");

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Field Records");
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("ID");
            header.createCell(1).setCellValue("Field ID");
            header.createCell(2).setCellValue("Timestamp");
            header.createCell(3).setCellValue("Value");

            int rowNum = 1;
            for (FieldRecord r : records) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(r.getId());
                row.createCell(1).setCellValue(r.getField().getId());
                row.createCell(2).setCellValue(r.getTimestamp().toString());
                row.createCell(3).setCellValue(r.getValue());
            }

            workbook.write(response.getOutputStream());
        }
    }

    private void exportToCSV(HttpServletResponse response, List<FieldRecord> records) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=data.csv");

        try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(response.getOutputStream()))) {
            writer.writeNext(new String[]{"ID", "Field ID", "Timestamp", "Value"});
            for (FieldRecord r : records) {
                writer.writeNext(new String[]{
                        r.getId().toString(),
                        r.getField().getId().toString(),
                        r.getTimestamp().toString(),
                        Double.toString(r.getValue())
                });
            }
        }
    }

    private void exportToJSON(HttpServletResponse response, List<FieldRecord> records) throws IOException {
        response.setContentType("application/json");
        response.setHeader("Content-Disposition", "attachment; filename=data.json");
        objectMapper.writeValue(response.getOutputStream(), records);
    }
}
