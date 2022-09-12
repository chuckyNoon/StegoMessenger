package com.example.diplomclient

import com.example.diplomclient.common.getBit
import com.example.diplomclient.common.setBit
import com.example.diplomclient.common.setLowestBit
import org.junit.Assert.*
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun addition_isCorrect() {
        val x = 7

        assertEquals(
            7,
            x.setBit(0, true)
        )
        assertEquals(
            6,
            x.setBit(0, false)
        )
        assertEquals(
            5,
            x.setBit(1, false)
        )

        assertEquals(
            7,
            x.setLowestBit(true)
        )
        assertEquals(
            6,
            x.setLowestBit(false)
        )

        assertEquals(
            true,
            x.getBit(0)
        )
        assertEquals(
            true,
            x.getBit(1)
        )
        assertEquals(
            true,
            x.getBit(2)
        )
        assertEquals(
            false,
            x.getBit(3)
        )
    }
}
