
# PredictBot

## Author:  Will McCall
September 22, 2021

PredictBot is a personal project that Identifies arbitrage opportunities in the Political Prediction market "Predictit," using negative risk, it identifies opportunities to hedge against all possible market outcomes and thus guarentee a profit.  This tool would not have been possible without the principals of programming I learned in Timothy Pierson's CS10: Object Oriented Programming Class.



# FullMarketAccess
`FullMarketAccess` is a Java class which parses the PredictIt Market API for price information, loading it into internal `Market` objects which are composed of `Contract` objects.  It then runs a series of simple tests to determine if a Market can be arbitraged. For more on how it works, see Section titled `Types of Arbitrage on Predictit`

The program uses a MaxPriorityQueue to output valid markets it finds in the following format in order of Most to Least profitable:

[MarketNumber] [MarketName] [MarketURL] [WorstCaseProfit]  

# Market
`Market` is a Java class designed to internally represent Predictit Markets on the machine for ease of calculations.  It is composed of  `Contract` objects.  It contains useful instance variables such as `this.sumBuyNo` which can quickly be compared to expected numbers to determine if arbitrage is possible.

# Contract

`Contract` contains the current best price being offered for yes and no options in the `Market `


# Types of Arbitrage on Predictit:
Currently, the most practical way to find arbitrage on Predictit is to examine the sum of "no" prices in large contract markets.  In a given market in which one outcome must happen, the sum of all "yes" should add to 100%, implying that sum of all "no" should be (n-1) *(100%) where n is the number of contracts in the market.  By calculating this value for all Markets, PredictBot identifies when the true sum of "no" prices are less than (n-1) * (100), and returns the market to the user if it is. As an example, If (n-1)*100 = 400, and the true market value is 3.96, this presents an opportunity to buy $4 for the price of $3.96.

Potential Profit (as a %) is calculated by taking (actual-expected)/actual.


