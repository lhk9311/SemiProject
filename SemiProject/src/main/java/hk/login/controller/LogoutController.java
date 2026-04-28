package hk.login.controller;

import hk.member.domain.MemberDTO;
import hk.member.model.MemberDAO;
import hk.member.model.MemberDAO_imple;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import sp.common.controller.AbstractController;

public class LogoutController extends AbstractController {

    private MemberDAO mdao = new MemberDAO_imple();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

    	// 세션 방식
        HttpSession session = request.getSession(false);

        if (session != null) {
            MemberDTO loginuser = (MemberDTO) session.getAttribute("loginuser");

            if (loginuser != null) {
                mdao.deleteRememberMeToken(loginuser.getUserid());
            }

            session.invalidate();
        }

        // remember me 방식
        Cookie rememberCookie = new Cookie("rememberMe", null);
        rememberCookie.setMaxAge(0);
        rememberCookie.setPath(request.getContextPath());
        response.addCookie(rememberCookie);

        super.setRedirect(true);
        super.setViewPage(request.getContextPath() + "/index.sp");
    }
}