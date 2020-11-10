package codeSnippets;

import java.util.function.Consumer;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

// venkat https://www.youtube.com/watch?v=e4MT_OguDKg
public class MailerSample {

	public static void main(String[] args) {
		// Mailer m = new Mailer();
		// m.from("humpert2017@ardistan.de");
		// m.to("humpert_anfrage@ardistan.de");
		// m.subject("öööööö");
		// m.body("llllll");
		// m.send();

		// 2
		// new Mailer().from("humpert2017@ardistan.de")
		// .to("humpert_anfrage@ardistan.de")
		// .subject("öööööö")
		// .body("llllll")
		// .send();
		// 3

		Mailer.send(mailer -> mailer.from("humpert2017@ardistan.de")
									.to("humpert_anfrage@ardistan.de")
									.subject("öööööö")
									.body("llllll"));
	}

}

class Mailer {
	public Mailer print(String s) {
		return this;
	}

	public Mailer from(String s) {
		return this;

	}

	public Mailer to(String s) {
		return this;

	}

	public Mailer subject(String s) {
		return this;

	}

	public Mailer body(String s) {
		return this;

	}

	public static void send(Consumer<Mailer> c) {
		Mailer m = new Mailer();
		c.accept(m);
		System.out.println("sending");
		
		
//		 try {
//	         Message message = new MimeMessage(session);
//	         message.setFrom(new InternetAddress(from));
//	         message.setRecipients(Message.RecipientType.TO,
//	            InternetAddress.parse(to));
//	         message.setSubject(subject);
//
//	         BodyPart part1=new MimeBodyPart();
//	         part1.setText(msg);
//
//
//	         File file=new File("/home/mano/temp/Myfile.doc");
//	         BodyPart part2=new MimeBodyPart();
//	         DataSource attachment=new FileDataSource(file);
//	         part2.setDataHandler(new DataHandler(attachment));
//	         part2.setFileName(file.getName());
//
//
//	         Multipart multiPart=new MimeMultipart();
//	         multiPart.addBodyPart(part1);
//	         multiPart.addBodyPart(part2);
//
//	         message.setContent(multiPart);
//
//	         Transport.send(message);
//	         System.out.println("Message with attachment	            sent successfully....");
//	      } catch (MessagingException e) {
//	         throw new RuntimeException(e);
//	      }
		
		
	}
}
