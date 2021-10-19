/*
 * Created by Dedi Nopriadi on 10/14/21, 3:45 PM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 10/14/21, 3:44 PM
 */

package nopriadi.dedi.pokemon.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;

import nopriadi.dedi.pokemon.R;
import nopriadi.dedi.pokemon.adapter.PokemonAdapter;
import nopriadi.dedi.pokemon.data.PokemonData;
import nopriadi.dedi.pokemon.helper.DatabaseHandler;
import nopriadi.dedi.pokemon.utils.Tools;

public class MyPokeListActivity extends AppCompatActivity {

    private Context context;
    private ProgressDialog pDialog;
    GridView list;
    SwipeRefreshLayout swipe;
    List<PokemonData> itemList = new ArrayList<PokemonData>();

    PokemonAdapter adapter;
    DatabaseHandler dbHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poke_list);
        context = MyPokeListActivity.this;

        pDialog = new ProgressDialog(context);
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        list = (GridView) findViewById(R.id.grid_pokemon);
        itemList.clear();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String idx = itemList.get(position).getPokeId();
                String namex = itemList.get(position).getPokeName();
                String urlx = itemList.get(position).getPokeUrl();

                menuDialog(idx, urlx, namex);
            }
        });

        dbHandler = new DatabaseHandler(getApplicationContext());
        itemList = dbHandler.getAllRecord();

        getMyList();

        swipe.post(new Runnable() {
                       @Override
                       public void run() {
                           adapter.notifyDataSetChanged();
                       }
                   }
        );
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(itemList.size() > 0) {
                    adapter.notifyDataSetChanged();
                }
            }
        },2000);
    }

    public void getMyList() {
        itemList = dbHandler.getAllRecord();

        adapter = new PokemonAdapter(MyPokeListActivity.this, itemList);
        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void menuDialog(String id, String url, String name) {
        final CharSequence[] items = {"View Detail", "Release Pokémon",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(MyPokeListActivity.this);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("View Detail")) {
                    Intent intent = new Intent(MyPokeListActivity.this, PokeDetailActivity.class);
                    intent.putExtra(PokeListActivity.TAG_ID, id);
                    intent.putExtra(PokeListActivity.TAG_URL, url);
                    intent.putExtra(PokeListActivity.TAG_NAME, name);
                    startActivity(intent);
                } else if (items[item].equals("Release Pokémon")) {
                    releaseConfir(id);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }

    public void releaseConfir(String id){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Are you sure want to release this Pokémon?");
        builder.setCancelable(false);
        builder.setTitle("Release");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dbHandler.deleteModel(id);
                getMyList();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
