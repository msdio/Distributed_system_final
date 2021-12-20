package com.example.cdstermproject;

public class Together_group_list {
    private int poster;
    private String movieName;
    private String grade;

    public Together_group_list(int poster, String movieName, String grade){
        this.poster = poster;
        this.movieName = movieName;
        this.grade = grade;
    }

    public int getPoster()
    {
        return this.poster;
    }

    public String getMovieName()
    {
        return this.movieName;
    }

    public String getGrade()
    {
        return this.grade;
    }
}