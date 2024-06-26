package org.example.springpfa.Controller;
import org.example.springpfa.DaoMotif;
import org.example.springpfa.Repository.DemandeRepository;
import org.example.springpfa.Repository.UserRepository;
import org.example.springpfa.Services.JWTUtils;
import org.example.springpfa.Services.MailService;
import org.example.springpfa.Services.UserService;
import org.example.springpfa.entities.Demande;
import org.example.springpfa.entities.DemandeDTO;
import org.example.springpfa.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class DemandeController {

    @Autowired
    DemandeRepository demandeRepository;
    @Autowired
    MailService mailService;
    @Autowired
    JWTUtils jwtUtils;
    @Autowired
    UserRepository userRepository;

    @PostMapping("/demandes")
    public List<Demande> GetDemandes(@RequestBody String token ) {
        System.out.println(token);
        User user = userRepository.findByUsername(jwtUtils.extractUserName(token));
        // Check if token is valid for the given user
        if (!jwtUtils.isTokenValid(token, user)) {
            //throw new RuntimeException("Invalid or expired token");
            return null;
        }
        // Fetch and return demandes from repository
        List<Demande> demandes = demandeRepository.findAll();
        System.out.println(token + "\n" + user.getUsername());
        return demandes;
    }
    //Get a specific user
    @GetMapping("/demandes/{id}")
    public Demande GetUser(@PathVariable long id){
        return demandeRepository.findByIdDemande(id);
    }
    //add new User
    @PostMapping("/demandes/add")
    public int CreateDemand(@RequestBody Demande demande , @RequestParam String token){

        System.out.println(token);
        String userName = jwtUtils.extractUserName(token);
        System.out.println("my demand json\n" + demande);
        return 1;
    }
    @PostMapping("/demandes/add/dto")
    public Map<String ,Object> CreateDemandeDTO(@RequestBody DemandeDTO demandeDTO , @RequestParam String token){
        Map<String ,Object> response = new HashMap<>();
        System.out.println(token);
        String userName = jwtUtils.extractUserName(token);
        Demande demande = Demande.builder()
                .date(demandeDTO.getDate())
                .etat("Pending")
                .sujet(demandeDTO.getSujet())
                .title(demandeDTO.getTitle())
                .user(userRepository.findByUsername(userName))
                .build();
        demandeRepository.save(demande);
        response.put("userName", userName);
        response.put("demandeDTO", demandeDTO);
        response.put("demade", demande);

        return response ;
    }


    //Edit an existing user :
    /*@PutMapping("/user/edit/{id}")
    public void PutUser(@RequestBody User user , @PathVariable long id ){
        userService.editUser(user,id);
    }
    @DeleteMapping("/users/delete/{id}")
    public void DeleteUser(@PathVariable long id){
        userService.dropUser(id);
    }
     */

    @GetMapping("/checkToken")
    public boolean checkToken(@RequestParam("token") String token) {
        System.out.println(token);
        return !jwtUtils.extractExpiration(token).before(new Date());
    }

    @PostMapping("/reclamation")
    public List<Demande> getRecalamtion(@RequestBody String token){
        User user = userRepository.findByUsername(jwtUtils.extractUserName(token));
        // Check if token is valid for the given user
        if (!jwtUtils.isTokenValid(token, user)) {
            throw new RuntimeException("Invalid or expired token");
            //return null;
        }
        // Fetch and return demandes from repository
        List<Demande> demandes = demandeRepository.findByUser(user);
        System.out.println(token + "\n" + user.getUsername());
        return demandes;
    }


    @PostMapping("/reclamation/solve")
    public int solveReclamation(@RequestBody String token ,@RequestParam Long id ){
        String userName = jwtUtils.extractUserName(token);
        User user = userRepository.findByUsername(userName);
        Demande demande = demandeRepository.findByIdDemande(id);
        demande.setEtat("Solved");
        demandeRepository.save(demande);
        System.out.println(user);
        return 1;
    }

    @PostMapping("/reclamation/reject")
    public int rejectReclamation(@RequestBody String token ,@RequestParam Long id){
        String userName = jwtUtils.extractUserName(token);
        User user = userRepository.findByUsername(userName);
        Demande demande = demandeRepository.findByIdDemande(id);
        demande.setEtat("Rejected");
        demandeRepository.save(demande);
        System.out.println(user);
        return 1;
    }

    @PostMapping("/reclamation/newSolve")
    public Map<String,Object> NewSolveReclamation(@RequestParam String token ,@RequestParam Long id , @RequestBody DaoMotif motif ){
        Map<String,Object> response = new HashMap<>();
        String userName = jwtUtils.extractUserName(token);
        User user = userRepository.findByUsername(userName);
        Demande demande = demandeRepository.findByIdDemande(id);
        demande.setEtat("Solved");
        demandeRepository.save(demande);
        System.out.println("sending  mail Ongoing ");
        String mailReceiver = userRepository.findByUsername(jwtUtils.extractUserName(token)).getEmail();//get admin's mail
        String  MailReceiver = demande.getUser().getEmail();//get the reclamation owner's mail
        mailService.sendEmail(motif.getMotif(),MailReceiver);
        response.put("response",200);
        response.put("Receiver" , mailReceiver);
        response.put("motif",motif.getMotif());
        return response;
    }

    @PostMapping("/reclamation/newReject")
    public Map<String,Object> NewRejectReclamation(@RequestParam String token , @RequestParam Long id , @RequestBody DaoMotif motif){
        Map<String,Object> respose = new HashMap<>();
        String userName = jwtUtils.extractUserName(token);
        User user = userRepository.findByUsername(userName);
        Demande demande = demandeRepository.findByIdDemande(id);
        demande.setEtat("Rejected");
        demandeRepository.save(demande);
        System.out.println("sending  mail Ongoing ");
        String mailReceiver = userRepository.findByUsername(jwtUtils.extractUserName(token)).getEmail();//get admin's mail
        String  MailReceiver = demande.getUser().getEmail();//get the reclamation owner's mail
        mailService.sendEmail(motif.getMotif(),MailReceiver);

        respose.put("response",200);
        respose.put("Receiver" , mailReceiver);
        respose.put("motif",motif.getMotif());
        return respose;
    }


}
