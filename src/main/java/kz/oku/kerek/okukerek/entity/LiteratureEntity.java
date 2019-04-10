package kz.oku.kerek.okukerek.entity;

import kz.oku.kerek.okukerek.model.LiteratureType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.util.Set;

/**
 * This class represents literature mapping into database table
 *
 * @author Dauren Delmukhambetov
 * @since 0.1.0
 */
@Entity
public class LiteratureEntity {

    @Id
    @Column
    protected Long id;

    @Column
    @Enumerated(value = EnumType.STRING)
    protected LiteratureType type;

    @Column
    protected String name;

    @Column
    protected Set<String> authors;

    @Column
    protected String provider;

    @Column
    protected String description;

    @Column
    protected Set<String> topics;

    @Column
    protected String annotation;

    @Column
    protected Set<String> keywords;

    @Column
    protected String isbn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LiteratureType getType() {
        return type;
    }

    public void setType(LiteratureType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<String> authors) {
        this.authors = authors;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<String> getTopics() {
        return topics;
    }

    public void setTopics(Set<String> topics) {
        this.topics = topics;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    public Set<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(Set<String> keywords) {
        this.keywords = keywords;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}
