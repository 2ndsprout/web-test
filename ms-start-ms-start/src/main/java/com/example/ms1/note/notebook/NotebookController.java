package com.example.ms1.note.notebook;

import com.example.ms1.note.note.Note;
import com.example.ms1.note.note.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class NotebookController {

    private final NotebookService notebookService;
    private final NoteService noteService;

    @PostMapping("/books/write")
    public String write () {

        Notebook notebook = this.notebookService.saveDefault();
        Note note = this.noteService.saveDefault();
        notebook.addNote(note);
        this.notebookService.save(notebook);

        return "redirect:/";
    }

    @PostMapping("/groups/books/{notebookId}/write")
    public String groupWrite (@PathVariable Long notebookId) {

        Notebook parent = this.notebookService.getNotebook(notebookId);

        if (parent.getParent() != null) {
            return "redirect:/books/%d".formatted(notebookId);
        }
        Notebook child = this.notebookService.saveDefault();
        Note note = this.noteService.saveDefault();
        child.addNote(note);
        this.notebookService.save(child);

        parent.addChild(child);
        this.notebookService.save(parent);

        return "redirect:/books/%d".formatted(notebookId);
    }

    @GetMapping("/books/{id}")
    public String detail (@PathVariable Long id, Model model) {

        Notebook notebook = this.notebookService.getNotebook(id);
        Long noteId = notebook.getNoteList().getLast().getId();

        return "redirect:/books/%d/notes/%d".formatted(id, noteId);
    }
}
