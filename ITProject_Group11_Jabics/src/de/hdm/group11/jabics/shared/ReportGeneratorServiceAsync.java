package de.hdm.group11.jabics.shared;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.hdm.group11.jabics.shared.bo.Property;
import de.hdm.group11.jabics.shared.report.AllContactsInSystemReport;
import de.hdm.group11.jabics.shared.report.ContactReport;
import de.hdm.group11.jabics.shared.report.FilteredContactsOfUserReport;
import de.hdm.group11.jabics.shared.report.Paragraph;

public interface ReportGeneratorServiceAsync {

	void createAllContactsInSystemReport(AsyncCallback<AllContactsInSystemReport> callback);

	void createFilteredContactsOfUserReport(ArrayList<ContactReport> reports, Paragraph pa, Property pp, String search,
			AsyncCallback<FilteredContactsOfUserReport> callback);

}
