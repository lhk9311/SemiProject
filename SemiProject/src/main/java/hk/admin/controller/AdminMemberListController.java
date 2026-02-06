package hk.admin.controller;

import sp.common.controller.AbstractController;
import hk.member.domain.MemberDTO;
import hk.member.model.MemberDAO;
import hk.member.model.MemberDAO_imple;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.util.List;

public class AdminMemberListController extends AbstractController {

    private MemberDAO mdao = new MemberDAO_imple();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        /* ===============================
         * 1. 관리자 로그인 체크
         * =============================== */
        HttpSession session = request.getSession();
        MemberDTO loginuser = (MemberDTO) session.getAttribute("loginuser");

        if (loginuser == null || !"admin".equals(loginuser.getUserid())) {
            request.setAttribute("message", "관리자만 접근할 수 있습니다.");
            request.setAttribute("loc", request.getContextPath() + "/index.sp");
            super.setRedirect(false);
            super.setViewPage("/WEB-INF/msg.jsp");
            return;
        }

        /* ===============================
         * 2. 검색 파라미터 
         * =============================== */
        String searchType = request.getParameter("searchType");
        String searchWord = request.getParameter("searchWord");

        List<MemberDTO> memberList;

        /* ===============================
         * 3. 검색 vs 전체목록 분기
         * =============================== */
        if (searchType != null && searchWord != null && !"".equals(searchWord.trim())) {

            // 검색
            memberList = mdao.selectMemberBySearch(searchType, searchWord.trim());

        }
        else {
            // 전체 목록 → 페이징

            int currentPage = 1;
            String pageNo = request.getParameter("pageNo");
            if (pageNo != null) {
                currentPage = Integer.parseInt(pageNo);
            }

            int sizePerPage = 20; // 한 페이지당 20명

            int totalCount = mdao.getTotalMemberCount();
            int totalPage = (int)Math.ceil((double)totalCount / sizePerPage);

            int startRno = (currentPage - 1) * sizePerPage + 1;
            int endRno   = startRno + sizePerPage - 1;

            memberList = mdao.selectAllMemberForAdmin(startRno, endRno);

            // 페이지바용
            request.setAttribute("currentPage", currentPage);
            request.setAttribute("totalPage", totalPage);
        }

        /* ===============================
         * 4. JSP로 전달
         * =============================== */
        request.setAttribute("memberList", memberList);

        super.setRedirect(false);
        super.setViewPage("/WEB-INF/hk_admin/memberList.jsp");
    }
}
