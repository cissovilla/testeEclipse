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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

/**
 * Demonstrates a "screen-slide" animation using a {@link ViewPager}. Because
 * {@link ViewPager} automatically plays such an animation when calling
 * {@link ViewPager#setCurrentItem(int)}, there isn't any animation-specific
 * code in this sample.
 * 
 * <p>
 * This sample shows a "next" button that advances the user to the next step in
 * a wizard, animating the current screen out (to the left) and the next screen
 * in (from the right). The reverse animation is played when the user presses
 * the "previous" button.
 * </p>
 * 
 * @see ScreenSlidePageFragment
 */
public class MainActivity extends FragmentActivity {

	// contacts JSONArray
	JSONArray contacts = null;

	/**
	 * The number of pages (wizard steps) to show in this demo.
	 */

	/**
	 * The pager widget, which handles animation and allows swiping horizontally
	 * to access previous and next wizard steps.
	 */
	private ViewPager mPager;
	private JSONArray agenda = null;
	private JSONObject json;
	private boolean Net;
	private int hora;
	
	
	/**
	 * The pager adapter, which provides the pages to the view pager widget.
	 */
	private PagerAdapter mPagerAdapter;

	public boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	private void createScheduledNotification(Date d, String nota,
			String subNota, int hora) {

		// Get new calendar object and set the date to now
		Calendar calendar = Calendar.getInstance();
		calendar.set((d.getYear() + 1900), d.getMonth(), d.getDate(), hora, 0,
				0);

		Log.v("DownloadManager", "Calendário dia " + calendar.getTime());
		// Retrieve alarm manager from the system
		AlarmManager alarmManager = (AlarmManager) getApplicationContext()
				.getSystemService(getBaseContext().ALARM_SERVICE);
		// Every scheduled intent needs a different ID, else it is just executed
		// once
		int id = (int) System.currentTimeMillis();

		// Prepare the intent which should be launched at the date
		Intent intent = new Intent(getBaseContext(), TimeAlarm.class);
		intent.putExtra("nota", nota);
		intent.putExtra("subNota", subNota);
		// Prepare the pending intent
		PendingIntent pendingIntent = PendingIntent.getBroadcast(
				getApplicationContext(), id, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		SharedPreferences prefs = getPreferences(MODE_PRIVATE);
		String restoredText = prefs.getString(d.toString(), null);
		if (restoredText == null) {
			SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE)
					.edit();
			editor.putString(d.toString(), "setado");
			editor.commit();
			// Register the alert in the system. You have the option to define
			// if the device has to wake up on the alert or not
			alarmManager.set(AlarmManager.RTC_WAKEUP,
					calendar.getTimeInMillis(), pendingIntent);
		}
	}

	@SuppressLint("NewApi")
	private void carrega() {
		//Inserido por Rafael Villa
		Log.v("AGENDA", "[Main]carrega");
		//Inserido por Rafael Villa
		String url = "http://www.cissovilla.xpg.com.br/agendadois";
		// Creating JSON Parser instance
		JSONParser jParser = new JSONParser();
		Net = isNetworkAvailable();
		if (Net) {
			// getting JSON string from URL
			json = jParser.getJSONFromUrl(url);
			try {
				new DownloadFilesTask().execute(new URL(
						"http://www.cissovilla.xpg.com.br/agendadois.txt"));
			} catch (IOException e) {
				Log.d("DownloadManager", "Error: " + e);
			} 

		} else {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Sem Internet");

			AlertDialog alert = builder.create();
			alert.show();
			json = loadJSONFromSD();
		}

		try {
			// Getting Array of Contacts
			if(json != null)Log.v("AGENDA", "CARREGOU!!!");
			agenda = json.getJSONArray("agenda");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		// Instantiate a ViewPager and a PagerAdapter.
		// createNotification();

		mPager = (ViewPager) findViewById(R.id.pager);
		mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), this, agenda);
		mPager.setAdapter(mPagerAdapter);
		mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@SuppressLint("NewApi")
			@Override
			public void onPageSelected(int position) {
				// When changing pages, reset the action bar actions since they
				// are dependent
				// on which page is currently active. An alternative approach is
				// to have each
				// fragment expose actions itself (rather than the activity
				// exposing actions),
				// but for simplicity, the activity provides the actions in this
				// sample.
				//Inserido por Rafael Villa
				Log.v("AGENDA", "[Main]PageSelected");
				//Inserido por Rafael Villa
				if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1)
					invalidateOptionsMenu();
			}
		});
		JSONObject data = null;
		int dia = 0;
		for (int i = 0; i < agenda.length(); i++) {
			try {
				data = agenda.getJSONObject(i);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Date d = null;
			String nota = null, subNota = null;
			Date currentDate = new Date(System.currentTimeMillis());
			try {

				SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				d = dateFormat.parse(data.getString("data"));
				currentDate = dateFormat.parse(currentDate.getDate() + "/"
						+ (currentDate.getMonth() + 1) + "/"
						+ (currentDate.getYear() + 1900));
				nota = data.getString("nota");
				subNota = data.getString("subNota");
				if (!data.getString("hora").equals("")) {
					hora = Integer.parseInt(data.getString("hora"));
				} else {
					hora = 15;
				}

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if ((d.before(currentDate))) {
				dia++;
			} else {
				createScheduledNotification(d, nota, subNota, hora);
			}
		}
		Log.v("DownloadManager", "não Entrou na data" + dia);
				//Inserido por Rafael Villa
				Log.v("AGENDA", "[Main]setItem");
				//Inserido por Rafael Villa
		mPager.setCurrentItem(dia);

	}

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//Inserido por Rafael Villa
		Log.v("AGENDA", "[Main]onCreate");
		//Inserido por Rafael Villa
		
		super.onCreate(savedInstanceState);
		mPager = null;
		mPagerAdapter = null;

		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.FROYO) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		setContentView(R.layout.activity_screen_slide);

		// the app is being launched for first time, do something
		Log.d("Comments", "First time");

		carrega();


		// mPager.addTouchables(lista);
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
				 * Read bytes to the Buffer until there is nothing more to
				 * read(-1).
				 */
				ByteArrayBuffer baf = new ByteArrayBuffer(5000);
				int current = 0;
				while ((current = bis.read()) != -1) {
					baf.append((byte) current);
				}

				/* Convert the Bytes read to a String. */
				File outFile = new File(getExternalFilesDir(null), "agendadois.txt");
				outFile.delete();
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
				//Inserido por Rafael Villa
				Log.v("AGENDA", "[Main]OptionMenu");
				//Inserido por Rafael Villa
		getMenuInflater().inflate(R.menu.activity_screen_slide, menu);
		
		int item = mPager.getCurrentItem();
		if ( item == 0) {
			menu.findItem(R.id.action_previous).setIcon(
					R.drawable.ic_action_previous_itemdark);
		}
		if (item == agenda.length() - 1) {
			menu.findItem(R.id.action_next).setIcon(
					R.drawable.ic_action_next_itemdark);
		}
		if (!Net) {
			menu.findItem(R.id.atualiza).setIcon(R.drawable.ic_action_refreshd);
		}

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		//Inserido por Rafael Villa
		Log.v("AGENDA", "[Main]OptionMenuSelected");
		//Inserido por Rafael Villa
		switch (item.getItemId()) {
		case android.R.id.home:
			// Navigate "up" the demo structure to the launchpad activity.
			// See http://developer.android.com/design/patterns/navigation.html
			// for more.
			NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
			return true;

		case R.id.action_previous:
			// Go to the previous step in the wizard. If there is no previous
			// step,
			// setCurrentItem will do nothing.
			mPager.setCurrentItem(mPager.getCurrentItem() - 1);
			return true;

		case R.id.action_next:
			// Advance to the next step in the wizard. If there is no next step,
			// setCurrentItem
			// will do nothing.
			mPager.setCurrentItem(mPager.getCurrentItem() + 1);
			ImageView img = new ImageView(this);
			img.setImageResource(mPager.getCurrentItem() + 1);

			return true;

		case R.id.atualiza:
			carrega();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	public JSONObject loadJSONFromSD() {
		JSONObject jObject = null;
		try {
			File yourFile = new File(getExternalFilesDir(null), "agendadois.txt");
			FileInputStream stream = new FileInputStream(yourFile);
			String jString = null;
			try {
				FileChannel fc = stream.getChannel();
				MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0,
						fc.size());
				/* Instead of using default, pass in a decoder. */
				jString = Charset.defaultCharset().decode(bb).toString();
			} finally {
				stream.close();
			}

			jObject = new JSONObject(jString);

		} catch (Exception e) {
			e.printStackTrace();
			Log.v("tag", "Failed to copy asset file: " + "agenda.json", e);
		}
		return jObject;

	}

	/**
	 * A simple pager adapter that represents 5 {@link ScreenSlidePageFragment}
	 * objects, in sequence.
	 */
	private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
		private Activity act;
		private JSONArray agenda = null;
		private android.support.v4.app.Fragment frafra;

		public ScreenSlidePagerAdapter(FragmentManager fm, Activity act,
				JSONArray agenda) {
			super(fm);
			this.act = act;
			this.agenda = agenda;
		}
		
		
		

		@Override
		public android.support.v4.app.Fragment getItem(int position) {
			//Inserido por Rafael Villa
			Log.v("AGENDA", "[Main]getItem");
			//Inserido por Rafael Villa
			return ScreenSlidePageFragment.create(position, this.act,
					this.agenda, Net);

		}
		
		
	   

		@Override
		public int getCount() {
			return agenda.length();
		}

	}
}
