/*
 * Created by Dedi Nopriadi on 10/15/21, 3:15 AM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 10/15/21, 3:15 AM
 */

package nopriadi.dedi.pokemon.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

import nopriadi.dedi.pokemon.R;
import nopriadi.dedi.pokemon.data.PokemonData;
import nopriadi.dedi.pokemon.utils.Tools;

public class PokemonAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<PokemonData> items;

    public PokemonAdapter(Activity activity, List<PokemonData> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int location) {
        return items.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.item_pokemon, null);

        TextView pokeName   = (TextView) convertView.findViewById(R.id.txt_item_poke);
        CardView pokeCard   = (CardView) convertView.findViewById(R.id.card_item_poke);
        ImageView pokeImage = (ImageView) convertView.findViewById(R.id.img_item_poke);

        PokemonData poke = items.get(position);

        pokeName.setText(Tools.Capitalize(poke.getPokeName()));

        if( poke.getPokeImage() != null || !poke.getPokeImage().equals(null) || !poke.getPokeImage().equals("null") || TextUtils.isEmpty(poke.getPokeImage())){
            Picasso.get()
                    .load(poke.getPokeImage())
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            pokeImage.setImageBitmap(bitmap);
                            pokeCard.setCardBackgroundColor(Tools.getDominantColor(bitmap));
                        }

                        @Override
                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });
        }

        return convertView;
    }
}
