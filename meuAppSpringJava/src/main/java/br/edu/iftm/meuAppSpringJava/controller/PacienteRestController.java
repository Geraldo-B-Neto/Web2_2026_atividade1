package br.edu.iftm.meuAppSpringJava.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.edu.iftm.meuAppSpringJava.model.Paciente;
import br.edu.iftm.meuAppSpringJava.service.pacienteService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteRestController {

    private final pacienteService pacienteService;

    public PacienteRestController(pacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_GERENTE', 'ROLE_ADMIN')")
    public ResponseEntity<List<Paciente>> getAllPacientes() {
        return ResponseEntity.ok(pacienteService.getAllPacientes());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_GERENTE', 'ROLE_ADMIN')")
    public ResponseEntity<Paciente> getPacienteById(@PathVariable Long id) {
        return pacienteService.getPacienteById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_GERENTE', 'ROLE_ADMIN')")
    public ResponseEntity<?> createPaciente(@Valid @RequestBody Paciente paciente, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }

        Paciente savedPaciente = pacienteService.savePaciente(paciente);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedPaciente.getId())
                .toUri();

        return ResponseEntity.created(location).body(savedPaciente);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_GERENTE', 'ROLE_ADMIN')")
    public ResponseEntity<?> updatePaciente(@PathVariable Long id, @Valid @RequestBody Paciente paciente,
            BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        if (pacienteService.getPacienteById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        paciente.setId(id);
        return ResponseEntity.ok(pacienteService.savePaciente(paciente));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deletePaciente(@PathVariable Long id) {
        if (pacienteService.getPacienteById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        pacienteService.deletePacienteById(id);
        return ResponseEntity.noContent().build();
    }
}
