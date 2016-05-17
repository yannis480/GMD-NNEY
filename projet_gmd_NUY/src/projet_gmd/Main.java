package com.telecomnancy;

import java.io.IOException;
import java.net.MalformedURLException;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.ViewQuery;
import org.ektorp.ViewResult;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbConnector;
import org.ektorp.impl.StdCouchDbInstance;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// GetDiseaseByName Orphanumber / Name
		// GetDiseaseClinicalSignsNoLang Orphanumber  / clinical sign name
		System.out.println("Orphan DB v1.0");

		HttpClient httpClient = null;
		try {
			httpClient = new StdHttpClient.Builder()
				.url("http://couchdb.telecomnancy.univ-lorraine.fr/")
				.build();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		System.out.println("Connected to database!");
		
		CouchDbInstance dbInstance = new StdCouchDbInstance(httpClient);
		CouchDbConnector db = new StdCouchDbConnector("orphadatabase", dbInstance);
		
		ViewQuery query = new ViewQuery()
		.designDocId("_design/clinicalsigns")
		.viewName("GetDiseaseClinicalSignsNoLang");
		System.out.println("Fetching data...");

		ObjectMapper mapper = new ObjectMapper();
		ViewResult result = db.queryView(query);
		System.out.println("Done!");
		for (ViewResult.Row row : result.getRows()) {
			try {
				ClinicalSign cs = mapper.readValue(row.getValue(), ClinicalSign.class);
				System.out.println(cs.disease.id);
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Terminated.");
	}

}
