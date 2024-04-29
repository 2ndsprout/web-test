package com.example.ms1.note.note;

import com.example.ms1.note.notebook.Notebook;
import com.example.ms1.note.notebook.NotebookService;
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
    private final NotebookService notebookService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/write")
    public String write(@PathVariable Long notebookId) {

        Notebook notebook = this.notebookService.getNotebook(notebookId);
        Note note = this.noteService.saveDefault();
        notebook.addNote(note);
        this.notebookService.save(notebook);

        return "redirect:/books/%d".formatted(notebookId);
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long notebookId,
                         @PathVariable Long id, Model model) {

        List<Notebook> notebookList = this.notebookService.getList();
        Notebook targetNotebook = this.notebookService.getNotebook(notebookId);
        Note targetNote = this.noteService.getNote(id);

        model.addAttribute("notebookList",notebookList);
        model.addAttribute("targetNotebook",targetNotebook);
        model.addAttribute("noteList", targetNotebook.getNoteList());
        model.addAttribute("targetNote", targetNote);

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
