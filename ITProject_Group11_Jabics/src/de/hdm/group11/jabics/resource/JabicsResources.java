package de.hdm.group11.jabics.resource;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface JabicsResources extends ClientBundle  {
	JabicsResources INSTANCE = GWT.create(JabicsResources.class);
	
	 @Source("LogoSmall.png")
	  ImageResource logo();
	 
	 @Source("greendot.png")
	  ImageResource greendot();
	 
	 @Source("greendot.png")
	  ImageResource greendotlist();
	 
	 @Source("yellowdot.png")
	  ImageResource yellowdot();
	 
	 @Source("reddot.png")
	  ImageResource reddot();
	 
	 @Source("reddot.png")
	  ImageResource reddotlist();
	 
	 @Source("Save.png")
	  ImageResource save();

}
