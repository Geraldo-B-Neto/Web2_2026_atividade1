package br.edu.iftm.meuAppSpringJava.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import br.edu.iftm.meuAppSpringJava.model.Paciente;
import br.edu.iftm.meuAppSpringJava.service.pacienteService;

@Controller
public class pacienteController {

    @Autowired
    private pacienteService pacienteService;

    @GetMapping("/")
    public String home() {
        return "redirect:/paciente";
    }

    @GetMapping("/paciente")
    public String index(Model model) {
        model.addAttribute("pacientesList", pacienteService.getAllPacientes());
        return "paciente/index";
    }

    @GetMapping("/paciente/create")
    public String create(Model model) {
        model.addAttribute("paciente", new Paciente());
        return "paciente/create";
    }

    @PostMapping("/paciente/save")
    public String save(@ModelAttribute("paciente") Paciente paciente) {
        pacienteService.savePaciente(paciente);
        return "redirect:/paciente";
    }

    @GetMapping("/paciente/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("paciente", pacienteService.getPacienteById(id));
        return "paciente/create"; // Reutiliza o formulário de cadastro para edição
    }

    @GetMapping("/paciente/delete/{id}")
    public String delete(@PathVariable Long id) {
        pacienteService.deletePacienteById(id);
        return "redirect:/paciente";
    }
}