package projet_gmd;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/** Index all text files under a directory.
 * This is a command-line application demonstrating simple Lucene indexing.
 * Run it with no command-line arguments for usage information.
 */
public class IndexFilesDrugbank 
{

    public IndexFilesDrugbank() {}

    /** Index all text files under a directory. */
    public static void index() 
    {

       // String indexPath = "./drugbank/index";
        Path INDEX_DIR = Paths.get("./drugbank/index");
        String fileToIndex = "./drugbank/drugParse.txt";

        final File file = new File(fileToIndex);
        if (!file.exists() || !file.canRead()) 
        {
            System.exit(1);
	        }

        Date start = new Date();
        
        try {
            Directory dir = FSDirectory.open(INDEX_DIR);
            Analyzer analyzer = new StandardAnalyzer();
            IndexWriterConfig iwc = new IndexWriterConfig(analyzer);

            iwc.setOpenMode(OpenMode.CREATE);

            // Optional: for better indexing performance, if you
            // are indexing many documents, increase the RAM
            // buffer. But if you do this, increase the max heap
            // size to the JVM (eg add -Xmx512m or -Xmx1g):
            //
            // iwc.setRAMBufferSizeMB(256.0);

            IndexWriter writer = new IndexWriter(dir, iwc);
            indexDrugs(writer, file);

            // NOTE: if you want to maximize search performance,
            // you can optionally call forceMerge here. This can be
            // a terribly costly operation, so generally it's only
            // worth it when your index is relatively static (ie
            // you're done adding documents to it):
            //
            // writer.forceMerge(1);

            writer.close();

            Date end = new Date();
            System.out.println(end.getTime() - start.getTime() + " total milliseconds");

        } catch (IOException e) {
            /*System.out.println(" caught a " + e.getClass() +
                    "\n with message: " + e.getMessage());*/
        }
    }

    /**
     * Indexes the given file using the given writer, or if a directory is given,
     * recurses over files and directories found under the given directory.
     *
     * NOTE: This method indexes one document per input file. This is slow. For good
     * throughput, put multiple documents into your input file(s). An example of this is
     * in the benchmark module, which can create "line doc" files, one document per line,
     * using the
     * <a href="../../../../../contrib-benchmark/org/apache/lucene/benchmark/byTask/tasks/WriteLineDocTask.html"
     * >WriteLineDocTask</a>.
     *
     * @param writer Writer to the index where the given file/dir info will be stored
     * @param file The file to index, or the directory to recurse into to find files to index
     * @throws IOException If there is a low-level I/O error
     */
    static void indexDrugs(IndexWriter writer, File file)
            throws IOException {
        // do not try to index files that cannot be read

        ArrayList<String> liste = null;
        if (file.canRead()) {

            FileInputStream fis;

            try {
                fis = new FileInputStream(file);

                // LIRE le fichier ligne par ligne
                InputStreamReader ipsr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(ipsr);
                String line;                
                Document doc = null;
                //int count=0;
                while((line=br.readLine())!=null){

                    if(line.startsWith("Name")){
                    	//ici mettre la liste 
                        doc = new Document();
                        liste = new ArrayList<String>();
                    }

                    if(line.startsWith("Name")){
                        doc.add(new TextField("name", line.substring(6), Field.Store.YES));

                    }
                    
                    if(line.startsWith("Indication")){
                        doc.add(new TextField("indication", line.substring(12), Field.Store.YES));

                    }
                    
                    if(line.startsWith("Toxicity")){
                        doc.add(new TextField("toxicity", line.substring(10), Field.Store.YES));

                    }

                    if(line.startsWith("Synonym")){
                    	
                    	liste.add(line.substring(9));
                    	//System.out.println(liste.size());
                    	
                    }
                    
                    if(line.startsWith("EOF")){
                        if ( liste!= null && liste.size()!=0) {
                        	//System.out.println(liste.size());
                        	for(int i=0;i<liste.size();i++){
                        		doc.add(new TextField("synonym language", liste.get(i), Field.Store.YES));
                        	}
                        }
                        //System.out.println("adding "/* + id*/);
                        writer.addDocument(doc);
                    }
                    

                }
                //System.out.println("j'ai indexe "+eltCount+" medicaments");
                        

                fis.close();
                br.close();

            } catch(FileNotFoundException fnfe) {
                fnfe.printStackTrace();
            }

        }
    }
}
 