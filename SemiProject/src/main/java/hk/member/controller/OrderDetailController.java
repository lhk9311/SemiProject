package hk.member.controller;

import java.util.List;

import sp.common.controller.AbstractController;
import hk.order.domain.OrderDTO;
import hk.order.domain.OrderDetailDTO;
import hk.order.model.OrderDAO;
import hk.order.model.OrderDAO_imple;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import hk.member.domain.MemberDTO;

public class OrderDetailController extends AbstractController {

    private OrderDAO odao = new OrderDAO_imple();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        /* ===============================
         * 1. 로그인 여부 체크
         * =============================== */
        HttpSession session = request.getSession();
        MemberDTO loginuser = (MemberDTO) session.getAttribute("loginuser");

        if (loginuser == null) {
            request.setAttribute("message", "로그인이 필요합니다.");
            request.setAttribute("loc", request.getContextPath() + "/login.sp");

            super.setRedirect(false);
            super.setViewPage("/WEB-INF/msg.jsp");
            return;
        }

        /* ===============================
         * 2. 주문번호 파라미터 받기
         * =============================== */
        String odrCodeStr = request.getParameter("odrcode");

        if (odrCodeStr == null) {
            request.setAttribute("message", "잘못된 접근입니다.");
            request.setAttribute("loc", request.getContextPath() + "/shop/orderList.sp");

            super.setRedirect(false);
            super.setViewPage("/WEB-INF/msg.jsp");
            return;
        }

        int odrCode = Integer.parseInt(odrCodeStr);

     // ★ 주문 헤더(결제정보) 조회 추가
        OrderDTO orderInfo = odao.selectOrderInfo(odrCode);

        /* ===============================
         * 3. 주문 상세 조회
         * =============================== */
        List<OrderDetailDTO> detailList = odao.selectOrderDetail(odrCode);

        request.setAttribute("orderInfo", orderInfo);  // 주문헤더
        request.setAttribute("detailList", detailList); // 주문목록
        request.setAttribute("odrCode", odrCode); // 화면에 주문번호 표시 위함, 버튼 / 링크에 다시 사용 하기 위함

        /* ===============================
         * 4. JSP forward
         * =============================== */
        super.setRedirect(false);
        super.setViewPage("/WEB-INF/hk_member/orderDetail.jsp");
    }
}
