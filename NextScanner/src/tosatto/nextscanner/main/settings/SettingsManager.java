package tosatto.nextscanner.main.settings;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.jdom2.Attribute;
import org.jdom2.DataConversionException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.AttributeFilter;
import org.jdom2.filter.ContentFilter;
import org.jdom2.filter.ElementFilter;
import org.jdom2.filter.Filter;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class SettingsManager {
	
	private static final String SETTINGS_FILE_NAME = "settings.xml";
	
	private static SettingsManager SM = null;
	
	private Element root;
	
	public static SettingsManager get()
	{
		if (SM == null)
			SM = new SettingsManager();
		
		return SM;
	}
	
	private SettingsManager()
	{
		
	}
	
	public void saveSettings ()
	{
		XMLOutputter outputter = new XMLOutputter();
		Document d = new Document(root);
		outputter.setFormat(Format.getPrettyFormat());
		try {
			outputter.output(d, new FileOutputStream (SETTINGS_FILE_NAME));
	
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		root.detach();
	}
	
	public void createDefault ()
	{
		root = new Element("settings");
		
		DefaultSettings.defaultSettings(this);
		
		
	}
	
	public Object getValue (String name)
	{
		Filter<Element> filter1 = new ElementFilter();
		 
		List<Element> listOfElements = root.getContent(filter1);
	
		 
		for (Element element : listOfElements) 
		{
		 
			if (element.getAttribute("name").getValue().equals(name))
				return getSetting(element);
		 
		}
		   
		return null;
	}
	
	private Object getSetting (Element el)
	{
		String type = el.getAttribute("type").getValue();
		
		Object res = null;
		
		if (type.equals(Integer.class.toString()))
		{
			try {
				res = (Object)el.getAttribute("value").getIntValue();
			} catch (DataConversionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if (type.equals(Double.class.toString()))
		{
			try {
				res = (Object)el.getAttribute("value").getDoubleValue();
			} catch (DataConversionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			res = (Object)el.getAttribute("value").getValue();
		}
		
		return res;
	}
	
	private String getValue (Object obj)
	{
		String type = obj.getClass().toString();
		
		String v = "";
		
		if (type.equals(int.class.toString()))
		{
			v = Integer.toString((int)obj);
		}
		else if (type.equals(double.class.toString()))
		{
			v = Double.toString((double)obj);
		}
		else
		{
			v = obj.toString();
		}
		
		return v;
	}
	
	public void addSetting (String name, Object value)
	{
		if (!existsSetting(name))
		{
			String type = value.getClass().toString();
			
			String v = getValue (value);
			
			Element setting = new Element("setting");
			
			setting.setAttribute("name", name);
			setting.setAttribute("value", v);
			setting.setAttribute("type", type);
			
			root.addContent(setting);
		}
		else 
		{
			updateSetting(name, value);
		}
	}
	
	public Element getSetting(String name)
	{
		Filter<Element> filter1 = new ElementFilter();
		 
		List<Element> listOfElements = root.getContent(filter1);
	
		 
		for (Element element : listOfElements) 
		{
		 
			if (element.getAttribute("name").getValue().equals(name))
				return element;
		 
		}
		   
		return null;
	}
	
	public boolean existsSetting (String name)
	{
		Filter<Element> filter1 = new ElementFilter();
		 
		List<Element> listOfElements = root.getContent(filter1);
	
		 
		for (Element element : listOfElements) 
		{
		 
			if (element.getAttribute("name").getValue().equals(name))
				return true;
		 
		}
		   
		return false;
	}
	
	public void updateSetting (String name, Object value)
	{
		
		String v = getValue (value);
		
		Element e = getSetting (name);
		
		e.setAttribute("value", v);
		
	}
	
	public void loadSettings ()
	{
		File f = new File(SETTINGS_FILE_NAME);
		if(f.exists() && !f.isDirectory()) 
		{ 
			//creating JDOM SAX parser
            SAXBuilder builder = new SAXBuilder();
			
			//reading XML document
            Document xml = null;
            try {
                    xml = builder.build(new File(SETTINGS_FILE_NAME));
            } catch (JDOMException e) {
                    e.printStackTrace();
            } catch (IOException e) {
                    e.printStackTrace();
            }
          
            root = xml.getRootElement();
            
            root.detach();
		}
		else
		{
			createDefault();
			saveSettings();
		}
	}
}
