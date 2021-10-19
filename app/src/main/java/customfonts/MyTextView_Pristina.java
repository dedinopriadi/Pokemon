/*
 * *
 *   Created by Dedi Nopriadi on 4/13/21 9:49 AM
 *   Copyright (c) 2021 . All rights reserved.
 *   Last modified 4/13/21 9:49 AM
 *
 */

package customfonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class MyTextView_Pristina extends TextView {

    public MyTextView_Pristina(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyTextView_Pristina(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyTextView_Pristina(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/pristina.TTF");
            setTypeface(tf);
        }
    }
}
