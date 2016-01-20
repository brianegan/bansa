package com.brianegan.rxredux.listOfTrendingGifs;

import static trikita.anvil.BaseDSL.attr;

import com.squareup.picasso.Picasso;
import trikita.anvil.Anvil;

import android.view.View;
import android.widget.ImageView;

public class LoadImage {
    public static Void loadImage(String url) {
        return attr(LoadImageFunc.instance, url);
    }

    private final static class LoadImageFunc implements Anvil.AttrFunc<String> {
        private final static LoadImageFunc instance = new LoadImageFunc();

        public void apply(View view, String newUrl, String oldUrl) {
            if (view instanceof ImageView && !newUrl.equals(oldUrl)) {
                Picasso.with(view.getContext())
                        .load(newUrl)
                        .into((ImageView) view);
            }
        }
    }
}
