package services;

public class SaleHouseException extends Exception{
    public SaleHouseException() {
    }

    public SaleHouseException(String message) {
        super(message);
    }

    public SaleHouseException(String message, Throwable cause) {
        super(message, cause);
    }
}
