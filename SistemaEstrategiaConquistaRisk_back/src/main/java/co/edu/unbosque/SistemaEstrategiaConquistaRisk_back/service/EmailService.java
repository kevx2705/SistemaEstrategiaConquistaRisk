package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.entity.Jugador;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.entity.Partida;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.estrucutres.MyLinkedList;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

/**
 * Servicio para el envío de correos electrónicos relacionados con el juego.
 * Proporciona funcionalidades para enviar correos de bienvenida, resultados de partida, y notificaciones.
 */
@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PDFService reportePdfService;

    /**
     * Envía un correo de bienvenida en formato HTML al jugador registrado.
     *
     * @param destinatario Correo electrónico del destinatario.
     * @param nombreJugador Nombre del jugador registrado.
     * @throws RuntimeException Si ocurre un error al enviar el correo.
     */
    public void enviarCorreoRegistroHTML(String destinatario, String nombreJugador) {
        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");
            helper.setTo(destinatario);
            helper.setSubject("🎉 ¡Bienvenido a Estrategia de Conquista: Risk!");
            String html = generarHtmlBienvenida(nombreJugador);
            helper.setText(html, true);
            FileSystemResource logo = new FileSystemResource("src/main/java/logoRisk.jpg");
            helper.addInline("logoRisk", logo);
            mailSender.send(mensaje);
        } catch (MessagingException e) {
            throw new RuntimeException("Error enviando correo HTML: " + e.getMessage());
        }
    }

    /**
     * Envía un correo con los resultados de una partida finalizada, incluyendo un PDF adjunto.
     *
     * @param email Correo electrónico del destinatario.
     * @param partida Objeto {@link Partida} con la información de la partida finalizada.
     * @param jugadores Lista enlazada de jugadores que participaron en la partida.
     * @throws Exception Si ocurre un error al generar el PDF o enviar el correo.
     */
    public void enviarCorreoFinalPartida(String email, Partida partida, MyLinkedList<Jugador> jugadores)
            throws Exception {
        byte[] pdfBytes = reportePdfService.generarPDFPartida(partida, jugadores);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(email);
        helper.setSubject("🎉 Resultados de la Partida – Estrategia de Conquista: RISK");
        helper.setText(crearHtmlCorreoFinal(partida), true);
        helper.addAttachment("Resultados_" + partida.getCodigoHash() + ".pdf", new ByteArrayResource(pdfBytes));
        mailSender.send(message);
    }

    /**
     * Genera el contenido HTML para el correo de resultados de partida.
     *
     * @param partida Objeto {@link Partida} con la información de la partida.
     * @return String con el HTML del correo.
     */
    private String crearHtmlCorreoFinal(Partida partida) {
        String logoUrl = "https://fbi.cults3d.com/uploaders/15076709/illustration-file/c76fe09d-7cec-413f-8bfd-1d3ec6464e91/11.jpg";
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append("<body style=\"font-family: Arial, sans-serif; background-color: #ffffff; padding: 20px;\">");
        sb.append(
                "<div style=\"max-width: 600px; margin: 0 auto; border: 2px solid #cc0000; border-radius: 10px; padding: 20px;\">");
        sb.append("<div style=\"text-align: center;\">");
        sb.append("<img src=\"").append(logoUrl)
                .append("\" alt=\"Logo Risk\" style=\"width:180px; border-radius:8px; margin-bottom: 10px;\">");
        sb.append("<h1 style=\"color:#cc0000; font-size: 28px; margin-top: 5px;\">Estrategia de Conquista: RISK</h1>");
        sb.append("<h2 style=\"color:#222222; font-size:22px;\">¡La partida ha finalizado!</h2>");
        sb.append("</div>");
        sb.append("<p style=\"font-size:16px; color:#333;\">");
        sb.append("Aquí tienes un resumen de la partida <b>").append(partida.getCodigoHash()).append("</b>. ");
        sb.append("En el archivo PDF adjunto encontrarás el informe completo.");
        sb.append("</p>");
        sb.append(
                "<div style=\"background:#ffecec; border-left: 5px solid #cc0000; padding:12px; margin: 20px 0; font-size:15px;\">");
        sb.append("<p><b>Fecha de inicio:</b> ").append(partida.getFechaInicio()).append("</p>");
        sb.append("<p><b>Fecha de finalización:</b> ").append(partida.getFechaFin()).append("</p>");
        sb.append("<p><b>Ganador ID:</b> ").append(partida.getGanadorId()).append("</p>");
        sb.append("<p><b>Jugador actual al finalizar:</b> ").append(partida.getJugadorActualId()).append("</p>");
        sb.append("</div>");
        sb.append("<p style=\"font-size:15px; color:#444;\">Gracias por jugar con nosotros. ");
        sb.append("Si deseas revisar las reglas oficiales del juego, puedes hacerlo aquí:</p>");
        sb.append("<div style=\"text-align:center; margin-top: 20px;\">");
        sb.append("<a href=\"https://www.hasbro.com/common/instruct/risk.pdf\" ");
        sb.append("style=\"background:#cc0000; color:white; padding:12px 20px; border-radius:6px; ");
        sb.append("text-decoration:none; font-size:16px; display:inline-block;\">");
        sb.append("📘 Ver Reglas de RISK</a>");
        sb.append("</div>");
        sb.append("<p style=\"margin-top:30px; font-size:14px; color:#888; text-align:center;\">");
        sb.append("Este correo es generado automáticamente por el sistema Estrategia de Conquista: RISK.");
        sb.append("</p>");
        sb.append("</div>");
        sb.append("</body>");
        sb.append("</html>");
        return sb.toString();
    }

    /**
     * Genera el contenido HTML para el correo de bienvenida al jugador.
     *
     * @param nombreJugador Nombre del jugador registrado.
     * @return String con el HTML del correo de bienvenida.
     */
    private String generarHtmlBienvenida(String nombreJugador) {
        String html = """
                <html>
                  <body style="font-family: Arial, sans-serif; background-color: #f5f5f5; padding: 20px;">
                    <div style="max-width: 600px; margin: auto; background: #ffffff;
                                border-radius: 10px; padding: 30px; border: 1px solid #cc0000;">
                      <!-- LOGO -->
                      <div style="text-align: center; margin-bottom: 25px;">
                    <img src="cid:logoRisk" alt="Logo Risk" style="width: 170px; border-radius: 8px; border: 2px solid #cc0000;">
                      </div>
                      <!-- TITULO PRINCIPAL -->
                      <h2 style="color: #cc0000; text-align: center; margin-bottom: 10px;">
                        ¡Bienvenido, NOMBRE_JUGADOR!
                      </h2>
                      <h3 style="color: #333; text-align: center; margin-top: 0;">
                        Estrategia de Conquista: Risk
                      </h3>
                      <!-- TEXTO PRINCIPAL -->
                      <p style="font-size: 16px; color: #444; line-height: 1.6;">
                        Gracias por registrarte en <b style="color: #cc0000;">Estrategia de Conquista: Risk</b>.
                        Ahora formas parte de un emocionante mundo de táctica, estrategia y dominio global.
                      </p>
                      <p style="font-size: 16px; color: #444; line-height: 1.6;">
                        Desde este momento podrás:
                      </p>
                      <!-- LISTA DE BENEFICIOS -->
                      <ul style="font-size: 16px; color: #333; padding-left: 20px;">
                        <li>✅ Crear partidas</li>
                        <li>✅ Liderar ejércitos y conquistar territorios</li>
                        <li>✅ Competir con otros jugadores en estrategia real</li>
                      </ul>
                      <!-- BOTÓN -->
                      <div style="text-align: center; margin-top: 30px;">
                        <a href="URL_REGLAS"
                           style="
                             background-color: #cc0000;
                             color: white;
                             padding: 14px 24px;
                             font-size: 17px;
                             border-radius: 6px;
                             text-decoration: none;
                             font-weight: bold;
                             display: inline-block;
                           ">
                          Ver las reglas
                        </a>
                      </div>
                      <!-- PIE DE PÁGINA -->
                      <p style="text-align: center; margin-top: 35px; color: #888; font-size: 14px;">
                        — Equipo de Estrategia de Conquista: Risk —
                      </p>
                    </div>
                  </body>
                </html>
                """;
        return html.replace("NOMBRE_JUGADOR", nombreJugador).replace("URL_REGLAS",
                "https://drive.google.com/file/d/1sZPfe2peCRIXsQy4rI4UllSJ1JT5HU9e/view");
    }
}
