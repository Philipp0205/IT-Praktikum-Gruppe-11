package de.hdm.group11.jabics.client;

import java.util.logging.Logger;
import com.google.gwt.core.shared.GWT;
import de.hdm.group11.jabics.shared.*;

/*
 * @author Thies
 * @author Gerlings
 * */
public class ClientsideSettings {

	public ClientsideSettings() {}
	/*
	 * Remote Service Proxy: Verbindungsaufnahme mit dem Server-Seitigen Dienst
	 * <code>EditorService</code>.
	 * 
	 */
	private static EditorServiceAsync editorService = null;

	private static ReportGeneratorServiceAsync reportGeneratorService = null;

	private static LoginServiceAsync loginService = null;

	/**
	 * Das EditorService Proxy-Objekt von GWT erstellen lassen.
	 * 
	 * @return editorService EditorService Proxy
	 * @throws IllegalArgumentException
	 */
	public static EditorServiceAsync getEditorService() {
		if (editorService == null) {
			editorService = GWT.create(EditorService.class);
		}
		return editorService;
	}

	/**
	 * Das ReportGeneratorService Proxy-Objekt von GWT erstellen lassen.
	 * 
	 * @return reportGeneratorService ReportGeneratorService Proxy
	 * @throws IllegalArgumentException
	 */
	public static ReportGeneratorServiceAsync getReportGeneratorService() {
		if (reportGeneratorService == null) {
			reportGeneratorService = GWT.create(ReportGeneratorService.class);
		}
		return reportGeneratorService;
	}
	
	/**
	 * Das LoginService Proxy-Objekt von GWT erstellen lassen.
	 * 
	 * @return loginService loginService Proxy
	 * @throws IllegalArgumentException
	 */
	public static LoginServiceAsync getLoginService() {
		if (loginService == null) {
			loginService = GWT.create(LoginService.class);
		}
		return loginService;
	}

}
