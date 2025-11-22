package To_Do_Liste.webtech;

public class ToDo {

    private Long id;
    private String text;
    private boolean done;

    public ToDo() {
        // Leerer Konstruktor (wichtig für JSON-Serialisierung)
    }

    public ToDo(Long id, String text, boolean done) {
        this.id = id;
        this.text = text;
        this.done = done;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
