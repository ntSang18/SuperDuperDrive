package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/note")
public class NoteController {

    private final NoteService noteService;

    @PostMapping("/save")
    public String saveNote(@ModelAttribute Note note,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes
    ) {
        int rowsEffected = noteService.saveNote(note, authentication.getName());
        redirectAttributes.addFlashAttribute("isSuccess", rowsEffected > 0);
        return "redirect:/home";
    }

    @GetMapping("/remove/{noteId}")
    public String removeNote(@PathVariable("noteId") int noteId,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes) {
        int rowsEffected = noteService.removeNote(noteId, authentication.getName());
        redirectAttributes.addFlashAttribute("isSuccess", rowsEffected > 0);
        return "redirect:/home";
    }
}
