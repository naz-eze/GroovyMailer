package io.github.nazcompile.mailer

import spock.lang.Specification

class MailerBuilderSpec extends Specification {

	def "Should be able to build recipients email"() {
		when:
		Mail mailer = new MailerBuilder().build {
				to {
					email 'naz@domail.com'
					email 'zan@domain.com'
				}
				cc {
					email 'someone@domain.com'
				}
				bcc {
					email 'anybody@domain.com'
				}
			}
			
		then:
			mailer.getTo() == ['naz@domail.com', 'zan@domain.com']
			mailer.getCc() == ['someone@domain.com']
			mailer.getBcc() == ['anybody@domain.com']
	}
	
	def "Should through IllegalStateException if email() is used incorrectly"() {
		when:
			new MailerBuilder().build {
				email 'wrongplace@domain.com'
			}
		then:
			thrown(IllegalStateException)
	}
}
