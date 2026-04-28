package hk.login.filter;

import java.io.IOException;

import hk.member.domain.MemberDTO;
import hk.member.model.MemberDAO;
import hk.member.model.MemberDAO_imple;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class RememberMeFilter implements Filter {

    private MemberDAO mdao = new MemberDAO_imple();

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        HttpSession session = request.getSession(false);

        // 이미 로그인 상태면 자동로그인 검사 필요 없음
        if (session == null || session.getAttribute("loginuser") == null) {

            String rememberToken = null;

            Cookie[] cookies = request.getCookies();

            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("rememberMe".equals(cookie.getName())) {
                        rememberToken = cookie.getValue();
                        break;
                    }
                }
            }

            if (rememberToken != null) {
                try {
                    MemberDTO loginuser = mdao.findUserByRememberMeToken(rememberToken);

                    if (loginuser != null) {
                        HttpSession newSession = request.getSession();
                        newSession.setAttribute("loginuser", loginuser);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        chain.doFilter(request, response);
    }
}