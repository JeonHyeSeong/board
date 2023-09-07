package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import domain.BoardVO;
import service.BoardServiceImpl;
import service.Service;

@WebServlet("/brd/*")
public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	// 로그 객체 선언
	private static final Logger log = LoggerFactory.getLogger(BoardController.class);
	// requestDespatcher 객체
	private RequestDispatcher rdp;
	// service interface
	private Service bsv;
	// destPage : 목적지 주소 저장변수
	private String destPage;
	// isOk
	private int isOk;
       
    public BoardController() {
        // bsv의 객체 연결
    	bsv = new BoardServiceImpl();
    }

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// encoding 설정, contentType 설정, 요청경로 파악
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=UTF-8");
		
		// jsp에서 오는 요청 주소
		String uri = request.getRequestURI(); // /brd/register
		String path = uri.substring(uri.lastIndexOf("/")+1);
		log.info("path>>>>>"+path);
		
		switch(path) {
		case "register" :
			destPage = "/board/register.jsp";
			log.info("register check");
			break;
		case "insert" :
			try {
				String title = request.getParameter("title");
				String writer = request.getParameter("writer");
				String content = request.getParameter("content");
				log.info("insert check 1");
				BoardVO bvo = new BoardVO(title, writer, content);
				isOk = bsv.register(bvo);
				log.info((isOk > 0)? "Ok" : "Fail");
				destPage = "/index.jsp";
			} catch (Exception e) {
				e.printStackTrace();
				log.info("insert error");
			}
			break;
		case "list" :
			try {
				log.info("list check 1");
				List<BoardVO> list = bsv.getList();
				log.info("list check 4");
				request.setAttribute("list", list);
				destPage = "/board/list.jsp";
			} catch (Exception e) {
				e.printStackTrace();
				log.info("list error");
			}
			break;
		case "detail" :
			try {
				int bno = Integer.parseInt(request.getParameter("bno"));
				log.info("detail check 1");
				BoardVO bvo = bsv.getDetail(bno);
				log.info("detail check 4");
				request.setAttribute("bvo", bvo);
				destPage = "/board/detail.jsp";
			} catch (Exception e) {
				e.printStackTrace();
				log.info("detail error");
			}
			break;
		case "modify" :
			try {
				int bno = Integer.parseInt(request.getParameter("bno"));
				log.info("modify check 1");
				BoardVO bvo = bsv.getDetail(bno);
				request.setAttribute("bvo", bvo);
				destPage = "/board/modify.jsp";
			} catch (Exception e) {
				e.printStackTrace();
				log.info("modify error");
			}
			break;
		case "edit" :
			try {
				int bno = Integer.parseInt(request.getParameter("bno"));
				String title = request.getParameter("title");
				String content = request.getParameter("content");
				log.info("edit check 1");
				BoardVO bvo = new BoardVO(bno, title, content);
				isOk = bsv.modify(bvo);
				log.info((isOk > 0)? "Ok" : "Fail");
				destPage = "detail?bno="+bno;
			} catch (Exception e) {
				e.printStackTrace();
				log.info("edit error");
			}
			break;
		case "remove" :
			try {
				int bno = Integer.parseInt(request.getParameter("bno"));
				log.info("remove check 1");
				isOk = bsv.remove(bno);
				log.info((isOk > 0)? "Ok" : "Fail");
				destPage = "list";
			} catch (Exception e) {
				e.printStackTrace();
				log.info("remove error");
			}
			break;
		}
		rdp = request.getRequestDispatcher(destPage);
		rdp.forward(request, response);
		
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		service(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		service(request, response);
	}

}
