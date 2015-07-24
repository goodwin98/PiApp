package ru.goodwin98.pikabuapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import ru.goodwin98.pikabuapp.models.PostListAdapter;

import org.jsoup.nodes.Element;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by goodwin98 on 14.07.2015.
 */
public class PostList extends Fragment {

    private ListView listview;
    private ArrayList<PikabuPost> posts;
    private int lastPage;
    private String lastUrl;
    HtmlHelper hh;
    boolean flagLoad;
    PostListAdapter adapter;

    public PostList ()
    {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.post_list,container,false);
        return v;
    }

    @Override
    public void onStart()
    {
        posts = new ArrayList<PikabuPost>();
        //Запускаем парсинг
        new ParseSite().execute("http://m.pikabu.ru/");
        //Находим ListView
        listview = (ListView) getView().findViewById(R.id.listPost);
        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if(firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount!=0 && !flagLoad)
                {
                    flagLoad = true;
                    new LoadSite().execute();
                }

            }
        });

        super.onStart();

    }
    private class LoadSite extends AsyncTask<String, Void, ArrayList<PikabuPost>> {
        //Фоновая операция
        protected ArrayList<PikabuPost> doInBackground(String... arg) {
            ArrayList<PikabuPost> posts = new ArrayList<PikabuPost>();
            try {
                 posts = hh.getNextPosts();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return posts;
        }
        //Событие по окончанию парсинга
        protected void onPostExecute(ArrayList<PikabuPost> output) {

            posts.addAll(output);
            adapter.notifyDataSetChanged();
            flagLoad = false;


        }
    }
    private class ParseSite extends AsyncTask<String, Void, ArrayList<PikabuPost>> {
        //Фоновая операция
        protected ArrayList<PikabuPost> doInBackground(String... arg) {
            ArrayList<PikabuPost> posts = new ArrayList<PikabuPost>();
            try {
                hh = new HtmlHelper(arg[0]);
                posts = hh.getPosts();

            } catch (Exception e) {
                e.printStackTrace(); //TODO обработка ошибок парсинга и подключений
            }
            return posts;
        }
        //Событие по окончанию парсинга
        protected void onPostExecute(ArrayList<PikabuPost> output) {

            //Загружаем в него результат работы doInBackground
            posts.addAll(output);
            adapter = new PostListAdapter(getActivity(),posts);
            listview.setAdapter(adapter);

        }
    }
}
