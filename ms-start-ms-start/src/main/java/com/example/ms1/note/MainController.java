package com.example.ms1.note;

import com.example.ms1.note.note.Note;
import com.example.ms1.note.note.NoteService;
import com.example.ms1.note.notebook.Notebook;
import com.example.ms1.note.notebook.NotebookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final NoteService noteService;
    private final NotebookService notebookService;

    @RequestMapping("/")
    public String main(Model model) {

        List<Notebook> notebookList = this.notebookService.getList();
        if (notebookList.isEmpty()) {

            Notebook notebook = this.notebookService.saveDefault();
            Note note = this.noteService.saveDefault();
            notebook.addNote(note);
            this.notebookService.save(notebook);

            return "redirect:/";
        }

        model.addAttribute("notebookList",notebookList);
        model.addAttribute("targetNotebook",notebookList.getLast());
        model.addAttribute("noteList", notebookList.getLast().getNoteList());
        model.addAttribute("targetNote", notebookList.getLast().getNoteList().getLast());

        return "main";
    }

}
