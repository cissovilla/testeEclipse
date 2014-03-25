package com.villa.festasufba;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;


public class TouchImageViewActivity extends Activity {
	//int sizeX, sizeY;
	TouchImageView img;
	boolean mostra=true;
	View mViewGroupUp, mViewGroupDown;
    String face;
    /** Called when the activity is first created. */
    @SuppressLint("NewApi")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mViewGroupUp = findViewById(R.id.viewsUp);
        mViewGroupDown = findViewById(R.id.viewsDown);
        img = (TouchImageView) findViewById(R.id.snoop);
        img.setImageBitmap(CommonResources.photoFinishBitmap);
        img.setMaxZoom(4f);
        img.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
               if(mostra){
            	  
            	   ObjectAnimator mSlidInAnimator = ObjectAnimator.ofFloat(mViewGroupUp, "alpha", 0);
            	   mSlidInAnimator.setDuration(500);
            	   mSlidInAnimator.start();
            	   
            	   ObjectAnimator mSlidInAnimator2 = ObjectAnimator.ofFloat(mViewGroupDown, "alpha", 0);
            	   mSlidInAnimator2.setDuration(500);
            	   mSlidInAnimator2.start();
            	   
            	   ((ImageButton) findViewById(R.id.button1)).setClickable(false);
            	   
            	   /*
            	   ObjectAnimator mSlidInAnimator = ObjectAnimator.ofFloat(mViewGroupUp, "translationY", -mViewGroupUp.getHeight());
            	   mSlidInAnimator.setDuration(500);
            	   mSlidInAnimator.start();
            	   
            	   ObjectAnimator mSlidInAnimator2 = ObjectAnimator.ofFloat(mViewGroupDown, "translationY", getWindowManager().getDefaultDisplay().getHeight());
            	   mSlidInAnimator2.setDuration(500);
            	   mSlidInAnimator2.start();*/
            	   mostra=false;
               }else{
            	   ObjectAnimator mSlidInAnimator = ObjectAnimator.ofFloat(mViewGroupUp, "alpha", 1);
            	   mSlidInAnimator.setDuration(500);
            	   mSlidInAnimator.start();
            	   
            	   ObjectAnimator mSlidInAnimator2 = ObjectAnimator.ofFloat(mViewGroupDown, "alpha", 1);
            	   mSlidInAnimator2.setDuration(500);
            	   mSlidInAnimator2.start();
            	   
            	   ((ImageButton) findViewById(R.id.button1)).setClickable(true);
            	   
            	  /*ObjectAnimator mSlidInAnimator = ObjectAnimator.ofFloat(mViewGroupUp, "translationY", 0);
            	   mSlidInAnimator.setDuration(500);
            	   mSlidInAnimator.start();
            	   
            	   ObjectAnimator mSlidInAnimator2 = ObjectAnimator.ofFloat(mViewGroupDown, "translationY", getWindowManager().getDefaultDisplay().getHeight()-mViewGroupDown.getHeight());
            	   mSlidInAnimator2.setDuration(500);
            	   mSlidInAnimator2.start();*/
            	   mostra=true;
               }
            }
        });

         Intent myIntent= getIntent(); 
         String data = myIntent.getStringExtra("data");
         String local = myIntent.getStringExtra("local");
         String valor = myIntent.getStringExtra("valor");
         face = myIntent.getStringExtra("face");
         if(data.equals("")){
        	 ((TextView)findViewById(R.id.textView10)).setText("");
        	 ((TextView)findViewById(R.id.textView9)).setText("");
        	 ((TextView)findViewById(R.id.textView6)).setText("");
        	 ((TextView)findViewById(R.id.textView1)).setText("");
        	 ((TextView)findViewById(R.id.textView5)).setText("");
        	 ((TextView)findViewById(R.id.textView2)).setText(local);
        	 ((TextView)findViewById(R.id.textView2)).setGravity(Gravity.CENTER);
        	 
        	 
         }else{
        	 ((TextView)findViewById(R.id.textView10)).setText(data);
        	 ((TextView)findViewById(R.id.textView9)).setText(local);
        	 if(valor.equals("ff")){
        		 ((TextView)findViewById(R.id.textView6)).setText(" FREE");
        		 ((TextView)findViewById(R.id.textView6)).setTextColor(Color.parseColor("#00DD00"));
        	 }else{
        		 ((TextView)findViewById(R.id.textView6)).setText(valor);
        		 ((TextView)findViewById(R.id.textView6)).setTextColor(Color.parseColor("#FFFFFF"));
        	 }
         }
         ((ImageButton)findViewById(R.id.button1)).setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {
            	 Uri uriUrl = Uri.parse(face);
                 Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                 startActivity(launchBrowser);
            	 
             }
         });
        //((TextView)findViewById(android.R.id.text1)).setText("detalhes \n detalhes \n detalhes \n detalhes \n detalhes \n detalhes \n detalhes \n detalhes \n detalhes \n detalhes \n detalhes \n detalhes \n detalhes \n detalhes \n detalhes \n detalhes \n detalhes \n detalhes \n detalhes \n detalhes v\n detalhes");
        //sizeX = img.getWidth();
       // sizeY = img.getHeight();
       
        //img.setScaleX(2f);
        //img.setScaleY(2f);
    }
}