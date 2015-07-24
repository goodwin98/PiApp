package ru.goodwin98.pikabuapp;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by goodwin98 on 09.07.2015.
 */
public class HtmlHelper {

    Document doc;
    String prevPage;
    int prevNumPage;
    String PHPSESS;

    public HtmlHelper(String htmlPage) throws IOException
    {
        Connection.Response res;
        prevPage = htmlPage;
        res = Jsoup.connect(htmlPage).method(Connection.Method.GET).execute();
        PHPSESS = res.cookie("PHPSESS");
        prevNumPage = 1;
        doc = res.parse();

    }

    public ArrayList<PikabuPost> getNextPosts() throws IOException, JSONException {

        ArrayList<PikabuPost> PostList = new ArrayList<PikabuPost>();
        Connection.Response res;
        res = Jsoup.connect(prevPage + "?twitmode=1&page=" + ++prevNumPage)
                .method(Connection.Method.GET)
                .cookie("PHPSESS",PHPSESS)
                .header("Accept","application/json, text/javascript, */*; q=0.01")
                .header("X-Csrf-Token",PHPSESS)
                .header("X-Requested-With", "XMLHttpRequest")
                .ignoreContentType(true)
                .execute();
        JSONObject jsonraw = new JSONObject ( res.body());
        JSONArray jsonPosts = jsonraw.getJSONObject("data").getJSONArray("stories");
        for(int i = 0; i < jsonPosts.length() ;i++)
        {
            doc =Jsoup.parseBodyFragment(jsonPosts.getJSONObject(i).getString("html"));
            Element body = doc.body();
            PostList.add(parsePost(body));
        }

        return PostList;
    }

    private PikabuPost parsePost (Element element)
    {
        PikabuPost.Builder result;
        String title= element.getElementsByClass("post_title").get(0).text();
        Element after_title_box = element.getElementsByClass("post_after_title_box").first();

        String date = after_title_box.getElementsByClass("post_date").get(0).text();
        String author = after_title_box.getElementsByClass("post_author").first().text();
        String urlPost = element.getElementsByClass("post_title").first().select("a").attr("href");

        result = new PikabuPost.Builder(urlPost,title,author,date);

        Elements body = element.getElementsByClass("bigtext");
        if(body.size() != 0)
        {
            for (int i = 0; i < body.size(); i++)
            {
                Element e = body.get(i);
                if(e.getElementsByClass("b-video").size() != 0)
                {
                    result.image(e.getElementsByClass("b-video").attr("data-url"));
                    String s = e.getElementsByClass("b-video__preview").first().attr("style");
                    result.image_preview(s.substring(22,s.length() - 3));
                } else
                {
                    result.text(e.html());
                }
            }
        }

        result.isMy(!after_title_box.getElementsByClass("post_tag_my").first().className().contains("hidden"));
        result.isStrawberry(!element.getElementsByClass("post_tag_strawberry").first().className().contains("hidden"));
        result.tags(element.getElementsByClass("post_tags").first().text());

        Elements g = element.getElementsByClass("post_img");
        if ( g.size() != 0 )
            if (g.first().getElementsByClass("b-gifx").size() != 0)
            {
                result.image(g.first().getElementsByClass("b-gifx__player").attr("data-src"));
                result.image_preview(g.first().getElementsByClass("b-gifx__image").attr("src"));
            } else {

                result.image(g.get(1).attr("src"));
            }

        Element action_box = element.getElementsByClass("post_actions_box").first();

        try{
            result.rating(Integer.parseInt(action_box.getElementsByClass("post_rating_count").first().text()));
            result.comments(Integer.parseInt(action_box.getElementsByClass("post_comments_count").first().text().replaceAll("\\D","")));
        } catch (NumberFormatException nfe)
        {
            System.out.println("Could not parse " + nfe);
        }
        return result.build();
    }

    public ArrayList<PikabuPost> getPosts()
    {
        ArrayList<PikabuPost> PostList = new ArrayList<PikabuPost>();
        List<Element> rawPost;
        rawPost = doc.getElementsByClass("post");
        for (int i =0 ; i < rawPost.size() ; i++)
        {
            Element post = rawPost.get(i);

            PostList.add(parsePost(post));

        }

        return PostList;
    }
}
