package hk.admin.controller;

import jakarta.servlet.http.*;
import sp.common.controller.AbstractController;
import hk.order.model.*;

public class AdminClaimApproveController extends AbstractController {

    private OrderDAO odao = new OrderDAO_imple();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        int odrDetailNo = Integer.parseInt(request.getParameter("odrdetailno"));

        odao.approveClaim(odrDetailNo);

        // redirect용 메시지는 session에 저장
        HttpSession session = request.getSession();
        session.setAttribute("alertMsg", "클레임이 승인되었습니다.");

        super.setRedirect(true);
        super.setViewPage(request.getContextPath() + "/admin/claimList.sp");
    }

}
