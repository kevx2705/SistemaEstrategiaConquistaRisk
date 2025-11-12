package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.service;

import java.io.ByteArrayOutputStream;
import java.time.Duration;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.entity.Jugador;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.entity.Partida;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.estrucutres.MyLinkedList;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

@Service
public class PDFService {

    public byte[] generarPDFPartida(Partida partida, MyLinkedList<Jugador> jugadores) throws Exception {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.LETTER);
        document.addPage(page);

        PDPageContentStream content = new PDPageContentStream(document, page);
        float y = 750;
        float margin = 50;

        // ------------------------------
        // Encabezado rojo
        // ------------------------------
        content.setNonStrokingColor(200, 30, 30);
        content.addRect(margin, y - 30, page.getMediaBox().getWidth() - 2 * margin, 30);
        content.fill();

        content.beginText();
        content.setNonStrokingColor(255, 255, 255);
        content.setFont(PDType1Font.HELVETICA_BOLD, 18);
        content.newLineAtOffset(margin + 10, y - 22);
        content.showText("REPORTE FINAL DE PARTIDA - RISK");
        content.endText();

        y -= 60;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        // ------------------------------
        // Datos básicos
        // ------------------------------
        content.beginText();
        content.setNonStrokingColor(0, 0, 0);
        content.setFont(PDType1Font.HELVETICA, 12);
        content.newLineAtOffset(margin, y);
        content.showText("Código de partida: " + partida.getCodigoHash());
        content.newLineAtOffset(0, -15);
        content.showText("Fecha inicio: " + partida.getFechaInicio().format(formatter));
        content.newLineAtOffset(0, -15);
        content.showText("Fecha fin: " + partida.getFechaFin().format(formatter));

        Duration duracion = Duration.between(partida.getFechaInicio(), partida.getFechaFin());
        long horas = duracion.toHours();
        long minutos = duracion.toMinutes() % 60;
        content.newLineAtOffset(0, -15);
        content.showText("Duración: " + horas + "h " + minutos + "m");

        content.endText();
        y -= 75;

        // ------------------------------
        // Ganador
        // ------------------------------
        Jugador ganador = null;
        for (int i = 0; i < jugadores.size(); i++) {
            Jugador j = jugadores.getPos(i).getInfo();
            if (j.getId().equals(partida.getGanadorId())) {
                ganador = j;
                break;
            }
        }

        if (ganador != null) {
            content.setNonStrokingColor(255, 230, 230);
            content.addRect(margin, y - 60, page.getMediaBox().getWidth() - 2 * margin, 60);
            content.fill();

            content.beginText();
            content.setNonStrokingColor(0, 0, 0);
            content.setFont(PDType1Font.HELVETICA_BOLD, 14);
            content.newLineAtOffset(margin + 5, y - 20);
            content.showText("Jugador Ganador");
            content.endText();

            y -= 35;

            content.beginText();
            content.setFont(PDType1Font.HELVETICA, 12);
            content.newLineAtOffset(margin + 10, y);
            content.showText("Nombre: " + ganador.getNombre());
            content.newLineAtOffset(0, -15);
            content.showText("Color: " + ganador.getColor());
            content.newLineAtOffset(0, -15);
            content.showText("Tropas al finalizar: " + ganador.getTropasDisponibles());
            content.endText();

            y -= 70;
        }

        // ------------------------------
        // Resumen por jugador
        // ------------------------------
        content.beginText();
        content.setFont(PDType1Font.HELVETICA_BOLD, 14);
        content.newLineAtOffset(margin, y);
        content.showText("Resumen por Jugador");
        content.endText();

        y -= 20;

        for (int i = 0; i < jugadores.size(); i++) {
            Jugador j = jugadores.getPos(i).getInfo();
            if (ganador != null && j.getId().equals(ganador.getId())) continue;

            content.setNonStrokingColor(245, 245, 245);
            content.addRect(margin, y - 50, page.getMediaBox().getWidth() - 2 * margin, 50);
            content.fill();

            content.beginText();
            content.setNonStrokingColor(0, 0, 0);
            content.setFont(PDType1Font.HELVETICA_BOLD, 12);
            content.newLineAtOffset(margin + 5, y - 15);
            content.showText("Jugador: " + j.getNombre());
            content.endText();

            content.beginText();
            content.setFont(PDType1Font.HELVETICA, 12);
            content.newLineAtOffset(margin + 10, y - 30);
            content.showText("Color: " + j.getColor());
            content.newLineAtOffset(0, -15);
            content.endText();

            y -= 65;

            if (y < 100) {
                content.close();
                page = new PDPage(PDRectangle.LETTER);
                document.addPage(page);
                content = new PDPageContentStream(document, page);
                y = 750;
            }
        }

        // ------------------------------
        // Observaciones
        // ------------------------------
        if (y < 100) { // espacio insuficiente
            content.close();
            page = new PDPage(PDRectangle.LETTER);
            document.addPage(page);
            content = new PDPageContentStream(document, page);
            y = 750;
        }
        content.beginText();
        content.setFont(PDType1Font.HELVETICA_BOLD, 14);
        content.newLineAtOffset(margin, y);
        content.showText("Observaciones de la Partida");
        content.endText();

        y -= 20;

        content.beginText();
        content.setFont(PDType1Font.HELVETICA, 12);
        content.newLineAtOffset(margin, y);
        content.showText("La partida se finalizó correctamente. Revisa los detalles de cada jugador.");
        content.endText();

        content.close();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        document.save(baos);
        document.close();

        return baos.toByteArray();
    }
}
