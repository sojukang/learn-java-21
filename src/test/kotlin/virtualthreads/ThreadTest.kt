package virtualthreads

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class ThreadTest {
    @Test
    fun `execute 이전 스레드 상태는 NEW 이다`() {
        val virtualThreadFactory = Thread.ofVirtual().factory()

        val virtualThread = virtualThreadFactory.newThread {
            Thread.currentThread().also {
                println(it.createLogMessage())
            }
        }

        assertEquals(Thread.State.NEW, virtualThread.state)
        assertTrue(virtualThread.isVirtual)
    }

    @Test
    fun `execute 이후 스레드 상태는 RUNNABLE 이다`() {
        val virtualThreadFactory = Thread.ofVirtual().factory()

        val virtualThread = virtualThreadFactory.newThread {
            Thread.currentThread().also {
                println(it.createLogMessage())
                assertEquals(it.state, Thread.State.RUNNABLE)
            }
        }

        virtualThread.start()
    }

    @Test
    fun `join 대기하는 경우 스레드 상태는 WAITING 이다`() {
        val virtualThreadFactory = Thread.ofVirtual().factory()

        val virtualThread = virtualThreadFactory.newThread {
            Thread.currentThread().also {
                Thread.sleep(50)
                println(it.createLogMessage())
            }
        }

        virtualThread.start()
        virtualThread.join(10)
        assertEquals(virtualThread.state, Thread.State.WAITING)
    }

    @Test
    fun `실행 종료한 경우 스레드 상태는 TERMINATED 이다`() {
        val virtualThreadFactory = Thread.ofVirtual().factory()

        val virtualThread = virtualThreadFactory.newThread {
            Thread.currentThread().also {
                println(it.createLogMessage())
            }
        }

        virtualThread.start()
        virtualThread.join()
        assertEquals(virtualThread.state, Thread.State.TERMINATED)
    }

    private fun Thread.createLogMessage() =
        "threadId: ${this.threadId()}, threadState: ${this.state}, threadGroup: ${this.threadGroup.name}"

}

