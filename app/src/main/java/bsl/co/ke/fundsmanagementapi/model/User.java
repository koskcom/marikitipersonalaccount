package bsl.co.ke.fundsmanagementapi.model;

public class User {
    private int id;
    private String name;
    private String idno;
    private String password;
    private String  selectionfunction;
    private String  typeofaccount;
    private String  phoneNumber;
    private String  amount;
    private String  pin;
    private String  recipient;
    private String  paymentmode;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdno() {
        return idno;
    }

    public void setIdno(String idno) {
        this.idno = idno;
    }

    public String getSelectionfunction() {
        return selectionfunction;
    }

    public void setSelectionfunction(String selectionfunction) {
        this.selectionfunction = selectionfunction;
    }

    public String getTypeofaccount() {
        return typeofaccount;
    }

    public void setTypeofaccount(String typeofaccount) {
        this.typeofaccount = typeofaccount;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getPaymentmode() {
        return paymentmode;
    }

    public void setPaymentmode(String paymentmode) {
        this.paymentmode = paymentmode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
