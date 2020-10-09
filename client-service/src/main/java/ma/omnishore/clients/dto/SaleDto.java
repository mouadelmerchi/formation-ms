package ma.omnishore.clients.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class SaleDto implements Serializable {

    private static final long serialVersionUID = 5257474071684681286L;

    private Long id;
    private String productCode;
    private Long clientId;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date operationDate;
    private Long quantity;
    private Double amount;

    public SaleDto(String productCode, Long clientId, Date operationDate, Long quantity, Double amount) {
        this.productCode = productCode;
        this.clientId = clientId;
        this.operationDate = operationDate;
        this.quantity = quantity;
        this.amount = amount;
    }

    public SaleDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Date getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(Date operationDate) {
        this.operationDate = operationDate;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
