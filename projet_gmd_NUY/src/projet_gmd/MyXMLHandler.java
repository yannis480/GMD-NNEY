package projet_gmd;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xml.sax.*;
import org.xml.sax.helpers.LocatorImpl;

public class MyXMLHandler implements ContentHandler 
{
	private long debut;
	private Locator locator;
	private Drug drug;
	private int nb_drug = 0;
	private String temp_globale;
	private int temp_cpt = 0;

    /**
     * Constructeur par defaut. 
    */
    public MyXMLHandler() 
    {
    	super();
    	locator = new LocatorImpl();
    }

    /**
     * Definition du locator qui permet a tout moment pendant l'analyse, de localiser
     * le traitement dans le flux. Le locator par defaut indique, par exemple, le numero
     * de ligne et le numero de caractere sur la ligne.
    */
    public void setDocumentLocator(Locator value)
    {
    	locator =  value;
    }

    /**
     * Evenement envoye au demarrage du parse du flux xml.
    */
    public void startDocument() throws SAXException 
    {
    	System.out.println("Debut de l'analyse du document");
        debut = System.currentTimeMillis();
    }

    /**
     * Evenement envoye a la fin de l'analyse du flux XML.
     */  
    public void endDocument() throws SAXException 
    {
    	long temp = (System.currentTimeMillis()-debut);
    	System.out.println("Fin de l'analyse du document\n"+nb_drug+" drug(s) - "+temp+"ms" );
    }

    /**
     * Debut de traitement dans un espace de nommage.
     * @param prefixe utilise pour cet espace de nommage dans cette partie de l'arborescence.
     * @param URI de l'espace de nommage.
     */    
     public void startPrefixMapping(String prefix, String URI) throws SAXException 
     {
    	 System.out.println("Traitement de l'espace de nommage : " + URI + ", prefixe choisi : " + prefix);
     }

     /**
      * Fin de traitement de l'espace de nommage.
      * @param prefixe le prefixe choisi a l'ouverture du traitement de l'espace nommage.
      * @see org.xml.sax.ContentHandler#endPrefixMapping(java.lang.String)
      */
     public void endPrefixMapping(String prefix) throws SAXException 
     {
     	System.out.println("Fin de traitement de l'espace de nommage : " + prefix);
     }  

     /**
      * Evenement recu a chaque fois que l'analyseur rencontre une balise XML ouvrante.
      * @param nameSpaceURI l'URL de l'espace de nommage.
      * @param localName le nom local de la balise.
      * @param rawName nom de la balise en version 1.0 <code>nameSpaceURI + ":" + localName</code>
     */
     public void startElement(String nameSpaceURI, String localName, String rawName, Attributes attributs) throws SAXException 
     {
    	 temp_globale = localName;
    	 if( temp_globale.equals("drug") || temp_globale.equals("toxicity") || (temp_globale.equals("name") && (temp_cpt ==0 || temp_cpt == 1))|| temp_globale.equals("indication"))
    	 {
    		// System.out.println("Balise : " + temp_globale);
    		 
    		 if ( temp_globale.equals("drug"))
             { 
        		 //drug = new Drug();
        		 nb_drug++;
             }
    		 
    		 if ( temp_globale.equals("name"))
             { 
        		 temp_cpt++;
             }

             if ( ! "".equals(nameSpaceURI)) 
             { // espace de nommage particulier
            	 System.out.println("  appartenant a l'espace de nom : "  + nameSpaceURI);
             }

           /*  System.out.println("  Attributs de la balise : ");

             for (int index = 0; index < attributs.getLength(); index++) 
             { // on parcourt la liste des attributs
            	 System.out.println("     - " +  attributs.getLocalName(index) + " = " + attributs.getValue(index));
             }*/
    	 }
     }
     
     /**
      * Evenement recu a chaque fermeture de balise.
      */
     public void endElement(String nameSpaceURI, String localName, String rawName) throws SAXException 
     {
     	/*System.out.print("Fermeture de la balise : " + localName);

         if ( ! "".equals(nameSpaceURI)) 
         { // name space non null
         	System.out.print("appartenant a l'espace de nommage : " + localName);
         }

         System.out.println();*/
     }
     
     /**
      * Evenement recu a chaque fois que l'analyseur rencontre des caracteres (entre
      * deux balises).
      * @param ch les caracteres proprement dits.
      * @param start le rang du premier caractere a traiter effectivement.
      * @param end le rang du dernier caractere a traiter effectivement
      */
     public void characters(char[] ch, int start, int end) throws SAXException 
     {
    	 if( temp_globale.equals("drug") || temp_globale.equals("toxicity") || (temp_globale.equals("name") && temp_cpt == 1) || temp_globale.equals("indication"))
    	 {
    		 String temp = new String(ch, start, end);
    	 
    		 Pattern pattern = Pattern.compile("[a-zA-Z0-9-]+");//on ne veut pas afficher des valeurs vides
    		 Matcher matcher = pattern.matcher(temp);
    		 if(matcher.find()) {System.out.println(temp_globale+ " - "+temp);}
    	 }
     }
     
     /**
      * Recu chaque fois que des caracteres d'espacement peuvent etre ignores au sens de
      * XML. C’est-a-dire que cet evenement est envoye pour plusieurs espaces se succedant,
      * les tabulations, et les retours chariot se succedant ainsi que toute combinaison de ces
      * trois types d'occurrence.
      * @param ch les caracteres proprement dits.
      * @param start le rang du premier caractere a traiter effectivement.
      * @param end le rang du dernier caractere a traiter effectivement
      * @see org.xml.sax.ContentHandler#ignorableWhitespace(char[], int, int)
      */
     public void ignorableWhitespace(char[] ch, int start, int end) throws SAXException 
     {
    	 System.out.println("espaces inutiles rencontres : ..." + new String(ch, start, end) +  "...");
     }

     /**
      * Rencontre une instruction de fonctionnement.
      * @param target la cible de l'instruction de fonctionnement.
      * @param data les valeurs associees a cette cible. En general, elle se presente sous la forme 
      * d'une serie de paires nom/valeur.
      * @see org.xml.sax.ContentHandler#processingInstruction(java.lang.String, java.lang.String)
      */
     public void processingInstruction(String target, String data) throws SAXException 
     {
     	System.out.println("Instruction de fonctionnement : " + target);
         System.out.println("  dont les arguments sont : " + data);
     }

     /**
      * Recu a chaque fois qu'une balise est evitee dans le traitement a cause d'un
      * probleme non bloque par le parser. Pour ma part je ne pense pas que vous
      * en ayez besoin dans vos traitements.
      * @see org.xml.sax.ContentHandler#skippedEntity(java.lang.String)
      */
     public void skippedEntity(String arg0) throws SAXException 
     {
             // Je ne fais rien, ce qui se passe n'est pas franchement normal.
             // Pour eviter cet evenement, le mieux est quand meme de specifier une DTD pour vos
             // documents XML et de les faire valider par votre parser.              
     }
}
