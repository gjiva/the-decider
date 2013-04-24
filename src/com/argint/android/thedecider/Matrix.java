/**
 * 
 */
package com.argint.android.thedecider;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import android.util.Log;

/**
 * @author snow
 *
 */
public class Matrix {
	
	public static final String GRAND_TOTAL_KEY = "grandTotal";
	
	private String[] rowLabels = new String[1];
	private String[] colLabels = new String[1];
	private float[][] rows;
	
	/**
	 * 
	 * @param rowLabels
	 * @param columnLabels
	 */
	public Matrix(Set<String> rowLabels, Set<String> columnLabels){
		
		this.rowLabels = rowLabels.toArray(this.rowLabels);
		this.colLabels = columnLabels.toArray(this.colLabels);
		
		Arrays.sort(this.rowLabels);
		Arrays.sort(this.colLabels);
		
		this.rows = new float[this.rowLabels.length][this.colLabels.length];
	}
	
	/**
	 * 
	 * @param rowLabel
	 * @param columnLabel
	 * @return
	 */
	public float getValue(String rowLabel, String columnLabel){
		
		int[] indices = getRowAndColIndex(rowLabel, columnLabel);
		return rows[indices[0]][indices[1]];
	}
	
	/**
	 * 
	 * @param rowLabel
	 * @param columnLabel
	 */
	public void setValue(String rowLabel, String columnLabel, float value){
		
		int[] indices = getRowAndColIndex(rowLabel, columnLabel);
		rows[indices[0]][indices[1]] = value;
	}
	
	/**
	 * 
	 * @return a map of row label to a value representing
	 * the total of all column values within that row added up.
	 * An extra GRAND_TOTAL_KEY key is put in, with the value of 
	 * the sum of all cells 
	 */
	public Map<String,Float> getRowTotals(){
		
		Map<String,Float> totals = new HashMap<String, Float>();
		float grandTotal = 0;
		for (int i=0; i<this.rowLabels.length; i++){
			float total = 0;
			for (int j=0; j<this.colLabels.length; j++){
				total += this.rows[i][j];
			}
			totals.put(this.rowLabels[i], total);
			grandTotal += total;
		}
		totals.put("grandTotal", grandTotal);
		
		return totals;
	}
	
	/**
	 * Converts a set or row and column labels into indices
	 * 
	 * @param rowLabel
	 * @param columnLabel
	 * @return a 2 element int array: the first element is the row index, 
	 * the second, the column index
	 */
	private int[] getRowAndColIndex(String rowLabel, String columnLabel){
		
		int rowIndex = Arrays.binarySearch(this.rowLabels, rowLabel);
		int colIndex = Arrays.binarySearch(this.colLabels, columnLabel);
		
		if (rowIndex >= 0){
			if (colIndex >= 0){
				int[] indices = new int[2];
				indices[0] = rowIndex;
				indices[1] = colIndex;
				return indices;
			}
			else {
				Log.i(Matrix.class.getName(), "colIndex: " + colIndex);
				Log.i(Matrix.class.getName(), "columnLabel: " + columnLabel);
				Log.i(Matrix.class.getName(), "colLabels: " + Arrays.toString(this.colLabels));
				throw new RuntimeException("Couldn't find column label: " + columnLabel);
			}
		}
		else {
			Log.i(Matrix.class.getName(), "rowIndex: " + rowIndex);
			Log.i(Matrix.class.getName(), "rowLabel: " + rowLabel);
			Log.i(Matrix.class.getName(), "rowLabels: " + Arrays.toString(this.rowLabels));
			throw new RuntimeException("Couldn't find row label: " + rowLabel);
		}
	}
	
	@Override
	public String toString(){
		
		StringBuffer sb = new StringBuffer();
		int[] colWidths = calculateColumnWidths();
		
		sb.append("Matrix:\n");
		// print the column labels row, with a blank row first
		sb.append(pad(colWidths[0],""));
		for (int i=0; i<this.colLabels.length; i++){
			sb.append(" " + pad(colWidths[i+1],this.colLabels[i]) + " ");
		}
		sb.append("\n");
		
		// print each row, with a column label first
		for (int i=0; i<this.rowLabels.length; i++){
			sb.append(pad(colWidths[0],this.rowLabels[i]));
			for (int j=0; j<this.colLabels.length; j++){
				sb.append(" " + pad(colWidths[j+1],String.valueOf(this.rows[i][j])) + " ");
			}
			sb.append("\n");
		}
		
		return sb.toString();
	}
	
	/**
	 * 
	 * @return
	 */
	private int[] calculateColumnWidths(){
		
		int[] widths = new int[this.colLabels.length + 1];
		
		// first, the max of the row labels
		int width = 0;
		for (int i=0; i<this.rowLabels.length; i++){
			if (this.rowLabels[i].length() > width){
				width = this.rowLabels[i].length();
			}
		}
		widths[0] = width;
		
		// then, each of the columns
		for (int i=0; i<this.colLabels.length; i++){
			
			width = 4; // assume the numbers are 4 digits long
			if (this.colLabels.length > width){
				width = this.colLabels.length;
			}
			widths[i+1] = width;
		}
		
		return widths;
	}
	
	/**
	 * Pads a string with spaces in front of it, such that
	 * its length after will the length of the numChars passed in.
	 * 
	 * If the string is longer than numChars, it gets truncated at the end
	 * 
	 * @param numChars the length of the string desired
	 * @param str the string to pad
	 * @return the padded string
	 */
	private String pad(int numChars, String str){
		
		// if it's too long, truncate it
		if (str.length() > numChars){
			return str.substring(0,numChars);
		}
		
		String paddedStr = str;
		for (int i=0; i<(numChars - str.length()); i++){
			paddedStr = " " + paddedStr;
		}
		return paddedStr;
	}
}
