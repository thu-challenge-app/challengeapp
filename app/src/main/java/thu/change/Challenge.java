package thu.change;

public class Challenge {
    private int id;
    private String name;
    private int maximum;
    private boolean weekly;
    private int average;
    private boolean active;
    private String unit;
    private boolean above;

    // Constructor
    public Challenge(){
        id = 0;
        name = "";
        maximum = 100;
        weekly = false;
        average = 0;
        active = false;
        unit = "";
        above = true;
    }
    public Challenge(int aId,
                     String aName,
                     int aMaximum,
                     boolean aWeekly,
                     int aAverage,
                     boolean aActive,
                     String aUnit,
                     boolean aAbove) {
        super();
        id = aId;
        name = aName;
        maximum = aMaximum;
        weekly = aWeekly;
        average = aAverage;
        active = aActive;
        unit = aUnit;
        above = aAbove;
    }

    // Getters
    public int getId()         { return id; }
    public String getName()    { return name; }
    public int getMaximum()    { return maximum; }
    public boolean getWeekly() { return weekly; }
    public int getAverage()    { return average; }
    public boolean getActive() { return active; }
    public String getUnit()    { return unit; }
    public boolean getAbove()  { return above; }

    // Setters
    public void setName(String aValue)    { name =    aValue; }
    public void setMaximum(int aValue)    { maximum = aValue; }
    public void setWeekly(boolean aValue) { weekly =  aValue; }
    public void setAverage(int aValue)    { average = aValue; }
    public void setActive(boolean aValue) { active =  aValue; }
    public void setUnit(String aValue)    { unit =    aValue; }
    public void setAbove(boolean aValue)  { above =   aValue; }
}
