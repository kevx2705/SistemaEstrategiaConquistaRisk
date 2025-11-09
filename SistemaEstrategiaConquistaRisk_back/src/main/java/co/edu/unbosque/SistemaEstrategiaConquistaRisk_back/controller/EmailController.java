package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.service.EmailService;

@RestController
@RequestMapping("/correo")
public class EmailController {

    @Autowired
    private EmailService mailService;

    @GetMapping("/test")
//    public String testCorreo() {
//        mailService.enviarCorreoSimple(
//                "santiagomunozv123@gmail.com",
//                "Prueba desde Spring Boot",
//                "¡Hola! Este es un correo de prueba."
//        );
//
//        return "Correo enviado";
//    }
}
