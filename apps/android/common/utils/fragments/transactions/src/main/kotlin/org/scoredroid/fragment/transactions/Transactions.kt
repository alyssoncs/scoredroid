package org.scoredroid.fragment.transactions

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit

inline fun FragmentManager.commitWithReordering(
    allowStateLoss: Boolean = false,
    body: FragmentTransaction.() -> Unit,
) {
    commit(allowStateLoss) {
        setReorderingAllowed(true)
        body()
    }
}
