package hk.order.domain;

public class OrderDetailDTO {

    private int odrDetailNo;        // ODRDETAILNO
    private int odrCode;            // FK_ODRCODE
    private int productId;          // FK_PRODUCT_ID
    private int odrQty;             // ODRQTY
    private int odrPrice;           // ODRPRICE
    private int deliveryStatus;     // DELIVERYSTATUS
    private String deliveryDate;    // DELIVERYDATE  // 삭제 에정
  
    
	// ===== 조회용 =====
    private String productImage;    // 추가 (이미지 조회용)
    private String productName;
    private String deliveryStatusName;
    
    
    // ===== 클레임(취소/반품/교환) =====
  	private String claimType;    // CANCEL / RETURN / EXCHANGE
  	private String claimStatus;  // REQUEST / APPROVED / REJECTED
  	private String claimReason;  // 사유



	public int getOdrDetailNo() { return odrDetailNo; }
    public void setOdrDetailNo(int odrDetailNo) { this.odrDetailNo = odrDetailNo; }

    public int getOdrCode() { return odrCode; }
    public void setOdrCode(int odrCode) { this.odrCode = odrCode; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public int getOdrQty() { return odrQty; }
    public void setOdrQty(int odrQty) { this.odrQty = odrQty; }

    public int getOdrPrice() { return odrPrice; }
    public void setOdrPrice(int odrPrice) { this.odrPrice = odrPrice; }

    public int getDeliveryStatus() { return deliveryStatus; }
    public void setDeliveryStatus(int deliveryStatus) { this.deliveryStatus = deliveryStatus; }

    public String getDeliveryDate() { return deliveryDate; }
    public void setDeliveryDate(String deliveryDate) { this.deliveryDate = deliveryDate; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getDeliveryStatusName() { return deliveryStatusName; }
    public void setDeliveryStatusName(String deliveryStatusName) { this.deliveryStatusName = deliveryStatusName; }


    // 추가 (이미지 조회용)
    public String getProductImage() {
  		return productImage;
  	}
  	public void setProductImage(String productImage) {
  		this.productImage = productImage;
  	}
  	
  	
  	
  	// ===== 클레임(취소/반품/교환) =====
    public String getClaimType() {
		return claimType;
	}
	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}
	public String getClaimStatus() {
		return claimStatus;
	}
	public void setClaimStatus(String claimStatus) {
		this.claimStatus = claimStatus;
	}
	public String getClaimReason() {
		return claimReason;
	}
	public void setClaimReason(String claimReason) {
		this.claimReason = claimReason;
	}
  	
   
}
