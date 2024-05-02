package com.example.ms1.note.notebook;

import com.example.ms1.note.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotebookService {

    private final NotebookRepository notebookRepository;

    public List<Notebook> getList () {
        return this.notebookRepository.findAll();
    }

    public Notebook saveDefaultNotebook() {
        Notebook notebook = new Notebook();

        notebook.setName("새노트");
        return this.notebookRepository.save(notebook);
    }
    public Notebook save (Notebook notebook) {
       return this.notebookRepository.save(notebook);
    }
    public void delete (Notebook notebook) {
        this.notebookRepository.delete(notebook);
    }
    public void update (Notebook notebook, String name) {
        if (name.trim().isEmpty()) {
            name = "제목없음";
        }

        notebook.setName(name);
        this.notebookRepository.save(notebook);
    }
    public Notebook getNotebook (Long id) {
        Optional<Notebook> notebook = this.notebookRepository.findById(id);
        if (notebook.isPresent()) {
            return notebook.get();
        }
        else {
            throw new DataNotFoundException("notebook not found");
        }
    }
}
