package vedam.subkuch.network.models;

public class DrinkingHabits {
    private int DrinkingStatus_Id;

    private String DrinkingStatus_Name;

    public DrinkingHabits(int drinkingStatus_Id, String drinkingStatus_Name) {
        DrinkingStatus_Id = drinkingStatus_Id;
        DrinkingStatus_Name = drinkingStatus_Name;
    }

    public int getDrinkingStatus_Id() {
        return DrinkingStatus_Id;
    }

    public void setDrinkingStatus_Id(int DrinkingStatus_Id) {
        this.DrinkingStatus_Id = DrinkingStatus_Id;
    }

    public String getDrinkingStatus_Name() {
        return DrinkingStatus_Name;
    }

    public void setDrinkingStatus_Name(String DrinkingStatus_Name) {
        this.DrinkingStatus_Name = DrinkingStatus_Name;
    }
}
