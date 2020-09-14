package bsl.co.ke.fundsmanagementapi.model;

import java.io.Serializable;

public class DePo  {
    private int id;
    private String agentDeposittype;
    private String agentNumber;
    private String  amount;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAgentDeposittype() {
        return agentDeposittype;
    }

    public void setAgentDeposittype(String agentDeposittype) {
        this.agentDeposittype = agentDeposittype;
    }

    public String getAgentNumber() {
        return agentNumber;
    }

    public void setAgentNumber(String agentNumber) {
        this.agentNumber = agentNumber;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
