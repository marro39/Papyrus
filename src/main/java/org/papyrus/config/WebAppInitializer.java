package org.papyrus.config;

import javax.servlet.Filter;

import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer{
		
		//This context will be shared by all elements in the application. Servlets, filters, listeners...
		//It returns a class containing an array of classes
		@Override
		protected Class<?>[] getRootConfigClasses() {		
			return new Class<?>[]{};
		}
		
		//Returns a class containing an array of spring configuration classes. They surve the purpuses of delegates
		//for the Dispatcherservlet
		@Override
		protected Class<?>[] getServletConfigClasses() {		
			return new Class<?>[]{WebConfig.class};
		}
		
		//Url that the servlet shall handle = All
		@Override
		protected String[] getServletMappings() {		
			return new String[]{"/"};
		}
		//Return a filter array containing encodingfilter objects
		@Override
		protected Filter[] getServletFilters(){
			CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
			characterEncodingFilter.setEncoding("UTF-8");		
			return new Filter[] {characterEncodingFilter};
		}
}
