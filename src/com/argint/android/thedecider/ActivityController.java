/**
 * 
 */
package com.argint.android.thedecider;

import com.argint.android.thedecider.activities.CriteriaEntryActivity;
import com.argint.android.thedecider.activities.CriteriaWeightDisplayActivity;
import com.argint.android.thedecider.activities.CriteriaWeightingActivity;
import com.argint.android.thedecider.activities.MainActivity;
import com.argint.android.thedecider.activities.OptionGradesDisplayActivity;
import com.argint.android.thedecider.activities.OptionRatingActivity;
import com.argint.android.thedecider.activities.OptionsEntryActivity;

import android.util.Log;

/**
 * @author Gabriel Jiva
 *
 */
public class ActivityController {
	
	/**
	 * 
	 * @param currentActivity
	 * @return the Activity that comes after the activity passed in
	 */
	public static Class<?> getNextActivity(Class<?> currentActivity){
		
		Class<?> nextClass = MainActivity.class;
		
		if (currentActivity.equals(MainActivity.class)){
			nextClass = CriteriaEntryActivity.class;
		}
		else if (currentActivity.equals(CriteriaEntryActivity.class)){
			nextClass = OptionsEntryActivity.class;
		}
		else if (currentActivity.equals(OptionsEntryActivity.class)){
			nextClass = CriteriaWeightingActivity.class;
		}
		else if (currentActivity.equals(CriteriaWeightingActivity.class)){
			nextClass = CriteriaWeightDisplayActivity.class;
		}
		else if (currentActivity.equals(CriteriaWeightDisplayActivity.class)){
			nextClass = OptionRatingActivity.class;
		}
		else if (currentActivity.equals(OptionRatingActivity.class)){
			nextClass = OptionGradesDisplayActivity.class;
		}
		else if (currentActivity.equals(OptionGradesDisplayActivity.class)){
			nextClass = MainActivity.class;
		}
		else {
			Log.e(ActivityController.class.getName(), 
					"Unknown current activity: " + currentActivity.getName());
		}
		
		return nextClass;
	}

}
