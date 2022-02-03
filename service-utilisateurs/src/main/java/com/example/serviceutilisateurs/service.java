package com.example.serviceutilisateurs;

import com.example.serviceutilisateurs.facade.FacadeParties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pileouface.modele.MauvaisIdentifiantConnexionException;
import pileouface.modele.Partie;
import pileouface.modele.Statistiques;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collection;

@RequestMapping("/jeu")
@RestController
public class service {

    @Autowired
    FacadeParties facadeParties;

    private String getPseudo(String token) throws IOException, InterruptedException{
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder().
                uri(URI.create(String.format("http://localhost:8081/authent/token?token=%s",token)))
                .GET()
                .build();
            return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
    }

    @PostMapping("/partie")
    public ResponseEntity creerPartie(@RequestHeader("token") String token, @RequestParam String prediction) throws IOException, InterruptedException {
        try {
            String pseudo = getPseudo(token);
            facadeParties.jouer(pseudo, prediction);
            return ResponseEntity.ok().build();
        } catch (MauvaisIdentifiantConnexionException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/partie")
    public ResponseEntity getAllParties(@RequestHeader String token) throws IOException, InterruptedException {

        try {
            String pseudo = getPseudo(token);
           Collection<Partie> parties = facadeParties.getAllParties(pseudo);
            return  ResponseEntity.ok().body(parties);
        } catch (MauvaisIdentifiantConnexionException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/statistiques")
    public ResponseEntity getStat(@RequestHeader String token) throws IOException, InterruptedException {
        try {
            String pseudo = getPseudo(token);
            Statistiques stat =  facadeParties.getStatistiques(pseudo);
            return ResponseEntity.ok().body(stat);
        } catch (MauvaisIdentifiantConnexionException e) {
            return ResponseEntity.notFound().build();
        }

    }

}
