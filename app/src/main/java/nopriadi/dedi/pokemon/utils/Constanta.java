/*
 * Created by Dedi Nopriadi on 10/14/21, 12:36 PM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 10/14/21, 12:36 PM
 */

package nopriadi.dedi.pokemon.utils;

public class Constanta {

    public static class Url {
        public static final String APP_SERVICE = "https://pokeapi.co/api/v2/";
        public static final String APP_IMAGE = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/";
    }

    public static class Message {
        public static final String FAILURE = "Connection Failure";
        public static final String ERROR_RESPONSE = "Error Response";
        public static final String INQUERY_SUCCESS = "Inquery Success";
        public static final String UNSUCCESS_RESPONSE = "Unsuccess Response";
        public static final String ERROR_PARSING = "Error Parsing";
    }

    public static class Values {
        public static final Integer LIMIT = 20;
    }

}
