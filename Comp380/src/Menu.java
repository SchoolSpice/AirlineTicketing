import java.util.Scanner;

public class Menu {
	/* Instance variables for Menu class */
	final String TITLE;
	final String[] OPTIONS;
	final String OPTION_ZERO;
	
	/* Constructor */
	public Menu(final String TITLE,
				final String[] OPTIONS,
				final String OPTION_ZERO) {
		this.TITLE = TITLE;
		this.OPTIONS = OPTIONS;
		this.OPTION_ZERO = OPTION_ZERO;
	}
	
	/* Calls printMenu() */
	/* Prompts the user to make a selection*/
	/* Verifies & sanitizes user input */
	/* Returns selected option as int-type */
	/* Tries again if user enters invalid input */
	/* *** Always returns 'good' values *** */
	public int makeSelection() {
		/* Instance variables for makeSelection() */
		Scanner input = new Scanner(System.in);
		String rawInput = "";
		boolean validInput = false;
		final int MENU_SIZE = OPTIONS.length;
		int selection = 0;
		
		/* Loops until user makes a valid selection */
		while(!validInput) {
			printMenu();
			System.out.print("Enter selection: ");
		    rawInput = input.nextLine();
			System.out.flush();
			/* try-catch for non int-type input */
			try {
				selection = Integer.parseInt(rawInput);
			} catch(Exception e) {
				invalid(MENU_SIZE);
				continue;	
			}
			/* After receiving a valid entry, */
			/* change validInput to fall out of loop */
			if(selection >= 0 && selection <= MENU_SIZE) {
				validInput = true;  
			} else {
				invalid(MENU_SIZE);
			}
		}
		/* Return the user-selected menu option */
		return selection;
	}
	
	/* Prints the menu and */
	/* automatically numbers each option */
	private void printMenu() {
		/* Instance variables for printMenu() */
		int optionNumber = 1;
		int menuWidth = TITLE.length();
		/* Prints menu with formatting */
		System.out.println(TITLE);
		printDashedLine(menuWidth);
		for (String prompt : OPTIONS) {
			System.out.println(optionNumber +
							  ") " +
							   prompt);
		    optionNumber++;
		}
		System.out.println("0) " + OPTION_ZERO);
		printDashedLine(menuWidth);
	}
	
	/* Prints a dashed line the same width as "TITLE" */
	private void printDashedLine(int width) {
			for (int i = width; i > 0; i--) {
				System.out.print("-");
			}
			System.out.println();			
	}
	
	/* Prints a console message after invalid entry */
	private void invalid(int MENU_SIZE) {
		System.out.println("\nINVALID INPUT: " +
				           "Please enter a number between 0 and " +
						    MENU_SIZE +
				           "\nTry again...\n");
	}
}