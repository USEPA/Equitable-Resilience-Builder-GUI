package com.epa.erb;

import java.util.ArrayList;

public class IndicatorCard {
	
	String system;
	String indicator;
	String definition;
	String unitOfMeasurement;
	String resilienceValue;
	String assessment;
	ArrayList<String> questionsToAnswer;
	String quantitativeDataSources;
	String quantitativeDataCollProcess;
	String qualitativeDataCollProcess;
	String additionalInformation;
	
	public IndicatorCard(String s, String i, String d, String m, String rV, String a, ArrayList<String> q, String qnDS, String qnDC, String qlDC, String etc) {
		this.system = s;
		this.indicator = i;
		this.definition = d;
		this.unitOfMeasurement = m;
		this.resilienceValue = rV;
		this.assessment = a;
		this.questionsToAnswer = q;
		this.quantitativeDataSources = qnDS;
		this.quantitativeDataCollProcess = qnDC;
		this.qualitativeDataCollProcess = qlDC;
		this.additionalInformation = etc;
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

	public String getUnitOfMeasurement() {
		return unitOfMeasurement;
	}

	public void setUnitOfMeasurement(String unitOfMeasurement) {
		this.unitOfMeasurement = unitOfMeasurement;
	}

	public String getResilienceValue() {
		return resilienceValue;
	}

	public void setResilienceValue(String resilienceValue) {
		this.resilienceValue = resilienceValue;
	}

	public String getAssessment() {
		return assessment;
	}

	public void setAssessment(String assessment) {
		this.assessment = assessment;
	}

	public ArrayList<String> getQuestionsToAnswer() {
		return questionsToAnswer;
	}

	public void setQuestionsToAnswer(ArrayList<String> questionsToAnswer) {
		this.questionsToAnswer = questionsToAnswer;
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
	
}
