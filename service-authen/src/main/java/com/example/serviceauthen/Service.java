package com.example.serviceauthen;

import authent.modele.JoueurInexistantException;
import authent.modele.MauvaisTokenException;
import authent.modele.OperationNonAutorisee;
import authent.modele.PseudoDejaPrisException;
import com.example.serviceauthen.facade.FacadeJoueur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authent")
public class Service {

    @Autowired
    FacadeJoueur facadeJoueur;

    @PostMapping(value="/inscription")
    public ResponseEntity inscription(@RequestParam(name="pseudo") String pseudo, @RequestParam(name="mdp") String mdp){
        try {
            facadeJoueur.inscription(pseudo,mdp);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (PseudoDejaPrisException e) {
            return  ResponseEntity.status (HttpStatus.CONFLICT).build();
        }
    }


    @PostMapping("/token")
    public ResponseEntity<String> genererToken(@RequestParam(name="pseudo") String pseudo, @RequestParam(name="mdp") String mdp){
        try {
            String token = facadeJoueur.genererToken(pseudo,mdp);
            return ResponseEntity.ok().header("token",token).build();
        } catch (JoueurInexistantException e) {
            return ResponseEntity.notFound().build();
        } catch (OperationNonAutorisee e) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/token")
    public ResponseEntity checkToken(@RequestParam(name="token") String token)   {
        try {
            facadeJoueur.checkToken(token);
            return ResponseEntity.ok().build();
        } catch (MauvaisTokenException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/inscription/{pseudo}")
    public ResponseEntity deleteUser(@PathVariable String pseudo, @RequestParam String mdp){
        try {
            facadeJoueur.desincription(pseudo,mdp);
            return ResponseEntity.ok().build();
        } catch (JoueurInexistantException e) {
            return ResponseEntity.notFound().build();
        } catch (OperationNonAutorisee e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
