package com.devWebResolv.mannagerSystem.controller;

import com.devWebResolv.mannagerSystem.model.Projeto;
import com.devWebResolv.mannagerSystem.model.StatusProjeto;
import com.devWebResolv.mannagerSystem.service.ProjetoService;
import com.devWebResolv.mannagerSystem.service.TecnologiaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashSet;
import java.util.Optional;

@Controller
@RequestMapping("/projetos")
public class ProjetoController {

    private final ProjetoService projetoService;
    private final TecnologiaService tecnologiaService;

    public ProjetoController(ProjetoService projetoService, TecnologiaService tecnologiaService) {
        this.projetoService = projetoService;
        this.tecnologiaService = tecnologiaService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("projetos", projetoService.listarTodos());
        return "projeto/index";
    }

    @GetMapping("/novo")
    public String exibirFormularioCadastro(Model model) {
        model.addAttribute("projeto", new Projeto());
        model.addAttribute("statusList", StatusProjeto.values());
        model.addAttribute("todasTecnologias", tecnologiaService.listarTodas());
        return "projeto/create";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute("projeto") Projeto projeto, 
                         RedirectAttributes redirectAttributes, 
                         Model model) {
        try {
            if (projeto.getTecnologias() == null) {
                projeto.setTecnologias(new HashSet<>());
            }
            projetoService.salvar(projeto);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Projeto salvo com sucesso!");
            return "redirect:/projetos";
        } catch (IllegalArgumentException e) {
            model.addAttribute("mensagemErro", e.getMessage());
            model.addAttribute("projeto", projeto);
            model.addAttribute("statusList", StatusProjeto.values());
            model.addAttribute("todasTecnologias", tecnologiaService.listarTodas());
            return "projeto/create";
        }
    }

    @GetMapping("/editar/{id}")
    public String exibirFormularioEdicao(@PathVariable("id") Long id, 
                                         Model model, 
                                         RedirectAttributes redirectAttributes) {
        Optional<Projeto> projetoOpt = projetoService.buscarPorId(id);
        if (projetoOpt.isPresent()) {
            model.addAttribute("projeto", projetoOpt.get());
            model.addAttribute("statusList", StatusProjeto.values());
            model.addAttribute("todasTecnologias", tecnologiaService.listarTodas());
            return "projeto/create";
        } else {
            redirectAttributes.addFlashAttribute("mensagemErro", "Projeto não encontrado com o ID " + id);
            return "redirect:/projetos";
        }
    }

    @GetMapping({"/deletar/{id}", "/excluir/{id}"})
    public String excluir(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            projetoService.deletar(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Projeto excluído com sucesso!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", e.getMessage());
        }
        return "redirect:/projetos";
    }
}
