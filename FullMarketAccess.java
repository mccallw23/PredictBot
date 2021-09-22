import javax.sound.midi.Soundbank;
import java.net.*;
import java.io.*;
import java.sql.SQLOutput;
import java.util.*;
import java.lang.*;
import java.util.Scanner;
//import java.util.ArrayList;
//import java.util.PriorityQueue;


/**
 * @author Will McCall, Dartmouth College, Fall, 2021
 * Principals adapted from:
 * @inspiration Tim Pierson's Dartmouth CS 10 "WWWget.Java" class.  Thanks Tim :)
 */
public class FullMarketAccess {


    public static void main(String[] args) {
        try {

            //subtract 8 and mod it by 15.
            //increment to get total number of contracts

            Market test = new Market("to be updated");

            // Create the URL; can throw MalformedURL
            URL url = new URL("https://www.predictit.org/api/marketdata/all/");
            System.out.println("*** getting " + url);

            // Create the reader for the stream; can throw IO
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            ArrayList<Market> seeIfItWorks = new ArrayList<Market>();
            //PriorityQueue<Market> bestDealsYES = new PriorityQueue<Market>((Market m1, Market m2) -> (int) (m1.sumBuyYes() - m2.sumBuyYes()));
            PriorityQueue<Market> bestDealsNO = new PriorityQueue<Market>((Market m1, Market m2) -> (int) (m2.getRelevantNo() - m1.getRelevantNo()));


            // Read the lines; can throw IO
            try {
                String line;
                boolean marketNotVisited = true;

                Market temp = new Market("failed");


                line = in.readLine();
                line = in.readLine();
                boolean moreLeft = true;

                while (moreLeft) {
                    int lineCount = 0;


                    while ((line = in.readLine()) != null) {

                        lineCount++;

                        line = line.toLowerCase();

                        if (lineCount == 3) {
                            String name = "";
                            StringBuilder nameEfficient = new StringBuilder();


                            for (int i = 0; i < line.length(); i++) {
                                if (line.charAt(i) == ':') {
                                    for (int j = i + 3; j < line.length() - 2; j++) {
                                        nameEfficient.append(Character.toString(line.charAt(j)));
                                    }
                                    name = nameEfficient.toString();

                                    break;

                                }

                            }

                            temp = new Market(name);
                            test = temp;

                        } else if (lineCount == 6) {
                            temp.setURL(line);

                        } else if (lineCount == 8) {
                            break;
                        }
                    }


                    Contract tempCont = new Contract("in the works");

                    boolean firstAfterTimeStamp = false;
                    boolean secondAfterTimeStamp = false;

                    while ((line = in.readLine()) != null) {
                        //System.out.println(line);


                        if (firstAfterTimeStamp) {
                            secondAfterTimeStamp = true;
                            firstAfterTimeStamp = false;

                        } else if (secondAfterTimeStamp) {
                            if (!line.contains("},")) {
                                moreLeft = false;
                                break;
                            } else {
                                secondAfterTimeStamp = false;
                                break;
                            }

                        } else {

                            lineCount++;

                            if ((lineCount - 8) % 15 == 1 && line.contains("timeStamp")) {
                                firstAfterTimeStamp = true;

                            }

                            if ((lineCount - 8) % 15 == 0) {

                                //add tempCont to the list
                                test.addContract(tempCont);

                                //set tempCont to be a new contract
                                tempCont = new Contract("under construction");
                            } else if ((lineCount - 8) % 15 == 4) {
                                String name = "";
                                StringBuilder nameEfficient = new StringBuilder();


                                for (int i = 0; i < line.length(); i++) {
                                    if (line.charAt(i) == ':') {
                                        for (int j = i + 3; j < line.length() - 2; j++) {
                                            nameEfficient.append(Character.toString(line.charAt(j)));
                                        }
                                        name = nameEfficient.toString();

                                        break;

                                    }

                                }

                                tempCont.setName(name);

                            } else if ((lineCount - 8) % 15 == 8) {
                                String handle = line.substring(line.length() - 5, line.length() - 1);
                                if (!handle.equals("null")) {
                                    tempCont.setBestbuyyescost(Double.parseDouble(handle));
                                }


                            } else if ((lineCount - 8) % 15 == 9) {
                                String handle = line.substring(line.length() - 5, line.length() - 1);
                                if (!handle.equals("null")) {
                                    tempCont.setBestbuynocost(Double.parseDouble(handle));
                                }

                            } else if ((lineCount - 8) % 15 == 10) {
                                String handle = line.substring(line.length() - 5, line.length() - 1);
                                if (!handle.equals("null")) {
                                    tempCont.setBestsellyescost(Double.parseDouble(handle));
                                }

                            } else if (lineCount - 8 % 15 == 11) {
                                String handle = line.substring(line.length() - 5, line.length() - 1);
                                if (!handle.equals("null")) {
                                    tempCont.setBestsellnocost(Double.parseDouble(handle));
                                }

                            }
                        }


                    }


                    test.fixThisSoItWorks();
                    seeIfItWorks.add(test);
                    bestDealsNO.add(test);

                }

            }
            // Be sure to close the reader, whether or not reading was successful

            finally {

                // Be sure to close the reader, whether or not reading was successful
                in.close();

                ArrayList<Market> furtherAnalysis = new ArrayList<Market>();

                int increment = 0;
                for (int i = 0; i < bestDealsNO.size(); i++)
                {
                    Market maybePrint = bestDealsNO.poll();


                    if(maybePrint.getRelevantNo() > 0)
                    {
                        furtherAnalysis.add(maybePrint);
                        System.out.println(Integer.toString(increment) + " " + maybePrint + Double.toString(maybePrint.getRelevantNo() * 100));
                        increment ++;
                    }
                }

                Scanner reading = new Scanner(System.in);
                int index = reading.nextInt();



                index = 0;

                while (index < furtherAnalysis.size()){

                Market theMarket = furtherAnalysis.get(index);
                ArrayList<Integer> contractVals = new ArrayList<Integer>();



                //ArrayList<Integer> incrementers = new ArrayList<Integer>();


                for(Contract c: theMarket.getContracts())
                {
                    double timesHundred = c.getBuyNo() * 100;
                    Integer finalVal = (int) timesHundred;
                    //System.out.println(finalVal);

                    contractVals.add(finalVal);
//                    incrementers.add(1);
//                    incrementers.set(incrementers.size() - 1, 0);
                }

                int p = -1;

                for(int i = 0; i < contractVals.size(); i++)
                {
                    //System.out.println(i);

                    if (contractVals.get(i) != 0) {

                        int loss = contractVals.get(i);
                        int offset = 0;

                        for (int j = 0; j < contractVals.size(); j++) {

                            if (j != i && contractVals.get(j) != 0) {
                                offset += .9 * (100 - contractVals.get(j));
                            }

                        }

                  //System.out.println("Loss:");
              //System.out.println(loss);
//                    System.out.println("offset:");
//                    System.out.println(offset);


                        if (offset - loss >= 0) {
                            //System.out.println(offset - loss
                            p = offset - loss;

                        } else {
                            //System.out.println("it not always profitable");
                            break;
                        }
                    }
                }
                //System.out.println("good to go");
                //System.out.println("offset-loss");

                    if (p > 0)
                    {
                    System.out.println(p);
                    System.out.println(index);
                    }
                    //System.out.println("  ");

                    index++;

                }


            }

        } catch (MalformedURLException e) {
            System.err.println("bad URL");
        } catch (IOException e) {
            System.err.println("problem opening/reading/closing");
        }

        System.out.println("*** done");
    }
}



