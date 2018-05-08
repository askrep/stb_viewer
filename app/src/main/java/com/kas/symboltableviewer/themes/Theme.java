package com.kas.symboltableviewer.themes;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;

import com.kas.symboltableviewer.MainNavigationDrawerActivity;
import com.kas.symboltableviewer.R;


public class Theme {
   
   private final static int THEME_DAY_LIGHT = 0;
   private final static int THEME_DAY_PLAIN = 1;
   
   private final static int THEME_NIGHT_BLACK = 0;
   private final static int THEME_NIGHT_DARK = 1;
   
   public static void setTheme(Context context) {
      SharedPreferences prefs = MainNavigationDrawerActivity.getSharedPrefs(context.getApplicationContext());
      
      if (prefs.getBoolean("invertedColors", false)) {
         int theme = Integer.getInteger(prefs.getString("nightTheme", "0"));
         switch (theme) {
            case THEME_NIGHT_DARK:
               context.setTheme(R.style.AppTheme); //R.style.Theme_Dark_Compat
               break;
            case THEME_NIGHT_BLACK:
               context.setTheme(R.style.AppTheme); //R.style.Theme_Black_Compat
               break;
         }
      } else {
         int theme = Integer.parseInt(prefs.getString("dayTheme", "0"));
         switch (theme) {
            case THEME_DAY_LIGHT:
               context.setTheme(R.style.AppTheme); //R.style.Theme_Light_Compat
               break;
            case THEME_DAY_PLAIN:
               context.setTheme(R.style.AppTheme); //R.style.Theme_Plain_Compat
               break;
         }
      }
   }
   
   /**
    * Return the current integer code of the theme being used, taking into account
    * whether we are in day mode or night mode.
    */
   public static int getCurrentTheme(Context context) {
      SharedPreferences prefs = MainNavigationDrawerActivity.getSharedPrefs(context);
      if (prefs.getBoolean("invertedColors", false)) {
         return Integer.parseInt(prefs.getString("nightTheme", "0"));
      } else {
         return Integer.parseInt(prefs.getString("dayTheme", "0"));
      }
   }
   
   public static int[] getResFromAttr(Context context, int[] attrs) {
      TypedArray ta = context.obtainStyledAttributes(attrs);
      for (int i = 0; i < attrs.length; i++) {
         attrs[i] = ta.getResourceId(i, 0);
      }
      ta.recycle();
      return attrs;
   }
   
   public static int getColorFromAttr(Context context, int colorAttr) {
      int[] attrs = new int[]{colorAttr};
      return getColorFromAttr(context, attrs)[0];
   }
   
   public static int[] getColorFromAttr(Context context, int[] attrs) {
      TypedArray ta = context.obtainStyledAttributes(attrs);
      for (int i = 0; i < attrs.length; i++) {
         attrs[i] = ta.getColor(i, ContextCompat.getColor(context, R.color.white));
      }
      ta.recycle();
      return attrs;
   }
}
