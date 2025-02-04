import org.hyperskill.hstest.v6.testcase.TestCase;
import org.hyperskill.hstest.v6.stage.BaseStageTest;
import org.hyperskill.hstest.v6.testcase.CheckResult;
import search.Main;

import java.util.*;

class TestClue {
    String input;

    TestClue(String input) {
        this.input = input;
    }
}

public class SimpleSearchEngineTest extends BaseStageTest<TestClue> {
    public SimpleSearchEngineTest() {
        super(Main.class);
    }

    public static final String test1 =
        "2\n" +
        "0";

    public static final String test2 =
        "1\n" +
        "qwerty\n" +
        "0";

    public static final String test3 =
        "1\n" +
        "Leopold\n" +
        "0";

    public static final String test4 =
        "3\n" +
        "1\n" +
        "Bob\n" +
        "2\n" +
        "2\n" +
        "1\n" +
        "Leopold\n" +
        "0";

    public static final String test5 =
        "1\n" +
        "@\n" +
        "1\n" +
        "Leopold\n" +
        "0";

    public static final String test6 =
        "0";

    public static final String test7 =
        "1\n" +
        "this text never find some match\n" +
        "0";

    public static final String test8 =
        "1\n" +
        "h\n" +
        "1\n" +
        "gallien@evilcorp.com\n" +
        "0";

    public static final String test9 =
        "4\n" +
        "2\n" +
        "2\n" +
        "1\n" +
        "this text never gonna be matched\n" +
        "1\n" +
        "h\n" +
        "1\n" +
        "gallien@evilcorp.com\n" +
        "0";

    @Override
    public List<TestCase<TestClue>> generate() {

        List<TestCase<TestClue>> tests = new ArrayList<>();

        for (String input : new String[]{
            test1, test2, test3, test4, test5, test6, test7, test8, test9}) {

            tests.add(new TestCase<TestClue>()
                .setAttach(new TestClue(input))
                .setInput(input)
                .addArguments("--data", "names.txt")
                .addFile("names.txt", SearchEngineTests.NAMES));
        }

        return tests;
    }

    @Override
    public CheckResult check(String reply, TestClue clue) {
        String cR = "\n";
        List<String> outputLines = new LinkedList<String>(Arrays.asList(reply.split(cR)));
        String[] inputLines = clue.input.split(cR);
        String[] reference;
        String[] currentSearchResult;

        reference = SearchEngineTests.NAMES.split("\n");

        //clear the list of unnecessary lines, if any
        List<String> cleanedOutput = new ArrayList<String>();
        for (int i = 0; i < outputLines.size(); i++) {
            if (containsItemFromList(outputLines.get(i), reference)) {
                cleanedOutput.add(outputLines.get(i).toLowerCase());
            }
        }

        int currentInputLine = 0;
        int currentOutputLine = 0;

        int actionType = -1;

        while (actionType != 0) {
            try {
                actionType = Integer.parseInt(inputLines[currentInputLine]);
            } catch (NumberFormatException e) {
                return new CheckResult(false,
                    "The number of menu item must be number!");
            }

            switch (actionType) {
                case 1:
                    currentInputLine++;

                    String toSearch = inputLines[currentInputLine].trim().toLowerCase();

                    currentInputLine++;

                    List<String> intendedResult = new ArrayList<>();

                    for (String s : reference) {
                        s = s.toLowerCase();
                        if (s.contains(" " + toSearch + " ")
                            || s.startsWith(toSearch + " ")
                            || s.endsWith(" " + toSearch)) {

                            intendedResult.add(s);
                        }
                    }



                    currentSearchResult = new String[intendedResult.size()];
                    for (int i = 0; i < intendedResult.size(); i++) {
                        try {
                            currentSearchResult[i] = cleanedOutput.get(currentOutputLine++);
                        } catch (IndexOutOfBoundsException e) {
                            return new CheckResult(false,
                                "Seems like you output less than expected. " +
                                    "Either you've lost someone in the printing of all " +
                                    "people, or you haven't printed all the necessary " +
                                    "people in the search.");
                        }
                    }

                    String[] correctOutput = intendedResult.toArray(String[]::new);

                    Arrays.sort(correctOutput);
                    Arrays.sort(currentSearchResult);

                    if (!Arrays.equals(correctOutput, currentSearchResult)) {
                        return new CheckResult(false,
                            "Search result is not equal " +
                                "to the expected search");
                    }
                    break;
                case 2:
                    currentInputLine++;

                    List<String> intendedResultAll = new ArrayList<>();

                    for (String s : reference) {
                        s = s.toLowerCase();
                        intendedResultAll.add(s);
                    }

                    String[] userResultAll = new String[intendedResultAll.size()];
                    for (int i = 0; i < intendedResultAll.size(); i++) {
                        try {
                            userResultAll[i] = cleanedOutput.get(currentOutputLine++);
                        } catch (IndexOutOfBoundsException e) {
                            return new CheckResult(false,
                                "Seems like you output less than expected. " +
                                    "Either you've lost someone in the printing of all " +
                                    "people, or you haven't printed all the necessary " +
                                    "people in the search.");
                        }
                    }

                    String[] correctOutputAll = intendedResultAll.toArray(String[]::new);

                    Arrays.sort(correctOutputAll);
                    Arrays.sort(userResultAll);

                    if (!Arrays.equals(correctOutputAll, userResultAll)) {
                        return new CheckResult(false,
                            "Looks like you're printing " +
                                "unknown people when you enter option 2.");
                    }
                    break;
                case 0:
                    return CheckResult.TRUE;
                default:
                    currentInputLine++;
                    break;
            }
        }

        return CheckResult.TRUE;
    }

    private static boolean containsItemFromList(String inputStr, String[] items) {
        return Arrays.stream(items).parallel().anyMatch(inputStr::contains);
    }
}

