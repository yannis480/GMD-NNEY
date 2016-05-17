package projet_gmd;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class HandlerDrugbank extends DefaultHandler 
{

	private ArrayList<String> listeDrug = null;
	boolean b_name = false;
	boolean b_indication = false;
	boolean b_toxicity = false;
	boolean b_synonym = false;

	//return list of drug
	public ArrayList<String> getListe() 
	{
		return listeDrug;
	}

	//call when the program found an open tag
	public void startElement(String uri, String localName,String qName, Attributes attributes) throws SAXException 
	{
		int attributeLength = attributes.getLength();
		
		if (qName.equals("drug")) //found a drug, creation of a list if don't exist
		{
			if (listeDrug == null) 
				listeDrug = new ArrayList<>();
		}
		
		if (qName.equalsIgnoreCase("name")) 
		{
			b_name = true;
		}

		if (qName.equalsIgnoreCase("indication")) 
		{
			b_indication = true;
		}

		if (qName.equalsIgnoreCase("toxicity")) 
		{
			b_toxicity = true;
		}

		if (qName.equalsIgnoreCase("synonym")) 
		{
			if (attributeLength != 0)
			{
				if (attributes.getQName(0).equals("language"))
				{
					b_synonym = true;
				}
			}
		}
	}

	//call at the end of a tag
	public void endElement(String uri, String localName,String qName) throws SAXException 
	{
		//System.out.println("End Element :" + qName);
	}

	//call after an open tag, to print the value of the tag
	public void characters(char ch[], int start, int length) throws SAXException 
	{
		String contenu = new String(ch, start, length);
		boolean DataContain = (contenu.indexOf("<") != -1);
		boolean temp = (contenu.indexOf(">") != -1);
		boolean temp1 = (contenu.length()>1);

		if (!DataContain & !temp & temp1) 
		{

			if (b_name) 
			{
				listeDrug.add("name");
				listeDrug.add(contenu);
				b_name = false;
			}

			if (b_indication) 
			{
				listeDrug.add("indication");
				listeDrug.add(contenu);
				b_indication = false;
			}

			if (b_toxicity)
			{
				listeDrug.add("toxicity");
				listeDrug.add(contenu);
				b_toxicity = false;
			}

			if (b_synonym) 
			{
				listeDrug.add("synonym");
				listeDrug.add(contenu);
				b_synonym = false;
			}
		}
	}
}