package com.grepp.teamnotfound.app.controller.web;


import com.grepp.teamnotfound.app.model.pet.PetImgService;
import com.grepp.teamnotfound.app.model.pet.dto.PetImgDTO;
import com.grepp.teamnotfound.app.model.pet.entity.Pet;
import com.grepp.teamnotfound.app.model.pet.repository.PetRepository;
import com.grepp.teamnotfound.util.CustomCollectors;
import com.grepp.teamnotfound.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/petImgs")
public class PetImgController {

    private final PetImgService petImgService;
    private final PetRepository petRepository;

    public PetImgController(final PetImgService petImgService, final PetRepository petRepository) {
        this.petImgService = petImgService;
        this.petRepository = petRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("petValues", petRepository.findAll(Sort.by("petId"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Pet::getPetId, Pet::getRegistNumber)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("petImgs", petImgService.findAll());
        return "petImg/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("petImg") final PetImgDTO petImgDTO) {
        return "petImg/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("petImg") @Valid final PetImgDTO petImgDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "petImg/add";
        }
        petImgService.create(petImgDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("petImg.create.success"));
        return "redirect:/petImgs";
    }

    @GetMapping("/edit/{petImgId}")
    public String edit(@PathVariable(name = "petImgId") final Long petImgId, final Model model) {
        model.addAttribute("petImg", petImgService.get(petImgId));
        return "petImg/edit";
    }

    @PostMapping("/edit/{petImgId}")
    public String edit(@PathVariable(name = "petImgId") final Long petImgId,
            @ModelAttribute("petImg") @Valid final PetImgDTO petImgDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "petImg/edit";
        }
        petImgService.update(petImgId, petImgDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("petImg.update.success"));
        return "redirect:/petImgs";
    }

    @PostMapping("/delete/{petImgId}")
    public String delete(@PathVariable(name = "petImgId") final Long petImgId,
            final RedirectAttributes redirectAttributes) {
        petImgService.delete(petImgId);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("petImg.delete.success"));
        return "redirect:/petImgs";
    }

}
