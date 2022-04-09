package by.bsuir.relaxapp;

public class UserInfoDB {
    public int WEIGHT;
    public int HEIGHT;
    public int SYS_PRESSURE;
    public int DIA_PRESSURE;
    public int AGE;
    public int ZODIAC;

    public UserInfoDB(int weight, int height, int sys_pressure, int dia_pressure,
                      int age, int zodiac){

        WEIGHT = weight;
        HEIGHT = height;
        SYS_PRESSURE = sys_pressure;
        DIA_PRESSURE = dia_pressure;
        AGE = age;
        ZODIAC = zodiac;
    }

    public UserInfoDB(){
        WEIGHT = -1;
        HEIGHT = -1;
        SYS_PRESSURE = -1;
        DIA_PRESSURE = -1;
        AGE = -1;
        ZODIAC = -1;
    }

    @Override
    public String toString(){
        return WEIGHT + " кг\n" + HEIGHT + " см\n" + SYS_PRESSURE + "/" + DIA_PRESSURE + "\n" + AGE + " годиков\n" + ZODIAC + " зодиак\n";
    }
}
