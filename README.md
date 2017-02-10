# GroovyMailer - A Groovy Mailer for sending e-mails 
-------------------

This provides a fluent builder-style mailing interface using the JavaMail Library.

The project is [Gradle](https://gradle.org/getting-started/using-a-build) bootstrapped, so to build simply change directory to the project root directory and..
*	for *nix OS run the command - ./gradlew clean build
* 	for Windows OS run the command - gradlew.bat clean build
   
Currently no "real" documentation, but the example below shows how to use it:

		Mail.configure {
			it.SMTP_HOST = 'smtp.yourdomain.com'
			it.SMTP_PORT = '25'
		}
		
		Mail mail = new MailBuilder().build {
			from 'sender@yourdomain.com'
			to {
				email 'recipientOne@yourdomain.com'
				email 'recipientTwo@yourdoimain.com'
				//...
			}
			cc {
				email 'cc@yourdomain.com'
				//...
			}
			bcc { 
				email 'bcc@yourdomain.com' 
				//...
			}
			subject "I'm happy"
			message {
				content "I'm very happy today!"
				type 'text/plain'
			}
			attachment {
					path '/Users/naz/someFile.pdf'
					path 'randomFile.txt'
			}
		}
		mail.send()



GroovyMailer is free to use and licensed under the 3-Clause BSD licence.

> Copyright &copy; 2017 Naz Ezeonyebuchi

> All rights reserved.
>
> Redistribution and use in source and binary forms, with or without modification,
> are permitted provided that the following conditions are met:
>
> 1. Redistributions of source code must retain the above copyright notice, this
> list of conditions and the following disclaimer.
>
> 2. Redistributions in binary form must reproduce the above copyright notice,
> this list of conditions and the following disclaimer in the documentation and/or
> other materials provided with the distribution.
>
> 3. Neither the name of the copyright holder nor the names of its contributors 
> may be used to endorse or promote products derived from this software without
> specific prior written permission.
>
> THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY
> EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
> OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT
> SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
> SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT
> OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
> HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
> TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
> EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.