package br.edu.iftm.meuAppSpringJava.controller;

import br.edu.iftm.meuAppSpringJava.model.Paciente;
import br.edu.iftm.meuAppSpringJava.service.pacienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class pacienteController {

    private final pacienteService pacienteService;

    public pacienteController(pacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/paciente";
    }

    @GetMapping("/paciente")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_GERENTE', 'ROLE_ADMIN')")
    public String index(Model model) {
        model.addAttribute("pacientesList", pacienteService.getAllPacientes());
        return "paciente/index";
    }

    @GetMapping("/paciente/create")
    @PreAuthorize("hasAnyAuthority('ROLE_GERENTE', 'ROLE_ADMIN')")
    public String create(Model model) {
        model.addAttribute("paciente", new Paciente());
        return "paciente/create";
    }

    @PostMapping("/paciente/save")
    @PreAuthorize("hasAnyAuthority('ROLE_GERENTE', 'ROLE_ADMIN')")
    public String save(@ModelAttribute("paciente") @Valid Paciente paciente, BindingResult result) {
        if (result.hasErrors()) {
            return "paciente/create";
        }

        pacienteService.savePaciente(paciente);
        return "redirect:/paciente";
    }

    @GetMapping("/paciente/edit/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_GERENTE', 'ROLE_ADMIN')")
    public String edit(@PathVariable Long id, Model model) {
        Paciente paciente = pacienteService.getPacienteById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Paciente não encontrado"));
        model.addAttribute("paciente", paciente);
        return "paciente/edit";
    }

    @PostMapping("/paciente/update/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_GERENTE', 'ROLE_ADMIN')")
    public String update(@PathVariable Long id, @ModelAttribute("paciente") @Valid Paciente paciente,
            BindingResult result) {
        if (result.hasErrors()) {
            paciente.setId(id);
            return "paciente/edit";
        }

        if (pacienteService.getPacienteById(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Paciente não encontrado");
        }
        paciente.setId(id);
        pacienteService.savePaciente(paciente);
        return "redirect:/paciente";
    }

    @GetMapping("/paciente/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String delete(@PathVariable Long id) {
        pacienteService.deletePacienteById(id);
        return "redirect:/paciente";
    }
}
