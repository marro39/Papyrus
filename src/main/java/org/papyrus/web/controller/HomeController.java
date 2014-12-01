package org.papyrus.web.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.papyrus.core.Process;
import org.papyrus.core.ProcessContainer;
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
	
	@RequestMapping(value={"/","test"}, method=RequestMethod.GET)	
	public String getHomePage(Model model){	
		System.out.println("In getHomePage function!");
		return "home";
	}	
	
	//This function handles an ajax request from home.html page. It processes a typed file
	@RequestMapping(value={"/process"}, method=RequestMethod.POST)	
	@ResponseBody public ProcessContainer[] processFile(@RequestParam("fileLocation") String fileLocation){	
		
		//System.out.println("In processFile function!");
		String sLine;		
		String[] sFileData;
		Process process;		
		
		processFile=new File(fileLocation);		
		ProcessContainer[] processContainer = new ProcessContainer[6];
		
		for(int i=0;i<processContainer.length;i++){
			processContainer[i] = new ProcessContainer();
		}		
		
		try{
			bufferedReader=new BufferedReader(new FileReader(processFile));
			while((sLine=bufferedReader.readLine()) != null){
				
				sFileData = sLine.split(",");
					
				if(!(sFileData[0].equals("Process step") || sFileData[1].equals("Measure") || sFileData[2].equals("Measure"))){
					
					//System.out.println("Size of sFileDate is: " + sFileData.length);
					//System.out.println(sFileData[0] + " || " + sFileData[1] + " || " + sFileData[2]);
					
					//Process contains all necessary data for a process step and specific measure.
					//Created a class to handle it for future extensions
					process=new Process();
					//Process
					process.setProcessStep(sFileData[0]);
					//Measure
					process.setMeausure(sFileData[1]);
					
					String temp = sFileData[2];
					if(temp.equals("ERROR")){
						process.setValue(0);
						process.setError(true);
					}						
					else{
						try{
							float tempNum = Float.parseFloat(sFileData[2]);
							process.setValue(tempNum);
						}
						catch(NumberFormatException ex){
							process.setValue(0);
						}						
					}						
					
					//Creating ProcessContainers for each process step and specific measure. It contains all Process
					//classes that belongs to it. ProcessContainers have functionality to calculate sum, average, and 
					//total value. Also here I create a class for future extensions.
					if(sFileData[0].equals("Picking")){						
						//ProcessContaioner[0]: Process = Picking, Measure = Duration
						if(sFileData[1].equals("Duration")){						
							processContainer[0].getProcessCont().add(process);
							if(process.isError()==true){							
								processContainer[0].addError();
							}							
						}	
						//ProcessContaioner[1]:Process = Picking, Measure = Round trips
						else if(sFileData[1].equals("Round trips")){						
							processContainer[1].getProcessCont().add(process);
							if(process.isError()==true){							
								processContainer[1].addError();
							}							
						}					
					}
					else if(sFileData[0].equals("Loading")){						
						//ProcessContaioner[2]:Process = Loading, Measure = Duration
						if(sFileData[1].equals("Duration")){						
							processContainer[2].getProcessCont().add(process);
							if(process.isError()==true){							
								processContainer[2].addError();
							}							
						}
						//ProcessContaioner[3]:Process = Loading, Measure = Hazards
						else if(sFileData[1].equals("Hazards")){						
							processContainer[3].getProcessCont().add(process);
							if(process.isError()==true){							
								processContainer[3].addError();
							}							
						}
					}
					//ProcessContaioner[4]:Process = Shipping, Measure = Fuel
					else if(sFileData[0].equals("Shipping")){					
						processContainer[4].getProcessCont().add(process);
						if(process.isError()==true){							
							processContainer[4].addError();
						}						
					}
					//ProcessContaioner[5]:Process = Unloading, Measure = Duration
					else if(sFileData[0].equals("Unloading")){					
						processContainer[5].getProcessCont().add(process);
						if(process.isError()==true){							
							processContainer[5].addError();
						}						
					}
				}		
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
		
		processContainer=sortContainers(processContainer);
		printContainers(processContainer);
		
		return processContainer;
	}
	public ProcessContainer[] sortContainers(ProcessContainer[] processContainer){		
		
		ProcessContainer temp;
		//Sort ProcessContaiers by errors at top
		for(int j=0;j<processContainer.length;j++){			
			for(int k=0;k<processContainer.length - 1;k++){
				if(processContainer[j].getErrors() > processContainer[k].getErrors()){
					temp=processContainer[j];
					processContainer[j]=processContainer[k];
					processContainer[k]=temp;
				}
			}
		}
		
		return processContainer;
	}
	
	public void printContainers(ProcessContainer[] processContainer){
		
		System.out.println("Process step || Measure || Errors || Avarage value || Total amount");
		for(int i=0;i<processContainer.length;i++){				
			
			//Also do some calculations before sending it through ajax
			processContainer[i].calculateSum();
			processContainer[i].calculateAverage();	
			
			System.out.println
			(
					processContainer[i].getProcessCont().get(0).getProcessStep()
					+ " || " + processContainer[i].getProcessCont().get(0).getMeausure()  
					+ " || " + processContainer[i].getErrors()  
					+ " || " + processContainer[i].getAverage() 
					+ " || " + processContainer[i].getSum()
			);			
		}
	}	
}
