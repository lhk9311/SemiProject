package hk.admin.controller;

import jakarta.servlet.http.*;
import sp.common.controller.AbstractController;
import hk.order.model.*;

public class AdminClaimApproveController extends AbstractController {

    private OrderDAO odao = new OrderDAO_imple();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        int odrDetailNo = Integer.parseInt(request.getParameter("odrdetailno"));
        String action = request.getParameter("action"); // APPROVE / REJECT

        //System.out.println("odrdetailno = " + odrDetailNo);
        //System.out.println("action = " + action); // 파라미터 제거. orderDetail만 있으면 됨
        
        odao.approveClaim(odrDetailNo);

        super.setRedirect(true);
        super.setViewPage(request.getContextPath() + "/admin/claimList.sp");
              
    }
}
