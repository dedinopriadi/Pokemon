/*
 * Created by Dedi Nopriadi on 10/14/21, 8:21 PM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 10/14/21, 8:21 PM
 */

package nopriadi.dedi.pokemon.utils;

import static android.text.TextUtils.isEmpty;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;

import es.dmoral.toasty.Toasty;

public class Tools {

    public static int getDominantColor(Bitmap bitmap) {
        if (bitmap == null) {
            return Color.TRANSPARENT;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int size = width * height;
        int pixels[] = new int[size];
        //Bitmap bitmap2 = bitmap.copy(Bitmap.Config.ARGB_4444, false);
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        int color;
        int r = 0;
        int g = 0;
        int b = 0;
        int a;
        int count = 0;
        for (int i = 0; i < pixels.length; i++) {
            color = pixels[i];
            a = Color.alpha(color);
            if (a > 0) {
                r += Color.red(color);
                g += Color.green(color);
                b += Color.blue(color);
                count++;
            }
        }
        r /= count;
        g /= count;
        b /= count;
        r = (r << 16) & 0x00FF0000;
        g = (g << 8) & 0x0000FF00;
        b = b & 0x000000FF;
        color = 0xFF000000 | r | g | b;
        return color;
    }

    public static void successMessage(Context context, String message) {
        Toasty.success(context, message, Toasty.LENGTH_LONG).show();
    }

    public static void warningMessage(Context context, String message) {
        Toasty.warning(context, message, Toasty.LENGTH_LONG).show();
    }

    public static void errorMessage(Context context, String message) {
        Toasty.error(context, message, Toasty.LENGTH_LONG).show();
    }

    public static String getUrlImage(String url) {
        String id = getIdPoke(url);
        return Constanta.Url.APP_IMAGE + id + ".png";
    }

    public static String getIdPoke(String url) {
        String separator = "pokemon/";
        if (isEmpty(url)) {
            return url;
        }
        if (separator == null) {
            return "";
        }
        final int pos = url.indexOf(separator);
        if (pos == 0) {
            return url;
        }
        String id = url.substring(pos + separator.length());
        return id.replace("/", "");
    }

    public static String Capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }

}
