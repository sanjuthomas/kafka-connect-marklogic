package kafka.connect.marklogic.beans;

/**
 *
 * @author Sanju Thomas
 *
 */
public class Client {

	public Client(){}

	public Client(final String id, final Account accout){
		this.id = id;
		this.account = accout;
	}

	private String id;
	private Account account;

	public String getId() {
		return this.id;
	}
	public void setId(final String id) {
		this.id = id;
	}
	public Account getAccount() {
		return this.account;
	}
	public void setAccount(final Account account) {
		this.account = account;
	}

}
