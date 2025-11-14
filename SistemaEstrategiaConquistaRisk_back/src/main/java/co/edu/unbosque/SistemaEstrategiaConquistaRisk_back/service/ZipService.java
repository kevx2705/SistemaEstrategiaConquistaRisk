package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.springframework.stereotype.Service;

/**
 * Servicio que gestiona la creación de archivos ZIP.
 * Proporciona funcionalidades para empaquetar archivos en formato ZIP,
 * como informes o documentos generados por el sistema.
 */
@Service
public class ZipService {

    /**
     * Genera un archivo ZIP que contiene un archivo PDF.
     *
     * @param nombreArchivoPDF Nombre que tendrá el archivo PDF dentro del ZIP.
     * @param pdfBytes Arreglo de bytes que representa el contenido del PDF.
     * @return byte[] Arreglo de bytes que representa el archivo ZIP generado.
     * @throws IOException Si ocurre un error durante la creación del ZIP.
     */
    public byte[] generarZipConPDF(String nombreArchivoPDF, byte[] pdfBytes) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(baos);

        ZipEntry entry = new ZipEntry(nombreArchivoPDF);
        zos.putNextEntry(entry);
        zos.write(pdfBytes);
        zos.closeEntry();
        zos.close();

        return baos.toByteArray();
    }
}
