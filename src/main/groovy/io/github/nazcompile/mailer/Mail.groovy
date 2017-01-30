package io.github.nazcompile.mailer

import javax.activation.DataHandler
import javax.activation.DataSource
import javax.activation.FileDataSource
import javax.mail.Address
import javax.mail.Message
import javax.mail.Multipart
import javax.mail.Session
import javax.mail.Message.RecipientType
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart

class Mail {

	public static def SMTP_HOST
	public static def SMTP_PORT
	private static def TRANSPORT_TYPE = 'smtp'

	private static Properties PROPERTIES

	String from = ''
	List<String> to = []
	List<String> cc = []
	List<String> bcc = []
	List<String> attachments = []
	String subject = ''
	String messageContent = ''
	String messageType = ''


	public static def configure(Closure closure) {
		closure(this)

		PROPERTIES = new Properties()
		PROPERTIES.put('mail.smtp.host', SMTP_HOST)
		PROPERTIES.put('mail.smtp.port', SMTP_PORT)
		PROPERTIES.put('mail.transport.protocol', TRANSPORT_TYPE)
	}

	def getSession(Properties props) {
		def session = Session.getDefaultInstance(props)
		session
	}

	private def createMessageBody() {
		MimeBodyPart bodyPart = new MimeBodyPart()
		bodyPart.setHeader("Content-Type", messageType)
		bodyPart.setContent(messageContent, messageType)

		Multipart multiPart = new MimeMultipart()
		multiPart.addBodyPart(bodyPart)

		if (attachments.size() > 0) {
			attachments.each { String fileName ->
				bodyPart = new MimeBodyPart()
				addAttachement(bodyPart, multiPart, new File(fileName))
			}
		}
		
		multiPart
	}
	
	private def addAttachement(MimeBodyPart bodyPart, Multipart multiPart, File attachment) {
		DataSource source = new FileDataSource(attachment)
		bodyPart.setDataHandler(new DataHandler(source))
		bodyPart.setFileName(attachment.getName())
		multiPart.addBodyPart(bodyPart)
	}
	
	private def createRecipients(MimeMessage message) {
		if (to.size() > 0) {
			setRecipients(message, Message.RecipientType.TO, to)
		}
		if (cc.size() > 0) {
			setRecipients(message, Message.RecipientType.CC, cc)
		}
		if (bcc.size() > 0) {
			setRecipients(message, Message.RecipientType.BCC, bcc)
		}
	}

	def setRecipients(MimeMessage message, recipientType, recipients) {
		message.setRecipients(recipientType, 
				recipients.collect { new InternetAddress(it) } as Address[])
	}
	
	private def constructMail() {
		MimeMessage mimeMessage = new MimeMessage(getSession(PROPERTIES))	
		createRecipients(mimeMessage)
		
		mimeMessage.setFrom(new InternetAddress(from))
		mimeMessage.setSubject(subject)
		mimeMessage.setContent(createMessageBody())
		mimeMessage.setSentDate(new Date())
		mimeMessage.saveChanges();
		
		mimeMessage
	}


}
