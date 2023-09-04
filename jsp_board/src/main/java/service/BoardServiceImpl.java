package service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dao.BoardDAO;
import dao.BoardDAOImpl;

public class BoardServiceImpl implements BoardService {
	
	private static final Logger log = LoggerFactory.getLogger(BoardServiceImpl.class);
	private BoardDAO bdao; // dao => interface 생성
	
	public BoardServiceImpl() {
		bdao = new BoardDAOImpl(); // dao => class로 생성
	}

}
