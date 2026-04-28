package hk.login.controller;

import java.util.HashMap;
import java.util.Map;

import sp.common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import hk.member.domain.MemberDTO;
import hk.member.model.MemberDAO;
import hk.member.model.MemberDAO_imple;

public class LoginController extends AbstractController {

	private MemberDAO mdao = new MemberDAO_imple();

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String method = request.getMethod();

		/*
		 * ===============================
		 * GET : 로그인 페이지 보여주기
		 * ===============================
		 */
		if ("GET".equalsIgnoreCase(method)) {

			String mode = request.getParameter("mode");

			// 관리자 로그인 화면
			if ("admin".equals(mode)) {
				super.setRedirect(false);
				super.setViewPage("/WEB-INF/hk_login/adminLogin.jsp");
			}
			// 일반 로그인 화면
			else {
				super.setRedirect(false);
				super.setViewPage("/WEB-INF/hk_login/login.jsp");
			}
			return;
		}

		/*
		 * ===============================
		 * POST : 로그인 처리
		 * ===============================
		 */
		else {

			String userid = request.getParameter("userid");   // name="userid"
			String passwd = request.getParameter("passwd");   // pwd → passwd 변경
			String mode   = request.getParameter("mode");     // 추가
			
			// 실패시 돌아갈 주소
		    String failLoc = request.getContextPath() + "/login.sp";
		    if ("admin".equals(mode)) {
		        failLoc = request.getContextPath() + "/login.sp?mode=admin";
		    }
			
			// Map 방식으로 DAO에 전달
			Map<String, String> paraMap = new HashMap<>();
			paraMap.put("userid", userid);
			paraMap.put("passwd", passwd);                    // 변경

			MemberDTO loginuser = mdao.login(paraMap);

			/*
			 * System.out.println("로그인 idle=" + loginuser.getIdle() + ", status=" +
			 * loginuser.getStatus() + ", userid=" + loginuser.getUserid());
			 * 
			 */
			
			if (loginuser == null) {
				request.setAttribute("message", "아이디 또는 비밀번호가 틀렸습니다.");
				request.setAttribute("loc", failLoc);         // 변경

				super.setRedirect(false);
				super.setViewPage("/WEB-INF/msg.jsp");
			}
			else {
				// 탈퇴회원
				if (loginuser.getStatus() == 0) {
					request.setAttribute("message", "탈퇴한 회원입니다.");
					request.setAttribute("loc", failLoc);

					super.setRedirect(false);
					super.setViewPage("/WEB-INF/msg.jsp");
					return;
				}

				// 휴면회원
				if (loginuser.getIdle() == 1) {
				    request.setAttribute("userid", userid);
				    super.setRedirect(false);
				    super.setViewPage("/WEB-INF/hk_login/idleAccount.jsp");
				    return;
				}

				// 로그인 성공 (정상회원)
				HttpSession session = request.getSession();
				session.setAttribute("loginuser", loginuser);
				
				// 추가 (remember me)
				String rememberMe = request.getParameter("rememberMe");

				if ("Y".equals(rememberMe)) {
				    String token = java.util.UUID.randomUUID().toString();

				    // DB에 userid, token, expire_at 저장
				    mdao.saveRememberMeToken(loginuser.getUserid(), token);

				    jakarta.servlet.http.Cookie rememberCookie =
				        new jakarta.servlet.http.Cookie("rememberMe", token);

				    rememberCookie.setMaxAge(60 * 60 * 24 * 7); // 7일
				    rememberCookie.setHttpOnly(true);
				    rememberCookie.setPath(request.getContextPath());

				    response.addCookie(rememberCookie);
				}

				// ===============================
				// 관리자 / 일반회원 분기
				// ===============================
				if ("admin".equals(loginuser.getUserid())) {
					// 관리자 로그인
					super.setRedirect(true);
					super.setViewPage(request.getContextPath() + "/admin.sp");
				}
				else {
					// 일반 회원 로그인
					super.setRedirect(true);
					super.setViewPage(request.getContextPath() + "/mypage.sp");
				}
			}
		}
	}
}