/**
 * 
 */
package com.fmat.proyecto3;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * @author Fabián Castillo
 *
 */
public class SettingsActivity extends PreferenceActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}
	
}
