/*
 * Created by Dedi Nopriadi on 10/14/21, 7:30 PM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 10/14/21, 7:30 PM
 */

package nopriadi.dedi.pokemon.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.SplittableRandom;

import nopriadi.dedi.pokemon.MainActivity;
import nopriadi.dedi.pokemon.R;
import nopriadi.dedi.pokemon.adapter.PokemonAdapter;
import nopriadi.dedi.pokemon.adapter.StatsAdapter;
import nopriadi.dedi.pokemon.api.Client;
import nopriadi.dedi.pokemon.api.Service;
import nopriadi.dedi.pokemon.data.PokemonData;
import nopriadi.dedi.pokemon.data.StatsData;
import nopriadi.dedi.pokemon.helper.DatabaseHandler;
import nopriadi.dedi.pokemon.utils.Constanta;
import nopriadi.dedi.pokemon.utils.Tools;

public class PokeDetailActivity extends AppCompatActivity {

    private Context context;
    private ProgressDialog pDialog;
    private RelativeLayout layDetail;
    private ImageView imgPoke;
    private ImageView imgBack;
    private ImageView imgFront;
    private TextView txtName;
    private TextView txtType;
    private TextView txtWeight;
    private TextView txtHeight;
    private TextView btnCatch;

    private String pokeId;
    private String pokeName;
    private String Url;

    DatabaseHandler dbHandler;
    AlertDialog myDialog;
    AlertDialog.Builder imageDialog;
    List<StatsData> itemList = new ArrayList<StatsData>();
    StatsAdapter adapter;
    GridView list;

    public static final String TAG_TYPE = "types";
    public static final String TAG_STATS = "stats";
    public static final String TAG_HEIGHT = "height";
    public static final String TAG_WEIGHT = "weight";
    public static final String TAG_SPRITES = "sprites";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poke_detail);
        context = PokeDetailActivity.this;

        Bundle b = getIntent().getExtras();
        Url = (String) b.getCharSequence(PokeListActivity.TAG_URL);
        pokeId = (String) b.getCharSequence(PokeListActivity.TAG_ID);
        pokeName = (String) b.getCharSequence(PokeListActivity.TAG_NAME);

        pDialog = new ProgressDialog(context);
        layDetail   = (RelativeLayout) findViewById(R.id.lay_detail);
        imgPoke     = (ImageView) findViewById(R.id.img_detail_pokemon);
        imgBack     = (ImageView) findViewById(R.id.img_detail_spritest_back);
        imgFront    = (ImageView) findViewById(R.id.img_detail_sprites_front);
        txtName     = (TextView) findViewById(R.id.txt_detail_name);
        txtType     = (TextView) findViewById(R.id.txt_detail_type);
        txtHeight   = (TextView) findViewById(R.id.txt_detail_height);
        txtWeight   = (TextView) findViewById(R.id.txt_detail_weight);
        btnCatch    = (TextView) findViewById(R.id.btn_catch_poke);

        txtName.setText(Tools.Capitalize(pokeName));

        getImagePoke(Url);
        callApiDetail(pokeId);

        list = (GridView) findViewById(R.id.grid_detail_stat);
        itemList.clear();

        adapter = new StatsAdapter(PokeDetailActivity.this, itemList);
        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        dbHandler = new DatabaseHandler(getApplicationContext());

        chechMyList(pokeId);

    }

    private void chechMyList(String id) {
        Cursor poke = dbHandler.getSinglePoke(id);
        if ((poke != null) && (poke.getCount() > 0)) {
            btnCatch.setText("You already have this Pokémon");
            btnCatch.setEnabled(false);
        } else {
            btnCatch.setText("Catch Pokémon");
            btnCatch.setEnabled(true);
        }
    }

    private void getImagePoke(String url) {
        if(url.trim().length() > 0){
            Picasso.get()
                    .load(Tools.getUrlImage(url))
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            imgPoke.setImageBitmap(bitmap);
                            layDetail.setBackgroundColor(Tools.getDominantColor(bitmap));
                        }

                        @Override
                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });
        }
    }

    private void callApiDetail(String id) {
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        showDialog();
        Service service = Client.initialize(Constanta.Url.APP_SERVICE);
        Client.request(service.getDetailPokemon(id), new Client.CallBackRequest() {

            @Override
            public void successCode(String message, JSONObject rd) {
               hideDialog();

                try {

                    double height = rd.getDouble(TAG_HEIGHT);
                    double weight = rd.getDouble(TAG_WEIGHT);
                    JSONArray types = rd.getJSONArray(TAG_TYPE);
                    JSONArray stats = rd.getJSONArray(TAG_STATS);
                    JSONObject sprites = rd.getJSONObject(TAG_SPRITES);

                    txtWeight.setText(String.valueOf(weight/10) + " Kg");
                    txtHeight.setText(String.valueOf(height/10) + " Metre");

                    try {
                        if (types.length() > 0) {
                            String pokeType = "| ";
                            for (int i = 0; i < types.length(); i++) {
                                JSONObject objdata = types.getJSONObject(i);

                                JSONObject type = objdata.getJSONObject("type");
                                pokeType += Tools.Capitalize(type.getString("name")) + " | ";
                            }
                            txtType.setText(pokeType);
                        }
                    } catch (JSONException e) {
                        Tools.errorMessage(context, Constanta.Message.ERROR_PARSING);
                        e.printStackTrace();
                    }

                    try {
                        String back = sprites.getString("back_default");
                        String front = sprites.getString("front_default");

                        if(!back.equals(null) || !back.equals("null") || back != null || TextUtils.isEmpty(back) || back.trim().length() > 0){
                            Picasso.get()
                                    .load(back)
                                    .into(imgBack);
                        }

                        if(!front.equals(null) || !front.equals("null") || front != null || TextUtils.isEmpty(front) || front.trim().length() > 0){
                            Picasso.get()
                                    .load(front)
                                    .into(imgFront);
                        }

                    } catch (JSONException e) {
                        Tools.errorMessage(context, Constanta.Message.ERROR_PARSING);
                        e.printStackTrace();
                    }

                    try {
                        if (stats.length() > 0) {
                            for (int i = 0; i < stats.length(); i++) {
                                JSONObject objdata = stats.getJSONObject(i);

                                StatsData data = new StatsData();

                                data.setStatsPercen(objdata.getInt("base_stat"));
                                JSONObject statistic = objdata.getJSONObject("stat");
                                data.setStatsName(Tools.Capitalize(statistic.getString("name").replace("-", " ")));

                                itemList.add(data);
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
                hideDialog();
                Tools.errorMessage(context, message);
            }

            @Override
            public void parsingError(String message) {
                hideDialog();
                Tools.errorMessage(context, message);
            }

            @Override
            public void otherError(String message) {
                hideDialog();
                Tools.errorMessage(context, message);
            }

            @Override
            public void failure(String message) {
                hideDialog();
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

    public void catchPoke(View view) {
        popUp();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    SplittableRandom random = new SplittableRandom();
                    boolean whoKnows = random.nextInt(1, 101) <= 50;
                    if (whoKnows == false) {
                        Tools.warningMessage(context, "You Failed to Beat Pokémon");
                    } else {
                        showNameDialog(context);

                    }
                } else {
                    Tools.warningMessage(context, "Android version must be at least version N");
                }
                myDialog.dismiss();
            }
        },2000);
    }

    private void showNameDialog(Context c) {
        final EditText taskEditText = new EditText(c);
        @SuppressLint("RestrictedApi") AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Pokémon")
                .setMessage("Give a name for your Pokémon")
                .setView(taskEditText, 50, 0, 50, 0)
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String task = String.valueOf(taskEditText.getText());
                        savePoke(task);
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    private void savePoke(String name) {
        HashMap<String, String> pokeMap = new HashMap<String, String>();
        pokeMap.put("id", pokeId);
        pokeMap.put("name", name);
        pokeMap.put("url", Url);
        pokeMap.put("img", Tools.getUrlImage(Url));

        dbHandler.addRecord(pokeMap);
        Tools.successMessage(context, "You caught a Pokémon");

        chechMyList(pokeId);
    }

    private void popUp() {
        LayoutInflater inflater;
        View dialogView;

        imageDialog = new AlertDialog.Builder(PokeDetailActivity.this, R.style.CustomDialog);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.popup_catch, null);
        imageDialog.setView(dialogView);
        imageDialog.setCancelable(true);

        myDialog = imageDialog.create();
        myDialog.show();
    }
}