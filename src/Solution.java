import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Solution {

    public static void main(String[] args) throws Exception {


        class Hastag {
            private String someString;
            private String[] args;
            private ArrayList<Integer> indexesCL = null;
            private ArrayList<Integer> indexesOP = null;

            public Hastag(String st, String[] ar) {
                indexesCL = new ArrayList<Integer>();
                indexesOP = new ArrayList<Integer>();
                this.someString = st;
                this.args = ar;
                init(st);
            }

            ArrayList<Integer> countInstances(String where, String tag, Integer indexFrom) {
                ArrayList<Integer> result = new ArrayList<Integer>();
                int tagLen = args[0].length();
                int index = indexFrom;
                boolean doContinue = true;
                while (doContinue) {
                    index = where.indexOf(tag, index);
                    if (index == -1) {
                        doContinue = false;
                        break;
                    } else {
                        result.add(index);
                        index = index + tagLen + 3;
                    }
                }

                return result;
            }

            private void init(String str) {
                int tagLen = args[0].length();
                String builder = str;
                Integer index = 0;
                String someString = builder.toString();
                String tag = "</" + args[0] + ">";
                indexesCL = countInstances(someString, tag, index);
                index = 0;
                String tagOP = "<" + args[0];
                indexesOP = countInstances(someString, tagOP, index);
            }

            public String hasTag() {
                String resultSTR = null;
                int OPcounter = 0;
                int CLcounter = 0;
                int tagLenCL = args[0].length() + 3;
                String testString = null;

                if (indexesOP.size() > 0 && indexesCL.size() > 0) {
                    testString = someString.substring(indexesOP.get(0), indexesCL.get(0) + tagLenCL);
                    OPcounter = (countInstances(testString, ("<" + args[0]), 0)).size();
                    CLcounter = (countInstances(testString, ("</" + args[0] + ">"), 0)).size();
                    if (OPcounter == CLcounter) {
                        resultSTR = testString;
                    } else {
                        int cccount = 0;
                        do {
                            // teststring next CLtag
                            if (indexesCL.size() > cccount) {
                                cccount++;
                            }
                            testString = someString.substring(indexesOP.get(0), indexesCL.get(cccount) + tagLenCL);
                            OPcounter = countInstances(testString, ("<" + args[0]), 0).size();
                            CLcounter = (countInstances(testString, ("</" + args[0] + ">"), 0)).size();
                        } while (OPcounter != CLcounter);
                        resultSTR = testString;
                    }

                } else {
                    resultSTR = "-1";
                }

                return resultSTR;
            }

            public String trim(String newString, String toTrim) {
                // this is hard, we have to remove first buffer if it is not nested
                // or remove first layer if it is nested
                String returnn = null;
                if ((newString).equals("-1") || toTrim.equals("-1")) {
                    return returnn;
                } else {
                    // if at least there is one tag inside it is nested and we have to peel off
                    // outermost layer
                    if (toTrim.substring(toTrim.indexOf('>') + 1, toTrim.lastIndexOf("</" + args[0] + ">"))
                            .indexOf("<" + args[0]) != -1) {
                        // first save what is behind
                        Hastag fromTrim = new Hastag(toTrim, args);
                        String zadBuffer = newString.substring(indexesOP.get(0) + toTrim.length(), newString.length());
                        String peredBuffer = toTrim.substring(fromTrim.indexesOP.get(1),
                                fromTrim.indexesCL.get(fromTrim.indexesCL.size() - 1));
                        if (zadBuffer != null) {
                            returnn = peredBuffer + zadBuffer;
                        } else {
                            returnn = peredBuffer;
                        }

                    } else {
                        // it is not nested, just simple one after another tag
                        returnn = newString.substring(toTrim.length(), newString.length());
                    }
                }

                return returnn;
            }
        }

        ArrayList<String> results = new ArrayList<String>();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String path = reader.readLine();
        reader.close();

        StringBuilder builder = new StringBuilder();
        BufferedReader fileRead = new BufferedReader(new FileReader(path));
        while (fileRead.ready()) {
            char buffer = (char) fileRead.read();
            builder.append(buffer);
        }
        fileRead.close();

        String builder2 = builder.toString().replaceAll("\\n", "").replaceAll("\\r", "")
                .replaceAll(System.lineSeparator(), "");

        String buffer = builder2.toString();
        String newStr = builder2.toString();
        boolean doContinue2 = true;
        Hastag hastag = null;

        int counter = 0;
        while (doContinue2) {
            if (newStr != null) {
                hastag = new Hastag(newStr, args);
                buffer = (hastag).hasTag();
            } else {
                break;
            }

            if (("-1").equals(buffer)) {
                doContinue2 = false;
                break;
            } else {
                if (buffer != null) {
                    results.add(buffer);
                }
                newStr = hastag.trim(newStr, buffer);
                counter++;
            }
        }

        for (int i = 0; i < results.size(); i++) {
            System.out.println(results.get(i));
        }

    }


}