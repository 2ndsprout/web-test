package com.example.ms1.note.note;

import com.example.ms1.note.MainDataDto;
import com.example.ms1.note.MainService;
import com.example.ms1.note.notebook.Notebook;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/books/{notebookId}/notes")
public class NoteController {

    private final NoteService noteService;
    private final MainService mainService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/write")
    public String write(@PathVariable Long notebookId) {

        Notebook notebook = this.mainService.getNotebook(notebookId);
        this.mainService.addToNotebook(notebook);

        return "redirect:/books/%d".formatted(notebookId);
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long notebookId,
                         @PathVariable Long id, Model model) {

        MainDataDto mainDataDto = this.mainService.getMainData(notebookId, id);

        model.addAttribute("mainDataDto", mainDataDto);
        return "main";
    }
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/update")
    public String update(@PathVariable Long notebookId,
                         @PathVariable Long id,
                         @RequestParam String title,
                         @RequestParam String content) {

        Note note = this.noteService.getNote(id);
        this.noteService.update(note, title, content);

        return "redirect:/books/%d/notes/%d".formatted(notebookId, id);

    }
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/delete")
    public String delete (@PathVariable Long notebookId,
                          @PathVariable Long id) {

        Note note = this.noteService.getNote(id);
        this.noteService.delete(note);

        return "redirect:/books/%d".formatted(notebookId);
    }
}
