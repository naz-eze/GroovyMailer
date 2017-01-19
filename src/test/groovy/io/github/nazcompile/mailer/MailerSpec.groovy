package io.github.nazcompile.mailer;

import spock.lang.Specification

public class MailerSpec extends Specification {
	
	def "Should be able to configure Mailer"() {
		given:
			Mailer.configure {
				it.SMTP_HOST = 'smtp.domain.com'
				it.SMTP_PORT = '25'
			}
		expect:
			Mailer.PROPERTIES.get('mail.smtp.host') == 'smtp.domain.com'
			Mailer.PROPERTIES.get('mail.smtp.port') == '25'
			Mailer.PROPERTIES.get('mail.transport.protocol') == 'smtp'
	}

}
