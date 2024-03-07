package we.nstu.registration.News;


import java.util.List;

public class News {
    private String newsTitle;
    private String newsText;
    private String newsTime;
    private List<String> iamgesURL;
    private String newsID;

    public News(String newsTitle, String newsText, String newsTime, List<String> iamgesURL, String newsID) {
        this.newsTitle = newsTitle;
        this.newsText = newsText;
        this.newsTime = newsTime;
        this.iamgesURL = iamgesURL;
        this.newsID = newsID;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getNewsText() {
        return newsText;
    }

    public void setNewsText(String newsText) {
        this.newsText = newsText;
    }

    public String getNewsTime() {
        return newsTime;
    }

    public void setNewsTime(String newsTime) {
        this.newsTime = newsTime;
    }

    public List<String> getIamgesURL() {
        return iamgesURL;
    }

    public void setIamgesURL(List<String> iamgesURL) {
        this.iamgesURL = iamgesURL;
    }

    public String getNewsID() {
        return newsID;
    }

    public void setNewsID(String newsID) {
        this.newsID = newsID;
    }
}