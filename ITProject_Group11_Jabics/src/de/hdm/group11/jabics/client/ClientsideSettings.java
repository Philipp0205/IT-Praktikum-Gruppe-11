package de.hdm.group11.jabics.client;

import java.util.logging.Logger;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import de.hdm.group11.jabics.shared.*;

/*
 * @author thies
 * @author Gerlings
 * 
 * */
public class ClientsideSettings {

	/*
	 * Remote Service Proxy: 
	 * Verbindungsaufnahme mit dem Server-Seitigen Dienst
	 * <code>EditorService</code>.
	 * 
	 * */
	
	/**
	 * TODO: add description
	 * @param u
	 * @return
	 * @throws IllegalArgumentException
	 */
	private static EditorServiceAsync contactAdmin = null;
	
	/**
	 * TODO: add description
	 * @param u
	 * @return
	 * @throws IllegalArgumentException
	 */
	private static ReportGeneratorServiceAsync reportGeneratorService = null;
	
	private static LoginServiceAsync loginServiceAsync = null;
	
	/**
	 * TODO: add description
	 * @param u
	 * @return
	 * @throws IllegalArgumentException
	 */
	private static final String LOGGER_NAME = "Jabics Web Client";
	
	/**
	 * TODO: add description
	 * @param u
	 * @return
	 * @throws IllegalArgumentException
	 */
	private static final Logger log = Logger.getLogger(LOGGER_NAME);
	
	/**
	 * TODO: add description
	 * @param u
	 * @return
	 * @throws IllegalArgumentException
	 */
	public static Logger getLogger() {
		return log;
	}
	
	/**
	 * TODO: add description
	 * @param u
	 * @return
	 * @throws IllegalArgumentException
	 */
	public static EditorServiceAsync getEditorService() {
		if (contactAdmin == null) {
			contactAdmin = GWT.create(EditorService.class);
		}
		
		return contactAdmin;
	}
	
	/**
	 * TODO: add description
	 * @param u
	 * @return
	 * @throws IllegalArgumentException
	 */
	public static ReportGeneratorServiceAsync getReportGeneratorService() {
		if(reportGeneratorService == null) {
			
			reportGeneratorService = GWT.create(ReportGeneratorService.class);
		
			final AsyncCallback<Void> initReportGeneratorCallback = new AsyncCallback<Void>() {
				@Override
				public void onFailure(Throwable caught) {
					
					ClientsideSettings.getLogger().severe("Der ReportGenerator konnte nicht initialisiert werden!");
					
				}

				@Override
				public void onSuccess(Void result) {
					// TODO Auto-generated method stub
					ClientsideSettings.getLogger().info("Der ReportGenerator wurde initialisiert");
					
				}
			};
			
			//reportGeneratorService.init(initReportGeneratorCallback);
		}
		
		return reportGeneratorService;
	}
	
	public static LoginServiceAsync getLoginService() { 
		if (loginServiceAsync == null) {
			loginServiceAsync = GWT.create(LoginService.class);
		}
		return loginServiceAsync;
	}
	
}
