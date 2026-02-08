package hk.admin.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import sp.common.controller.AbstractController;
import hk.order.model.OrderDAO;
import hk.order.model.OrderDAO_imple;

public class AdminClaimCompleteController extends AbstractController {

    private OrderDAO odao = new OrderDAO_imple();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        int odrDetailNo = Integer.parseInt(request.getParameter("odrdetailno"));

        int result = odao.completeClaim(odrDetailNo);

        // 세션 추가!! (얼럿 메시지 출력용)
        HttpSession session = request.getSession();

        if (result == 1) {
            session.setAttribute("alertMsg", "클레임이 처리완료되었습니다.");
        } else {
            session.setAttribute("alertMsg", "클레임 처리 중 오류가 발생했습니다.");
        }

        super.setRedirect(true);
        super.setViewPage(request.getContextPath() + "/admin/claimList.sp");
    }
}
