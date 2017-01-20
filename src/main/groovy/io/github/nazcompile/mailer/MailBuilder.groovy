package io.github.nazcompile.mailer

class MailBuilder {

	private Mail mail

	private def toMode = false
	private def ccMode = false
	private def bccMode = false
	private def attachmentMode = false
	private def messageMode = false


	Mail build(Closure definition) {
		mail = new Mail()
		runClosure definition
		mail
	}

	def to(Closure email) {
		toMode = true
		runClosure email
		toMode = false
	}

	def cc(Closure email) {
		ccMode = true
		runClosure email
		ccMode = false
	}

	def bcc(Closure email) {
		bccMode = true
		runClosure email
		bccMode = false
	}

	def email(String toEmail) {
		if (toMode) {
			mail.to << toEmail
		} else if (ccMode) {
			mail.cc << toEmail
		} else if (bccMode) {
			mail.bcc << toEmail
		} else {
			throw new IllegalStateException("email() only allowed in to, cc or bcc context.")
		}
	}
	
	private def methodMissing(String name, arguments) {
		if (name == 'from') {
			mail.from = arguments[0]
		} else if (name == 'subject') {
			mail.subject = arguments[0]
		}
	}

	def attachment(Closure closure) {
		attachmentMode = true
		runClosure closure
		attachmentMode = false
	}
	
	def name(String fileName) {
		if (attachmentMode) {
			mail.attachments << fileName
		} else {
			throw new IllegalStateException("name() only allowed in attachment context.")
		}
	}
	
	def message(Closure messageClosure) {
		messageMode = true
		runClosure messageClosure
		messageMode = false
	}
	
	def content(String messageContent) {
		if (messageMode) {
			mail.messageContent = messageContent
		} else {
			throw new IllegalStateException("content() only allowed in message context.")	
		}
	}
	
	def type(String messageType) {
		if (messageMode) {
			mail.messageType = messageType
		} else {
			throw new IllegalStateException("type() only allowed in message context.")
		}
	}
	
	private def runClosure(Closure closure) {
		Closure runClone = closure.clone()
		runClone.delegate = this
		runClone.resolveStrategy = Closure.DELEGATE_ONLY
		runClone()
	}
}

