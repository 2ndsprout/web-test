package com.example.ms1.note;

import com.example.ms1.note.note.Note;
import com.example.ms1.note.note.NoteService;
import com.example.ms1.note.notebook.Notebook;
import com.example.ms1.note.notebook.NotebookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MainService {

    private final NoteService noteService;
    private final NotebookService notebookService;

    public Notebook getNotebook (Long id) {

        return this.notebookService.getNotebook(id);
    }

    public Note getNote (Long id) {
        return this.noteService.getNote(id);
    }

    public void saveNotebook(Notebook notebook) {

        this.notebookService.save(notebook);
    }

    public List<Notebook> getNotebookList () {
        return this.notebookService.getList();
    }

    public Note saveDefaultNote() {

        return this.noteService.saveDefaultNote();
    }

    public Notebook saveDefaultNotebook() {

        return this.notebookService.saveDefaultNotebook();
    }

    public MainDataDto getDefaultMainData() {

        List<Notebook> notebookList = this.notebookService.getList();
        if (notebookList.isEmpty()) {

            Notebook notebook = this.saveDefaultNotebook();
            Note note = this.saveDefaultNote();
            notebook.addNote(note);
            this.notebookService.save(notebook);
        }
        Notebook targetNotebook = notebookList.getLast();
        List<Note> noteList = targetNotebook.getNoteList();
        Note targetNote = noteList.getLast();

        return new MainDataDto(notebookList, targetNotebook, noteList, targetNote);
    }

    public MainDataDto getMainData (Long notebookId, Long noteId) {

        MainDataDto mainDataDto = this.getDefaultMainData();

        Notebook targetNotebook = this.getNotebook(notebookId);
        List<Note> noteList = targetNotebook.getNoteList();
        Note targetNote = this.getNote(noteId);

        mainDataDto.setTargetNotebook(targetNotebook);
        mainDataDto.setNoteList(noteList);
        mainDataDto.setTargetNote(targetNote);

        return mainDataDto;
    }
}
