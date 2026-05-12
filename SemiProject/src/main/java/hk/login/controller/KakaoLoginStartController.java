package hk.login.controller;

import java.net.URLEncoder;

import sp.common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class KakaoLoginStartController extends AbstractController {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String clientId = "b9bece0348ecbd39646aba63ecf1f7ea"; // 카카오REST_API_KEY

        // 배포
        String redirectURI =
        		"http://158.247.198.57:8080/SemiProject/kakaoCallback.sp";
        // 로컬
        // String redirectURI = URLEncoder.encode("http://localhost:9090/SemiProject/kakaoCallback.sp", "UTF-8");

        String apiURL = "https://kauth.kakao.com/oauth/authorize"
                      + "?response_type=code"
                      + "&client_id=" + clientId
                      + "&redirect_uri=" + redirectURI;

        super.setRedirect(true);
        super.setViewPage(apiURL);
    }
}
