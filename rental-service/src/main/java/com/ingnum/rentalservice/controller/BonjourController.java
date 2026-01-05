package com.ingnum.rentalservice.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@RestController
public class BonjourController {

    private final RestTemplate restTemplate;
    private final String firstnameServiceUrl;

    // Injection du RestTemplate et de l'URL via le constructeur
    public BonjourController(RestTemplate restTemplate,
                             @Value("${firstname.service.url:http://firstname-service/index.php}") String firstnameServiceUrl) {
        this.restTemplate = restTemplate;
        this.firstnameServiceUrl = firstnameServiceUrl;
    }

    @GetMapping("/bonjour")
    public String bonjour() {
        String firstname = "inconnu";

        try {
            // Appel au microservice PHP
            FirstnameResponse response = restTemplate.getForObject(firstnameServiceUrl, FirstnameResponse.class);
            
            if (response != null && response.getFirstname() != null) {
                firstname = response.getFirstname();
            }

        } catch (RestClientException e) {
            // En cas d'erreur (PHP éteint, mauvaise URL), on affiche l'erreur dans la console serveur
            System.err.println("Erreur lors de l'appel au service PHP : " + e.getMessage());
        }

        return "bonjour " + firstname;
    }

    // Classe interne statique pour mapper le JSON { "firstname": "Jean" }
    public static class FirstnameResponse {
        private String firstname;

        // Constructeur vide nécessaire pour la désérialisation JSON
        public FirstnameResponse() {
        }

        public String getFirstname() {
            return firstname;
        }

        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }
    }
}