package com.telecomnancy;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ClinicalSign {
	public class _name {
		public String lang;
		public String text;
		
		@JsonProperty("lang")
		public void setLang(String lang) {
			this.lang = lang;
		}
		
		@JsonProperty("text")
		public void setText(String text) {
			this.text = text;
		}
	}

	public class _disease {
		public int id;
		public int OrphaNumber;
		public _name Name;
		
		@JsonProperty("id")
		public void setId(int id) {
			this.id = id;
		}
		
		@JsonProperty("OrphaNumber")
		public void setOrphaNumber(int id) {
			this.OrphaNumber = id;
		}
		
		@JsonProperty("Name")
		public void setName(_name name) {
			this.Name = name;
		}
	}
	
	public class _clinicalSign {
		public int id;
		public _name Name;
	}

	public _disease disease;
	public _clinicalSign clinicalSign;
}
