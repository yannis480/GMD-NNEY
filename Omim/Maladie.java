public class Maladie {

	String cuid="";
	String symptome="";
	String omimID="";
	String synonym="";
	String prefLab="";
	
	
		
	public Maladie() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Maladie(String cuid, String symptome, String omimID, String prefLab, String synonym) {
		super();
		this.cuid = cuid;
		this.symptome = symptome;
		this.omimID = omimID;
		this.synonym = synonym;
		this.prefLab = prefLab;
	}
	
	public String toString(){
		return "CUID : "+this.cuid+"\n"+
				"OMIMID : "+this.omimID+"\n"+
				"Symptome name : "+this.symptome+"\n"+
				"Synonym : "+this.synonym+"\n"+
				"Prefered Label : "+this.prefLab+"\n";
	}
	
	public String getCuid() {
		return cuid;
	}
	public void setCuid(String cuid) {
		this.cuid = cuid;
	}
	public String getSymptome() {
		return symptome;
	}
	public void setSymptome(String symptome) {
		this.symptome = symptome;
	}
	public String getOmimID() {
		return omimID;
	}
	public void setOmimID(String omimID) {
		this.omimID = omimID;
	}

	public String getSynonym() {
		return synonym;
	}

	public void setSynonym(String synonym) {
		this.synonym = synonym;
	}

	public String getPrefLab() {
		return prefLab;
	}

	public void setPrefLab(String prefLab) {
		this.prefLab = prefLab;
	}

	
	
}