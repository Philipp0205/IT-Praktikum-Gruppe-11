package de.hdm.group11.jabics.server;

import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.types.CommandlineJava.SysProperties;

import com.google.gwt.user.server.rpc.*;
import de.hdm.group11.jabics.shared.ReportGeneratorService;
import de.hdm.group11.jabics.shared.bo.Property;
import de.hdm.group11.jabics.shared.report.AllContactsInSystemReport;
import de.hdm.group11.jabics.shared.report.ContactReport;
import de.hdm.group11.jabics.shared.report.FilteredContactsOfUserReport;
import de.hdm.group11.jabics.shared.report.Paragraph;
import de.hdm.group11.jabics.shared.report.PropertyView;

public class ReportGeneratorServiceImpl extends RemoteServiceServlet 
	implements ReportGeneratorService {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4462530285584570547L;
	/**
	 * 
	 */
	@Override
	public AllContactsInSystemReport createAllContactsInSystemReport() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FilteredContactsOfUserReport createFilteredContactsOfUserReport(ArrayList<ContactReport> reports,
			Paragraph pa, Property pp, String search) throws IllegalArgumentException {
		
		
		switch (pp.getType())  {
		case STRING: this.filterContactsByString(reports, pa, pp);
		case INT: this.filterContactsByInt(reports, pa, pp);
		case FLOAT: this.filterContactsByFloat(reports, pa, pp);
		case DATE: this.filterContactsByDate(reports, pa, pp);
			
		}
	
		return null;
	}
	
	public ArrayList<ContactReport> filterContactsByString(ArrayList<ContactReport> reports,
			Paragraph pa, Property pp) {
		
		ArrayList<ContactReport> results = new ArrayList<ContactReport>();
		
		for (ContactReport cr : reports) {
			for (PropertyView pv : cr.getPropertyViews()) {
				if (pv.getPvalue() == pa.getContent()) { 
					results.add(cr);
				} else System.out.println("Die Suche war erfolglos.");
			} 
		}
		return results; 
	}
		
	public ArrayList<ContactReport> filterContactsByInt(ArrayList<ContactReport> reports,
			Paragraph pa, Property pp) {
		
		ArrayList<ContactReport> results = new ArrayList<ContactReport>();
		
		for (ContactReport cr : reports) {
			for (PropertyView pv : cr.getPropertyViews()) {
				if (pv.getPvalue() == pa.getContent()) { 
					results.add(cr);
				} else System.out.println("Die Suche war erfolglos.");
			} 
		} return results; 		
		
	}
	
	public ArrayList<ContactReport> filterContactsByDate(ArrayList<ContactReport> reports,
			Paragraph pa, Property pp) {
		
		ArrayList<ContactReport> results = new ArrayList<ContactReport>();
		
		for (ContactReport cr : reports) {
			for (PropertyView pv : cr.getPropertyViews()) {
				if (pv.getPvalue() == pa.getContent()) { 
					results.add(cr);
				} else System.out.println("Die Suche war erfolglos.");
			} 
		} return results; 		
		
	}
	
	public ArrayList<ContactReport> filterContactsByFloat(ArrayList<ContactReport> reports,
			Paragraph pa, Property pp) {
		
		ArrayList<ContactReport> results = new ArrayList<ContactReport>();
		
		for (ContactReport cr : reports) {
			for (PropertyView pv : cr.getPropertyViews()) {
				if (pv.getPvalue() == pa.getContent()) { 
					results.add(cr);
				} else System.out.println("Die Suche war erfolglos.");
			} 
		} return results; 		
		
	}
		
		
		
		
		
		
	}

