public class Customer {
// Creating the Customer Object
	// Attributes
	String cusFirstName;
	String cusLastName;
	String cusContactNo;
	String cusAddress;
	String cusLocation;
	String cusEmail;

	// Methods
	public Customer() {
		this.cusFirstName = getFirstName();
		this.cusLastName = getLastName();
		this.cusContactNo = getContactNo();
		this.cusAddress = getAddress();
		this.cusLocation = getLocation();
		this.cusEmail = getEmail();
	}

	// Receives user's first name
	public static String getFirstName() {

		System.out.println("Please enter your first name: ");
		String userName = Main.input.nextLine();
		// Uppercase for consistency
		Main.upperAllWords(userName);
		return userName;
	}

	// Receives user's last name
	public static String getLastName() {

		System.out.println("Please enter your last name: ");
		String userName = Main.input.nextLine();
		Main.upperAllWords(userName);
		return userName;
	}

	// Receives user's contact number
	public static String getContactNo() {
		String returnContactNo;
		// Matches numbers only, for validating contact number
		String regexStr = "^[0-9]*$";

		// Repeats until a valid number is given
		while (true) {

			System.out.println("Please enter your contact number: ");
			returnContactNo = Main.input.nextLine();

			// if only numbers, returns contact number
			if (returnContactNo.matches(regexStr)) {
				return returnContactNo;
			} else { // prompts user to try again
				System.out.println("Please enter a valid phone number!");
			}
		}
	}

	// Receives user's street address (Not area)
	public static String getAddress() {

		System.out.println("Please enter your address: ");
		String userAddress = Main.input.nextLine();

		Main.upperAllWords(userAddress);
		return userAddress;
	}

	// Receives user's Email address
	public static String getEmail() {

		System.out.println("Please enter your email address: ");
		String userEmail = Main.input.nextLine();
		return userEmail;
	}

	// Receives user's location by having them select a corresponding number
	// Must match the requested restaurant location to continue the order
	public static String getLocation() {
		String userRestLoc = "";
		int userSelectionNo;

		while (true) {
			try {
				System.out.println("Please enter your location: ");
				System.out.println("Select the corresponding number: \r\n");
				System.out.println(
						"1:Cape Town\r\n2:Durban\r\n3:Johannesburg\r\n4:Potchefstroom\r\n5:Springbok\r\n6:Bloemfontein\r\n7:Port Elizabeth\r\n8:Witbank");

				userSelectionNo = Main.enterNumber();

				if (userSelectionNo == 1) {
					userRestLoc = "Cape Town";
				} else if (userSelectionNo == 2) {
					userRestLoc = "Durban";
				} else if (userSelectionNo == 3) {
					userRestLoc = "Johannesburg";
				} else if (userSelectionNo == 4) {
					userRestLoc = "Potchefstroom";
				} else if (userSelectionNo == 5) {
					userRestLoc = "Springbok";
				} else if (userSelectionNo == 6) {
					userRestLoc = "Bloemfontein";
				} else if (userSelectionNo == 7) {
					userRestLoc = "Port Elizabeth";
				} else if (userSelectionNo == 8) {
					userRestLoc = "Witbank";
				} else if (userSelectionNo > 8 || userSelectionNo < 1) {
					throw new Exception("Out of range! Please enter a valid number: ");
				}
				return userRestLoc;

			} catch (Exception e) {
				System.out.println("Please enter a valid number!");
			}
		}

	}

}
