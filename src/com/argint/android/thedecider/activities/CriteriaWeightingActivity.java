package com.argint.android.thedecider.activities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.argint.android.thedecider.DecisionData;
import com.argint.android.thedecider.Matrix;
import com.argint.android.thedecider.R;

public class CriteriaWeightingActivity 
	extends DecisionActivity 
	implements OnItemSelectedListener, OnClickListener {
	
	private static final String [] spinnerLabelsTemplate = {
			" is extremely less important than ", 
			" is much less important than ", 
			" is less important than ",
			" is slightly less important than ", 
			" is as important as ", 
			" is slightly more important than ",
			" is more important than ", 
			" is much more important than ", 
			" is extremely more important than "};
	private static final float [] spinnerValues = {1f/9f,1f/7f,1f/5f,1f/3f,1f,3f,5f,7f,9f};
	private static final int PARITY_SPINNER_VALUE_POSITION = 4; // the "equals" position
	private static final float PARITY_SPINNER_VALUE = spinnerValues[PARITY_SPINNER_VALUE_POSITION];
	private static final int DEFAULT_SPINNER_VALUE_POSITION = PARITY_SPINNER_VALUE_POSITION;
	private static final float DEFAULT_SPINNER_VALUE = spinnerValues[DEFAULT_SPINNER_VALUE_POSITION];

	private Matrix matrix;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Get the decision data from the intent
		DecisionData decisionData = super.getDecisionData();
		List<String> comparisons = generateCriteriaPairs(decisionData.criterionToWeightMap.keySet());
		
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		
		TextView instructions = new TextView(this);
		instructions.setText(getString(R.string.criteria_weighting_message));
		instructions.setPadding(5, 5, 5, 5);
		
		layout.addView(instructions);
		
		// generate the spinners
		for (String comparison:comparisons) {

			Spinner spinner = createSpinner(comparison);
			layout.addView(spinner);
		}
		
		Button doneButton = new Button(this);
		doneButton.setText("Done");
		doneButton.setOnClickListener(this);
		layout.addView(doneButton);
		
		// Set the scroll view as the activity layout
		ScrollView scroller = new ScrollView(this);
		scroller.addView(layout);
		setContentView(scroller);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.criteria_weighting, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void calculateWeights(){
		
		DecisionData dd = super.getDecisionData();
		Map<String, Float> rowTotals = this.matrix.getRowTotals();
		
		// go through the criteria values and convert them to percentages
		Iterator<String> it = rowTotals.keySet().iterator();
		float total = rowTotals.get(Matrix.GRAND_TOTAL_KEY);
		while (it.hasNext()){
			
			String criterion = it.next();
			Float rawWeight = rowTotals.get(criterion);
			Float percentageWeight = rawWeight/total;
			if (!criterion.equals(Matrix.GRAND_TOTAL_KEY)){
				dd.criterionToWeightMap.put(criterion, percentageWeight);
			}
		}
	}
	
	/**
	 * Generates the list of criteria comparisons that need to be made
	 * 
	 * @param criteria the list of criteria
	 * @return a list of criterion-criterion, where each item is a map containing
	 * one key-value pair which indicates a comparison
	 */
	private List<String> generateCriteriaPairs(Set<String> criteriaSet){
		
		this.matrix = new Matrix(criteriaSet,criteriaSet);
		List<String> comparisons = new ArrayList<String>();
		
		// create a map in which each entry represents a cell in 
		Object[] criteria = criteriaSet.toArray();
		for (int i=0; i<criteria.length; i++){
			
			String rowCriterion = (String) criteria[i];
			// add the parity cell to the matrix
			this.matrix.setValue(rowCriterion,rowCriterion,PARITY_SPINNER_VALUE);
			
			for (int j=i+1; j<criteria.length; j++){
				
				String colCriterion = (String) criteria[j];
				String normalEncoding = super.encodeStringPair(rowCriterion,colCriterion);
				
				// add both normal and inverse cells to matrix
				this.matrix.setValue(rowCriterion,colCriterion,DEFAULT_SPINNER_VALUE);
				this.matrix.setValue(colCriterion,rowCriterion,DEFAULT_SPINNER_VALUE);
				
				// add only normal cells to user comparisons; inverse values will be calculated
				comparisons.add(normalEncoding);
			}
		}
		
		return comparisons;
	}
	
	/**
	 * Creates a number picker to choose the importance of one criterion vs another
	 * 
	 * @param leftCriterion
	 * @param rightCriterion
	 * @return
	 */
/*	went with a spinner instead
 * private NumberPicker createNumberPicker(String leftCriterion, String rightCriterion){
		
		NumberPicker picker = new NumberPicker(this);
		// this generates the same list multiple times, since the left
		// criterion will be repeated, so if this step gets too slow, try
		// storing in a hash
		picker.setDisplayedValues(generatePickerDisplayedValues(leftCriterion, rightCriterion));
		picker.setMinValue(1);
		picker.setMaxValue(9);
		picker.setValue(5);
		picker.setEnabled(true);
		picker.setWrapSelectorWheel(false);
		
		return picker;
	}*/
	
	/**
	 * Creates a spinner to choose the importance of one criterion vs another
	 * 
	 * @param encodedCriteriaPair
	 * @return
	 */
	private Spinner createSpinner(String encodedCriteriaPair){
		
		String[] criteria = super.decodeStringPair(encodedCriteriaPair);
		
		ArrayAdapter<CharSequence> adapter = 
				new ArrayAdapter<CharSequence>(
						this, 
						android.R.layout.simple_spinner_item, 
						generatePickerDisplayedValues(criteria[0], criteria[1]));
		adapter.setDropDownViewResource(R.layout.multiline_spinner_dropdown_item);
		
		Spinner spinner = new Spinner(this);
		spinner.setTag(encodedCriteriaPair);
		spinner.setAdapter(adapter);
		spinner.setSelection(DEFAULT_SPINNER_VALUE_POSITION);
		spinner.setOnItemSelectedListener(this);
		
		return spinner;
	}
	
	/**
	 * Generates the values to be displayed in a criterion's picker
	 * 
	 * @param criterion
	 * @return
	 */
	private String[] generatePickerDisplayedValues(String leftCriterion, String rightCriterion){
		
		String[] values = new String[spinnerLabelsTemplate.length];
		for (int i=0; i<spinnerLabelsTemplate.length; i++){
			values[i] = leftCriterion + spinnerLabelsTemplate[i] + rightCriterion;
		}
		return values;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		
		String encodedPair = (String) parent.getTag();
		String[] criteria = super.decodeStringPair(encodedPair);
		String rowCriterion = criteria[0];
		String colCriterion = criteria[1];
		float normalValue = spinnerValues[pos];
		float invertedValue = 1/normalValue;
		super.logInfo("Spinner value: " + normalValue + 
				" | invertedValue: " + invertedValue);
		
		
		this.matrix.setValue(rowCriterion, colCriterion, normalValue);
		this.matrix.setValue(colCriterion, rowCriterion, invertedValue);
		super.logInfo(this.matrix.toString());
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// nothing to do
		
	}

	@Override
	public void onClick(View v) {
		calculateWeights();
		startActivity(super.createIntent());
		
	}

}
