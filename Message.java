public class Message {

	private final int messageType;

	public Message(int messageType) {
		this.messageType = messageType;
	}

	public int messageType() {
		return this.messageType;
	}

}
