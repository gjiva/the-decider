/**
 * 
 */
package com.argint.android.thedecider;

import java.io.Serializable;
import java.util.Map;

/**
 * @author snow
 *
 */
public class DecisionData implements Serializable {

	public static final long serialVersionUID = 1;
	
	public String decisionName;
	public Map<String, Float> criterionToWeightMap;
	public Map<String, Float> optionToGradeMap;
	
	public DecisionData(String decisionName){
		
		this.decisionName = decisionName;
	}
}
