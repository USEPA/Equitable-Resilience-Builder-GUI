package com.epa.erb;

import java.util.ArrayList;

public class IndicatorCard {
	
	String id;
	String system;
	String indicator;
	String definition;
	String resilienceValue;
	String equityValue;
	String localConcern;
	String quantitativeDataSources;
	String quantitativeDataCollProcess;
	String qualitativeDataCollProcess;
	String additionalInformation;
	String rawDataCollectionNotes;
	String dataPoints;
	String thresholds;
	ArrayList<String> questionsToAnswer;
	
	public IndicatorCard(String id, String s, String i, String d, String rV, String eV, String lC,  String qnDS, String qnDC, String qlDC, String aI, String rdColl, String dP, String t, ArrayList<String> q) {
		this.id = id;
		this.system = s;
		this.indicator = i;
		this.definition = d;
		this.resilienceValue = rV;
		this.equityValue = eV;
		this.localConcern =lC;
		this.quantitativeDataSources = qnDS;
		this.quantitativeDataCollProcess = qnDC;
		this.qualitativeDataCollProcess = qlDC;
		this.additionalInformation = aI;
		this.rawDataCollectionNotes = rdColl;
		this.dataPoints = dP;
		this.thresholds = t;
		this.questionsToAnswer = q;
	}
	
	private final String socialEnvironmentSystemColor = "#A6D0E2"; //Blue
	private final String builtEnvironmentSystemColor = "#F0C485"; //Orange
	private final String naturalEnvironmentSystemColor = "#CBE2A6"; //Green

	public String getSocialEnvironmentSystemColor() {
		return socialEnvironmentSystemColor;
	}
	public String getBuiltEnvironmentSystemColor() {
		return builtEnvironmentSystemColor;
	}
	public String getNaturalEnvironmentSystemColor() {
		return naturalEnvironmentSystemColor;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSystem() {
		return system;
	}
	public void setSystem(String system) {
		this.system = system;
	}
	public String getIndicator() {
		return indicator;
	}
	public void setIndicator(String indicator) {
		this.indicator = indicator;
	}
	public String getDefinition() {
		return definition;
	}
	public void setDefinition(String definition) {
		this.definition = definition;
	}
	public String getResilienceValue() {
		return resilienceValue;
	}
	public void setResilienceValue(String resilienceValue) {
		this.resilienceValue = resilienceValue;
	}
	public String getEquityValue() {
		return equityValue;
	}
	public void setEquityValue(String equityValue) {
		this.equityValue = equityValue;
	}
	public String getLocalConcern() {
		return localConcern;
	}
	public void setLocalConcern(String localConcern) {
		this.localConcern = localConcern;
	}
	public String getQuantitativeDataSources() {
		return quantitativeDataSources;
	}
	public void setQuantitativeDataSources(String quantitativeDataSources) {
		this.quantitativeDataSources = quantitativeDataSources;
	}
	public String getQuantitativeDataCollProcess() {
		return quantitativeDataCollProcess;
	}
	public void setQuantitativeDataCollProcess(String quantitativeDataCollProcess) {
		this.quantitativeDataCollProcess = quantitativeDataCollProcess;
	}
	public String getQualitativeDataCollProcess() {
		return qualitativeDataCollProcess;
	}
	public void setQualitativeDataCollProcess(String qualitativeDataCollProcess) {
		this.qualitativeDataCollProcess = qualitativeDataCollProcess;
	}
	public String getAdditionalInformation() {
		return additionalInformation;
	}
	public void setAdditionalInformation(String additionalInformation) {
		this.additionalInformation = additionalInformation;
	}
	public String getRawDataCollectionNotes() {
		return rawDataCollectionNotes;
	}
	public void setRawDataCollectionNotes(String rawDataCollectionNotes) {
		this.rawDataCollectionNotes = rawDataCollectionNotes;
	}
	public String getDataPoints() {
		return dataPoints;
	}
	public void setDataPoints(String dataPoints) {
		this.dataPoints = dataPoints;
	}
	public String getThresholds() {
		return thresholds;
	}
	public void setThresholds(String thresholds) {
		this.thresholds = thresholds;
	}
	public ArrayList<String> getQuestionsToAnswer() {
		return questionsToAnswer;
	}
	public void setQuestionsToAnswer(ArrayList<String> questionsToAnswer) {
		this.questionsToAnswer = questionsToAnswer;
	}
}
