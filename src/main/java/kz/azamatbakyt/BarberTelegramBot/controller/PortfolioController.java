package kz.azamatbakyt.BarberTelegramBot.controller;

import kz.azamatbakyt.BarberTelegramBot.service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/portfolio")
public class PortfolioController {

    private final PortfolioService portfolioService;

    @Autowired
    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    @GetMapping
    public String images(Model model) {
        model.addAttribute("images", portfolioService.getAllLinks());
        return "portfolio/list";
    }
    public String uploadPage(){
        return "portfolio/form";
    }

    @PostMapping("/upload")
    public String uploadImage(MultipartFile file, Model model) throws IOException {
        portfolioService.save(file);
        return "redirect:/portfolio";
    }

    @GetMapping("/{id}/delete")
    public String deleteImage(@PathVariable Long id){
        portfolioService.deleteImages(id);
        return "redirect:/portfolio";
    }




}
