package br.edu.iftm.meuAppSpringJava.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import br.edu.iftm.meuAppSpringJava.service.pacienteService;

@Controller
public class pacienteController {

    @Autowired
    private pacienteService pacienteService;

    // Redireciona http://localhost:8081/ direto para a lista
    @GetMapping("/")
    public String home() {
        return "redirect:/paciente";
    }

    // Carrega a lista de pacientes no Model e exibe o template
    @GetMapping("/paciente")
    public String index(Model model) {
        model.addAttribute("pacientesList", pacienteService.getAllPacientes());
        return "paciente/index"; // Sem barra inicial
    }
}