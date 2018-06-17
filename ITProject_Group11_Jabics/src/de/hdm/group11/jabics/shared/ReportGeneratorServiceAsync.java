package de.hdm.group11.jabics.shared;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.hdm.group11.jabics.shared.bo.*;
import de.hdm.group11.jabics.shared.report.*;

public interface ReportGeneratorServiceAsync {
	
	void createAllContactsOfUserReport(JabicsUser u, AsyncCallback<AllContactsOfUserReport> callback);
	
	void createAllContactsInSystemReport(AsyncCallback<AllContactsInSystemReport> callback);

	void createFilteredContactsOfUserReport(PValue pv, JabicsUser u,
			AsyncCallback<FilteredContactsOfUserReport> callback);
	
	void init(AsyncCallback<Void> callback);

}
