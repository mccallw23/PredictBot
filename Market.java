import java.util.ArrayList;
public class Market {

    private String name;
    private String URL;
    private ArrayList<Contract> contracts = new ArrayList<Contract>();
    private double sumYes;
    private double sumNo;
    private double sumYesRelaxed;
    private double relevantNo;
    private double relevantYes;
    private double relevantYesRelaxed;

    public double getRelevantNo(){return relevantNo;}
    public double getRelevantYes(){return relevantYes;}
    public double getRelevantYesRelaxed(){return relevantYesRelaxed;}



    public Market(String name)
    {
        this.name = name;


    }
    public String getURL()
    {
        return this.URL;
    }

    public void setURL(String URL)
    {
        this.URL = URL;
    }

    public double getViability()
    {
        return 1.00;
    }

    public String getName()
    {
        return this.name;
    }

    public void addContract(Contract c)
    {
        contracts.add(c);
    }

    public ArrayList<Contract> getContracts()
    {
        return contracts;
    }


    public void fixThisSoItWorks()
    {
        sumYes = sumBuyYes();
        sumNo = sumBuyNo();
        sumYesRelaxed = sumbuyYesRelaxed();
        relevantYes = 1.0 - sumYes;
        relevantYesRelaxed = 1.0 - sumYesRelaxed;

    }


    public double sumbuyYesRelaxed()
    {
        double res = 0.0;

        if (contracts.size() == 1)
        {
            for (Contract c: this.contracts)
            {
                res = c.getBuyYes() + c.getBuyNo();
            }

        }
        else
        {
            for (Contract c: this.contracts)
            {
                if (c.getBuyNo() != 0.0)
                {
                    res += c.getBuyYes();
                }
            }

        }

        return res;

    }

    public double sumBuyYes()
    {
        double res = 0.0;

        if (contracts.size() == 1)
        {
            for (Contract c: this.contracts)
            {
                res = c.getBuyYes() + c.getBuyNo();
            }

        }
        else
            {
                for (Contract c: this.contracts)
                {
                    res += c.getBuyYes();
                }

            }

        return res;
    }

    public double sumBuyNo()
    {
        double L = 0;
        double O = 0;
        double res = 0.0;
        if (contracts.size() == 1)
        {
            return -1;

        }
        else
            {



                double n = 0;
                int i = 0;

                for (Contract c: this.contracts)
                {
                    if (c.getBuyNo() != 0.0)
                    {
                        n ++;
                        res +=  c.getBuyNo();











                        double loss = c.getBuyNo();
                        double offset = 0;
                        L = loss;
                        O = offset;

                        int j = 0;
                        for (Contract h: this.contracts) {

                            if (j != i && contracts.get(j).getBuyNo() != 0) {
                                offset += .9 * (100 - contracts.get(j).getBuyNo());
                                O += offset;
                            }


                            j++;

                        }

                    }

                    i++;


                }

                n--;
                relevantNo = (n - res)/ n;
                //relevantNo = O - L;
                return res;

            }

    }

    //public double SumBuyYesAdvanced...

    @Override
    public String toString()
    {
//        double res = sumBuyYes();
//        if (res < 1.00)
//    {
//        return this.name + " " + res + this.getURL();
//    }
//        else
//            {
//                return this.name + " Don't buy this. It's overvalued. ";
//            }
        return this.name + this.getURL();


    }

    public static void main(String[] args) {

        Market test = new Market("pp");
        test.addContract(new Contract("pp"));
        test.addContract(new Contract("pp"));
        System.out.println(test.sumBuyNo());
    }
}
