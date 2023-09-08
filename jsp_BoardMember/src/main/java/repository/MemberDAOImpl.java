package repository;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import domain.MemberVO;
import orm.DatabaseBuilder;

public class MemberDAOImpl implements MemberDAO {
	private static final Logger log = LoggerFactory.getLogger(MemberDAOImpl.class);
	
	private SqlSession sql;
	private final String NS = "MemberMapper."; // namespace.id
	
	public MemberDAOImpl() {
		new DatabaseBuilder();
		sql = DatabaseBuilder.getFactory().openSession();
	}

	@Override
	public int insert(MemberVO mvo) {
		log.info("join check 3");
		int isOk = sql.insert(NS+"add", mvo);
		if(isOk > 0) {
			sql.commit();
		}
		return isOk;
	}

	@Override
	public MemberVO login(MemberVO mvo) {
		log.info("login check 3");
		return sql.selectOne(NS+"login", mvo);
	}

	@Override
	public int lastLogin(String id) {
		log.info("lastLogin check 3");
		int isOk = sql.update(NS+"last",id);
		if(isOk > 0) {
			sql.commit();
		}
		return isOk;
	}

	@Override
	public List<MemberVO> getList() {
		log.info("list check 3");
		return sql.selectList(NS+"list");
	}
}
