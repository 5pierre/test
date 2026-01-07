package com.ingnum.rentalservice.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@RestController
public class BonjourController {

    private final RestTemplate restTemplate;
    private final String phpServiceUrl;

    // --- CONSTRUCTEUR (Doit avoir le même nom que la classe) ---
    public BonjourController(RestTemplate restTemplate,
                             @Value("${firstname.service.url:http://firstname-service/index.php}") String phpServiceUrl) {
        this.restTemplate = restTemplate;
        this.phpServiceUrl = phpServiceUrl;
    }

    // --- PARTIE 1 : Communication avec le PHP ---
    
    @GetMapping("/bonjour")
    public String bonjour() {
        String firstname = "inconnu";
        try {
            // Appel au microservice PHP via son nom Docker
            FirstnameResponse response = restTemplate.getForObject(phpServiceUrl, FirstnameResponse.class);
            
            if (response != null && response.getFirstname() != null) {
                firstname = response.getFirstname();
            }
        } catch (RestClientException e) {
            // Log de l'erreur dans la console pour debugger si besoin
            System.err.println("Erreur appel PHP : " + e.getMessage());
        }
        return "bonjour " + firstname;
    }

    // --- PARTIE 2 : Les routes demandées (Customer & Rentals) ---

    /**
     * URL : http://localhost:8080/customer/{name}
     */
    @GetMapping("/customer/{name}")
    public String getCustomer(@PathVariable String name) {
        return "Bonjour " + name;
    }

    @GetMapping("/api/rentals")
    public String getRentals() {
        return "GET → Java : liste des locations";
    }

    @PostMapping("/api/rentals")
    public String createRental(@RequestBody String body) {
        return "POST → Java : création location " + body;
    }

    @PutMapping("/api/rentals/{id}")
    public String updateRental(@PathVariable int id, @RequestBody String body) {
        return "PUT → Java : remplacement location " + id;
    }

    @PatchMapping("/api/rentals/{id}")
    public String patchRental(@PathVariable int id, @RequestBody String body) {
        return "PATCH → Java : modification partielle location " + id;
    }

    @DeleteMapping("/api/rentals/{id}")
    public String deleteRental(@PathVariable int id) {
        return "DELETE → Java : suppression location " + id;
    }

    // --- CLASSE INTERNE (DTO pour le JSON) ---
    public static class FirstnameResponse {
        private String firstname;

        public FirstnameResponse() { }

        public String getFirstname() {
            return firstname;
        }

        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }
    }
}