package pe.com.master.machines.myapplication.interfaces

import android.text.Editable
import android.text.TextWatcher

interface TextWatcherAdapter : TextWatcher {
    enum class Action {
        beforeTextChanged, onTextChanged, afterTextChanged
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        action(Action.beforeTextChanged, s, start, count, after)
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        action(Action.onTextChanged, s, start, count, before)
    }

    override fun afterTextChanged(editable: Editable) {
        action(Action.afterTextChanged, editable, 0, 0, 0)
    }

    fun action(
        action: Action, charOrEditable: CharSequence, start: Int,
        count: Int, afterOrBefore: Int
    )
}