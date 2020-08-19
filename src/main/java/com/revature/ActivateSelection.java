package com.revature;

import java.io.IOException;
import java.util.logging.FileHandler;

/**
 * ActivateSelection takes the user's choice of menu input and activates the
 * corresponding classes.
 * 
 * @param s is their selection which corresponds to the integer output from
 *          MainMenu class
 */
public class ActivateSelection {
    private int selection;
    private String dateInput;
    private String keywordInput;
    java.util.logging.Logger logger = java.util.logging.Logger.getLogger(this.getClass().getName());

    public ActivateSelection(int s) {
        selection = s;
    }

    /**
     * activates the selection from the menu and will run the menu again if the
     * input was invalid
     */
    public void activate() {
        if (selection == 1) {
            // TEMPORARY SWITCH to JournalEntryCopy from JournalEntry
            JournalEntry entryObj = new JournalEntry();
            entryObj.assembleEntry();
        }
        if (selection == 2) {
            System.out.println();
            System.out.println(
                    "Please enter the date you would like to view entries from in this format (without parentheses): (yyyy-MM-dd). ");
            System.out.println(
                    "Or, to view all entries in a time window, enter two dates in this format (without parentheses): (yyyy-MM-dd,yyyy-MM-dd).");
            System.out.println();
            try {
                dateInput = new JournalInput().queryInput();
                DateChecker checkerObj = new DateChecker(dateInput);
                if (checkerObj.runAll()) {
                    System.out.println();
                }
            } catch (IOException e) {
                System.err.println("There was an IOException in Activate Selection (selection 2).");
            }
            DateChecker checkerObj = new DateChecker(dateInput);
            checkerObj.runAll();
            if (dateInput.length() == 10) {
                LookUp searchObj = new LookUp(checkerObj.getDate1());
                searchObj.lookInTable();
            } else if (dateInput.length() == 21) {
                LookUp searchObj = new LookUp(checkerObj.getDate1(), checkerObj.getDate2());
                searchObj.lookInTable();
            }
        }
        if (selection == 3) {
            System.out.println();
            System.out.println("Please enter the keyword phrase you would like to search for:");
            System.out.println();
            try {
                keywordInput = new JournalInput().queryInput();
                Keyword keywordObj = new Keyword(keywordInput);
                keywordObj.search();
            } catch (IOException e) {
                System.err.println("There was an IOException in Activate Selection (selection 3).");
                e.printStackTrace();
            }
        }
        if (selection == 9) {
            SqlOperation queryObj = new SqlOperation();
            queryObj.dropTable("\"journal_table\"");
        }
        if (selection == -1) {
            System.out.println();
            System.out.println("Please try again.");
            System.out.println();
            MainMenu anotherMenu = new MainMenu();
            int anotherSelection = anotherMenu.querySelection();
            new ActivateSelection(anotherSelection).activate();
        }

        logger.setUseParentHandlers(false);
        try {
            FileHandler fileHandler = new FileHandler("status.log");
            logger.addHandler(fileHandler);
            logger.info(java.time.LocalTime.now() + " App Running");
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}