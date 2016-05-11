/*
 * Created on 2 nov. 03 with Eclipse for Java
 */
package projet_gmd;

import java.io.IOException;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * Cette classe est livree telle quelle.
 * @author smeric
 * @version 1.0
 */
public class SimpleSaxParser {

        /**
         * Contructeur.
         */
        public SimpleSaxParser(String uri) throws SAXException, IOException {
                        XMLReader saxReader = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
                        saxReader.setContentHandler(new MyXMLHandler());
                        saxReader.parse(uri);
        }

        public static void main(String[] args) 
        {
                String uri = "file:///home/nicolas/workspace/projet_gmd/drugbank.xml";

                try {
                        SimpleSaxParser parser = new SimpleSaxParser(uri);
                } catch (Throwable t) {
                        t.printStackTrace();
                }
        }
}
