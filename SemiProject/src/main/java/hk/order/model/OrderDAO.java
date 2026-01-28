package hk.order.model;

import java.sql.SQLException;
import java.util.List;
import hk.order.domain.OrderDTO;
import hk.order.domain.OrderDetailDTO;

public interface OrderDAO {

    // 마이페이지 주문 목록 (주문처리상태, 기간 필터, 페이징 처리)
	List<OrderDTO> selectMyOrderList(String memberId, String status, String startDate, String endDate, int startRow, int endRow) throws SQLException;

	// 주문 총 건수 (페이지바용)
    int getOrderTotalCount(String userid, String status, String startDate, String endDate) throws SQLException;
	
    // 주문 상세
    List<OrderDetailDTO> selectOrderDetail(int odrCode) throws SQLException;

	// 주문 상세 내 결제 정보 출력용
	OrderDTO selectOrderInfo(int odrCode) throws SQLException;
    
    // 주문취소/반품/교환 팝업창 상품 목록 조회
	List<OrderDetailDTO> getOrderDetailList(int odrcode) throws SQLException;

	// 주문취소/반품/교환 신청
	int requestClaim(int odrDetailNo, String type, String reason) throws SQLException;

	// 주문취소/반품/교환 신청 요청목록 조회
	List<OrderDetailDTO> getClaimList() throws SQLException;

	// 처리대기 건수 표시용
	int getPendingClaimCount() throws SQLException;
	
	// 주문취소/교환/반품 신청 후 관리자 승인용
	public int approveClaim(int odrDetailNo, String action) throws SQLException;

	// 반려 사유 창 주문내역 조회용
	OrderDetailDTO getClaimDetail(int odrDetailNo) throws SQLException;

	// 주문취소/교환/반품 신청 후 관리자 반려용
	int rejectClaim(int odrDetailNo, String rejectReason) throws SQLException;

	// 주문취소/교환/반품 신청 후 관리자 승인 -> 처리대기 -> 처리완료
	int completeClaim(int odrDetailNo) throws SQLException;



}
