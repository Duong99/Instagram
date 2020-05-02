package com.example.instagram.adapter.adapterfirebase;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.example.instagram.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ViewPageAdapter extends PagerAdapter {
    private ArrayList<String> listUrlPicture;
    private Context context;
    private SubsamplingScaleImageView imvWatchViewPage;

    public ViewPageAdapter(ArrayList<String> listUrlPicture, Context context) {
        this.listUrlPicture = listUrlPicture;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listUrlPicture.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_viewpager_imageview , null);
        imvWatchViewPage = view.findViewById(R.id.imvWatchViewPage);

        new LoadImageViewPage().execute(listUrlPicture.get(position));


        container.addView(view);
        return view;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    private class LoadImageViewPage extends AsyncTask<String, Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(String... strings) {
            String url = strings[0];
            Bitmap bitmap = null;
            try {
                InputStream inputStream = new URL(url).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imvWatchViewPage.setImage(ImageSource.bitmap(bitmap));
        }
    }
}
