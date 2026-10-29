package br.edu.iftm.meuAppSpringJava.controller;

import br.edu.iftm.meuAppSpringJava.model.Consulta;
import br.edu.iftm.meuAppSpringJava.service.ConsultaService;
import br.edu.iftm.meuAppSpringJava.service.pacienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/consultas")
public class ConsultaController {

    private final ConsultaService consultaService;
    private final pacienteService pacienteService;

    public ConsultaController(ConsultaService consultaService, pacienteService pacienteService) {
        this.consultaService = consultaService;
        this.pacienteService = pacienteService;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("consultas", consultaService.getAllConsultas());
        return "consultas/index";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("consulta", new Consulta());
        addPacientes(model);
        return "consultas/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("consulta") @Valid Consulta consulta, BindingResult result, Model model) {
        if (result.hasErrors()) {
            addPacientes(model);
            return "consultas/form";
        }

        try {
            consultaService.saveConsulta(consulta);
        } catch (IllegalArgumentException exception) {
            result.reject("consulta.paciente", exception.getMessage());
            addPacientes(model);
            return "consultas/form";
        }
        return "redirect:/consultas";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Consulta consulta = consultaService.getConsultaById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Consulta não encontrada"));
        model.addAttribute("consulta", consulta);
        addPacientes(model);
        return "consultas/form";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id, @ModelAttribute("consulta") @Valid Consulta consulta,
            BindingResult result, Model model) {
        consulta.setId(id);
        if (result.hasErrors()) {
            addPacientes(model);
            return "consultas/form";
        }

        try {
            consultaService.saveConsulta(consulta);
        } catch (IllegalArgumentException exception) {
            result.reject("consulta.paciente", exception.getMessage());
            addPacientes(model);
            return "consultas/form";
        }
        return "redirect:/consultas";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        consultaService.deleteConsultaById(id);
        return "redirect:/consultas";
    }

    private void addPacientes(Model model) {
        model.addAttribute("pacientes", pacienteService.getAllPacientes());
    }
}