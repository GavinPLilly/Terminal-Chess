import java.util.Scanner;

public class Reader {

	public Reader() {}

	public Message getInput() {
		String inputString = getInputString();
		if(validSyntax(inputString)) {
			System.out.println("Valid syntax");
			return new VIMessage(inputString);
		}
		return new IIMessage();
	}

	private String getInputString() {
		Scanner scanner = new Scanner(System.in);
		return scanner.nextLine();
	}

	private boolean validSyntax(String inputString) {
		inputString = inputString.toUpperCase();
		// Queenside castle
		if(inputString.equals("0-0-0") || inputString.equals("O-O-O")) {
			return true;
		}
		// Kingside castle
		if(inputString.equals("0-0") || inputString.equals("O-O")) {
			return true;
		}
		// Normal moves
		// Format checking
		if(inputString.length() != 5) {
			return false;
		}
		if(inputString.charAt(2) != ' ') {
			return false;
		}
		// Bounds checking
		if(inputString.charAt(0) < 'A' || inputString.charAt(0) > 'Z') {
			return false;
		}
		if(inputString.charAt(3) < 'A' || inputString.charAt(3) > 'Z') {
			return false;
		}
		if(inputString.charAt(1) < '1' || inputString.charAt(1) > '8') {
			return false;
		}
		if(inputString.charAt(4) < '1' || inputString.charAt(4) > '8') {
			return false;
		}
		return true;
	}

}
