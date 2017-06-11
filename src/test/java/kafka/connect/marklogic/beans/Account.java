package kafka.connect.marklogic.beans;

/**
 *
 * @author Sanju Thomas
 *
 */
public class Account {

	public Account(){}

	public Account(final String id){
		this.id = id;
	}

	private String id;

	public String getId() {
		return this.id;
	}

	public void setId(final String id) {
		this.id = id;
	}

}
