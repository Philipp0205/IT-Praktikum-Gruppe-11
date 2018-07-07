package de.hdm.group11.jabics.shared;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.hdm.group11.jabics.shared.report.*;
import de.hdm.group11.jabics.shared.bo.*;

/**
 * Diese Klasse ist eine synchrone Schnittstelle zur Erstellung von allen
 * Reports. In Jabics gibt es drei verschiedene Reportarten, welche ausgegeben
 * werden k�nnen. Siehe {@link AllContactsInSystemReport},
 * {@link AllContactsOfUserReport} und {@link FilteredContactsOfUserReport}.
 * 
 * Dabei bietet diese Klasse untere anderem verschiedene
 * <code>create</code>-Methoden, mit denen die zuvor genannten Reports erstellt
 * werden k�nnen.
 * 
 * @author Kurrle
 * @author Anders
 */

@RemoteServiceRelativePath("report")
public interface ReportGeneratorService extends RemoteService {

	public AllContactsInSystemReport createAllContactsInSystemReport() throws IllegalArgumentException;

	public AllContactsOfUserReport createAllContactsOfUserReport(JabicsUser u) throws IllegalArgumentException;

	public FilteredContactsOfUserReport createFilteredContactsOfUserReport(PValue pv, JabicsUser u)
			throws IllegalArgumentException;

	public void init() throws IllegalArgumentException;

	public ArrayList<Property> getPropertysOfJabicsUser(JabicsUser u) throws IllegalArgumentException;

	public FilteredContactsOfUserReport createAllSharedContactsReport(JabicsUser u, ArrayList<JabicsUser> finalUser);

}
