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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(String numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getRatings() {
        return ratings;
    }

    public void setRatings(int ratings) {
        this.ratings = ratings;
    }
}
