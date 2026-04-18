package hk.order.domain;

import java.util.Date;

public class OrderDTO {

    private int odrCode;              // ODRCODE
    private String memberId;          // FK_MEMBER_ID
    private int addrId;               // FK_ADDR_ID
    private int odrTotalPrice;        // ODRTOTALPRICE
    private int odrTotalPoint;        // ODRTOTALPOINT
    private Date odrDate;             // ODRDATE   // String -> Date 타입 변경
    private int paymentStatus;        // PAYMENT_STATUS

    // ===== 조회용 확장 필드 =====
    private String paymentStatusName; // 결제상태 한글
    private String productName;       // 대표 상품명
    private int totalQty;             // 총 수량
    
    // 클레임상태
    private String claimStatus;
    
    // reject 상태
    private String rejectReason;
    
    // 스냅샷 address
    private String postcode;
    private String address;
    private String detailaddress;
    private String extraaddress;


    // getter / setter
    public int getOdrCode() { return odrCode; }
    public void setOdrCode(int odrCode) { this.odrCode = odrCode; }

    public String getMemberId() { return memberId; }
    public void setMemberId(String memberId) { this.memberId = memberId; }

    public int getAddrId() { return addrId; }
    public void setAddrId(int addrId) { this.addrId = addrId; }

    public int getOdrTotalPrice() { return odrTotalPrice; }
    public void setOdrTotalPrice(int odrTotalPrice) { this.odrTotalPrice = odrTotalPrice; }

    public int getOdrTotalPoint() { return odrTotalPoint; }
    public void setOdrTotalPoint(int odrTotalPoint) { this.odrTotalPoint = odrTotalPoint; }

    // ordDate 변경
    public Date getOdrDate() {
		return odrDate;
	}
	public void setOdrDate(Date odrDate) {
		this.odrDate = odrDate;
	}
	
	public int getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(int paymentStatus) { this.paymentStatus = paymentStatus; }

    public String getPaymentStatusName() { return paymentStatusName; }
    public void setPaymentStatusName(String paymentStatusName) { this.paymentStatusName = paymentStatusName; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public int getTotalQty() { return totalQty; }
    public void setTotalQty(int totalQty) { this.totalQty = totalQty; }
	
    // 클레임용 getter/setter
    public String getClaimStatus() {
		return claimStatus;
	}
	public void setClaimStatus(String claimStatus) {
		this.claimStatus = claimStatus;
	}

	// reject 표기 용 getter/setter
	public String getRejectReason() {
	    return rejectReason;
	}
	
	public void setRejectReason(String rejectReason) {
	    this.rejectReason = rejectReason;
	}
	
    
    
}
