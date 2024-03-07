package we.nstu.registration.News;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class SchoolNews
{
    private List<News> newsList;

    public SchoolNews(List<News> newsList) {
        this.newsList = newsList;
    }

    public List<News> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<News> newsList) {
        this.newsList = newsList;
    }

    public String newsToJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static SchoolNews newsFromJson(String json) {
        Gson gson = new Gson();
        Type schoolNewsType = new TypeToken<SchoolNews>(){}.getType();
        return gson.fromJson(json, schoolNewsType);
    }

}
