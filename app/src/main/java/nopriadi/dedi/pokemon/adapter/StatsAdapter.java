/*
 * Created by Dedi Nopriadi on 10/14/21, 1:50 PM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 10/14/21, 1:49 PM
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
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.timqi.sectorprogressview.ColorfulRingProgressView;

import java.util.List;

import nopriadi.dedi.pokemon.R;
import nopriadi.dedi.pokemon.data.PokemonData;
import nopriadi.dedi.pokemon.data.StatsData;
import nopriadi.dedi.pokemon.utils.Tools;

public class StatsAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<StatsData> items;

    public StatsAdapter(Activity activity, List<StatsData> items) {
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
            convertView = inflater.inflate(R.layout.item_stats, null);

        TextView pokeName = (TextView) convertView.findViewById(R.id.txt_item_stats);
        ColorfulRingProgressView statsPoke = (ColorfulRingProgressView) convertView.findViewById(R.id.circle_item_stats);

        StatsData stat = items.get(position);

        pokeName.setText(Tools.Capitalize(stat.getStatsName()));
        statsPoke.setPercent(stat.getStatsPercen());

        return convertView;
    }
}
