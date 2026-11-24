package com.devWebResolv.mannagerSystem.controller;

import com.devWebResolv.mannagerSystem.model.Tecnologia;
import com.devWebResolv.mannagerSystem.service.TecnologiaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/tecnologias")
public class TecnologiaController {

    private final TecnologiaService tecnologiaService;

    public TecnologiaController(TecnologiaService tecnologiaService) {
        this.tecnologiaService = tecnologiaService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("tecnologias", tecnologiaService.listarTodas());
        return "tecnologia/index";
    }

    @GetMapping("/novo")
    public String exibirFormularioCadastro(Model model) {
        model.addAttribute("tecnologia", new Tecnologia());
        return "tecnologia/create";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute("tecnologia") Tecnologia tecnologia,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        try {
            tecnologiaService.salvar(tecnologia);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Tecnologia salva com sucesso!");
            return "redirect:/tecnologias";
        } catch (IllegalArgumentException e) {
            model.addAttribute("mensagemErro", e.getMessage());
            model.addAttribute("tecnologia", tecnologia);
            return "tecnologia/create";
        }
    }

    @GetMapping("/editar/{id}")
    public String exibirFormularioEdicao(@PathVariable("id") Long id,
                                         Model model,
                                         RedirectAttributes redirectAttributes) {
        try {
            Tecnologia tecnologia = tecnologiaService.buscarPorId(id);
            model.addAttribute("tecnologia", tecnologia);
            return "tecnologia/create";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", e.getMessage());
            return "redirect:/tecnologias";
        }
    }

    @GetMapping({"/deletar/{id}", "/excluir/{id}"})
    public String excluir(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            tecnologiaService.excluir(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Tecnologia excluída com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", e.getMessage());
        }
        return "redirect:/tecnologias";
    }
}
