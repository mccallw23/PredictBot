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
            // note: to obtain number of contracts in a given market, keep track of a local linecount for that market and:
            // subtract 8 and mod linecount by 15.
            // increment to get total number of contracts


            // preserve an additional pointer to a market object which will temporarily point to every newly created market
            // initialized to an empty market object
            Market test = new Market("to be updated");

            // Create the URL; can throw MalformedURL exception
            // this URL is where we'll be reading data from. It's the API for the prediction market
            URL url = new URL("https://www.predictit.org/api/marketdata/all/");
            System.out.println("*** getting " + url);

            // Create the reader for the stream; can throw IO
            // Note:  Input stream is in JSON
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            ArrayList<Market> finalMarkets = new ArrayList<Market>();
            PriorityQueue<Market> bestDealsNO = new PriorityQueue<Market>((Market m1, Market m2) -> (int) (m2.getRelevantNo() - m1.getRelevantNo()));


           // TODO: configuration for buying opportunities on yes side
           // note: there are fewer of these.
            // PriorityQueue<Market> bestDealsYES = new PriorityQueue<Market>((Market m1, Market m2) -> (int) (m1.sumBuyYes() - m2.sumBuyYes()));


            // Read the lines; can throw IO
            try {
                String line;
                boolean marketNotVisited = true;

                Market temp = new Market("failed");

                // read and do nothing with the first two lines in the json file.
                line = in.readLine();
                line = in.readLine();
                boolean moreLeft = true;
                // we are going to continue reading markets from the API while there are more left
                while (moreLeft) {
                    int lineCount = 0;

                    while ((line = in.readLine()) != null) {

                        lineCount++;
                        //convert to lowercase
                        line = line.toLowerCase();
                        // the name is stored on the 3rd line of every market object in the API
                        if (lineCount == 3) {
                            String name = "";
                            StringBuilder nameEfficient = new StringBuilder();

                            //the name starts after the colon of this line and continues until the final two characters of the line
                            for (int i = 0; i < line.length(); i++) {
                                if (line.charAt(i) == ':') {
                                    for (int j = i + 3; j < line.length() - 2; j++) {
                                        nameEfficient.append(Character.toString(line.charAt(j)));
                                    }
                                    //convert name from stringbuilder.
                                    name = nameEfficient.toString();
                                    break;

                                }

                            }
                            //create a new market object based on the name that has been found.

                            temp = new Market(name);
                            test = temp;

                        } else if (lineCount == 6) {
                            temp.setURL(line);

                        } else if (lineCount == 8) {
                            break;
                        }
                    }

                    //make a contract object pointer available to help construct and allocate contracts
                    Contract tempCont = new Contract("in the works");

                    // logic for handling JSON parsing before and after a TimeStamp line appears

                    boolean firstAfterTimeStamp = false;
                    boolean secondAfterTimeStamp = false;

                    while ((line = in.readLine()) != null) {
                        //System.out.println(line);


                        if (firstAfterTimeStamp) {
                            secondAfterTimeStamp = true;
                            firstAfterTimeStamp = false;
                            // if the second line after a timestamp line doesnt have a bracket followed by a ',' we have finished reading markets from the API
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
                                // under this condition we have identified a contract in this market
                                //add tempCont to the list
                                test.addContract(tempCont);

                                //set tempCont to be a new contract
                                tempCont = new Contract("under construction");
                                //subtract by 8 and mod by 15.  If the result is 4 we have reached a name.
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

                                //logic for adding the best buy/sell yes/no information about the given contract.

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

                    //once we get here, our market test has enough information to initialize all of its interesting attributes
                    //namely, opportunities for arbitrage
                    test.initAttributes();
                    //add this market to all the final markets and to the best deals priorityQueue
                    finalMarkets.add(test);
                    bestDealsNO.add(test);

                }

            }


            finally {
                // Be sure to close the reader, whether or not reading was successful
                in.close();
                ArrayList<Market> furtherAnalysis = new ArrayList<Market>();
                //now from all of the markets we're going to send the program user ONLY those markets with
                //arbitrage opportunity

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
                // later this feature will allow the user to enter a market ID from the console and get more info on it
                // currently the program just does analysis on all markets.
                Scanner reading = new Scanner(System.in);

                // for now, index is programmed to be irrelevant as the program will simply perform analysis on
                // later it will be programmed so the user can enter a market ID and get more information about
                // that specific market
                // int index = reading.nextInt();

                int index = 0;

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

                // this number p is the amount of profit in cents that a user is mathematically guarenteed
                    // from purchasing one share of each contract on the no side for the given market
                int p = -1;

                for(int i = 0; i < contractVals.size(); i++)
                {

                    if (contractVals.get(i) != 0) {

                        int loss = contractVals.get(i);
                        int offset = 0;
                        //since the market makers take 10% commision on the profit from trades we must
                        // account for that below
                        // this is what makes it substantially harder to find true negative risk on predictit

                        for (int j = 0; j < contractVals.size(); j++) {

                            if (j != i && contractVals.get(j) != 0) {
                                offset += .9 * (100 - contractVals.get(j));
                            }

                        }

                        // if our hedged amount is greater than our loss, we have negative risk arbitrage
                        // set p to this difference and print it to the console
                        if (offset - loss >= 0) {
                            //System.out.println(offset - loss)
                            p = offset - loss;

                        } else {
                            break;
                        }
                    }
                }

                    if (p > 0)
                    {
                    System.out.println(p);
                    System.out.println(index);
                    }
                    index++;

                }

            }

            //close up shop
        } catch (MalformedURLException e) {
            System.err.println("bad URL");
        } catch (IOException e) {
            System.err.println("problem opening/reading/closing");
        }

        System.out.println("*** done");
    }
}



