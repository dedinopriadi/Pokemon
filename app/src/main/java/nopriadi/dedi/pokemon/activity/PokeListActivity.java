/*
 * Created by Dedi Nopriadi on 10/14/21, 7:29 PM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 10/14/21, 7:28 PM
 */

package nopriadi.dedi.pokemon.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.sargunvohra.lib.pokekotlin.client.PokeApi;
import me.sargunvohra.lib.pokekotlin.client.PokeApiClient;
import me.sargunvohra.lib.pokekotlin.model.Move;
import me.sargunvohra.lib.pokekotlin.model.PokemonMove;
import me.sargunvohra.lib.pokekotlin.model.PokemonSpecies;
import me.sargunvohra.lib.pokekotlin.model.PokemonType;
import me.sargunvohra.lib.pokekotlin.model.Type;
import nopriadi.dedi.pokemon.R;
import nopriadi.dedi.pokemon.adapter.PokemonAdapter;
import nopriadi.dedi.pokemon.api.Client;
import nopriadi.dedi.pokemon.api.Service;
import nopriadi.dedi.pokemon.data.PokemonData;
import nopriadi.dedi.pokemon.utils.Constanta;
import nopriadi.dedi.pokemon.utils.Tools;

public class PokeListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private Context context;
    private ProgressDialog pDialog;
    GridView list;
    SwipeRefreshLayout swipe;
    List<PokemonData> itemList = new ArrayList<PokemonData>();

    private int offSet = 0;
    PokemonAdapter adapter;

    Handler handler;
    Runnable runnable;

    public static final String TAG_ID      = "id";
    public static final String TAG_URL     = "url";
    public static final String TAG_NAME    = "name";
    public static final String TAG_COUNT   = "count";
    public static final String TAG_RESULTS = "results";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poke_list);
        context = PokeListActivity.this;

        pDialog = new ProgressDialog(context);
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        list = (GridView) findViewById(R.id.grid_pokemon);
        itemList.clear();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(PokeListActivity.this, PokeDetailActivity.class);
                intent.putExtra(TAG_ID, itemList.get(position).getPokeId());
                intent.putExtra(TAG_URL, itemList.get(position).getPokeUrl());
                intent.putExtra(TAG_NAME, itemList.get(position).getPokeName());
                startActivity(intent);
            }
        });

        adapter = new PokemonAdapter(PokeListActivity.this, itemList);
        list.setAdapter(adapter);

        swipe.setOnRefreshListener(this);

        swipe.post(new Runnable() {
                       @Override
                       public void run() {
                           swipe.setRefreshing(true);
                           itemList.clear();
                           adapter.notifyDataSetChanged();
                           getPokemon(0);
                       }
                   }
        );

        list.setOnScrollListener(new AbsListView.OnScrollListener() {

            private int currentVisibleItemCount;
            private int currentScrollState;
            private int currentFirstVisibleItem;
            private int totalItem;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                this.currentScrollState = scrollState;
                this.isScrollCompleted();
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                this.currentFirstVisibleItem = firstVisibleItem;
                this.currentVisibleItemCount = visibleItemCount;
                this.totalItem = totalItemCount;
            }

            private void isScrollCompleted() {
                if (totalItem - currentFirstVisibleItem == currentVisibleItemCount
                        && this.currentScrollState == SCROLL_STATE_IDLE) {

                    swipe.setRefreshing(true);
                    handler = new Handler();

                    runnable = new Runnable() {
                        public void run() {
                            getPokemon(offSet);
                        }
                    };

                    handler.postDelayed(runnable, 3000);
                }
            }

        });

    }

    @Override
    public void onRefresh() {
        itemList.clear();
        adapter.notifyDataSetChanged();
        offSet = 0;
        getPokemon(0);
    }

    private void getPokemon(int num_offSet)
    {
        HashMap<String, String> values = new HashMap<>();
        values.put("offset", String.valueOf(num_offSet));
        values.put("limit", Constanta.Values.LIMIT.toString());

        callApiPokemon(values);
    }

    private void callApiPokemon(HashMap params) {
        swipe.setRefreshing(true);
        Service service = Client.initialize(Constanta.Url.APP_SERVICE);
        Client.request(service.getPokemon(params), new Client.CallBackRequest() {

            @Override
            public void successCode(String message, JSONObject rd) {
                swipe.setRefreshing(false);

                try {

                    Integer count  = rd.getInt(TAG_COUNT);
                    JSONArray arrdata = null;

                    try {

                        arrdata = rd.getJSONArray(TAG_RESULTS);
                        if (arrdata.length() > 0) {
                            for (int i = 0; i < arrdata.length(); i++) {
                                JSONObject objdata = arrdata.getJSONObject(i);

                                PokemonData data = new PokemonData();

                                data.setPokeId(Tools.getIdPoke(objdata.getString(TAG_URL)));
                                data.setPokeImage(Tools.getUrlImage(objdata.getString(TAG_URL)));
                                data.setPokeName(objdata.getString(TAG_NAME));
                                data.setPokeUrl(objdata.getString(TAG_URL));

                                itemList.add(data);

                                if (count > offSet)
                                    offSet ++;

                                Log.d("offSet", String.valueOf(offSet));

                                adapter.notifyDataSetChanged();
                            }
                        }

                    } catch (JSONException e) {
                        Tools.errorMessage(context, Constanta.Message.ERROR_PARSING);
                        e.printStackTrace();
                    }

                } catch (JSONException e) {
                    Tools.errorMessage(context, Constanta.Message.ERROR_PARSING);
                    e.printStackTrace();
                }

            }

            @Override
            public void unsuccessResponse(String message) {
                swipe.setRefreshing(false);
                Tools.errorMessage(context, message);
            }

            @Override
            public void parsingError(String message) {
                swipe.setRefreshing(false);
                Tools.errorMessage(context, message);
            }

            @Override
            public void otherError(String message) {
                swipe.setRefreshing(false);
                Tools.errorMessage(context, message);
            }

            @Override
            public void failure(String message) {
                swipe.setRefreshing(false);
                Tools.errorMessage(context, message);
            }
        });
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}