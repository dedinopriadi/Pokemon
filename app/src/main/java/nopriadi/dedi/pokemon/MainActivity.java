package nopriadi.dedi.pokemon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import nopriadi.dedi.pokemon.activity.MyPokeListActivity;
import nopriadi.dedi.pokemon.activity.PokeListActivity;
import nopriadi.dedi.pokemon.utils.Tools;

public class MainActivity extends AppCompatActivity {
    private CardView cardList;
    private CardView cardPoke;
    private ImageView imgPokemon;
    private ImageView imgPokeball;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cardList    = (CardView) findViewById(R.id.cardList);
        cardPoke    = (CardView) findViewById(R.id.cardCollect);
        imgPokemon  = (ImageView) findViewById(R.id.img_pokemon);
        imgPokeball = (ImageView) findViewById(R.id.img_pokeball);

        Bitmap bmp_pokemon = ((BitmapDrawable) imgPokemon.getDrawable()).getBitmap();
        Bitmap bmp_pokeball = ((BitmapDrawable) imgPokeball.getDrawable()).getBitmap();

        cardList.setCardBackgroundColor(Tools.getDominantColor(bmp_pokemon));
        cardPoke.setCardBackgroundColor(Tools.getDominantColor(bmp_pokeball));
    }

    public void viewList(View view) {
        startActivity(new Intent(MainActivity.this, PokeListActivity.class));
    }

    public void viewCollection(View view) {
        startActivity(new Intent(MainActivity.this, MyPokeListActivity.class));
    }
}