package repo.pattimuradev.fsearch.misc

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

/**
 * Objek untuk menampung custom observer untuk live data
 * @author PattimuraDev (Dwi Satria Patra)
 */
object CustomObserver {
    /**
     * Fungsi untuk custom observer yang hanya berjalan sekali
     * @author PattimuraDev (Dwi Satria Patra)
     */
    fun <T> LiveData<T>.observeOnce(owner: LifecycleOwner, observer: (T) -> Unit) {
        observe(owner, object: Observer<T> {
            override fun onChanged(value: T) {
                removeObserver(this)
                observer(value)
            }
        })
    }
}