package bsl.co.ke.fundsmanagementapi.model;

public class BWithdrawal {
    private int id;
    private String trans_id;
    private String trader_number;
    private String agent_number;
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

    public String getTrader_number() {
        return trader_number;
    }

    public void setTrader_number(String trader_number) {
        this.trader_number = trader_number;
    }

    public String getAgent_number() {
        return agent_number;
    }

    public void setAgent_number(String agent_number) {
        this.agent_number = agent_number;
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
