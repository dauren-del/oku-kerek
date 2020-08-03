package kz.oku.kerek.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class BookDownloadEvent extends ApplicationEvent {

    private final Long bookId;

    public BookDownloadEvent(Object source, Long bookId) {
        super(source);
        this.bookId = bookId;
    }
}
