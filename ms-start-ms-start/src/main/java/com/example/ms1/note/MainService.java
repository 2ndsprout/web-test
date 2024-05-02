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

    public void delete(Notebook notebook) {

        if (notebook.getChildren().isEmpty()) {     // 해당하는 notebook 에 child 가 없으면 deleteNotebook 메서드 실행
            this.deleteNotebook(notebook);
        }
        else {
            this.deleteGroup(notebook);             // 해당하는 notebook 에 child 가 있으면 deleteGroup 메서드 실행
        }
    }

    public void deleteNotebook (Notebook notebook) {

        // 외래키는 자식 엔터티에 붙어있다. 자식먼저 삭제 후 부모를 삭제해야한다.

        List<Note> noteList = notebook.getNoteList(); // 해당하는 notebook 의 noteList 를 불러옴
        for (Note note : noteList) {
            this.noteService.delete(note);            // noteList 의 note 들을 순차적으로 삭제
        }
        this.notebookService.delete(notebook);        // note 들을 전부 삭제 후 해당하는 notebook 삭제
    }

    public void deleteGroup(Notebook parent) {

        List<Notebook> children = parent.getChildren(); // 해당하는 notebook 의 children (자식노트북리스트) 을 불러옴
        for (Notebook child : children) {

            this.deleteNotebook(child);                   // children 의 child(자식노트북) 을 먼저 만들어놓은 deleteNotebook 메서드로
        }                                                 // note 들을 순차적으로 삭제 후 child 도 순차적으로 삭제
        this.deleteNotebook(parent);                      // 모든 child 삭제 후 부모 notebook(parent) 삭제
    }
}
