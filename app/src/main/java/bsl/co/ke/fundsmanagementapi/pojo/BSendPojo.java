package bsl.co.ke.fundsmanagementapi.pojo;

public class BSendPojo {
    private int id;
    private String trans_id;
    private String payment_type;
    private String recipient_number;
    private String amount;
    private  String marikiti_pin;
    private String status;
    private String localID;
    private String date;

    public BSendPojo() {
    }

    public String getLocalID() {
        return localID;
    }

    public void setLocalID(String localID) {
        this.localID = localID;
    }

    public BSendPojo(int id, String payment_type, String recipient_number, String amount, String status) {
        this.id = id;
        this.payment_type = payment_type;
        this.recipient_number = recipient_number;
        this.amount = amount;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTrans_id() {
        return trans_id;
    }

    public void setTrans_id(String trans_id) {
        this.trans_id = trans_id;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public String getRecipient_number() {
        return recipient_number;
    }

    public void setRecipient_number(String recipient_number) {
        this.recipient_number = recipient_number;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMarikiti_pin() {
        return marikiti_pin;
    }

    public void setMarikiti_pin(String marikiti_pin) {
        this.marikiti_pin = marikiti_pin;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
