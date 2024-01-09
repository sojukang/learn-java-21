package virtualthreads

import java.util.concurrent.atomic.AtomicInteger

data class Counter(
    var count: AtomicInteger = AtomicInteger(0)
) {
    fun increment() {
        count.getAndIncrement()
    }
}