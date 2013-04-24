/**
 * 
 */
package com.argint.android.thedecider.activities;

import com.argint.android.thedecider.ActivityController;
import com.argint.android.thedecider.DecisionData;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

/**
 * @author Gabriel Jiva
 * 
 * Parent class to all Activity classes in TheDecider. Contains convenience methods.
 *
 */
public abstract class DecisionActivity extends Activity {
	
	public final static String DECISION_DATA = "com.argint.android.thedecider.DECISION_DATA";
	private static final String STRING_ENCODING_SEPARATOR = "-&@&-";
	
	public Intent createIntent(){
		return createIntent(getDecisionData());
	}
	
	/**
	 * 
	 * @param decisionName
	 * @return
	 */
	public Intent createIntent(String decisionName){
		DecisionData decisionData = new DecisionData(decisionName);
		return createIntent(decisionData);
	}
	
	/**
	 * 
	 * @param decisionData
	 * @return
	 */
	public Intent createIntent(DecisionData decisionData){
		Intent intent = new Intent(this, ActivityController.getNextActivity(this.getClass()));
		intent.putExtra(DECISION_DATA, decisionData);
		return intent;
	}
	
	/**
	 * 
	 * @return
	 */
	public DecisionData getDecisionData(){
		
		return (DecisionData) getIntent().getSerializableExtra(DECISION_DATA);
	}
	
	/**
	 * Encodes a pair of strings into one string
	 * 
	 * @param left
	 * @param right
	 * @return the decoded string
	 */
	public String encodeStringPair(String left, String right){
		
		return left + STRING_ENCODING_SEPARATOR + right;
	}
	
	/**
	 * Decodes an encoded String of a pair of Strings into two separate Strings 
	 * 
	 * @param encodedPair
	 * @return a String array with the two Strings
	 */
	public String[] decodeStringPair(String encodedPair){
		
		String[] strs = encodedPair.split(STRING_ENCODING_SEPARATOR);
		if (strs.length != 2){
			throw new RuntimeException("invalid string pair encoding: " + encodedPair);
		}
		return strs;
	}
	
	/**
	 * Turns an encoded a-b string pair into b-a
	 * 
	 * @param encodedPair
	 * @return
	 */
	public String invertEncodedStringPair(String encodedPair){
		
		String[] strs = decodeStringPair(encodedPair);
		return encodeStringPair(strs[1], strs[0]);
	}
	
	protected void logInfo(String msg){
		Log.i(this.getClass().getName(), msg);
	}
	
	protected void logWarning(String msg){
		Log.w(this.getClass().getName(), msg);
	}
	
	protected void logError(String msg){
		Log.e(this.getClass().getName(), msg);
	}

}
