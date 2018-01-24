package spring.board.model;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.annotation.Resource;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Repository;

import com.board.beans.Board;
import com.ibatis.sqlmap.client.SqlMapClient;

@Repository
public class BoardDao extends SqlMapClientDaoSupport{
	
	@Resource(name="sqlMapClient")
	public void setSuperSqlMapClient(SqlMapClient sqlMapClient){
		super.setSqlMapClient(sqlMapClient);
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Board> getArticleList(int page) throws SQLException  {
		return  (ArrayList<Board>)getSqlMapClient().queryForList("getArticleList", null, page, 10 );
	}

	public Board getArticle(int idx) throws SQLException {
		return  (Board)getSqlMapClient().queryForObject("getArticle", idx );
	}

	public void deleteArticle(int idx) throws SQLException {
		getSqlMapClient().delete("deleteArticle", idx);
	}

	public void insertArticle(Board article) throws SQLException {
		getSqlMapClient().insert("insertArticle", article);
		
	}

	public void setArticleCount(Board article) throws SQLException {
		getSqlMapClient().update("setArticleCount", article);
	}
	
	
}
