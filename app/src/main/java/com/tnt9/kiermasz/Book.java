package com.tnt9.kiermasz;

public class Book {



    private String userId;
    private String title;
    private String author;
    private String imageURL;
    private String publisher;
    private String date;
    private String numberOfPages;
    private String ISBN;
    private String language;
    private String description;
    private double rating;
    private int ratings;

    public Book() {
    }

    public Book(String title, String author) {
        this.title = title;
        this.author = author;
    }

    public Book(String userId, String title, String author, String imageURL) {
        this.userId = userId;
        this.title = title;
        this.author = author;
        this.imageURL = imageURL;
    }

    public Book(String title, String author, String imageURL, String publisher, String date, String numberOfPages, String ISBN, String language, String description) {
        this.title = title;
        this.author = author;
        this.imageURL = imageURL;
        this.publisher = publisher;
        this.date = date;
        this.numberOfPages = numberOfPages;
        this.ISBN = ISBN;
        this.language = language;
        this.description = description;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getDate() {
        return date;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getNumberOfPages() {
        return numberOfPages;
    }

    public String getISBN() {
        return ISBN;
    }

    public String getLanguage() {
        return language;
    }

    public String getDescription() {
        return description;
    }

    public int getRatings() {
        return ratings;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public double getRating() {
        return rating;
    }




}
