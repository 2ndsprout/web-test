package com.example.ms1.note.notebook;

import com.example.ms1.note.MainService;
import com.example.ms1.note.note.Note;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class NotebookController {

    private final NotebookService notebookService;
    private final MainService mainService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/books/write")
    public String write () {

        Notebook notebook = this.notebookService.saveDefaultNotebook();

        this.mainService.addToNotebook(notebook);

        return "redirect:/";
    }
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/groups/books/{notebookId}/write")
    public String groupWrite (@PathVariable Long notebookId) {

        Notebook parent = this.notebookService.getNotebook(notebookId);

        if (parent.getParent() != null) {
            return "redirect:/books/%d".formatted(notebookId);
        }
        this.mainService.addToChild(parent);

        return "redirect:/books/%d".formatted(notebookId);
    }

    @GetMapping("/books/{id}")
    public String detail (@PathVariable Long id, Model model) {

        Notebook notebook = this.notebookService.getNotebook(id);
        if (notebook.getNoteList().isEmpty()) {

            this.mainService.addToNotebook(notebook);

            return "redirect:/books/%d".formatted(id);
        }

        Long noteId = notebook.getNoteList().getLast().getId();

        return "redirect:/books/%d/notes/%d".formatted(id, noteId);
    }
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/books/{id}/delete")
    public String delete (@PathVariable Long id) {

        Notebook notebook = this.notebookService.getNotebook(id);

        this.mainService.delete(notebook);

        return "redirect:/";
    }
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/books/{id}/update")
    public String update (@PathVariable Long id,
                          @RequestParam String name) {

        Notebook notebook = this.notebookService.getNotebook(id);

        this.notebookService.update(notebook, name);

        return "redirect:/books/%d".formatted(id);
    }
}
