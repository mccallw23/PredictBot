
# PredictBot

## Author:  Will McCall
September 22, 2021

PredictBot is a personal project that Identifies arbitrage opportunities in the Political Prediction market "Predictit," using negative risk, it identifies opportunities to hedge against all possible market outcomes and thus guarentee a profit.  This tool would not have been possible without the principals of programming I learned in Timothy Pierson's CS10: Object Oriented Programming Class.

# Types of Arbitrage on Predictit:
Currently, the most practical way to find arbitrage on Predictit is to examine the sum of "no" prices in large contract markets.  In a given market in which one outcome must happen, the sum of all "yes" should add to 100%, implying that sum of all "no" should be (n-1) *(100%) where n is the number of contracts in the market.  By calculating this value for all Markets, PredictBot identifies when the true sum of "no" prices are less than (n-1) * (100), and returns the market to the user if it is. As an example, If (n-1)*100 = 400, and the true market value is 3.96, this presents an opportunity to buy $4 for the price of $3.96.

Potential Profit (as a %) is calculated by taking (actual-expected)/actual.


# FullMarketAccess
`FullMarketAccess` is a Java class which parses the PredictIt Market API for price information, loading it into internal `Market` objects which are composed of `Contract` objects.  It then runs a series of simple tests to determine if a Market can be arbitraged. 

The program outputs any valid market it finds in the following format:

[MarketNumber] <MarketName> <MarketURL> <WorstCaseProfit>  For example, when I just ran it, the first line reads:
