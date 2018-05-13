package de.hdm.group11.jabics.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.hdm.group11.jabics.shared.report.AllContactsInSystemReport;

public interface ReportGeneratorServiceAsync {

	void createAllContactsInSystemReport(AsyncCallback<AllContactsInSystemReport> callback);

}
