package bsl.co.ke.fundsmanagementapi.model;

public class PTransfer {
    private int id;
    private String trans_id;
    private String bank_number;
    private String bank_ac_number;
    private String amount;
    private  String marikiti_pin;
    private String date;

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

    public String getBank_number() {
        return bank_number;
    }

    public void setBank_number(String bank_number) {
        this.bank_number = bank_number;
    }

    public String getBank_ac_number() {
        return bank_ac_number;
    }

    public void setBank_ac_number(String bank_ac_number) {
        this.bank_ac_number = bank_ac_number;
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
