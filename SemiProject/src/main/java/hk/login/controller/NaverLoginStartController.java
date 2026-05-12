package hk.login.controller;

import java.net.URLEncoder;
import java.util.UUID;

import sp.common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class NaverLoginStartController extends AbstractController {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String clientId = "PYBFkSdMo4dphDcHvPnj"; // 네이버ClientId

        // ★ 네이버 개발자센터에 등록한 Redirect URI 그대로
        // 배포
        String redirectURI = URLEncoder.encode(
        	    "http://158.247.198.57:8080/SemiProject/naverCallback.sp",
        	    "UTF-8"
        	);
        // 로컬
        //String redirectURI = URLEncoder.encode("http://localhost:9090/SemiProject/naverCallback.sp", "UTF-8");

        // state는 필수(보안)
        String state = UUID.randomUUID().toString();
        HttpSession session = request.getSession();
        session.setAttribute("NAVER_STATE", state);

        String apiURL = "https://nid.naver.com/oauth2.0/authorize"
                      + "?response_type=code"
                      + "&client_id=" + clientId
                      + "&redirect_uri=" + redirectURI
                      + "&state=" + state;

        super.setRedirect(true);
        super.setViewPage(apiURL);
    }
}
