package io.github.nazcompile.mailer

import javax.activation.DataHandler
import javax.activation.DataSource
import javax.activation.FileDataSource
import javax.mail.Multipart
import javax.mail.Session
import javax.mail.internet.MimeBodyPart
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

	private def getSession(Properties props) {
		def session = Session.getDefaultInstance(props)
		session
	}

	private def createMessageBody() {
		MimeBodyPart bodyPart = new MimeBodyPart()
		bodyPart.setContent(messageContent, messageType)

		Multipart multiPart = new MimeMultipart()
		multiPart.addBodyPart(bodyPart)
		
		multiPart
	}
	
	private def addAttachement(MimeBodyPart bodyPart, Multipart multiPart, File attachment) {
		DataSource source = new FileDataSource(attachment)
		bodyPart.setDataHandler(new DataHandler(source))
		bodyPart.setFileName(attachment.getName())
		multiPart.addBodyPart(bodyPart)
	}

}
