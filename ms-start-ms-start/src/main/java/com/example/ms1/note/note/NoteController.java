package com.example.ms1.note.note;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    @RequestMapping("/test")
    @ResponseBody
    public String test() {
        return "test";
    }

    @RequestMapping("/")
    public String main(Model model) {

        List<Note> noteList = this.noteService.getList();

        if(noteList.isEmpty()) {
            Note note = this.noteService.saveDefault();
        }

        model.addAttribute("noteList", noteList);
        model.addAttribute("targetNote", noteList.get(0));

        return "main";
    }

    @PostMapping("/write")
    public String write() {
        this.noteService.saveDefault();

        return "redirect:/";
    }

    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable Long id) {

        Note note = this.noteService.getNote(id);
        model.addAttribute("targetNote", note);
        model.addAttribute("noteList", this.noteService.getList());

        return "main";
    }
    @PostMapping("/update")
    public String update(Long id, String title, String content) {

        Note note = this.noteService.getNote(id);
        this.noteService.update(note, title, content);

        return "redirect:/detail/" + id;
    }
    @PostMapping("/delete/{id}")
    public String delete (@PathVariable Long id) {
        Note note = this.noteService.getNote(id);
        this.noteService.delete(note);

        return "redirect:/";
    }
}
