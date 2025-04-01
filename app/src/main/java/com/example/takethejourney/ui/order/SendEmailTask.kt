import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import com.example.takethejourney.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class SendEmailTask(private val context: Context) {

    private fun getEmailCredentials(): Pair<String, String> {
        return try {
            val appContext = context.applicationContext
            val packageName = appContext.packageName
            val appInfo = appContext.packageManager
                .getApplicationInfo(packageName, PackageManager.GET_META_DATA)

            val email = BuildConfig.SMTP_EMAIL
            val password = BuildConfig.SMTP_PASSWORD

            Log.d("SendEmailTask", "Получены email: $email, password: ${"*".repeat(password.length)}")
            Pair(email, password)
        } catch (e: Exception) {
            Log.e("SendEmailTask", "Ошибка получения email/пароля", e)
            Pair("", "")
        }
    }

    suspend fun sendEmail(recipient: String, subject: String, body: String): Boolean {
        return withContext(Dispatchers.IO) {
            val (email, password) = getEmailCredentials()
            if (email.isEmpty() || password.isEmpty()) {
                Log.e("SendEmailTask", "Email или пароль пусты")
                return@withContext false
            }

            val props = Properties().apply {
                put("mail.smtp.host", "smtp.gmail.com")
                put("mail.smtp.port", "465")
                put("mail.smtp.ssl.enable", "true")
                put("mail.smtp.auth", "true")
            }

            try {
                val session = Session.getInstance(props, object : Authenticator() {
                    override fun getPasswordAuthentication() =
                        PasswordAuthentication(email, password)
                })

                MimeMessage(session).apply {
                    setFrom(InternetAddress(email))
                    addRecipient(Message.RecipientType.TO, InternetAddress(recipient))
                    this.subject = subject
                    setText(body)
                }.let { Transport.send(it) }

                Log.d("SendEmailTask", "Email отправлен успешно")
                true
            } catch (e: Exception) {
                Log.e("SendEmailTask", "Ошибка при отправке email", e)
                false
            }
        }
    }
}
