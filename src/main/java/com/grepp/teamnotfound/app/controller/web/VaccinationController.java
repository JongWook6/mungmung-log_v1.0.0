package com.grepp.teamnotfound.app.controller.web;


import com.grepp.teamnotfound.app.model.pet.entity.Pet;
import com.grepp.teamnotfound.app.model.pet.repository.PetRepository;
import com.grepp.teamnotfound.app.model.vaccination.VaccinationService;
import com.grepp.teamnotfound.app.model.vaccination.dto.VaccinationDTO;
import com.grepp.teamnotfound.app.model.vaccination.entity.Vaccine;
import com.grepp.teamnotfound.app.model.vaccination.repository.VaccineRepository;
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
@RequestMapping("/vaccinations")
public class VaccinationController {

    private final VaccinationService vaccinationService;

    public VaccinationController(final VaccinationService vaccinationService,
            final VaccineRepository vaccineRepository, final PetRepository petRepository) {
        this.vaccinationService = vaccinationService;
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("vaccinations", vaccinationService.findAll());
        return "vaccination/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("vaccination") final VaccinationDTO vaccinationDTO) {
        return "vaccination/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("vaccination") @Valid final VaccinationDTO vaccinationDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "vaccination/add";
        }
        vaccinationService.create(vaccinationDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("vaccination.create.success"));
        return "redirect:/vaccinations";
    }

    @GetMapping("/edit/{vaccinationId}")
    public String edit(@PathVariable(name = "vaccinationId") final Long vaccinationId,
            final Model model) {
        model.addAttribute("vaccination", vaccinationService.get(vaccinationId));
        return "vaccination/edit";
    }

    @PostMapping("/edit/{vaccinationId}")
    public String edit(@PathVariable(name = "vaccinationId") final Long vaccinationId,
            @ModelAttribute("vaccination") @Valid final VaccinationDTO vaccinationDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "vaccination/edit";
        }
        vaccinationService.update(vaccinationId, vaccinationDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("vaccination.update.success"));
        return "redirect:/vaccinations";
    }

    @PostMapping("/delete/{vaccinationId}")
    public String delete(@PathVariable(name = "vaccinationId") final Long vaccinationId,
            final RedirectAttributes redirectAttributes) {
        vaccinationService.delete(vaccinationId);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("vaccination.delete.success"));
        return "redirect:/vaccinations";
    }

}
