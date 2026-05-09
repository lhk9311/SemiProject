package sp.common.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@WebServlet(
    description = "사용자가 웹에서 *.sp 을 했을 경우 이 서블릿이 응답을 해주도록 한다.",
    urlPatterns = { "*.sp" }
)
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 1,
    maxFileSize      = 1024 * 1024 * 10,
    maxRequestSize   = 1024 * 1024 * 50
)
public class FrontController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private Map<String, Object> cmdMap = new HashMap<>();

    @Override
    public void init(ServletConfig config) throws ServletException {

    	super.init(config);
    	
        // ✅ 핵심 수정: 하드코딩 경로 대신 getRealPath() 사용
        String props = getServletContext().getRealPath("/WEB-INF/Command.properties");

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(props);

            Properties pr = new Properties();
            pr.load(fis);

            Enumeration<Object> en = pr.keys();
            while (en.hasMoreElements()) {
                String key = (String) en.nextElement();
                String className = pr.getProperty(key);

                if (className != null) {
                    className = className.trim();
                    Class<?> cls = Class.forName(className);
                    Constructor<?> constrt = cls.getDeclaredConstructor();
                    Object obj = constrt.newInstance();
                    cmdMap.put(key, obj);
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println(">> 문자열로 명명되어진 클래스가 존재하지 않습니다. <<");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String uri = request.getRequestURI();

        if (uri.contains("/img/")) {
            return;
        }

        String key = uri.substring(request.getContextPath().length());
        AbstractController action = (AbstractController) cmdMap.get(key);

        if (action == null) {
            System.out.println(">>> " + key + " 은 URI 패턴에 매핑된 클래스는 없습니다. <<<");
        } else {
            try {
                action.execute(request, response);

                boolean bool = action.isRedirect();
                String viewPage = action.getViewPage();

                if (!bool) {
                    if (viewPage != null) {
                        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPage);
                        dispatcher.forward(request, response);
                    }
                } else {
                    if (viewPage != null) {
                        response.sendRedirect(viewPage);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}