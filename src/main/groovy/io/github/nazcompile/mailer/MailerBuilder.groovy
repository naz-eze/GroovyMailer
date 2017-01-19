package io.github.nazcompile.mailer

class MailerBuilder {

	Mailer mailer

	private def toMode = false
	private def ccMode = false
	private def bccMode = false


	Mailer build(Closure definition) {
		mailer = new Mailer()
		runClosure definition
		mailer
	}

	void to(Closure email) {
		toMode = true
		runClosure email
		toMode = false
	}

	void cc(Closure email) {
		ccMode = true
		runClosure email
		ccMode = false
	}

	void bcc(Closure email) {
		bccMode = true
		runClosure email
		bccMode = false
	}

	void email(String toEmail) {
		if (toMode) {
			mailer.to << toEmail
		} else if (ccMode) {
			mailer.cc << toEmail
		} else if (bccMode) {
			mailer.bcc << toEmail
		} else {
			throw new IllegalStateException("email() only allowed in to, cc or bcc context.")
		}
	}

	private runClosure(Closure closure) {
		Closure runClone = closure.clone()
		runClone.delegate = this
		runClone.resolveStrategy = Closure.DELEGATE_ONLY
		runClone()
	}
}

