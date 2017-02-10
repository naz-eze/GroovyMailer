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

	def to(Closure toClosure) {
		toMode = true
		runClosure toClosure
		toMode = false
	}

	def cc(Closure ccClosure) {
		ccMode = true
		runClosure ccClosure
		ccMode = false
	}

	def bcc(Closure bccClosure) {
		bccMode = true
		runClosure bccClosure
		bccMode = false
	}

	def email(String email) {
		if (toMode) {
			mail.to << email
		} else if (ccMode) {
			mail.cc << email
		} else if (bccMode) {
			mail.bcc << email
		} else {
			throw new UnsupportedOperationException("email() only allowed in to, cc or bcc context.")
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
	
	def path(String filePath) {
		if (attachmentMode) {
			mail.attachments << filePath
		} else {
			throw new UnsupportedOperationException("path() only allowed in attachment context.")
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
			throw new UnsupportedOperationException("content() only allowed in message context.")	
		}
	}
	
	def type(String messageType) {
		if (messageMode) {
			mail.messageType = messageType
		} else {
			throw new UnsupportedOperationException("type() only allowed in message context.")
		}
	}
	
	private def runClosure(Closure closure) {
		Closure runClone = closure.clone()
		runClone.delegate = this
		runClone.resolveStrategy = Closure.DELEGATE_ONLY
		runClone()
	}
}

