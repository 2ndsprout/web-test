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

    public Notebook saveNotebook(Notebook notebook) {

        return this.notebookService.save(notebook);
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

        List<Notebook> notebookList = this.notebookService.getList(); // notebookList 불러옴

        if (notebookList.isEmpty()) {                           // notebookList 비어있을때 notebook, note 생성
                                                                // notebook 에 note 추가 하고 해당 notebook db에 저장
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
    public Notebook addToNotebook (Notebook notebook) {

        Note note = this.saveDefaultNote();
        notebook.addNote(note);
        return this.saveNotebook(notebook);
    }
    public Notebook addToChild (Notebook parent) {

        Notebook child = this.notebookService.saveDefaultNotebook();
        Note note = this.saveDefaultNote();
        child.addNote(note);
        this.notebookService.save(child);

        parent.addChild(child);
        return this.notebookService.save(parent);
    }
}
