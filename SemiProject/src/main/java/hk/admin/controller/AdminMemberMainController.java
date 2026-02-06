package hk.admin.controller;

import sp.common.controller.AbstractController;

import java.util.List;
import java.util.Map;

import hk.member.domain.MemberCountDTO;
import hk.member.domain.MemberDTO;
import hk.member.model.MemberDAO;
import hk.member.model.MemberDAO_imple;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class AdminMemberMainController extends AbstractController {

    private MemberDAO mdao = new MemberDAO_imple();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        /* ==================================================
         * 1. 관리자 로그인 / 권한 체크
         * ================================================== */
        HttpSession session = request.getSession();
        MemberDTO loginuser = (MemberDTO) session.getAttribute("loginuser");

        if (loginuser == null || !"admin".equals(loginuser.getUserid())) {
            request.setAttribute("message", "관리자만 접근할 수 있습니다.");
            request.setAttribute("loc", request.getContextPath() + "/index.sp");

            super.setRedirect(false);
            super.setViewPage("/WEB-INF/msg.jsp");
            return;
        }

        
        /* ==================================================
         * 2. 회원 요약 데이터 조회
         * ================================================== */
        int totalCount  = mdao.getTotalMemberCount();     // 전체 회원 수
        int activeCount = mdao.getActiveMemberCount();    // 정상 회원
        int deleteCount = mdao.getDeleteMemberCount();    // 탈퇴 회원
        int idleCount = mdao.getIdleMemberCount();        // 휴면 회원 수
        
        /* ==================================================
         * 2-1. 오늘 회원가입 수 조회
         * ================================================== */
        int todayRegisterCount = mdao.getTodayRegisterCount();
        
        
        /* ==================================================
         * 2-2. 최근 가입 회원 TOP 5
         * ================================================== */
        List<MemberDTO> recentMemberList = mdao.getRecentMemberList(5);
       
                
        /* ==================================================
         * 2-3. 최근 7일 가입 / 탈퇴율
         * ================================================== */
        int last7daysCount = mdao.getLast7DaysRegisterCount();
        
        // 그래프 구현 위해 추가
       
        List<Map<String, Object>> last7daysList = mdao.getLast7DaysRegisterList();
        
        // 탈퇴율 (컨트롤러 안 만들고 여기서만 처리함)
        /* 탈퇴율 계산 (0으로 나누기 방지) */
        double withdrawRate = 0.0;
        if (totalCount > 0) {
            withdrawRate = (double) deleteCount / totalCount * 100;
        }

        /* 소수점 한 자리 반올림 */
        withdrawRate = Math.round(withdrawRate * 10) / 10.0;
        
        
        /* ==================================================
         * 2-4. 등급별 회원수/ 성별 회원수
         * ================================================== */
        List<MemberCountDTO> gradeCountList = mdao.getGradeCountList();
        List<MemberCountDTO> genderCountList = mdao.getGenderCountList();
        
        
        /* ==================================================
         * 3. JSP로 전달
         * ================================================== */
        request.setAttribute("totalCount", totalCount);
        request.setAttribute("activeCount", activeCount);
        request.setAttribute("deleteCount", deleteCount);
        request.setAttribute("todayRegisterCount", todayRegisterCount);
        request.setAttribute("last7daysCount", last7daysCount);
        
        request.setAttribute("last7daysList", last7daysList);
        
        request.setAttribute("withdrawRate", withdrawRate);
        request.setAttribute("recentMemberList", recentMemberList);
        request.setAttribute("gradeCountList", gradeCountList);
        request.setAttribute("genderCountList", genderCountList);
        
        request.setAttribute("idleCount", idleCount);
        
        /* ==================================================
         * 4. 회원관리 대시보드 JSP 이동
         * ================================================== */
        super.setRedirect(false);
        super.setViewPage("/WEB-INF/hk_admin/memberMain.jsp");
    }
}
