package io.github.nazcompile.mailer;

import javax.activation.DataHandler
import javax.mail.Message
import javax.mail.Multipart
import javax.mail.Session
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage

import spock.lang.Specification

public class MailSpec extends Specification {
	
	Mail mail
	
	def setup() {
		mail = new Mail()
	}
	
	def "Should be able to configure Mail"() {
		given:
			Mail.configure {
				it.SMTP_HOST = 'smtp.domain.com'
				it.SMTP_PORT = '25'
			}
		expect:
			Mail.PROPERTIES.get('mail.smtp.host') == 'smtp.domain.com'
			Mail.PROPERTIES.get('mail.smtp.port') == '25'
			Mail.PROPERTIES.get('mail.transport.protocol') == 'smtp'
	}
	
	def "Should be able to create a Mail Session"() {
		setup:
			def seesionMock = GroovyMock(Session, global: true)
			def propsMock = Mock(Properties)
		when:
			def result = mail.getSession(propsMock)
		then:
			1 * Session.getDefaultInstance(propsMock) >> seesionMock
	}
	
	def "Should be able to create message body"() {
		given:
			mail.messageContent = "Hello World"
			mail.messageType = "text/plain"
			mail.attachments = ["one_attachment.pdf"]
		when:
			Multipart multiPart = mail.createMessageBody()
		then:
			multiPart.getBodyPart(0).getContent() == mail.getMessageContent()
			multiPart.getBodyPart(0).getContentType() == mail.getMessageType()		
			multiPart.getBodyPart(1).getFileName() == "one_attachment.pdf"
		and:
			multiPart.getCount() == 2
	}
	
	def "Should not add attachement if empty when creating message body"() {
		when:
			Multipart multiPart = mail.createMessageBody()
		then:
			multiPart.getCount() == 1
	}
	
	def "Should call all methods required to add an attachment"() {
		setup:
			MimeBodyPart mimeBodyPartSpy = Spy()
			Multipart multiPartSpy = Spy()
			File mockFile = Mock()
			mockFile.getName() >> "somefile.txt"
			
		when:
			mail.addAttachement(mimeBodyPartSpy, multiPartSpy, mockFile)
		
		then:
			1 * mimeBodyPartSpy.setDataHandler(_ as DataHandler)
			1 * mimeBodyPartSpy.setFileName("somefile.txt")
			1 * multiPartSpy.addBodyPart(_ as MimeBodyPart)
	}
	
	def "Should be able to set TO recipients if they exists"() {
		given:
			Mail mailSpy = Spy()
			MimeMessage message = Mock()
		when:
			mailSpy.to = ['test@domain.com']
			mailSpy.createRecipients(message)
		then:
			1 * mailSpy.setRecipients(message, Message.RecipientType.TO, ['test@domain.com']) 
		when:
			mailSpy.to = []
			mailSpy.createRecipients(message)
		then:
			0 * mailSpy.setRecipients(message, Message.RecipientType.TO, _ as List)
	}
	
	def "Should be able to set CC recipients if they exists"() {
		given:
			Mail mailSpy = Spy()
			MimeMessage message = Mock()
		when:
			mailSpy.cc = ['cc@domain.com']
			mailSpy.createRecipients(message)
		then:
			1 * mailSpy.setRecipients(message, Message.RecipientType.CC, ['cc@domain.com'])
		when:
			mailSpy.cc = []
			mailSpy.createRecipients(message)
		then:
			0 * mailSpy.setRecipients(message, Message.RecipientType.CC, _ as List)
	}
	
	def "Should be able to set BCC recipients if they exists"() {
		given:
			Mail mailSpy = Spy()
			MimeMessage message = Mock()
		when:
			mailSpy.bcc = ['bcc@domain.com']
			mailSpy.createRecipients(message)
		then:
			1 * mailSpy.setRecipients(message, Message.RecipientType.BCC, ['bcc@domain.com'])
		when:
			mailSpy.bcc = []
			mailSpy.createRecipients(message)
		then:
			0 * mailSpy.setRecipients(message, Message.RecipientType.BCC, _ as List)
	}
	
	def "Should be able to construct mail message"() {
		given:
			def seesionMock = GroovyMock(Session, global: true)
			mail.getSession(_ as Properties) >> seesionMock
			
			mail.from = "sender@domain.com"
			mail.to = ["recipient@domain.com"]
			mail.cc = ["cc1@domain.com", "cc2@domain.com"]
			mail.bcc = ["bcc@domain.com"]
			mail.subject = "Test"
			mail.messageContent = "<p> The email content </p>"
			mail.messageType = "text/html"
			
		when:
			MimeMessage message = mail.constructMail()
			
		then:
			message.getFrom()[0] == new InternetAddress("sender@domain.com")
			message.getRecipients(Message.RecipientType.TO)[0] == new InternetAddress("recipient@domain.com")
			message.getRecipients(Message.RecipientType.CC)[1] == new InternetAddress("cc2@domain.com")
			message.getRecipients(Message.RecipientType.BCC)[0] == new InternetAddress("bcc@domain.com")
			
			message.getSubject() == "Test"
			message.getContent().getBodyPart(0).getContent() == mail.getMessageContent()
			message.getContent().getBodyPart(0).getContentType() == "text/html; charset=us-ascii" //default charset
			
			message.getSentDate() != null
	}
}
