package com.example.ttpm.game_on;

import android.app.Activity;
import com.robotium.recorder.executor.Executor;

@SuppressWarnings("rawtypes")
public class SplashActivityExecutor extends Executor {

	@SuppressWarnings("unchecked")
	public SplashActivityExecutor() throws Exception {
		super((Class<? extends Activity>) Class.forName("com.example.ttpm.game_on.activities.SplashActivity"),  "com.example.ttpm.game_on.R.id.", new android.R.id(), false, false, "1460354501264");
	}

	public void setUp() throws Exception { 
		super.setUp();
	}
}
