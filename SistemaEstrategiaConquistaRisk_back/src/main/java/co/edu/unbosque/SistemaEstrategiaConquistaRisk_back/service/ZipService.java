package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.stereotype.Service;

@Service
public class ZipService {

    public byte[] generarZipConPDF(String nombreArchivoPDF, byte[] pdfBytes) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(baos);

        // Creamos la entrada del PDF dentro del ZIP
        ZipEntry entry = new ZipEntry(nombreArchivoPDF);
        zos.putNextEntry(entry);
        zos.write(pdfBytes);
        zos.closeEntry();

        zos.close();
        return baos.toByteArray();
    }
}
