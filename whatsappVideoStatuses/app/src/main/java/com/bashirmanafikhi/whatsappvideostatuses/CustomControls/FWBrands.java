package com.bashirmanafikhi.whatsappvideostatuses.CustomControls;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.bashirmanafikhi.whatsappvideostatuses.R;

public class FWBrands extends TextView {


    public FWBrands(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public FWBrands(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FWBrands(Context context) {
        super(context);
        init();
    }

    private void init() {

        //Font name should not contain "/".
        setTypeface(ResourcesCompat.getFont(getContext(), R.font.fwbrands));
    }

}
