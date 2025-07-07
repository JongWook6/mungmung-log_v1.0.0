package com.grepp.teamnotfound.app.controller.web;


import com.grepp.teamnotfound.app.model.vaccination.VaccineService;
import com.grepp.teamnotfound.app.model.vaccination.dto.VaccineDTO;
import com.grepp.teamnotfound.util.ReferencedWarning;
import com.grepp.teamnotfound.util.WebUtils;
import jakarta.validation.Valid;
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
@RequestMapping("/vaccines")
public class VaccineController {

    private final VaccineService vaccineService;

    public VaccineController(final VaccineService vaccineService) {
        this.vaccineService = vaccineService;
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("vaccines", vaccineService.findAll());
        return "vaccine/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("vaccine") final VaccineDTO vaccineDTO) {
        return "vaccine/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("vaccine") @Valid final VaccineDTO vaccineDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "vaccine/add";
        }
        vaccineService.create(vaccineDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("vaccine.create.success"));
        return "redirect:/vaccines";
    }

    @GetMapping("/edit/{vaccineId}")
    public String edit(@PathVariable(name = "vaccineId") final Long vaccineId, final Model model) {
        model.addAttribute("vaccine", vaccineService.get(vaccineId));
        return "vaccine/edit";
    }

    @PostMapping("/edit/{vaccineId}")
    public String edit(@PathVariable(name = "vaccineId") final Long vaccineId,
            @ModelAttribute("vaccine") @Valid final VaccineDTO vaccineDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "vaccine/edit";
        }
        vaccineService.update(vaccineId, vaccineDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("vaccine.update.success"));
        return "redirect:/vaccines";
    }

    @PostMapping("/delete/{vaccineId}")
    public String delete(@PathVariable(name = "vaccineId") final Long vaccineId,
            final RedirectAttributes redirectAttributes) {
        final ReferencedWarning referencedWarning = vaccineService.getReferencedWarning(vaccineId);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
                    WebUtils.getMessage(referencedWarning.getKey(), referencedWarning.getParams().toArray()));
        } else {
            vaccineService.delete(vaccineId);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("vaccine.delete.success"));
        }
        return "redirect:/vaccines";
    }

}
