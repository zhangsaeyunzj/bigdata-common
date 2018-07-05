package com.nuonuo.bigdata.entity;

import com.nuonuo.bigdata.annotation.HBaseField;
import com.nuonuo.bigdata.annotation.RowKey;
import com.nuonuo.bigdata.common.ContactRowKeyGenerator;
import com.nuonuo.bigdata.common.RowKeyType;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RowKey(field = "invoiceNum,invoiceCode,invoiceKind",method = RowKeyType.CUSTOM,clazzName = ContactRowKeyGenerator.class)
public class InvoiceMain implements Serializable {
    //主键id
    @JSONField(name = "id")
    private String id;
    //机器编号
    @JSONField(name = "machine_Num")
    private String machineNum;
    //发票种类
    @JSONField(name = "invoice_kind")
    private String invoiceKind;
    //发票编码
    @JSONField(name = "invoice_code")
    private String invoiceCode;
    //发票号码
    @JSONField(name = "invoice_num")
    private String invoiceNum;
    //购方名称
    @JSONField(name = "buyer_name")
    private String buyerName;
    //购方税号
    @JSONField(name = "buyer_taxnum")
    private String buyerTaxnum;
    //购方联系方式
    @JSONField(name = "buyer_contact")
    private String buyerContact;
    //购方账号
    @JSONField(name = "buyer_account")
    private String buyerAccount;
    //销方名称
    @JSONField(name = "saler_name")
    private String salerName;
    //销方税号
    @JSONField(name = "saler_taxnum")
    private String salerTaxnum;
    //销方联系方式
    @JSONField(name = "saler_contact")
    private String salerContact;
    //销方账户
    @JSONField(name = "saler_account")
    private String salerAccount;
    //开发票日期
    @JSONField(name = "invoice_date", format = "yyyy-MM-dd HH:mm:ss")
    @HBaseField(format = "yyyy-MM-dd")
    private Date invoiceDate;
    //发票总额
    @JSONField(name = "total_amount_sum")
    private Double totalAmountSum;
    //无税总额
    @JSONField(name = "notax_amount_sum")
    private Double notaxAmountSum;
    //税务总额
    @JSONField(name = "tax_amount_sum")
    private Double taxAmountSum;
    //备注
    @JSONField(name = "comment")
    private String comment;
    //开票人
    @JSONField(name = "invoicer")
    private String invoicer;
    //检查者
    @JSONField(name = "checker")
    private String checker;
    //收款人
    @JSONField(name = "receiver")
    private String receiver;
    //打印标志
    @JSONField(name = "print_flag")
    private String printFlag;
    //是否取消 (是 表示取消，否代表未取消)
    @JSONField(name = "cancel_flag")
    private String cancelFlag;
    //清单标志
    @JSONField(name = "bill_flag")
    private String billFlag;
    //网络发票号
    @JSONField(name = "net_invoice_num")
    private String netInvoiceNum;
    //更新年月
    @JSONField(name = "update_date", format = "yyyy-MM-dd HH:mm:ss")
    private Date updateDate;

    //销方地区编码
    @JSONField(name = "saler_area_code")
    private String salerAreaCode;
    //购方地区编码
    @JSONField(name = "buyer_area_code")
    private String buyerAreaCode;

    @HBaseField(cf = "info")
    private List<Map<String,String>> detail;

    public String getSalerAreaCode() {
        return salerAreaCode;
    }

    public void setSalerAreaCode(String salerAreaCode) {
        this.salerAreaCode = salerAreaCode;
    }

    public String getBuyerAreaCode() {
        return buyerAreaCode;
    }

    public void setBuyerAreaCode(String buyerAreaCode) {
        this.buyerAreaCode = buyerAreaCode;
    }

    public String toString() {
        return JSONObject.toJSONString(this);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMachineNum() {
        return machineNum;
    }

    public void setMachineNum(String machineNum) {
        this.machineNum = machineNum;
    }

    public String getInvoiceKind() {
        return invoiceKind;
    }

    public void setInvoiceKind(String invoiceKind) {
        this.invoiceKind = invoiceKind;
    }

    public String getInvoiceCode() {
        return invoiceCode;
    }

    public void setInvoiceCode(String invoiceCode) {
        this.invoiceCode = invoiceCode;
    }

    public String getInvoiceNum() {
        return invoiceNum;
    }

    public void setInvoiceNum(String invoiceNum) {
        this.invoiceNum = invoiceNum;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getBuyerTaxnum() {
        return buyerTaxnum;
    }

    public void setBuyerTaxnum(String buyerTaxnum) {
        this.buyerTaxnum = buyerTaxnum;
    }

    public String getBuyerContact() {
        return buyerContact;
    }

    public void setBuyerContact(String buyerContact) {
        this.buyerContact = buyerContact;
    }

    public String getBuyerAccount() {
        return buyerAccount;
    }

    public void setBuyerAccount(String buyerAccount) {
        this.buyerAccount = buyerAccount;
    }

    public String getSalerName() {
        return salerName;
    }

    public void setSalerName(String salerName) {
        this.salerName = salerName;
    }

    public String getSalerTaxnum() {
        return salerTaxnum;
    }

    public void setSalerTaxnum(String salerTaxnum) {
        this.salerTaxnum = salerTaxnum;
    }

    public String getSalerContact() {
        return salerContact;
    }

    public void setSalerContact(String salerContact) {
        this.salerContact = salerContact;
    }

    public String getSalerAccount() {
        return salerAccount;
    }

    public void setSalerAccount(String salerAccount) {
        this.salerAccount = salerAccount;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public Double getTotalAmountSum() {
        return totalAmountSum;
    }

    public void setTotalAmountSum(Double totalAmountSum) {
        this.totalAmountSum = totalAmountSum;
    }

    public Double getNotaxAmountSum() {
        return notaxAmountSum;
    }

    public void setNotaxAmountSum(Double notaxAmountSum) {
        this.notaxAmountSum = notaxAmountSum;
    }

    public Double getTaxAmountSum() {
        return taxAmountSum;
    }

    public void setTaxAmountSum(Double taxAmountSum) {
        this.taxAmountSum = taxAmountSum;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getInvoicer() {
        return invoicer;
    }

    public void setInvoicer(String invoicer) {
        this.invoicer = invoicer;
    }

    public String getChecker() {
        return checker;
    }

    public void setChecker(String checker) {
        this.checker = checker;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getPrintFlag() {
        return printFlag;
    }

    public void setPrintFlag(String printFlag) {
        this.printFlag = printFlag;
    }

    public String getCancelFlag() {
        return cancelFlag;
    }

    public void setCancelFlag(String cancelFlag) {
        this.cancelFlag = cancelFlag;
    }

    public String getBillFlag() {
        return billFlag;
    }

    public void setBillFlag(String billFlag) {
        this.billFlag = billFlag;
    }

    public String getNetInvoiceNum() {
        return netInvoiceNum;
    }

    public void setNetInvoiceNum(String netInvoiceNum) {
        this.netInvoiceNum = netInvoiceNum;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String combineESId() {
        return invoiceKind + invoiceCode + invoiceNum;
    }

    public List<Map<String, String>> getDetail() {
        return detail;
    }

    public void setDetail(List<Map<String, String>> detail) {
        this.detail = detail;
    }
}
