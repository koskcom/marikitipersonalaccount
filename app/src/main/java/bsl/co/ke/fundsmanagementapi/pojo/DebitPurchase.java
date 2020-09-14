package bsl.co.ke.fundsmanagementapi.pojo;

import java.io.Serializable;

public class DebitPurchase implements Serializable {
    private int id;
    private String bank__acc_namer;
    private String bank_acc_number;
    private String user_national_id;
    private  String marikiti_number;
    private String amount;
    private String trans_id;
    private String trans_type;
    private String date;
    private String marikiti_recipient_name;

    public DebitPurchase(int id, String bank__acc_namer, String bank_acc_number, String marikiti_number, String amount, String trans_type) {
        this.id = id;
        this.bank__acc_namer = bank__acc_namer;
        this.bank_acc_number = bank_acc_number;
        this.marikiti_number = marikiti_number;
        this.amount = amount;
        this.trans_type = trans_type;
    }


    public String getTrans_type() {
        return trans_type;
    }

    public void setTrans_type(String trans_type) {
        this.trans_type = trans_type;
    }

    public DebitPurchase() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBank__acc_namer() {
        return bank__acc_namer;
    }

    public void setBank__acc_namer(String bank__acc_namer) {
        this.bank__acc_namer = bank__acc_namer;
    }

    public String getBank_acc_number() {
        return bank_acc_number;
    }

    public void setBank_acc_number(String bank_acc_number) {
        this.bank_acc_number = bank_acc_number;
    }

    public String getTrans_id() {
        return trans_id;
    }

    public void setTrans_id(String trans_id) {
        this.trans_id = trans_id;
    }

    public String getUser_national_id() {
        return user_national_id;
    }

    public void setUser_national_id(String user_national_id) {
        this.user_national_id = user_national_id;
    }

    public String getMarikiti_number() {
        return marikiti_number;
    }

    public void setMarikiti_number(String marikiti_number) {
        this.marikiti_number = marikiti_number;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMarikiti_recipient_name() {
        return marikiti_recipient_name;
    }

    public void setMarikiti_recipient_name(String marikiti_recipient_name) {
        this.marikiti_recipient_name = marikiti_recipient_name;
    }
}
