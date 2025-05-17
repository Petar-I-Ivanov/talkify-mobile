package uni.fmi.masters.talkify.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import uni.fmi.masters.talkify.R
import uni.fmi.masters.talkify.service.api.CsrfApi
import uni.fmi.masters.talkify.service.api.UserApi
import uni.fmi.masters.talkify.service.api.base.ApiClient
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    @Inject lateinit var userApi: UserApi
    @Inject lateinit var csrfApi: CsrfApi

    private val coroutineScope = CoroutineScope(Dispatchers.Main + Job())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        findViewById<Button>(R.id.login_btn).setOnClickListener {
            onLogin()
        }
    }

    private fun onLogin() {
        val username = findViewById<TextInputEditText>(R.id.username_input).text.toString()
        val password = findViewById<TextInputEditText>(R.id.password_input).text.toString()

        if (username.isBlank() || password.isBlank()) {
            findViewById<TextView>(R.id.login_error).text = getString(R.string.login_required_error)
            return
        }

        coroutineScope.launch {
            try {
                userApi.login(username, password)
                csrfApi.loadCsrf()
                startActivity(Intent(this@LoginActivity, TalkifyActivity::class.java))
                finish()
            } catch (e: Exception) {
                findViewById<TextView>(R.id.login_error).text = getString(R.string.login_non_existing_error)
            }
        }
    }
}