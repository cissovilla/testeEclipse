package com.villa.festasufba;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class myView extends ImageView implements OnClickListener {
	private String data,local,valor,face;

    public myView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(this);

    }
    public myView(Context context) {
        super(context);
        setOnClickListener(this);
 
    }
    // skipping measure calculation and drawing
    @Override
	 public void onClick(View v) {
        Intent myIntent = new Intent(getContext(),TouchImageViewActivity.class);
        CommonResources.photoFinishBitmap = getBit();
        myIntent.putExtra("data",data);
        myIntent.putExtra("local",local);
        myIntent.putExtra("valor",valor);
        myIntent.putExtra("face",face);
        ((Activity)getContext()).startActivity(myIntent);

    }
    public void setTudo(String d,String l,String v,String f ){
    	data=d;
    	local=l;
    	valor=v;
    	face =f;
    }

    public Bitmap getBit(){
    	return ((BitmapDrawable)this.getDrawable()).getBitmap();
    }
}
