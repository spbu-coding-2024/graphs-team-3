package viewmodel.screens

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import view.screens.Storage

interface ScreenViewModel {
    val _storage: MutableState<Storage?>
    val storage: State<Storage?> get() = _storage

    val _uri: MutableState<String?>
    val uri: State<String?> get() = _uri

    val _username: MutableState<String?>
    val username: State<String?> get() = _username

    val _password: MutableState<String?>
    val password: State<String?> get() = _password

    fun selectStorage(storage: Storage?)

    fun setUri(uri: String?)

    fun setUsername(username: String?)

    fun setPassword(password: String?)

    fun clearAuthData()
}
