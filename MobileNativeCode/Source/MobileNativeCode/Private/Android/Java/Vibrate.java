package com.Plugins.MobileNativeCode;
import com.Plugins.MobileNativeCode.NativeUI;

import android.app.Activity;
import android.support.annotation.Keep;
import android.os.VibratorManager;
import android.os.Vibrator;
import android.os.VibrationEffect;
//Android Context
import android.content.Context;


@Keep
public class Vibrate {

	private static class VibrateRunnable implements Runnable 
	{
		private Activity GameActivity;
		private Vibrator Local_vibrator;
		private long[] local_pattern;
		private int local_vibrateType;

		VibrateRunnable(final Activity activity, final Vibrator vibrator, final long[] pattern, final int vibrateType)
		{
			this.GameActivity = activity;
			this.Local_vibrator = vibrator;
			this.local_pattern = pattern;
			this.local_vibrateType = vibrateType;
		}

		@Override
		public void run ()
		{
			if(Local_vibrator != null)
			{
				String output = String.valueOf(local_pattern[0]) + ", " + String.valueOf(local_vibrateType);
				NativeUI.showToast(GameActivity, output, 3500);
				Local_vibrator.vibrate(local_pattern, local_vibrateType); 
			}
			else
			{
				NativeUI.showToast(GameActivity, "Vibrator is null", 3500);
			}
		}
	}

	@Keep
	public static void Vibrate_During_Duration(Activity activity, final int duration) 
	{

		Vibrator vibrator = Get_Vibrate_Object(activity); 
		//idiot unreal, fucking intellisense
		if (vibrator != null)
		{
			//api level 21
			long[] pattern = {0,duration};
			//int[] local_amplitudes = {0, amplitudes};

			//NativeUI.showToast(activity, "before ve", 3500);
			//VibrationEffect ve = VibrationEffect.createWaveform(local_timing, local_amplitudes, -1);		
			//NativeUI.showToast(activity, "after ve", 3500);
			activity.runOnUiThread(new VibrateRunnable(activity, vibrator, pattern, -1));
		}
	}
	
	@Keep
	public static void Vibrate_Keep_Going(Activity activity) 
	{
		Vibrator vibrator = Get_Vibrate_Object(activity); 
		if (vibrator != null)
		{
			// long[] duration_long = {1};
			// int[] amplitudes_array = {amplitudes};
			//VibrationEffect ve = VibrationEffect.createWaveform(duration_long, amplitudes_array, 0);
			//NativeUI.showToast(activity, "Keep", 3500);
			
			long[] pattern = {0,1000};

			activity.runOnUiThread(new VibrateRunnable(activity, vibrator, pattern, 0));

		}

	}
	
	@Keep
	public static void Vibrate_Cancel(Activity activity) 
	{
		Vibrator vibrator = Get_Vibrate_Object(activity);
		if (vibrator != null)
		{
			NativeUI.showToast(activity, "Cancel", 3500);

			vibrator.cancel();
		}
	}

	@Keep
	private static Vibrator Get_Vibrate_Object(Activity activity) 
	{
	// 	VibratorManager vm = activity.getApplicationContext().getSystemService(activity.getApplicationContext().VIBRATOR_MANAGER_SERVICE);
	// 	Vibrator vibrator = vm.getDefaultVibrator();

		Vibrator vibrator = (Vibrator) activity.getApplicationContext().getSystemService(activity.getApplicationContext().VIBRATOR_SERVICE);
		if(vibrator == null)
		{
			//NativeUI.showToast(activity, "why null", 3500);
			return null;
		}
		else
		{
			//NativeUI.showToast(activity, "not null", 3500);
			return vibrator;
		}
		
	}

}
