package io.github.nazcompile.mailer;

import javax.mail.Multipart
import javax.mail.Session

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
		when:
			Multipart multiPart = mail.createMessageBody()
		then:
			multiPart.getBodyPart(0).getContent() == mail.getMessageContent()
			multiPart.getBodyPart(0).getContentType() == mail.getMessageType()			
	}

}
