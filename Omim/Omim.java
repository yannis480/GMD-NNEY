import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream.GetField;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class Omim {

	public static Path INDEX_DIR = Paths.get("./Omim_index");
	public static int cpt = 0;
	public static int cptDoc = 0;
	public static ArrayList<Maladie> al = new ArrayList<Maladie>();
	public static ArrayList<Maladie> csvl = new ArrayList<Maladie>();
	public static ArrayList<Maladie> omi = new ArrayList<Maladie>();
	Maladie mal = new Maladie();
	static Analyzer analyzer = new StandardAnalyzer();

	public static void main(String[] args) {

		try {
			final File file = new File(
					"/home/yannis/workspace/projGMD/omim.txt");
			final File csv = new File(
					"/home/yannis/workspace/projGMD/omim_onto.csv");

			IndexWriterConfig config = new IndexWriterConfig(analyzer);
			IndexWriter writer = new IndexWriter(FSDirectory.open(INDEX_DIR),
					config);
			//indexDoc(writer, file);
			//CSV(writer, csv);
			//omi=compareListe(al,csvl);
			//indexList(writer,omi);
			//writer.close();
			
			// Param to search
			String query = "609109";
			String field = "URL";
			
			QuerryB(field, query, analyzer);
			
			//afficheListe(al);
			//afficheListe(al);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	static void indexDoc(IndexWriter writer, File file) throws IOException {
		if (file.canRead() && !file.isDirectory()) {
			try {
				InputStream ips = new FileInputStream(file);
				InputStreamReader ipsr = new InputStreamReader(ips);
				BufferedReader br = new BufferedReader(ipsr);
				String line;
				String cs = Pattern.quote("*FIELD* TI");
				Document doc = null;
				Maladie maladie = null;

				while ((line = br.readLine()) != null) {
					if (line.startsWith("*RECORD*")) {
						//doc = new Document();
						maladie = new Maladie();
						al.add(maladie);

					} else if (Pattern.matches(cs, line)) {
						
						line = br.readLine();
						line = line.replaceAll(";", ",");
						String[] tabChaine = line.split(",");
						String name = tabChaine[0].substring(7);
						//doc.add(new TextField("SYMPT", name, Field.Store.YES));
						maladie.setSymptome(name);
						
						//writer.addDocument(doc);

					} else if (Pattern.matches("^[0-9]{6}+$", line)) {

						//doc.add(new TextField("ID", line, Field.Store.YES));
						maladie.setOmimID(line);
						cpt += 1;

					}

				}

			} catch (Exception e) {
				e.printStackTrace();

			}

		}
	}

	static void CSV(IndexWriter writer, File file) throws IOException {
		if (file.canRead() && !file.isDirectory()) {
			try {
				InputStream ips = new FileInputStream(file);
				InputStreamReader ipsr = new InputStreamReader(ips);
				BufferedReader br = new BufferedReader(ipsr);
				String line;
				String omimid = " ";
				String syn = " ";
				String prefLabel = " ";
				String cuid = null;
				String tabChaine[] = null;
				Maladie mal = null;
				int i = 1;

				while ((line = br.readLine()) != null) {
					mal = new Maladie();
					prefLabel = "";
					tabChaine = reconstructLine(line);
					syn = tabChaine[2];
					prefLabel = tabChaine[1];

					// Récupération omimid
					if (tabChaine[0].contains("http"))
						omimid = tabChaine[0].substring(42);

					// Indexation des champs
					//Document doc = new Document();

					//if (tabChaine.length > 5)
						//doc.add(new TextField("CUID", tabChaine[5],	Field.Store.YES));

					//writer.addDocument(doc);
					//doc.add(new TextField("URL", omimid, Field.Store.YES));
					//doc.add(new TextField("Prefered Label", prefLabel,
					//		Field.Store.YES));
					//doc.add(new TextField("Synonyms", syn, Field.Store.YES));
					csvl.add(mal);
					if (tabChaine.length > 5)
						mal.setCuid(tabChaine[5]);
					mal.setOmimID(omimid);
					mal.setPrefLab(prefLabel);
					mal.setSynonym(syn);
					//writer.addDocument(doc);

				}

			} catch (Exception e) {
				e.printStackTrace();

			}

		}

	}

	static void QuerryB(String field, String qu, Analyzer an) {
		try {
			Query q = new QueryParser(field, an).parse(qu);
			int hitsPerPage = 1;
			IndexReader reader = DirectoryReader.open(FSDirectory
					.open(INDEX_DIR));
			IndexSearcher searcher = new IndexSearcher(reader);
			TopDocs docs = searcher.search(q, hitsPerPage);
			ScoreDoc[] hits = docs.scoreDocs;
			// AFFICHER
			System.out.println("Found " + hits.length + " hits.");
			for (int i = 0; i < hits.length; ++i) {
				int docId = hits[i].doc;
				Document d = searcher.doc(docId);
				System.out.println((i + 1) + ". CUID :" + d.get("CUID")
						+ "\n Omimid : " + d.get("URL")
						+ "\n Symptome name : " + d.get("SYMPT")
						+ "\n Prefered Label : " + d.get("Prefered Label")
						+ "\n Synonyms : " + d.get("Synonyms"));
				System.out.println(d.getFields());

			}
			reader.close();
		} catch (ParseException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	static void afficheListe(ArrayList<Maladie> l) {
		int i = 0;
		for (Maladie mal : l)

		{

			System.out.println("donnée à l'indice " + i + ": \n"
					+ mal.toString());
			i += 1;

		}
	}

	static String[] reconstructLine(String line) {
		String[] tab = null;
		String label = null;
		String syn = null;

		if (line.contains("\"")) {

			Pattern p = Pattern.compile("(\"[^\"]+\")");
			Matcher m = p.matcher(line);

			if (m.find()) {
				syn = m.group(1).replaceAll(",", ";");
				if (m.find()) {
					label = m.group(1).replaceAll(",", ";");
					line = line.replaceAll("(\"[^\"]+\")", label);
				}

				line = line.replaceFirst("(\"[^\"]+\")", syn);
			}

		}

		tab = line.split(",");

		return tab;

	}

	static ArrayList<Maladie> compareListe(ArrayList<Maladie> mal, ArrayList<Maladie> mal2) {
		
		int i = 0;
		int j = 0;
		boolean flag = false;
		for (Maladie m : mal) {
			i = 0;
			for (Maladie m2 : mal2) {
				i++;
				
				if (m.getOmimID().equals(m2.getOmimID())) {
					j = i;
					flag = true;
					m.setCuid(m2.getCuid());
					m.setPrefLab(m2.getPrefLab());
					m.setSynonym(m2.getSynonym());
					
					

				}
			}
			if (flag){
				mal2.remove(j);
				flag = false;
			}
			System.out.println(m.toString());
		}
		//System.out.println(mal.toString());
		return mal;
		
	}
	
	static void indexList(IndexWriter writer,ArrayList<Maladie> mal){
		for (Maladie m: mal){
			Document doc = new Document();
			doc.add(new TextField("URL", m.getOmimID(), Field.Store.YES));
			doc.add(new TextField("Prefered Label", m.getPrefLab(),	Field.Store.YES));
			doc.add(new TextField("Synonyms", m.getSynonym(), Field.Store.YES));
			doc.add(new TextField("CUID", m.getCuid(), Field.Store.YES));
			doc.add(new TextField("SYMPT", m.getSymptome(), Field.Store.YES));
			try {
				writer.addDocument(doc);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} 
	}
}
