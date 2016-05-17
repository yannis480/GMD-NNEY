package projet_gmd;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;





import org.xml.sax.SAXException;

public class ParserDrugbank 
{
	public static void fileToIndex() //read the xml, parse it and print it to a file
	{
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		try {
			SAXParser saxParser = saxParserFactory.newSAXParser();
			HandlerDrugbank handler = new HandlerDrugbank();
			saxParser.parse("./drugbank/drugbank2.xml", handler);

			ArrayList<String> liste = handler.getListe();
			FileWriter fw = new FileWriter ("./drugbank/drugParse.txt");
			for(int i = 0;i<liste.size();i++)
			{
				if(i<liste.size()-4 && liste.get(i)=="name" && liste.get(i+2)=="indication" && liste.get(i+4)=="toxicity") 
				{
					if(i>1){fw.write("\nEOF\n");}
					fw.write("\nName : "+liste.get(i+1)+"\nIndication : "+liste.get(i+3)+"\nToxicity : "+liste.get(i+5));
				}

				if(liste.get(i)=="synonym")
				{
					fw.write("\nSynonym : "+liste.get(i+1));
				}
			}
			fw.write("\nEOF\n");
			fw.close();

		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
	}
}
