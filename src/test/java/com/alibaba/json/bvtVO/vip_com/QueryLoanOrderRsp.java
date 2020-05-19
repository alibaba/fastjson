package com.alibaba.json.bvtVO.vip_com;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

public class QueryLoanOrderRsp {
  private String loan_card_no;
  private String loan_prod_code;
  private String last_row_type;//最后一条记录类型
  private String last_row_key;//最后一条记录键值
  private String nextpage_flag;//是否有下一页标志
  private List<TxnListItsm> txn_list;

  

  public QueryLoanOrderRsp() {
    super();
  }

  public String getLoan_card_no() {
    return loan_card_no;
  }

  public void setLoan_card_no(String loan_card_no) {
    this.loan_card_no = loan_card_no;
  }

  public String getLoan_prod_code() {
    return loan_prod_code;
  }

  public void setLoan_prod_code(String loan_prod_code) {
    this.loan_prod_code = loan_prod_code;
  }

  public String getLast_row_type() {
    return last_row_type;
  }

  public void setLast_row_type(String last_row_type) {
    this.last_row_type = last_row_type;
  }

  public String getLast_row_key() {
    return last_row_key;
  }

  public void setLast_row_key(String last_row_key) {
    this.last_row_key = last_row_key;
  }

  public String getNextpage_flag() {
    return nextpage_flag;
  }

  public void setNextpage_flag(String nextpage_flag) {
    this.nextpage_flag = nextpage_flag;
  }

  public List<TxnListItsm> getTxn_list() {
    return txn_list;
  }

  public void setTxn_list(List<TxnListItsm> txn_list) {
    this.txn_list = txn_list;
  }
  
  public static void main(String[] args) {
    QueryLoanOrderRsp rsp = new QueryLoanOrderRsp();
    
    rsp.setLast_row_key("A");
    List<TxnListItsm> txn_list = new ArrayList<TxnListItsm>();
    TxnListItsm itsm = new TxnListItsm();
    itsm.setAssets_no("B");
    itsm.setCover_vol(new BigDecimal("300"));
    txn_list.add(itsm);
    rsp.setTxn_list(txn_list);
    
    String txt = JSON.toJSONString(rsp);
    System.out.println(txt);
    
    String txt2 = JSON.toJSONString(txn_list);
    System.out.println(txt2);
    
    List<TxnListItsm> itsms = JSON.parseObject(txt2, 
        new TypeReference<List<TxnListItsm>>(){});
    System.out.println(itsms);

    rsp = JSON.parseObject(txt, 
        new TypeReference<QueryLoanOrderRsp>(){});
    
    System.out.println(rsp);
  }
}
