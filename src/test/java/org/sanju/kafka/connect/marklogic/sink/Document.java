package org.sanju.kafka.connect.marklogic.sink;

/**
 * 
 * @author Sanju Thomas
 *
 */
class Document{
		
		private String name;
		private int id;
		
		Document(String name, int id){
			this.name = name;
			this.id = id;
		}
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		
	}