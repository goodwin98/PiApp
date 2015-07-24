package ru.goodwin98.pikabuapp;


import android.content.Intent;

/**
 * Created by goodwin98 on 09.07.2015.
 */
public class PikabuPost {
    private final String BigText;
    private final String Tags;
    private final String Title;
    private final String Author;
    private final String Image;
    private final String ImagePreview;
    private final String UrlPost;
    private final boolean isMy;
    private final boolean isStrawberry;
    private final String Date;
    private int Rating;
    private int CommentsCount;


    public static class Builder{
        private final String UrlPost;
        private final String Title;
        private final String Author;
        private final String Date;

        private String Image = null;
        private String ImagePreview = null;
        private String BigText = null;
        private String Tags = null;
        private boolean isMy = false;
        private boolean isStrawberry = false;
        private int Rating = 0;
        private int CommentsCount = 0;

        public Builder (String UrlPost, String Title, String Author, String Date)
        {
            this.UrlPost = UrlPost;
            this.Title = Title;
            this.Author = Author;
            this.Date = Date;
        }

        public Builder image(String Image)
        {
            this.Image = Image;
            return this;
        }
        public Builder image_preview(String ImagePreview)
        {
            this.ImagePreview = ImagePreview;
            return this;
        }
        public Builder text(String BigText)
        {
            this.BigText = BigText;
            return this;
        }
        public Builder tags(String Tags)
        {
            this.Tags = Tags;
            return this;
        }
        public Builder isMy(boolean isMy)
        {
            this.isMy = isMy;
            return this;
        }
        public Builder isStrawberry(boolean isStrawberry)
        {
            this.isStrawberry = isStrawberry;
            return this;
        }
        public Builder rating(int Rating)
        {
            this.Rating = Rating;
            return this;
        }
        public Builder comments(int CommentsCount)
        {
            this.CommentsCount = CommentsCount;
            return this;
        }
        public PikabuPost build() {
            return new PikabuPost(this);
        }


    }


    public PikabuPost (Builder builder)
    {
        UrlPost = builder.UrlPost;
        Title = builder.Title;
        Author = builder.Author;
        Date = builder.Date;
        Image = builder.Image;
        BigText = builder.BigText;
        Tags = builder.Tags;
        isMy = builder.isMy;
        isStrawberry = builder.isStrawberry;
        Rating = builder.Rating;
        CommentsCount = builder.CommentsCount;
        ImagePreview = builder.ImagePreview;
    }


    public String getBigText() {
        return BigText;
    }

    public String getTags() {
        return Tags;
    }

    public String getTitle() {
        return Title;
    }

    public String getAuthor() {
        return Author;
    }

    public String getImage() {
        return Image;
    }

    public boolean isMy() {
        return isMy;
    }

    public boolean isStrawberry() {
        return isStrawberry;
    }

    public String getDate() {
        return Date;
    }

    public int getRating() {
        return Rating;
    }

    public int getCommentsCount() {
        return CommentsCount;
    }

    public String getUrlPost() {
        return UrlPost;
    }

    public String getImagePreview() {
        return ImagePreview;
    }

}
