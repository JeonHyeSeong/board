package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import domain.MemberVO;
import service.MemberService;
import service.MemberServiceImpl;

@WebServlet("/mem/*")
public class MemberController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = LoggerFactory.getLogger(MemberController.class);
	
	private RequestDispatcher rdp;
	private String destPage;
	private int isOk;
	
	private MemberService msv; // service -> interface 생성
       
    public MemberController() {
        msv = new MemberServiceImpl(); // service -> class 생성
    }

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=UTF-8");
		
		String uri = request.getRequestURI();
		String path = uri.substring(uri.lastIndexOf("/")+1);
		log.info("path>>>>"+path);
		
		switch(path) {
		case "join" :
			log.info("회원가입 페이지 열기");
			destPage = "/member/join.jsp";
			break;
		case "register" : // 회원가입 DB저장
			try {
				// jsp에서 보낸 파라미터 받기
				String id = request.getParameter("id");
				String pwd = request.getParameter("pwd");
				String email = request.getParameter("email");
				int age = Integer.parseInt(request.getParameter("age"));
				MemberVO mvo = new MemberVO(id, pwd, email, age);
				log.info(">>> join > check 1"+mvo);
				isOk = msv.register(mvo);
				log.info("check 4 "+((isOk > 0)? "OK" : "Fail"));
				destPage = "/index.jsp";
			} catch (Exception e) {
				e.printStackTrace();
				log.info("register error");
			}
			break;
		case "login" : // 로그인이 일어나는 케이스
			try {
				// 파라미터가 DB의 값에 있는지 확인
				// 해당 id, pwd가 일치하는 데이터를 가져오기
				// 가져온 데이터를 세션에 저장
				// session : 모든 jsp 페이지에 공유되는 데이터
				// 아이디가 없다면, 메세지를 보내서 alert 창 띄우기
				String id = request.getParameter("id");
				String pwd = request.getParameter("pwd");
				MemberVO mvo = new MemberVO(id, pwd);
				
				MemberVO loginmvo = msv.login(mvo);
				log.info("login check 1 >>> "+loginmvo);
				// 가져온 데이터를 세션에 저장
				// 세션 가져오기
				if(loginmvo != null) {
					// 연결된 세션이 있다면 기존의 세션 가져오기, 없으면 새로 생성
					HttpSession ses = request.getSession();
					ses.setAttribute("ses", loginmvo);
					ses.setMaxInactiveInterval(10*60); // 초단위로 설정(로그인 유지시간)
				}else {
					// 로그인 객체가 없다면...
					request.setAttribute("msg_login", 0);
				}
				destPage = "/index.jsp";
			} catch (Exception e) {
				e.printStackTrace();
				log.info("login error");
			}
			break;
		case "logout" :
			try {
				// 연결된 세션이 있다면 해당 세션을 가져오기
				HttpSession ses = request.getSession(); // 로그인한 세션
				// lastlogin 시간 기록, id가 필요
				// session에서 id 가져오기
				MemberVO mvo = (MemberVO)ses.getAttribute("ses");
				String id = mvo.getId();
				log.info("ses에서 id 추출 >>> "+id);
				isOk = msv.lastLogin(id);
				ses.invalidate();
				log.info("logout >> "+((isOk > 0)? "OK" : "Fail"));
				destPage = "/index.jsp";
			} catch (Exception e) {
				e.printStackTrace();
				log.info("logout error");
			}
			break;
		case "list" :
			try {
				List<MemberVO> list = msv.getList();
				request.setAttribute("list", list);
				destPage = "/member/list.jsp";
			} catch (Exception e) {
				e.printStackTrace();
				log.info("list error");
			}
			break;
		}
		// 목적지 주소값 셋팅
		rdp = request.getRequestDispatcher(destPage);
		// 정보 실어 보내기
		rdp.forward(request, response);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		service(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		service(request, response);
	}

}
