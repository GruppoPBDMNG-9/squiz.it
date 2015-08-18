package exception;

public class CustomizationNotValid extends RuntimeException{

    public CustomizationNotValid(){}

    public CustomizationNotValid(String msg){
        super(msg);
    }

}
