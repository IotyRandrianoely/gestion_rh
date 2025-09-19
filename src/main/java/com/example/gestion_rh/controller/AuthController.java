package com.example.gestion_rh.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.gestion_rh.model.Utilisateur;
import com.example.gestion_rh.service.UtilisateurService;

import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

    private final UtilisateurService utilisateurService;

    public AuthController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    // Page de login (page d'accueil)
    @GetMapping("/")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // Traitement du login
    @PostMapping("/login")
    public String processLogin(@RequestParam String username,
            @RequestParam String password,
            HttpSession session,
            Model model) {

        System.out.println("=== TENTATIVE DE LOGIN ===");
        System.out.println("Username: " + username);
        System.out.println("Password: " + password);

        Utilisateur user = utilisateurService.authenticate(username, password);

        if (user != null) {
            System.out.println("LOGIN RÉUSSI pour: " + user.getUsername() + " (rôle: " + user.getRole() + ")");

            // Stocker l'utilisateur en session
            session.setAttribute("user", user);
            session.setAttribute("userRole", user.getRole());
            session.setAttribute("username", user.getUsername());

            // Rediriger selon le rôle
            switch (user.getRole()) {
                case "admin":
                    return "redirect:/admin/dashboard";
                case "rh":
                    return "redirect:/rh/dashboard";
                case "client":
                default:
                    return "redirect:/annonces";
            }
        } else {
            System.out.println("LOGIN ÉCHOUÉ pour: " + username);
            model.addAttribute("error", "Nom d'utilisateur ou mot de passe incorrect");
            return "login";
        }
    }

    // Logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        Utilisateur user = (Utilisateur) session.getAttribute("user");
        if (user != null) {
            System.out.println("LOGOUT pour: " + user.getUsername());
        }
        session.invalidate();
        return "redirect:/login?logout=true";
    }

    // Dashboard admin
    @GetMapping("/admin/dashboard")
    public String adminDashboard(HttpSession session, Model model) {
        Utilisateur user = (Utilisateur) session.getAttribute("user");
        if (user == null || !"admin".equals(user.getRole())) {
            System.out.println("ACCÈS REFUSÉ - Dashboard admin");
            return "redirect:/login";
        }
        System.out.println("ACCÈS AUTORISÉ - Dashboard admin pour: " + user.getUsername());
        model.addAttribute("user", user);
        return "admin/dashboard";
    }

    // Dashboard RH
    @GetMapping("/rh/dashboard")
    public String rhDashboard(HttpSession session, Model model) {
        Utilisateur user = (Utilisateur) session.getAttribute("user");
        if (user == null || !"rh".equals(user.getRole())) {
            System.out.println("ACCÈS REFUSÉ - Dashboard RH");
            return "redirect:/login";
        }
        System.out.println("ACCÈS AUTORISÉ - Dashboard RH pour: " + user.getUsername());
        model.addAttribute("user", user);
        return "rh/dashboard";
    }

    // Page d'accueil après login client
    @GetMapping("/client/home")
    public String clientHome(HttpSession session, Model model) {
        Utilisateur user = (Utilisateur) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "redirect:/annonces";
    }
}