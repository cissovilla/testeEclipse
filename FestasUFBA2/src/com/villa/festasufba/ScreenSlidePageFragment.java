/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.villa.festasufba;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * A fragment representing a single step in a wizard. The fragment shows a dummy title indicating
 * the page number, along with some dummy text.
 *
 * <p>This class is used by the {@link CardFlipActivity} and {@link
 * ScreenSlideActivity} samples.</p>
 */
public class ScreenSlidePageFragment extends Fragment {
	//Para o onPause() e onResume() --Código inserido por Rafael Villa
	final static String APP_PREFS = "app_prefs";
	final static String NET_KEY = "net";
	final static String AGENDA_KEY = "agenda";
	final static String MPAGENUMBER_KEY = "mpagenumber";
	//--Código inserido por Rafael Villa
    /**
     * The argument key for the page number this fragment represents.
     */
    public static final String ARG_PAGE = "page";
    private static Activity Pai;
    private static JSONArray agenda;

    /**
     * The fragment's page number, which is set to the argument value for {@link #ARG_PAGE}.
     */
    private static int mPageNumber;
    private static JSONObject data;
    private static boolean Net;
    private URL url;
    private File sdCard=null;
    //private static myButton myButton;
    //private ZoomableImageView iv=null;
    private myView iv=null;
    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static ScreenSlidePageFragment create(int pageNumber, Activity pai, JSONArray agen, boolean net) {
    	//Código inserido por Rafael Villa
    	Log.v("AGENDA","[Fragment]create");
    	//Código inserido por Rafael Villa
    	Net=net;
    	agenda=agen;
    	Pai=pai;
        ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public ScreenSlidePageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Log.v("AGENDA", "[Fragment]onCreateScreenSlide");
        
    	//--Código inserido por Rafael Villa
    	if (savedInstanceState != null) {
            // Restore value of members from saved state
    		//mPageNumber = savedInstanceState.getInt(MPAGENUMBER_KEY);
            //Net = savedInstanceState.getBoolean(NET_KEY);
            String strJson = savedInstanceState.getString(AGENDA_KEY);

            if(strJson != null){
            	
            	try{
            		agenda = new JSONArray(strJson);
            	}catch(JSONException e){
            			e.printStackTrace();
            	}
            }
    	}
    	//--Código inserido por Rafael Villa
    	//create(mPageNumber, this.getActivity(), agenda, Net);
        //mPageNumber = getArguments().getInt(ARG_PAGE);
        //Log.v("AGENDA", "[Fragment]mPager "+mPageNumber);
        //Log.v("AGENDA","Sai "+mPageNumber);
        //Log.v("AGENDA", "Create");
        
    }
    
    //--Código inserido por Rafael Villa
    /*
    @Override
    public void onPause() {
    	// TODO Auto-generated method stub
    	super.onPause();
    	SharedPreferences prefs = this.getActivity().getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
    	SharedPreferences.Editor editor = prefs.edit();
    	editor.putString(AGENDA_KEY, agenda.toString());
    	editor.putBoolean(NET_KEY, Net);
    	editor.putInt(MPAGENUMBER_KEY, mPageNumber);
    	editor.commit();
    	Log.v("AGENDA", "Pausou");
    }
    
    @Override
    public void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	Log.v("AGENDA", "onResume");
    	SharedPreferences prefs = this.getActivity().getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
        Net = prefs.getBoolean(NET_KEY, false);
        mPageNumber = prefs.getInt(MPAGENUMBER_KEY, 0);
        String strJson = prefs.getString(AGENDA_KEY, null);
        //Log.v("AGENDA",strJson);
        if(strJson != null){
        	
        	try{
        		agenda = new JSONArray(strJson);
        	}catch(JSONException e){
        			e.printStackTrace();
        	}
        }
        	
        
    }

    */
    
    @Override
    public void onPause() {
    	// TODO Auto-generated method stub
    	super.onPause();
    	this.onDestroy();
    }
    
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
    	// TODO Auto-generated method stub
    	Log.v("AGENDA","[Fragment]onSavedInstanceState");
    	outState.putInt(MPAGENUMBER_KEY, mPageNumber);
    	outState.putString(AGENDA_KEY, agenda.toString());
   
    	
    	//outState.putBoolean(NET_KEY, Net);
    	
    	super.onSaveInstanceState(outState);
    	
    }
    
    @Override
    public void onStop() {
    	// TODO Auto-generated method stub
    	super.onStop();
    	Log.v("AGENDA", "[Fragment]onStop");
    }
    
    
  //--Código inserido por Rafael Villa

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
    	
    	//Código inserido por Rafael Villa
    	Log.v("AGENDA", "[Fragment]onCreateView");
        //Código inserido por Rafael Villa
    	mPageNumber = getArguments().getInt(ARG_PAGE);
    	 Log.v("AGENDA", "[Fragment]mPager "+mPageNumber);
        
        // Inflate the layout containing a title and body text.
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.fragment_screen_slide_page, container, false);
  
        
        //ScrollView sv = (ScrollView) findViewById(R.layout.fragment_screen_slide_page);
        //((LinearLayout) linearLayout1 = (LinearLayout) findViewById(R.layout.fragment_screen_slide_page);
        //sv.addView(linearLayout1);
       // ImageViewTouch iv = new ImageViewTouch(Pai);
        //ScaleImageView iv = (ScaleImageView) Pai.findViewById(R.drawable.image1);
        
        //--Comentado por Rafa Villa//iv = (myView) Pai.findViewById(R.drawable.logofimm);
        Pai = this.getActivity();
        //--Código inserido por Rafa Villa
        iv = (myView) Pai.findViewById(R.drawable.logofimm);
        
        LinearLayout linearLayout1 =((LinearLayout) rootView.findViewById(R.id.linear));

       
       try {
    	   Bitmap bitmap=null;
           		data=agenda.getJSONObject(mPageNumber);
           		((TextView) rootView.findViewById(android.R.id.text1)).setText(data.getString("data"));
           		((TextView) rootView.findViewById(android.R.id.text2)).setText(pegaDia(data.getInt("dia")));
           		// Getting Array of Contacts
                JSONArray images = data.getJSONArray("baners");
                for(int i=0; i<images.length();i++){
                	JSONObject img = images.getJSONObject(i);
                	String name = img.getString("img");
                    iv = new myView(Pai);
                    iv.setTudo(img.getString("data"), img.getString("local"), img.getString("valor"), img.getString("face"));
                    try {
						url = new URL(name);
						
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                    //File file = new File(Pai.getExternalFilesDir(null)+"/"+url.getFile().replace( "/" , ""));
                    sdCard = Environment.getExternalStorageDirectory();
                    File file = new File(sdCard.getAbsolutePath() +"/FestasUFBA/"+url.getFile().replace( "/" , ""));
                    if(file.exists() || !Net){
                    	sdCard = Environment.getExternalStorageDirectory();
                    	bitmap = BitmapFactory.decodeFile(sdCard.getAbsolutePath() +"/FestasUFBA/"+url.getFile().replace( "/" , ""));
                    	//Bitmap bitmap = BitmapFactory.decodeFile(Pai.getExternalFilesDir(null) +url.getFile().replace( "/" , ""));
                    	iv.setImageBitmap(bitmap);
                    }else{
                    	bitmap = getBitmapFromURL(name);
                    	iv.setImageBitmap(bitmap);
                    	//SaveIamge(bitmap,url.getFile().replace( "/" , "") );
						
                    	
                    	try{
        					new DownloadFilesTask().execute(new URL(name));
        				} catch (IOException e) {
        		    	       Log.d("DownloadManager", "Error: " + e);
        		    	}
                    }
                    
                    
                    //iv.setImageBitmap(
                    //        BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length)
                    //);
                    RelativeLayout RelativeLayout1 = new RelativeLayout(Pai);
                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(2,2,2,2);
                    iv.setLayoutParams(lp);
                    iv.setAdjustViewBounds(true);
                   /* myButton = new myButton(Pai);
                    myButton.setBitmap(bitmap);
                    myButton.setBackgroundDrawable(getResources().getDrawable( R.drawable.logofim ));
                    RelativeLayout.LayoutParams lpButton = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    lpButton.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                    lpButton.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    myButton.setLayoutParams(lpButton);*/
                    RelativeLayout1.addView(iv);
                   // RelativeLayout1.addView(myButton);
                    linearLayout1.addView(RelativeLayout1);
                }
               
                
        } catch (JSONException e) {
            e.printStackTrace();
        }  
       


        return rootView;
    }
 
    public static Bitmap drawableToBitmap (Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap); 
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    private String pegaDia(int dia){
    	String retorno ="";
    	switch(dia){
	    	case 1:
	    		retorno = "Domingo";
	    	break;
	    	case 2:
	    		retorno = "Segunda-feira";
	    	break;
	    	case 3:
	    		retorno = "Terça-feira";
	    	break;
	    	case 4:
	    		retorno = "Quarta-feira";
	    	break;
	    	case 5:
	    		retorno = "Quinta-feira";
	    	break;
	    	case 6:
	    		retorno = "Sexta-feira";
	    	break;
	    	case 7:
	    		retorno = "Sábado";
	    		break;
	    	default:
	    		retorno = "-----------";
    	}
    	
    	return retorno;
    }
    public static Bitmap getBitmapFromAsset(Context context, String strName) {
        AssetManager assetManager = context.getAssets();

        InputStream istr;
        Bitmap bitmap = null;
        try {
            istr = assetManager.open(strName);
            bitmap = BitmapFactory.decodeStream(istr);
        } catch (IOException e) {
            return null;
        }

        return bitmap;
    }
    /**
     * Returns the page number represented by this fragment object.
     */
    public int getPageNumber() {
        return mPageNumber;
    }

    public class DownloadFilesTask extends AsyncTask<URL, Integer, Long> {
        protected Long doInBackground(URL... urls) {
        	 try {
    	          
    	           /* Open a connection to that URL. */
    	           URLConnection ucon = urls[0].openConnection();

    	           /*
    	            * Define InputStreams to read from the URLConnection.
    	            */
    	           InputStream is = ucon.getInputStream();
    	           BufferedInputStream bis = new BufferedInputStream(is);

    	           /*
    	            * Read bytes to the Buffer until there is nothing more to read(-1).
    	            */
    	           ByteArrayBuffer baf = new ByteArrayBuffer(5000);
    	           int current = 0;
    	           while ((current = bis.read()) != -1) {
    	              baf.append((byte) current);
    	           }


    	           /* Convert the Bytes read to a String. */
    	           File sdCard = Environment.getExternalStorageDirectory();
    	           File dir = new File (sdCard.getAbsolutePath() + "/FestasUFBA");
    	           dir.mkdirs();
    	           File outFile = new File(dir, urls[0].getFile().replace( "/" , ""));
    	           FileOutputStream fos = new FileOutputStream(outFile);
    	           fos.write(baf.toByteArray());
    	           fos.flush();
    	           fos.close();
    	        

    	   } catch (IOException e) {
    	       Log.d("DownloadManager", "Error: " + e);
    	   }
        	 return 1l;
        }

        
    }
    
}
