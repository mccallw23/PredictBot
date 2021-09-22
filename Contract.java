public class Contract {

    private double bestbuyyescost;
    private double bestbuynocost;
    private double bestsellyescost;
    private double bestsellnocost;

    private String name;


    public Contract(String name, double buyYes, double buyNo, double sellYes, double sellNo) {
        this.name = name;
        this.bestbuyyescost = buyYes;
        this.bestbuynocost = buyNo;
        this.bestsellnocost = sellNo;
        this.bestsellyescost = sellYes;
    }

    public String getName() {
        return this.name;
    }

    //we're gonna be working on this for a sec.
    public Contract(String name) {
        this.name = name;
    }

    public double getBuyNo() {
        return bestbuynocost;
    }

    public double getSellNo() {
        return bestsellnocost;
    }

    public double getBuyYes() {
        return bestbuyyescost;
    }

    public double getSellYes() {
        return bestsellyescost;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBestbuynocost(double bestbuynocost) {
        this.bestbuynocost = bestbuynocost;
    }

    public void setBestbuyyescost(double bestbuyyescost) {
        this.bestbuyyescost = bestbuyyescost;
    }

    public void setBestsellnocost(double bestsellnocost) {
        this.bestsellnocost = bestsellnocost;
    }

    public void setBestsellyescost(double bestsellyescost) {
        this.bestsellyescost = bestsellyescost;
    }

    @Override
    public String toString()
    {
        return this.name;

    }
}
