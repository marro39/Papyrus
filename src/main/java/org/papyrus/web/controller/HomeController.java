package org.papyrus.web.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.papyrus.core.Measure;
import org.papyrus.core.Measure_Container;
import org.papyrus.core.Process_Step;
import org.papyrus.core.Process_Step_Container;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {
	
	private File processFile;
	private BufferedReader bufferedReader = null;
	//Contains all process_steps
	private Process_Step_Container process_Step_Container;
	
	//----------------------------------Controller methods----------------------------------------------------------
	@RequestMapping(value={"/","test"}, method=RequestMethod.GET)	
	public String getHomePage(Model model){	
		//System.out.println("In getHomePage function!");
		return "home";
	}	
	
	//This function handles an ajax request from home.html page. It processes a typed csv file
	@RequestMapping(value={"/process"}, method=RequestMethod.POST)	
	@ResponseBody public Process_Step_Container processFile(@RequestParam("fileLocation") String fileLocation){	
		
		//System.out.println("In processFile function!");
		String sLine;		
		String[] sFileData;
		Process_Step process_Step;
		Measure_Container measure_Container;
		Measure measure;
		int lineCounter = 0;
		Boolean process_stepExists=false;
		Boolean measureExists=false;
		
		processFile=new File(fileLocation);
		process_Step_Container = new Process_Step_Container();
		
		try{			
			
			bufferedReader=new BufferedReader(new FileReader(processFile));
			while((sLine=bufferedReader.readLine()) != null){
				sFileData = sLine.split(",");
				//First line with the headings, continue to the next line
				if((sFileData[0].equals("Process step"))){
					continue;
				}				
				
				//Loop through each Process_Step and check if process step exists or not.				
				for(Process_Step processStepTemp : process_Step_Container.getProcess_steps()){					
					if(processStepTemp.getProcessStep().equals(sFileData[0])){							
						
						//Loop through each measure container in each process step
						for(Measure_Container measure_ContainerTemp : processStepTemp.getMeasures()){
							if(measure_ContainerTemp.getMeasureTitel().equals(sFileData[1])){
								measureExists=true;
								//Add measure value to the measure container!
								measure = setMeasureItems(sFileData[2]);
								measure_ContainerTemp.getMeasureContainer().add(measure);
								break;
							}
							else{
								measureExists=false;
								//Create a new measure container, add it to its process step. Add measure value to it`s 
								//measure container
							}
							
						}
						
						process_stepExists=true;
						//A process step exists but it has no measure. Create a measure and add value to it.
						if(measureExists==false){
							measure_Container = new Measure_Container();
							measure_Container.setMeasureTitel(sFileData[1]);
							measure = setMeasureItems(sFileData[2]);
							measure_Container.getMeasureContainer().add(measure);
							processStepTemp.getMeasures().add(measure_Container);							
						}									
						break;
					}					
					else{						
						process_stepExists=false;						
					}												
				}
				if(process_stepExists==false){					
					process_Step = new Process_Step(); 
					process_Step.setProcessStep(sFileData[0]);
					
					if(measureExists==false){
						measure_Container = new Measure_Container();
						measure_Container.setMeasureTitel(sFileData[1]);
						measure = setMeasureItems(sFileData[2]);
						measure_Container.getMeasureContainer().add(measure);
						process_Step.getMeasures().add(measure_Container);
					}									
					
					//process_Step.getMeasures().add(measure_Container);
					process_Step_Container.getProcess_steps().add(process_Step);					
					//System.out.println("Added new process step!");										
				}					
				process_stepExists=false;
				lineCounter++;				
			}						
		} 
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} 
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		//printProcessStepsFullData();
		calculateMeasures();
		printCompletedTable();		
		
		return process_Step_Container;
	}
	//------------------------------------------------Methods-------------------------------------------------------
	public Measure setMeasureItems(String value){
		Measure measureTemp = new Measure();												
		if(value.equals("ERROR")){
			measureTemp.setValue(0);
			measureTemp.setError(true);
		}						
		else{
			try{
				float tempNum = Float.parseFloat(value);
				measureTemp.setValue(tempNum);
			}
			catch(NumberFormatException ex){
				measureTemp.setValue(0);
				measureTemp.setError(true);
			}						
		}
		return measureTemp;
	}
	public void printProcessStepsFullData(){
		System.out.println("------------------------------------------------------------------------------------");
		System.out.println("Size of process steps are: " + process_Step_Container.getProcess_steps().size());
		System.out.println("Process steps are: ");
		for(Process_Step processStepTemp : process_Step_Container.getProcess_steps()){
			System.out.println("\t" + processStepTemp.getProcessStep());
			
			for(Measure_Container measure_Container1 : processStepTemp.getMeasures()){
				System.out.println("\t\t" + measure_Container1.getMeasureTitel());
				
				for(Measure measure1 : measure_Container1.getMeasureContainer()){
					System.out.println("\t\t\t" + measure1.getValue() + "\t" + measure1.getError());
				}
				
			}
		}
		System.out.println("------------------------------------------------------------------------------------");
	}
	public void calculateMeasures(){
		int tempMaxMeasures=0;
		for(Process_Step processStepTemp : process_Step_Container.getProcess_steps()){
			if(tempMaxMeasures < processStepTemp.getMeasures().size()){
				tempMaxMeasures = processStepTemp.getMeasures().size();
			}			
			//System.out.println("Measures sorted by errors: " + processStepTemp.get);
			for(Measure_Container measure_Container : processStepTemp.getMeasures()){
				measure_Container.calcTotalAmount();
				measure_Container.calcAverage();
				measure_Container.calcErrors();
				//System.out.println("Total amount in " + processStepTemp.getProcessStep() + " with measure: " + measure_Container.getMeasureTitel() + " is: " + measure_Container.getTotalAmount());
				//System.out.println("Average value in " + processStepTemp.getProcessStep() + " with measure: " + measure_Container.getMeasureTitel() + " is: " + measure_Container.getAverage());
				//System.out.println("Errors  value in " + processStepTemp.getProcessStep() + " with measure: " + measure_Container.getMeasureTitel() + " is: " + measure_Container.getErrors());				
			}
			//System.out.println("------------------------------------------------------------------------------------");
			processStepTemp.sortMeasures();
			/*
			System.out.println("Process step: " + processStepTemp.getProcessStep());			
			for(Measure_Container measure_Container : processStepTemp.getMeasures()){
				System.out.println("\tMeasure: " + measure_Container.getMeasureTitel());
				System.out.println("\tErrors: " + measure_Container.getErrors());				
			}
			*/
		}
		process_Step_Container.setMaximumMeasures(tempMaxMeasures);
		//System.out.println("Maximum measures are: " + process_Step_Container.getMaximumMeasures());
	}
	public void printCompletedTable(){		
		System.out.println("------------------------------------------------------------------------------------");		
		System.out.flush();
		System.out.print("Process step\t");
		for(int i=0;i<process_Step_Container.getMaximumMeasures();i++){
			System.out.print("Average\t");
			System.out.print("Errors\t");
		}
		System.out.print("\n");
		System.out.flush();
		for(Process_Step processStepTemp : process_Step_Container.getProcess_steps()){
			System.out.print(processStepTemp.getProcessStep() + "\t");			
			for(Measure_Container measure_Container: processStepTemp.getMeasures()){
				System.out.print(measure_Container.getAverage() + "\t");
				System.out.print(measure_Container.getErrors() + "\t");				
			}
			System.out.print("\n");
			System.out.flush();
		}
	}
}
