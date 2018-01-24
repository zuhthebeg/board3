package spring.board.service;
import java.io.File;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.board.beans.Board;

import spring.board.controller.*;
import spring.board.model.BoardDao;
@Service
public class BoardService extends BoardController {
	@Autowired
	private BoardDao boardDao;
	
	@Override
	public ModelAndView listAction(@PathVariable Integer page) throws Exception {
		if(page == null){				 // �Ѿ�� �Ķ���Ͱ� �ִٸ� 	
			page =0;
		}			  // �ش� �Ķ���͸� int������ ĳ������ ������ �����մϴ�.
		ArrayList<Board> articleList = boardDao.getArticleList(page);			// �׸��� ����� dao �޼��忡 �־��ݴϴ�.
		ModelAndView mav = new ModelAndView("list");
		mav.addObject("articleList", articleList);	// ���õ� ����Ʈ�� �信 �������մϴ�.
		mav.addObject("page", page);// ��������ȣ�� �信�� �������� ǥ���մϴ�.
		return mav;
	}

	@Override
	public ModelAndView insertAction(@ModelAttribute Board article, HttpServletRequest request)
			throws Exception {
		MultipartFile file = article.getFile(); // ������ �ް�
		if(!file.isEmpty()){		// ������ �����ϸ� 
			String filename = file.getOriginalFilename();		//���Ͽ��� ���ε� ���� �̸��� �ް�
			File tempfile =new File(request.getRealPath("/upload"), file.getOriginalFilename());	//���� ������ 
			if(tempfile.exists() && tempfile.isFile()){	// �̹� �����ϴ� �����ϰ�� ����ð��� �����ͼ� ������
				filename =System.currentTimeMillis()  +"_"+ file.getOriginalFilename() ;
				tempfile = new File(request.getRealPath("/upload"),filename);	//�����ӵ� �����̸����� �����
			}
			file.transferTo(tempfile);	// ���ε� ���丮�� ���� �̵�
			article.setFilename(filename);	// �Խñۿ� ���ε�� �����̸� ���
		}
		article.setRegip(request.getRemoteAddr());
		article.setCount(0);
		
		boardDao.insertArticle(article);	
		return new ModelAndView("redirect:list/0");	// redirect: ��� ���ξ�� �ٷ� �����̷�Ʈ�� �����մϴ�.
	}

	@Override
	public ModelAndView ajaxListAction(Integer page) throws Exception {
		if(page == null){				 // �Ѿ�� �Ķ���Ͱ� ������
			page = 0;		//0���� �ʱ�ȭ
		}			 
			ArrayList<Board> articleList =boardDao.getArticleList(page);	
			return new ModelAndView("ajaxList", "articleList", articleList);
		}

	@Override
	public ModelAndView writeAction() throws Exception {
		return new ModelAndView("write");
	}

	@Override
	public ModelAndView countAction(Integer idx, HttpServletRequest request) throws Exception {
		Board article =boardDao.getArticle(idx);		// �Խñ� ��ü�� �����ɴϴ�.
		String regip = request.getRemoteAddr();	// ���� ��ȸ�� ��û�� ������� ip�� �ް�

		if(!regip.equals(article.getRegip())){			// �Խñ��� ip�� �������� ������
			int count = article.getCount();					// �Խñ��� ip�� �޾Ƽ� 
			article.setCount(++count);						// +1 ���ְ� �Խñۺ� �����մϴ�.
			boardDao.setArticleCount(article);	// ���� �� ���� �Ķ���ͷ� ������Ʈ�մϴ�.
		}
		return new ModelAndView("redirect:content","idx", idx);
	}

	@Override
	public ModelAndView contentAction(Integer idx) throws Exception {
		Board article = boardDao.getArticle(idx);	
		
		return new ModelAndView("content","article", article);
	}

	@Override
	public ModelAndView deleteAction(Integer idx, HttpServletRequest request) throws Exception {
		Board article = boardDao.getArticle(idx);
		String filename = article.getFilename();
		String uploadFileName = request.getRealPath("/upload") +"/"+ filename;

		File uploadfile = new File (uploadFileName);
		if ( uploadfile.exists()&& uploadfile.isFile() )
		{
			 uploadfile.delete();		// ���� ����
		}
		boardDao.deleteArticle(idx);
		return new ModelAndView("json", "status", "success");
	}

	@Override
	public ModelAndView downloadAction(@PathVariable Integer idx, HttpServletRequest request)
			throws Exception {
		Board article = boardDao.getArticle(idx);
		String filename = article.getFilename();
		
		String uploadFileName = request.getRealPath("/upload") +"/"+ filename;
		
		File downFile = new File(uploadFileName);
		return new ModelAndView("downloadView", "file", downFile);
	}

}
