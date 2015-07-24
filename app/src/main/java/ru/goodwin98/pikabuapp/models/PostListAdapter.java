package ru.goodwin98.pikabuapp.models;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import ru.goodwin98.pikabuapp.PikabuPost;
import ru.goodwin98.pikabuapp.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

/**
 * Created by goodwin98 on 14.07.2015.
 */
public class PostListAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<PikabuPost> objects;

    public PostListAdapter(Context context,ArrayList<PikabuPost> posts)
    {
        ctx = context;
        objects = posts;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    // кол-во элементов
    @Override
    public int getCount() {
        return objects.size();
    }

    // элемент по позиции
    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }
    // id по позиции
    @Override
    public long getItemId(int position) {
        return position; //TODO get id post
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        final ViewHolder viewHolder;
        // используем созданные, но не используемые view
        View view = convertView;
        final ImageView imageView;
        if (view == null) {
            view = lInflater.inflate(R.layout.small_post, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.author = (TextView) view.findViewById(R.id.author);
            viewHolder.big_text = (TextView) view.findViewById(R.id.big_text);
            viewHolder.date = (TextView) view.findViewById(R.id.date);
            viewHolder.tags = (TextView) view.findViewById(R.id.tags);
            viewHolder.title = (TextView) view.findViewById(R.id.title);
            viewHolder.imageView = (ImageView)view.findViewById(R.id.image_post);
            view.setTag(viewHolder);
        } else
        {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.imageView.setVisibility(ImageView.GONE);
        viewHolder.big_text.setVisibility(TextView.GONE);
        PikabuPost p = objects.get(position);

        viewHolder.title.setText(p.getTitle());
        viewHolder.author.setText(p.getAuthor());
        viewHolder.date.setText(p.getDate());
        viewHolder.tags.setText(p.getTags());
        if (p.getBigText() != null) {
            viewHolder.big_text.setText(Html.fromHtml(p.getBigText()));
            viewHolder.big_text.setVisibility(TextView.VISIBLE);
        } else {
            viewHolder.big_text.setText("");
        }

        if (p.getImage() != null) {
            Picasso.with(ctx).setIndicatorsEnabled(true);
            Transformation transformation = new Transformation() {

                @Override
                public Bitmap transform(Bitmap source) {
                    int targetWidth = viewHolder.imageView.getWidth();

                    double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
                    int targetHeight = (int) (targetWidth * aspectRatio);
                    Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, true);
                    if(targetHeight > 1000)
                        result = Bitmap.createBitmap(result,0,0,targetWidth,1000);
                    if (result != source) {
                        // Same bitmap is returned if sizes are the same
                        source.recycle();
                    }
                    return result;
                }

                @Override
                public String key() {
                    return "transformation" + " desiredWidth";
                }
            };

            if (p.getImagePreview() == null) {
                Picasso.with(ctx).load(p.getImage()).transform(transformation).into(viewHolder.imageView);
            } else {
                Picasso.with(ctx).load(p.getImagePreview()).transform(transformation).into(viewHolder.imageView);
            }
            viewHolder.imageView.setVisibility(ImageView.VISIBLE);
        } else
        {
            viewHolder.imageView.setImageDrawable(null);
        }


        return view;
    }
    static class ViewHolder {
        TextView big_text;
        TextView author;
        TextView date;
        TextView tags;
        TextView title;
        ImageView imageView;
    }
}
