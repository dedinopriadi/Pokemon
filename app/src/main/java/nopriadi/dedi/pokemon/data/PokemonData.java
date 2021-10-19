/*
 * Created by Dedi Nopriadi on 10/14/21, 3:16 AM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 10/14/21, 3:16 AM
 */

package nopriadi.dedi.pokemon.data;

public class PokemonData {
    String pokeId;
    String pokeName;
    String pokeImage;
    String PokeUrl;

    public PokemonData() {
    }

    public PokemonData(String pokeId, String pokeName, String pokeImage, String pokeUrl) {
        this.pokeId = pokeId;
        this.pokeName = pokeName;
        this.pokeImage = pokeImage;
        PokeUrl = pokeUrl;
    }

    public String getPokeId() {
        return pokeId;
    }

    public void setPokeId(String pokeId) {
        this.pokeId = pokeId;
    }

    public String getPokeName() {
        return pokeName;
    }

    public void setPokeName(String pokeName) {
        this.pokeName = pokeName;
    }

    public String getPokeImage() {
        return pokeImage;
    }

    public void setPokeImage(String pokeImage) {
        this.pokeImage = pokeImage;
    }

    public String getPokeUrl() {
        return PokeUrl;
    }

    public void setPokeUrl(String pokeUrl) {
        PokeUrl = pokeUrl;
    }
}
