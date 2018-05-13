package de.hdm.group11.jabics.shared;

import com.google.gwt.user.client.rpc.RemoteService;

import de.hdm.group11.jabics.shared.report.AllContactsInSystemReport;

public interface ReportGeneratorService extends RemoteService {
	
	public AllContactsInSystemReport createAllContactsInSystemReport();
	
	

}
