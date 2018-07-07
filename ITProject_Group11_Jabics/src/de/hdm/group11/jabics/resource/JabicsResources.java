package de.hdm.group11.jabics.resource;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * Interface für die Ressourcen wie pngs.
 * 
 * @author Brase
 */
public interface JabicsResources extends ClientBundle  {
	JabicsResources INSTANCE = GWT.create(JabicsResources.class);
	
	 @Source("isshared.png")
	  ImageResource isshared();
	 
	 @Source("isnotshared.png")
	  ImageResource isnotshared();

}
