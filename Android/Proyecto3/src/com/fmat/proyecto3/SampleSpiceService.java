package com.fmat.proyecto3;

import android.app.Application;

import com.octo.android.robospice.GoogleHttpClientSpiceService;
import com.octo.android.robospice.persistence.CacheManager;
import com.octo.android.robospice.persistence.googlehttpclient.json.GsonObjectPersisterFactory;

/**
 * Simple service
 * 
 * @author sni
 * 
 */
public class SampleSpiceService extends GoogleHttpClientSpiceService {

	@Override
	public CacheManager createCacheManager(Application application) {
		CacheManager cacheManager = new CacheManager();

		GsonObjectPersisterFactory gsonPF = new GsonObjectPersisterFactory(
				application);

		// init

		cacheManager.addPersister(gsonPF);
		return cacheManager;
	}
}