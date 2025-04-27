package uni.fmi.masters.talkify.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import uni.fmi.masters.talkify.R
import uni.fmi.masters.talkify.service.api.UserApi

class LoginActivity(private val userApi: UserApi) : AppCompatActivity() {

    private val coroutineScope = CoroutineScope(Dispatchers.Main + Job())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        findViewById<Button>(R.id.login_btn).setOnClickListener {
            onLogin()
        }
    }

    private fun onLogin() {
        val username = findViewById<TextInputLayout>(R.id.username_input).editText?.text.toString()
        val password = findViewById<TextInputLayout>(R.id.password_input).editText?.text.toString()

        if (username.isBlank() || password.isBlank()) {
            findViewById<TextView>(R.id.login_error).text = getString(R.string.login_required_error)
            return
        }

        try {
            coroutineScope.launch {
                userApi.login(username, password)
            }
            startActivity(Intent(this, TalkifyActivity::class.java))
        } catch (e: Exception) {
            findViewById<TextView>(R.id.login_error).text = getString(R.string.login_non_existing_error)
        }
    }
}