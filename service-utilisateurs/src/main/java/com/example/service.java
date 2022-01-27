package com.example;

import com.example.serviceauthen.facade.FacadeParties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pileouface.modele.MauvaisIdentifiantConnexionException;
import pileouface.modele.Partie;
import pileouface.modele.Statistiques;

import java.util.Collection;

@RequestMapping("/jeu")
@RestController
public class service {

    @Autowired
    FacadeParties facadeParties;

    @PostMapping("/partie")
    public ResponseEntity creerPartie(@RequestHeader("token") String token, @RequestBody String prediction){
        try {
            facadeParties.jouer(token,prediction);
            return ResponseEntity.ok().build();
        } catch (MauvaisIdentifiantConnexionException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/partie")
    public ResponseEntity getAllParties(@RequestHeader String token){
        //faire appel au service authen pour demander le pseudo qui correspond au token
        //HttpClient ...

        try {
           Collection<Partie> parties = facadeParties.getAllParties(token);
            return  ResponseEntity.ok().body(parties);
        } catch (MauvaisIdentifiantConnexionException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/statistiques")
    public ResponseEntity getStat(@RequestHeader String token){
        try {
            Statistiques stat =  facadeParties.getStatistiques(token);
            return ResponseEntity.ok().body(stat);
        } catch (MauvaisIdentifiantConnexionException e) {
            return ResponseEntity.notFound().build();
        }

    }

}
