package spring.board.controller;

import java.lang.reflect.Method;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.board.beans.Email;
import com.board.beans.EmailSender;
import com.board.beans.Member;

@Controller
@RequestMapping("/email")
public class EmailController {
	@Autowired
	private EmailSender emailSender;

	@RequestMapping("/send")
	public ModelAndView sendEmailAction() throws Exception {
		Member user = new Member();
		user.setEmail("user@email.com");
		user.setPhone("01000001111");
		user.setDepartment("mydepartment");

		Email email = new Email();

		String reciver = user.getEmail();
		String subject = "";// getEmailTemplateSubjectFromDB();
		String content = "";// getEmailTemplateContentFromDB();

		Class cls = user.getClass();
		Method[] invokeMethod = cls.getMethods();
		for (int i = 0; i < invokeMethod.length; i++) {
			if (invokeMethod[i].getName().contains("get")
					&& invokeMethod[i].invoke(user, null) != null) {
				content = content.replaceAll(invokeMethod[i].getName(),
						invokeMethod[i].invoke(user, null).toString());
				subject = subject.replaceAll(invokeMethod[i].getName(),
						invokeMethod[i].invoke(user, null).toString());
			}
		}

		email.setReciver(user.getEmail());
		email.setSubject(subject);
		email.setContent(content);
		emailSender.SendEmail(email);

		return new ModelAndView("success");
	}
}