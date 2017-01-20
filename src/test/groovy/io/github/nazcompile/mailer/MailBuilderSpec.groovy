package io.github.nazcompile.mailer

import spock.lang.Specification

class MailBuilderSpec extends Specification {

	def "Should be able to build recipients email"() {
		when:
			Mail mail = new MailBuilder().build {
				to {
					email 'naz@domail.com'
					email 'zan@domain.com'
				}
				cc { email 'someone@domain.com' }
				bcc { email 'anybody@domain.com' }
			}

		then:
			mail.getTo() == [
				'naz@domail.com',
				'zan@domain.com'
			]
			mail.getCc() == ['someone@domain.com']
			mail.getBcc() == ['anybody@domain.com']
	}

	def "Should through IllegalStateException if email() is used incorrectly"() {
		when:
			new MailBuilder().build { email 'wrongplace@domain.com' }
		then:
			thrown(IllegalStateException)
	}
	
	def "Should be able to build mail object with sender's email"() {
		when:
			Mail mail = new MailBuilder().build {
				from 'noreply@domain.com'
			}
		then:
			mail.getFrom() == 'noreply@domain.com'
	}
	
	def "Should be able to build mail object with any attachments"() {
		when:
			Mail mail = new MailBuilder().build {
				attachment {
					name 'somefile.pdf'
					name 'random.txt'
				}
			}
		then:
			mail.getAttachments() == ['somefile.pdf', 'random.txt']
	}
	
	def "Should through IllegalStateException if name() is used incorrectly"() {
		when:
			new MailBuilder().build { name 'wrongplace.doc' }
		then:
			thrown(IllegalStateException)
	}
	
	def "Should be able to build mail object with email subject"() {
		when:
			Mail mail = new MailBuilder().build {
				subject 'This is a test'
			}
		then:
			mail.getSubject() == 'This is a test'
	}

}
