package com.grepp.teamnotfound.app.controller.web.life_record; // 패키지 경로는 프로젝트에 맞게 수정하세요.

import com.grepp.teamnotfound.app.controller.api.life_record.payload.LifeRecordData;
import com.grepp.teamnotfound.app.model.life_record.LifeRecordService;
import com.grepp.teamnotfound.app.model.life_record.dto.LifeRecordDto;
import com.grepp.teamnotfound.util.WebUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/lifeRecords")
public class LifeRecordController {

    private final LifeRecordService lifeRecordService;

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("lifeRecords", lifeRecordService.findAll());
        return "life_record/list"; // lifeRecord/list.html 템플릿을 반환합니다.
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("lifeRecord") final LifeRecordData lifeRecordData) {
        return "life_record/add";
    }

    @PostMapping("/add")
    public String add(
            @ModelAttribute("lifeRecord") @Valid final LifeRecordData request,
            final BindingResult bindingResult,
            final RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            return "life_record/add";
        }
        LifeRecordDto dto = LifeRecordDto.toDto(request);
        lifeRecordService.createLifeRecord(dto);

        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("lifeRecord.create.success"));
        return "redirect:/lifeRecords";
    }

    @PostMapping("/delete/{id}")
    public String delete(
            @PathVariable(name = "id") final Long lifeRecordId
    ){
        lifeRecordService.deleteLifeRecord(lifeRecordId);

        return "redirect:/lifeRecords";
    }

}