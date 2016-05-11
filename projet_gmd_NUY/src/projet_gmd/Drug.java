package projet_gmd;

public class Drug
{
	private int id;
	private String name;
	private String toxicity;
	private String synonyms;
	private String indication;
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getSynonyms() {
		return synonyms;
	}

	public void setSynonyms(String synonyms) {
		this.synonyms = synonyms;
	}

	public String getIndication() {
		return indication;
	}

	public void setIndication(String indication) {
		this.indication = indication;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getToxicity() {
		return toxicity;
	}

	public void setToxicity(String toxicity) {
		this.toxicity = toxicity;
	}

	public String getName() {
		return name;
	}	
}
