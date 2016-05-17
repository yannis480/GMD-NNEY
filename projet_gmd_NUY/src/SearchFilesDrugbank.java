package projet_gmd;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

//import GUI.TextAreaOutputStream;

public class SearchFilesDrugbank 
{
	public SearchFilesDrugbank() {}

	public static void run(String str) throws Exception 
	{
		//System.setOut( TextAreaOutputStream.getOut() );
		
		Path INDEX_DIR = Paths.get("./drugbank/index");
		String queries = null;
		int repeat = 0;
		boolean raw = false;
		int hitsPerPage = 100;
		
		IndexReader reader = DirectoryReader.open(FSDirectory.open(INDEX_DIR));
		IndexSearcher searcher = new IndexSearcher(reader);
		Analyzer analyzer = new StandardAnalyzer();
		
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
		
		MultiFieldQueryParser queryParser = new MultiFieldQueryParser(new String[] {"name","toxicity","indication","synonyms"}, analyzer);
	
		while (true) 
		{
			if (queries == null && str == null) 
			{// prompt the user
				System.out.println("Enter a disease: ");
			}
			
			String line = str != null ? str : in.readLine();
			
			if (line == null || line.length() == -1){break;}
			
			line = line.trim();
			
			if (line.length() == 0){break;} 
			
			Query query = queryParser.parse(QueryParser.escape(line));

			if (repeat > 0) 
			{// repeat & time as benchmark
				Date start = new Date();
				for (int i = 0; i < repeat; i++) 
				{
					searcher.search(query, null, 100);
				}
				Date end = new Date();
				System.out.println("Time: "+(end.getTime()-start.getTime())+"ms");
			}
			
			doPagingSearch(in, searcher, query, hitsPerPage, raw, queries == null && str == null);
				
			if (str != null) {break;}
		}
		reader.close();
	}
	
	public static void runByIndication(String str) throws Exception 
	{
		Path INDEX_DIR = Paths.get("./drugbank/index");
		String queries = null;
		int repeat = 0;
		boolean raw = false;
		int hitsPerPage = 100;
		
		IndexReader reader = DirectoryReader.open(FSDirectory.open(INDEX_DIR));
		IndexSearcher searcher = new IndexSearcher(reader);
		Analyzer analyzer = new StandardAnalyzer();
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));

		MultiFieldQueryParser queryParser = new MultiFieldQueryParser(new String[] {"indication"},
				analyzer);
	
		while (true) 
		{
			if (queries == null && str == null) 
			{                        // prompt the user
				//System.out.println("Enter a disease: ");
				return;
			}
			
			String line = str != null ? str : in.readLine();
			
			if (line == null || line.length() == -1){break;	}
			line = line.trim();
			
			if (line.length() == 0){break;}   
			
			Query query = queryParser.parse(QueryParser.escape(line));

			//System.out.println("Searching for: " + str+"\n"); //on récup l'entrée tapée, ex: skin
			if (repeat > 0) 
			{                           // repeat & time as benchmark
				Date start = new Date();
				for (int i = 0; i < repeat; i++) 
				{
					searcher.search(query, null, 100);
				}
				Date end = new Date();
				System.out.println("Time: "+(end.getTime()-start.getTime())+"ms");
			}
			doPagingSearchDrugnameDisplay(in, searcher, query, hitsPerPage, raw, queries == null && str == null);

			if (str != null){break;}
		}
		reader.close();
	}

	public static void runByToxicity(String str) throws Exception 
	{
		Path INDEX_DIR = Paths.get("./drugbank/index");
		String queries = null;
		int repeat = 0;
		boolean raw = false;
		int hitsPerPage = 100;
		IndexReader reader = DirectoryReader.open(FSDirectory.open(INDEX_DIR));
		IndexSearcher searcher = new IndexSearcher(reader);
		Analyzer analyzer = new StandardAnalyzer();
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));

		MultiFieldQueryParser queryParser = new MultiFieldQueryParser(new String[] {"toxicity"},
				analyzer);

		while (true) {
			if (queries == null && str == null) {                        // prompt the user
				System.out.println("Enter a disease: ");
			}
			String line = str != null ? str : in.readLine();
			if (line == null || line.length() == -1) {
				break;
			}
			line = line.trim();
			if (line.length() == 0) {
				break;
			}    			
			//Query query = queryParser.parse(line);
			Query query = queryParser.parse(QueryParser.escape(line));

			//System.out.println("Searching for: " + str+"\n"); //on récup l'entrée tapée, ex: skin
			if (repeat > 0) {                           // repeat & time as benchmark
				Date start = new Date();
				for (int i = 0; i < repeat; i++) {
					searcher.search(query, null, 100);
				}
				Date end = new Date();
				System.out.println("Time: "+(end.getTime()-start.getTime())+"ms");
			}
			doPagingSearchDrugnameDisplay(in, searcher, query, hitsPerPage, raw, queries == null && str == null);
			//doPagingSearchIndication(in, searcher, query, hitsPerPage, raw, queries == null && str == null);

			if (str != null) {
				break;
			}
		}
		reader.close();
	}

	public static void runBySynonym(String str) throws Exception {
		Path INDEX_DIR = Paths.get("./drugbank/index");
		String queries = null;
		int repeat = 0;
		boolean raw = false;
		int hitsPerPage = 100;
		IndexReader reader = DirectoryReader.open(FSDirectory.open(INDEX_DIR));
		IndexSearcher searcher = new IndexSearcher(reader);
		Analyzer analyzer = new StandardAnalyzer();
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
		
		//QueryParser parser = new QueryParser(Version.LUCENE_40, field, analyzer);
		MultiFieldQueryParser queryParser = new MultiFieldQueryParser(new String[] {"synonym language"},
				analyzer);
		//Hits hits = searcher.Search(queryParser.parse("<text>"));
		while (true) {
			if (queries == null && str == null) {                        // prompt the user
				System.out.println("Enter a disease: ");
			}
			String line = str != null ? str : in.readLine();
			if (line == null || line.length() == -1) {
				break;
			}
			line = line.trim();
			if (line.length() == 0) {
				break;
			}    			
			//Query query = queryParser.parse(line);
			Query query = queryParser.parse(QueryParser.escape(line));

			//System.out.println("Searching for: " + str+"\n"); //on récup l'entrée tapée, ex: skin
			if (repeat > 0) {                           // repeat & time as benchmark
				Date start = new Date();
				for (int i = 0; i < repeat; i++) {
					searcher.search(query, null, 100);
				}
				Date end = new Date();
				System.out.println("Time: "+(end.getTime()-start.getTime())+"ms");
			}
			doPagingSearch(in, searcher, query, hitsPerPage, raw, queries == null && str == null);
//			doPagingSearchIndication(in, searcher, query, hitsPerPage, raw, queries == null && str == null);
//			doPagingSearchToxicity(in, searcher, query, hitsPerPage, raw, queries == null && str == null);
//			doPagingSearchDrugnameDisplay(in, searcher, query, hitsPerPage, raw, queries == null && str == null);

			if (str != null) {
				break;
			}
		}
		reader.close();
	}

	public static void runByDrugNameToxicity(String str) throws Exception {
		Path INDEX_DIR = Paths.get("./drugbank/index");
		String queries = null;
		int repeat = 0;
		boolean raw = false;
		int hitsPerPage = 100;
		IndexReader reader = DirectoryReader.open(FSDirectory.open(INDEX_DIR));
		IndexSearcher searcher = new IndexSearcher(reader);
		Analyzer analyzer = new StandardAnalyzer();
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
		
		//QueryParser parser = new QueryParser(Version.LUCENE_40, field, analyzer);
		MultiFieldQueryParser queryParser = new MultiFieldQueryParser(new String[] {"name", "synonym language"},
				analyzer);
		//Hits hits = searcher.Search(queryParser.parse("<text>"));
		while (true) {
			if (queries == null && str == null) {                        // prompt the user
				System.out.println("Enter a disease: ");
			}
			String line = str != null ? str : in.readLine();
			if (line == null || line.length() == -1) {
				break;
			}
			line = line.trim();
			if (line.length() == 0) {
				break;
			}    			
			//Query query = queryParser.parse(line);
			Query query = queryParser.parse(QueryParser.escape(line));
			//System.out.println("Searching for: " + str+"\n"); //on récup l'entrée tapée, ex: skin
			if (repeat > 0) {                           // repeat & time as benchmark
				Date start = new Date();
				for (int i = 0; i < repeat; i++) {
					searcher.search(query, null, 100);
				}
				Date end = new Date();
				System.out.println("Time: "+(end.getTime()-start.getTime())+"ms");
			}
			
			//doPagingSearch(in, searcher, query, hitsPerPage, raw, queries == null && str == null);
//			doPagingSearchIndication(in, searcher, query, hitsPerPage, raw, queries == null && str == null);
			doPagingSearchToxicity(in, searcher, query, hitsPerPage, raw, queries == null && str == null);
//			doPagingSearchSynonym(in, searcher, query, hitsPerPage, raw, queries == null && str == null);	
//			
			if (str != null) {
				break;
			}
		}
		reader.close();
	}
	
	public static void runByDrugNameIndication(String str) throws Exception {
		Path INDEX_DIR = Paths.get("./drugbank/index");
		String queries = null;
		int repeat = 0;
		boolean raw = false;
		int hitsPerPage = 100;
		IndexReader reader = DirectoryReader.open(FSDirectory.open(INDEX_DIR));
		IndexSearcher searcher = new IndexSearcher(reader);
		Analyzer analyzer = new StandardAnalyzer();
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
		
		//QueryParser parser = new QueryParser(Version.LUCENE_40, field, analyzer);
		MultiFieldQueryParser queryParser = new MultiFieldQueryParser(new String[] {"name","synonym language"},
				analyzer);
		//Hits hits = searcher.Search(queryParser.parse("<text>"));
		while (true) {
			if (queries == null && str == null) {                        // prompt the user
				System.out.println("Enter a disease: ");
			}
			String line = str != null ? str : in.readLine();
			if (line == null || line.length() == -1) {
				break;
			}
			line = line.trim();
			if (line.length() == 0) {
				break;
			}    			
			//Query query = queryParser.parse(line);
			Query query = queryParser.parse(QueryParser.escape(line));
			//System.out.println("Searching for: " + str+"\n"); //on récup l'entrée tapée, ex: skin
			if (repeat > 0) {                           // repeat & time as benchmark
				Date start = new Date();
				for (int i = 0; i < repeat; i++) {
					searcher.search(query, null, 100);
				}
				Date end = new Date();
				System.out.println("Time: "+(end.getTime()-start.getTime())+"ms");
			}
			
//			doPagingSearch(in, searcher, query, hitsPerPage, raw, queries == null && str == null);
			doPagingSearchIndication(in, searcher, query, hitsPerPage, raw, queries == null && str == null);
//			doPagingSearchToxicity(in, searcher, query, hitsPerPage, raw, queries == null && str == null);
//			doPagingSearchSynonym(in, searcher, query, hitsPerPage, raw, queries == null && str == null);	
//			
			if (str != null) {
				break;
			}
		}
		reader.close();
	}

	public static void doPagingSearch(BufferedReader in, IndexSearcher searcher, Query query, int hitsPerPage, boolean raw, boolean interactive) throws IOException 
	{
		//System.setOut( TextAreaOutputStream.getOut() );
		// Collect enough docs to show 5 pages
		TopDocs results = searcher.search(query, 5 * hitsPerPage);
		ScoreDoc[] hits = results.scoreDocs;
		int numTotalHits = results.totalHits;
		//System.out.println("\n"+numTotalHits + " total matching documents\n");
		int start = 0;
		int end = Math.min(numTotalHits, hitsPerPage);
		while (true) {
			if (end > hits.length) {
				System.out.println("Only results 1 - " + hits.length +" of " + numTotalHits + " total matching documents collected.");
				System.out.println("Collect more (y/n) ?");
				String line = in.readLine();
				if (line.length() == 0 || line.charAt(0) == 'n') {
					break;
				}
				hits = searcher.search(query, numTotalHits).scoreDocs;
			}
			end = Math.min(hits.length, start + hitsPerPage);
			for (int i = start; i < end; i++) {
				if (raw) {                              // output raw format
					System.out.println("doc="+hits[i].doc+" score="+hits[i].score);
					continue;
				}
				Document doc = searcher.doc(hits[i].doc);
				String name = doc.get("name");
				String indication = doc.get("indication");
				String toxicity = doc.get("toxicity");
				ArrayList<String> syno = null;
				syno = new ArrayList();
				int taille = doc.getValues("synonym language").length;
				for(int j=0;j<taille;j++){
					syno.add(doc.getValues("synonym language")[j]);
				}
				if(indication!=null || syno!=null || toxicity!=null || name!=null) {
					System.out.println("\t"+(i+1) + ". " + name);
					System.out.println("\t"+indication);
					System.out.println("\t"+toxicity);
					//System.out.println("\nSynonyms:");
//					for(int j=0;j<taille;j++){
//						System.out.println((j+1)+"\t"+syno.get(j));
//					}
				}
			}
			if (!interactive || end == 0) {
				break;
			}
			if (numTotalHits >= end) {
				boolean quit = false;
				while (true) {
					System.out.print("Press ");
					if (start - hitsPerPage >= 0) {
						System.out.print("(p)revious page, ");  
					}
					if (start + hitsPerPage < numTotalHits) {
						System.out.print("(n)ext page, ");
					}
					System.out.println("(q)uit or enter number to jump to a page.");

					String line = in.readLine();
					if (line.length() == 0 || line.charAt(0)=='q') {
						quit = true;
						break;
					}
					if (line.charAt(0) == 'p') {
						start = Math.max(0, start - hitsPerPage);
						break;
					} else if (line.charAt(0) == 'n') {
						if (start + hitsPerPage < numTotalHits) {
							start+=hitsPerPage;
						}
						break;
					} else {
						int page = Integer.parseInt(line);
						if ((page - 1) * hitsPerPage < numTotalHits) {
							start = (page - 1) * hitsPerPage;
							break;
						} else {
							System.out.println("No such page");
						}
					}
				}
				if (quit) break;
				end = Math.min(numTotalHits, start + hitsPerPage);
			}
		}
	}
	
	public static void doPagingSearchDrugnameDisplay(BufferedReader in, IndexSearcher searcher, Query query, 
			int hitsPerPage, boolean raw, boolean interactive) throws IOException {
		
		//System.setOut( TextAreaOutputStream.getOut() );
		//System.setErr( TextAreaOutputStream.getOut() );
		
		// Collect enough docs to show 5 pages
		TopDocs results = searcher.search(query, 5 * hitsPerPage);
		ScoreDoc[] hits = results.scoreDocs;

		int numTotalHits = results.totalHits;
		//System.out.println("\n"+numTotalHits + " total matching documents\n");

		int start = 0;
		int end = Math.min(numTotalHits, hitsPerPage);

		while (true) {
			if (end > hits.length) {
				System.out.println("Only results 1 - " + hits.length +" of " + numTotalHits + " total matching documents collected.");
				System.out.println("Collect more (y/n) ?");
				String line = in.readLine();
				if (line.length() == 0 || line.charAt(0) == 'n') {
					break;
				}

				hits = searcher.search(query, numTotalHits).scoreDocs;
			}

			end = Math.min(hits.length, start + hitsPerPage);
			//FileWriter fw = new FileWriter ("/home/chouder/workspace/GMD/index/omim.csv");
			
			for (int i = start; i < end; i++) {
				if (raw) {                              // output raw format
					System.out.println("doc="+hits[i].doc+" score="+hits[i].score);
					continue;
				}

				Document doc = searcher.doc(hits[i].doc);
				String name = doc.get("name");
				//String indication = doc.get("indication");
				//String toxicity = doc.get("toxicity");
				//ArrayList<String> syno = null;
				//syno = new ArrayList();
				//int taille = doc.getValues("synonym language").length;
				
				/*for(int j=0;j<taille;j++){
					syno.add(doc.getValues("synonym language")[j]);
				}*/
		
				if(name!=null) {
					System.out.println("\t " +  name);
					//System.out.println("Indication:\n\t"+indication);
					//System.out.println("\nToxicity:\n\t"+toxicity);
					//System.out.println("\nSynonyms:");
					/*for(int j=0;j<taille;j++){
						System.out.println((j+1)+"\t"+syno.get(j));
					}*/
					//System.out.println(synonym);
				}
			}

			//fw.close();
			if (!interactive || end == 0) {
				break;
			}

			if (numTotalHits >= end) {
				boolean quit = false;
				while (true) {
					System.out.print("Press ");
					if (start - hitsPerPage >= 0) {
						System.out.print("(p)revious page, ");  
					}
					if (start + hitsPerPage < numTotalHits) {
						System.out.print("(n)ext page, ");
					}
					System.out.println("(q)uit or enter number to jump to a page.");

					String line = in.readLine();
					if (line.length() == 0 || line.charAt(0)=='q') {
						quit = true;
						break;
					}
					if (line.charAt(0) == 'p') {
						start = Math.max(0, start - hitsPerPage);
						break;
					} else if (line.charAt(0) == 'n') {
						if (start + hitsPerPage < numTotalHits) {
							start+=hitsPerPage;
						}
						break;
					} else {
						int page = Integer.parseInt(line);
						if ((page - 1) * hitsPerPage < numTotalHits) {
							start = (page - 1) * hitsPerPage;
							break;
						} else {
							System.out.println("No such page");
						}
					}
				}
				if (quit) break;
				end = Math.min(numTotalHits, start + hitsPerPage);
			}
		}
	}
		
	public static void doPagingSearchToxicity(BufferedReader in, IndexSearcher searcher, Query query, 
			int hitsPerPage, boolean raw, boolean interactive) throws IOException {
		
		//System.setOut( TextAreaOutputStream.getOut() );
		//System.setErr( TextAreaOutputStream.getOut() );
		
		// Collect enough docs to show 5 pages
		TopDocs results = searcher.search(query, 5 * hitsPerPage);
		ScoreDoc[] hits = results.scoreDocs;

		int numTotalHits = results.totalHits;
		//System.out.println("\n"+numTotalHits + " total matching documents\n");

		int start = 0;
		int end = Math.min(numTotalHits, hitsPerPage);

		while (true) {
			if (end > hits.length) {
				System.out.println("Only results 1 - " + hits.length +" of " + numTotalHits + " total matching documents collected.");
				System.out.println("Collect more (y/n) ?");
				String line = in.readLine();
				if (line.length() == 0 || line.charAt(0) == 'n') {
					break;
				}

				hits = searcher.search(query, numTotalHits).scoreDocs;
			}

			end = Math.min(hits.length, start + hitsPerPage);
			//FileWriter fw = new FileWriter ("/home/chouder/workspace/GMD/index/omim.csv");
			
			for (int i = start; i < end; i++) {
				if (raw) {                              // output raw format
					System.out.println("doc="+hits[i].doc+" score="+hits[i].score);
					continue;
				}

				Document doc = searcher.doc(hits[i].doc);
				String toxicity = doc.get("toxicity");

				if(toxicity!=null) {
					System.out.println("\t"+toxicity);
				}
			}

			//fw.close();
			if (!interactive || end == 0) {
				break;
			}

			if (numTotalHits >= end) {
				boolean quit = false;
				while (true) {
					System.out.print("Press ");
					if (start - hitsPerPage >= 0) {
						System.out.print("(p)revious page, ");  
					}
					if (start + hitsPerPage < numTotalHits) {
						System.out.print("(n)ext page, ");
					}
					System.out.println("(q)uit or enter number to jump to a page.");

					String line = in.readLine();
					if (line.length() == 0 || line.charAt(0)=='q') {
						quit = true;
						break;
					}
					if (line.charAt(0) == 'p') {
						start = Math.max(0, start - hitsPerPage);
						break;
					} else if (line.charAt(0) == 'n') {
						if (start + hitsPerPage < numTotalHits) {
							start+=hitsPerPage;
						}
						break;
					} else {
						int page = Integer.parseInt(line);
						if ((page - 1) * hitsPerPage < numTotalHits) {
							start = (page - 1) * hitsPerPage;
							break;
						} else {
							System.out.println("No such page");
						}
					}
				}
				if (quit) break;
				end = Math.min(numTotalHits, start + hitsPerPage);
			}
		}
	}
		
	public static void doPagingSearchSynonym(BufferedReader in, IndexSearcher searcher, Query query, 
			int hitsPerPage, boolean raw, boolean interactive) throws IOException {
		
		//System.setOut( TextAreaOutputStream.getOut() );
		//System.setErr( TextAreaOutputStream.getOut() );
		
		// Collect enough docs to show 5 pages
		TopDocs results = searcher.search(query, 5 * hitsPerPage);
		ScoreDoc[] hits = results.scoreDocs;

		int numTotalHits = results.totalHits;
		//System.out.println("\n"+numTotalHits + " total matching documents\n");

		int start = 0;
		int end = Math.min(numTotalHits, hitsPerPage);

		while (true) {
			if (end > hits.length) {
				System.out.println("Only results 1 - " + hits.length +" of " + numTotalHits + " total matching documents collected.");
				System.out.println("Collect more (y/n) ?");
				String line = in.readLine();
				if (line.length() == 0 || line.charAt(0) == 'n') {
					break;
				}

				hits = searcher.search(query, numTotalHits).scoreDocs;
			}

			end = Math.min(hits.length, start + hitsPerPage);
			//FileWriter fw = new FileWriter ("/home/chouder/workspace/GMD/index/omim.csv");
			
			for (int i = start; i < end; i++) {
				if (raw) {                              // output raw format
					System.out.println("doc="+hits[i].doc+" score="+hits[i].score);
					continue;
				}

				Document doc = searcher.doc(hits[i].doc);
				ArrayList<String> syno = null;
				syno = new ArrayList();
				int taille = doc.getValues("synonym language").length;
				
				for(int j=0;j<taille;j++){
					syno.add(doc.getValues("synonym language")[j]);
				}

				if(syno!=null) {
					System.out.println("\nSynonyms:");
					for(int j=0;j<taille;j++){
						System.out.println("\t"+(j+1)+"."+syno.get(j));
					}
					//System.out.println(synonym);
				}
			}

			//fw.close();
			if (!interactive || end == 0) {
				break;
			}

			if (numTotalHits >= end) {
				boolean quit = false;
				while (true) {
					System.out.print("Press ");
					if (start - hitsPerPage >= 0) {
						System.out.print("(p)revious page, ");  
					}
					if (start + hitsPerPage < numTotalHits) {
						System.out.print("(n)ext page, ");
					}
					System.out.println("(q)uit or enter number to jump to a page.");

					String line = in.readLine();
					if (line.length() == 0 || line.charAt(0)=='q') {
						quit = true;
						break;
					}
					if (line.charAt(0) == 'p') {
						start = Math.max(0, start - hitsPerPage);
						break;
					} else if (line.charAt(0) == 'n') {
						if (start + hitsPerPage < numTotalHits) {
							start+=hitsPerPage;
						}
						break;
					} else {
						int page = Integer.parseInt(line);
						if ((page - 1) * hitsPerPage < numTotalHits) {
							start = (page - 1) * hitsPerPage;
							break;
						} else {
							System.out.println("No such page");
						}
					}
				}
				if (quit) break;
				end = Math.min(numTotalHits, start + hitsPerPage);
			}
		}
	}
	
	public static void doPagingSearchIndication(BufferedReader in, IndexSearcher searcher, Query query, 
			int hitsPerPage, boolean raw, boolean interactive) throws IOException {
		
		//System.setOut( TextAreaOutputStream.getOut() );
		//System.setErr( TextAreaOutputStream.getOut() );
		
		// Collect enough docs to show 5 pages
		TopDocs results = searcher.search(query, 5 * hitsPerPage);
		ScoreDoc[] hits = results.scoreDocs;

		int numTotalHits = results.totalHits;
		//System.out.println("\n"+numTotalHits + " total matching documents\n");

		int start = 0;
		int end = Math.min(numTotalHits, hitsPerPage);

		while (true) {
			if (end > hits.length) {
				System.out.println("Only results 1 - " + hits.length +" of " + numTotalHits + " total matching documents collected.");
				System.out.println("Collect more (y/n) ?");
				String line = in.readLine();
				if (line.length() == 0 || line.charAt(0) == 'n') {
					break;
				}

				hits = searcher.search(query, numTotalHits).scoreDocs;
			}

			end = Math.min(hits.length, start + hitsPerPage);
			//FileWriter fw = new FileWriter ("/home/chouder/workspace/GMD/index/omim.csv");
			
			for (int i = start; i < end; i++) {
				if (raw) {                              // output raw format
					System.out.println("doc="+hits[i].doc+" score="+hits[i].score);
					continue;
				}

				Document doc = searcher.doc(hits[i].doc);
				//String name = doc.get("name");
				String indication = doc.get("indication");
				//String toxicity = doc.get("toxicity");
				//ArrayList<String> syno = null;
				//syno = new ArrayList();
				//int taille = doc.getValues("synonym language").length;
				
				/*for(int j=0;j<taille;j++){
					syno.add(doc.getValues("synonym language")[j]);
				}*/

				if(indication!=null) {
					//System.out.println((i+1) + ". " + name);
					System.out.println("\t"+indication);
					//System.out.println("\nToxicity:\n\t"+toxicity);
					//System.out.println("\nSynonyms:");
					/*for(int j=0;j<taille;j++){
						System.out.println((j+1)+"\t"+syno.get(j));
					}*/
					//System.out.println(synonym);
				}
			}

			//fw.close();
			if (!interactive || end == 0) {
				break;
			}

			if (numTotalHits >= end) {
				boolean quit = false;
				while (true) {
					System.out.print("Press ");
					if (start - hitsPerPage >= 0) {
						System.out.print("(p)revious page, ");  
					}
					if (start + hitsPerPage < numTotalHits) {
						System.out.print("(n)ext page, ");
					}
					System.out.println("(q)uit or enter number to jump to a page.");

					String line = in.readLine();
					if (line.length() == 0 || line.charAt(0)=='q') {
						quit = true;
						break;
					}
					if (line.charAt(0) == 'p') {
						start = Math.max(0, start - hitsPerPage);
						break;
					} else if (line.charAt(0) == 'n') {
						if (start + hitsPerPage < numTotalHits) {
							start+=hitsPerPage;
						}
						break;
					} else {
						int page = Integer.parseInt(line);
						if ((page - 1) * hitsPerPage < numTotalHits) {
							start = (page - 1) * hitsPerPage;
							break;
						} else {
							System.out.println("No such page");
						}
					}
				}
				if (quit) break;
				end = Math.min(numTotalHits, start + hitsPerPage);
			}
		}
	}
}