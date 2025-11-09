package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender mailSender;

	// Método para enviar un correo simple
	public void enviarCorreo(String para, String asunto, String mensaje) {

		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(para);
		email.setSubject(asunto);
		email.setText(mensaje);

		mailSender.send(email);
	}
	 public void enviarCorreoHtml(String para, String asunto, String htmlContenido) {
	        try {
	            MimeMessage mensaje = mailSender.createMimeMessage();
	            MimeMessageHelper helper = new MimeMessageHelper(mensaje, "utf-8");

	            helper.setTo(para);
	            helper.setSubject(asunto);
	            helper.setText(htmlContenido, true); // true = HTML

	            mailSender.send(mensaje);
	        } catch (Exception e) {
	            throw new RuntimeException("Error enviando correo: " + e.getMessage());
	        }
	    }
}
