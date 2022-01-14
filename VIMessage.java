// Valid Input Message = VIMessage
public class VIMessage extends Message {

	String input;

	public VIMessage(String input) {
		super(16);
		this.input = input;
	}

	public String input() {
		return input;
	}

}
