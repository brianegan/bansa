package com.brianegan.rxredux.listOfTrendingGifs;

import static trikita.anvil.BaseDSL.attr;

import com.bumptech.glide.Glide;
import trikita.anvil.Anvil;

import android.view.View;
import android.widget.ImageView;

public class LoadImage {
    public static Void loadImage(String url) {
        return attr(LoadImageFunc.instance, url);
    }

    private final static class LoadImageFunc implements Anvil.AttrFunc<String> {
        private final static LoadImageFunc instance = new LoadImageFunc();
        public void apply(View v, String newUrl, String oldUrl) {
            if (v instanceof ImageView && !newUrl.equals(oldUrl)) {
                Glide.with(v.getContext())
                        .load(newUrl)
                        .into((ImageView) v);
            }
        }
    }
}
