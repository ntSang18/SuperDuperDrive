package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.models.Credential;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("credential")
@RequiredArgsConstructor
public class CredentialController {

    private final CredentialService credentialService;

    @PostMapping("/save")
    public String saveCredential(@ModelAttribute Credential credential,
                                 Authentication authentication,
                                 RedirectAttributes redirectAttributes) {
        int rowsEffected = credentialService.saveCredential(credential, authentication.getName());
        redirectAttributes.addFlashAttribute("isSuccess", rowsEffected > 0);
        return "redirect:/home";
    }

    @GetMapping("/remove/{credentialId}")
    public String removeNote(@PathVariable("credentialId") int credentialId,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes) {
        int rowsEffected = credentialService.removeCredential(credentialId, authentication.getName());
        redirectAttributes.addFlashAttribute("isSuccess", rowsEffected > 0);
        return "redirect:/home";
    }
}
