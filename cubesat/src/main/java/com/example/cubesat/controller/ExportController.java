package com.example.cubesat.controller;

import com.example.cubesat.model.Device;
import com.example.cubesat.model.FieldRecord;
import com.example.cubesat.repository.DeviceRepository;
import com.example.cubesat.repository.FieldRecordRepository;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.OutputStream;
import java.security.Principal;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/export")
public class ExportController {

    private final DeviceRepository deviceRepository;
    private final FieldRecordRepository fieldRecordRepository;

    @GetMapping("/field-records/pdf")
    public void exportDeviceDataToPDF(@RequestParam String accessToken, HttpServletResponse response, Principal principal) {
        try {
            Device device = deviceRepository.findByAccessToken(accessToken)
                    .orElseThrow(() -> new RuntimeException("Device not found"));

            if (!device.getOwner().getUsername().equals(principal.getName())) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
                return;
            }

            List<FieldRecord> records = fieldRecordRepository.findByFieldDeviceId(device.getId());


            response.setContentType(MediaType.APPLICATION_PDF_VALUE);
            response.setHeader("Content-Disposition", "attachment; filename=field-records.pdf");

            Document document = new Document();
            OutputStream out = response.getOutputStream();
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            document.add(new Paragraph("Field Records for device: " + device.getName(), titleFont));
            document.add(new Paragraph(" "));

            Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
            for (FieldRecord record : records) {
                String line = String.format(
                        "Field: %s | Value: %.2f %s | Time: %s",
                        record.getField().getName(),
                        record.getValue(),
                        record.getField().getUnit(),
                        record.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                );
                document.add(new Paragraph(line, bodyFont));
            }

            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException("PDF creating error", e);
        } catch (IOException e) {
            throw new RuntimeException("IO error", e);
        }

    }
}
