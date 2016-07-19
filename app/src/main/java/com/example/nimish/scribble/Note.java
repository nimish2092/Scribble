package com.example.nimish.scribble;

/**
 * Created by NIMISH on 02-07-2016.
 */
public class Note {

    private String title,message;
    private long noteid,dateCreatedMilli;
    private Category category;

    public enum Category{ GROCERY, DEADLINE, NOTE}

    public Note(String title,String message,Category category){

        this.title = title;
        this.message = message;
        this.category = category;
        this.noteid = 0;
        this.dateCreatedMilli = 0;
    }

    public Note(String title,String message,Category category,long noteid,long dateCreatedMilli){

        this.title = title;
        this.message = message;
        this.category = category;
        this.noteid = noteid;
        this.dateCreatedMilli = dateCreatedMilli;
    }

    public String getTitle() {
        return title;
    }

    public Category getCategory() {
        return category;
    }

    public String getMessage() {
        return message;
    }

    public long getNoteid() {
        return noteid;
    }

    public long getDateCreatedMilli() {
        return dateCreatedMilli;
    }

    public String toString(){
        return "ID: " + noteid + "Title: " + title + "Message: " + message + "IconID: " + category.name() + "Date: ";
    }

    public int getAssociatedDrawable(){
        return categoryToDrawable(category);
    }

    public static int categoryToDrawable(Category noteCategory){
        switch (noteCategory){
            case GROCERY:return R.drawable.g;
            case DEADLINE:return R.drawable.d;
            case NOTE:return R.drawable.n;
        }

        return R.drawable.n;
    }
}
