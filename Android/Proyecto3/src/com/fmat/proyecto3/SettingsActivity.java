/**
 * 
 */
package com.fmat.proyecto3;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * @author Fabi�n Castillo
 *
 */
public class SettingsActivity extends PreferenceActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}
	
}
