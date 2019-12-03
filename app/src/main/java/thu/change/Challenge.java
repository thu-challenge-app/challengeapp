package thu.change;

public class Challenge {
    private int id;
    private String name;
    private int maximum;
    private boolean weekly;
    private int average;
    private boolean active;
    private boolean predefined;

    // Constructor
    public Challenge(){}
    public Challenge(int aId,
                     String aName,
                     int aMaximum,
                     boolean aWeekly,
                     int aAverage,
                     boolean aActive,
                     boolean aPredefined) {
        super();
        id = aId;
        name = aName;
        maximum = aMaximum;
        weekly = aWeekly;
        average = aAverage;
        active = aActive;
        predefined = aPredefined;
    }

    // Getters
    public int getId()             { return id; }
    public String getName()        { return name; }
    public int getMaximum()        { return maximum; }
    public boolean getWeekly()     { return weekly; }
    public int getAverage()        { return average; }
    public boolean getActive()     { return active; }
    public boolean getPredefined() { return predefined; }

    // Setters
    public void setName(String aValue)        { name =       aValue; }
    public void setMaximum(int aValue)        { maximum =    aValue; }
    public void setWeekly(boolean aValue)     { weekly =     aValue; }
    public void setAverage(int aValue)        { average =    aValue; }
    public void setActive(boolean aValue)     { active =     aValue; }
    public void setPredefined(boolean aValue) { predefined = aValue; }
}
